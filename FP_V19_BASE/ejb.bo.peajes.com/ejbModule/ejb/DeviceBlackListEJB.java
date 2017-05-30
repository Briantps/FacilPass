package ejb;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.DeviceState;
import constant.DistributionType;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TransactionType;
import constant.TypeTask;
import constant.VialTypeTag;

import jpa.ReAccountDevice;
import jpa.ReDeviceTagType;
import jpa.TbAccount;
import jpa.TbDevice; 
import jpa.TbDeviceState;
import jpa.TbProcessTrack;
import jpa.TbTag;

import crud.ObjectEM;
import ejb.taskeng.TransitTask;

@Stateless(mappedName = "ejb/DeviceBlackList")
public class DeviceBlackListEJB implements DeviceBlackList{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask; 
	
	@EJB(mappedName ="ejb/Transaction")
	private Transaction transaction;
	
	public DeviceBlackListEJB() {		
	}
	
	@Override
	public boolean enterDeviceBL(String deviceCode, String ip, Long user) {
		boolean result = false;	
	  try{	
		  System.out.println("Codigo de Dispositivo: "+deviceCode);
		  Query q = em.createQuery("select dev from TbDevice dev where deviceCode=?1");
		  q.setParameter(1, deviceCode);
		  
		  BigDecimal saldoDev = null;
		
		TbDevice dev = (TbDevice) q.getSingleResult();
		TbDeviceState estado = em.find(TbDeviceState.class, DeviceState.BLACKLIST.getId().longValue());
		
        if(!dev.getTbDeviceState().getDeviceStateId().equals(DeviceState.BLACKLIST.getId())){
        	dev.setTbDeviceState(estado);
    		if(dev.getDeviceCurrentBalance().longValue() > 0){
    			saldoDev = dev.getDeviceCurrentBalance();
    			dev.setDeviceCurrentBalance(new BigDecimal(0));
    		}
    		objectEM = new  ObjectEM(em);
    		objectEM.update(dev);
    		long pp = 0;
    		TbProcessTrack p=process.searchProcess(ProcessTrackType.DEVICE, dev.getDeviceId());
    		if(p==null){
    			pp=process.createProcessTrack(ProcessTrackType.DEVICE, dev.getDeviceId(), ip, user);
    			
    			log.insertLog("Dispositivo Ingresado a Lista Negra", LogReference.BLACKLIST, LogAction.UPDATE, ip, user);
    		}
    		else{
    		    pp= p.getProcessTrackId();
    		}
    		
    		process.createProcessDetail(pp, ProcessTrackDetailType.DEVICE_BLACK_LIST,
					"Dispositivo Electrónico TAG ID Facial "+dev.getDeviceFacialId()+" y TID "+dev.getDeviceCode()+" en lista negra", user, ip, 1, 
					"No se pudo ingresar dispositivo a lista negra con facial:  " + dev.getDeviceFacialId(),null,null,null,null);
    		
    		Query qr = em.createQuery("select rdt from ReDeviceTagType rdt where tbDevice = ?1");
    		qr.setParameter(1, dev);					
    		
    		ReDeviceTagType manufaturerId = (ReDeviceTagType) qr.getSingleResult();
    		ReAccountDevice d = null;
    		try{
    			Query qa = em.createQuery("Select rad from ReAccountDevice rad where rad.tbDevice = ?1 and rad.relationState=0");
        		qa.setParameter(1, dev);
        		d = (ReAccountDevice) qa.getSingleResult();
        		
        		if(saldoDev != null){
        			TbAccount cta = em.find(TbAccount.class, d.getTbAccount().getAccountId());
        			Long accountPreviousBalance = cta.getAccountBalance().longValue();
        			cta.setAccountBalance(cta.getAccountBalance().add(saldoDev));
        			ObjectEM objectEMCta = new ObjectEM(em);
        			if(objectEMCta.update(cta)){
        				try{
        				transaction.saveAccountTransaction(cta.getAccountId(), null, null, TransactionType.CREDIT,
        						"Reintegro por inclusion de Dispositivo "+d.getTbDevice().getDeviceCode()+" a Lista Negra ."  , 
        						saldoDev.longValue(), ip, user, accountPreviousBalance, cta.getAccountBalance().longValue(), null, null, null, null);
        				
        				log.insertLog("Reintegro de Saldo | Cambio saldo cuenta: Anterior : " + accountPreviousBalance + ", Actual: " + cta.getAccountBalance() + ".", 
        						LogReference.ACCOUNT, LogAction.UPDATE, ip, user);
        					} catch (NoSuchMethodError e){
        						return false;					
        				}
        			}
        		}
                 TbTag tag = em.find(TbTag.class, dev.getDeviceCode());		
        		
        		if(tag == null){
        			TbTag tagNew = new TbTag();						
        			tagNew.setDeviceId(dev.getDeviceCode());
        			tagNew.setDeviceCode(dev.getDeviceFacialId());
        			tagNew.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
        			tagNew.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
        			tagNew.setContractualAuth(5L);
        			if(d.getTbVehicle()!=null){
        				tagNew.setVehiclePlate(d.getTbVehicle().getVehiclePlate());
        				if(d.getTbVehicle().getTbCategory()!=null){
        					tagNew.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());
        				}
        			}
        			tagNew.setDeviceStateId(5L); //estado de ln en vial
        			tagNew.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
        			
        			if((d.getTbAccount().getDistributionTypeId().getDistributionTypeId().longValue()==DistributionType.BAGMONEY.getDistributionID().longValue())){
        				tagNew.setAccountId(d.getTbAccount().getAccountId().longValue());
        			} 
        			objectEM = new ObjectEM(em);
        			objectEM.create(tagNew);
        			transitTask.createTask(TypeTask.DEVICE, tagNew.getDeviceId());
        		} else {
        			tag.setDeviceId(dev.getDeviceCode());
        			tag.setDeviceCode(dev.getDeviceFacialId());
        			tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
        			tag.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
        			tag.setDeviceStateId(5L); //estado de ln en vial
        			tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
        			tag.setContractualAuth(5L);
        			if(d.getTbVehicle()!=null){
        				if(d.getTbVehicle().getTbCategory()!=null){
        					tag.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());
        				}
        			}
        			
        			if((d.getTbAccount().getDistributionTypeId().getDistributionTypeId().longValue()==DistributionType.BAGMONEY.getDistributionID().longValue())){
        				tag.setAccountId(d.getTbAccount().getAccountId().longValue());
        			} 
        			objectEM = new ObjectEM(em);
        			objectEM.update(tag);
        			transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());		
        			
        			
        		}
        		result = true;
    		}catch(NoResultException ex){
    		
               TbTag tag = em.find(TbTag.class, dev.getDeviceCode());	
               if(tag == null){
       		       TbTag tagNew = new TbTag();						
       			   tagNew.setDeviceId(dev.getDeviceCode());
       			   tagNew.setDeviceCode(dev.getDeviceFacialId());
       			   tagNew.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
       			   tagNew.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
       			   tagNew.setDeviceStateId(5L); //estado de ln en vial
       			   tagNew.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
       			   tagNew.setContractualAuth(5L);
       			   objectEM = new ObjectEM(em);
       			   boolean r1=objectEM.create(tagNew);
       			   transitTask.createTask(TypeTask.DEVICE, tagNew.getDeviceId());
       			   if(r1==true){
        			  result = true;
        		   }
        		   else{
        		      result = false;
        		   }
       		   }

               else{
            	   tag.setDeviceStateId(5L); //estado de ln en vial
          		 
           		   objectEM = new ObjectEM(em);
           		   boolean r=objectEM.update(tag);
           		   transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());	 
           		
        		   if(r==true){
        			  result = true;
        		   }
        		   else{
        			  result = false;
        		   }
               }
        	   
        	 }
	
		}
        else{
        	System.out.println("El dispositivo ya se encuentra en lista negra");
        	result = false;
        }

		
		
	  }catch (Exception e){
		  e.printStackTrace();
		  result = false;
	  }
		
		return result;
	}

	public void setObjectEM(ObjectEM objectEM) {
		this.objectEM = objectEM;
	}

	public ObjectEM getObjectEM() {
		return objectEM;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Process getProcess() {
		return process;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void setTransitTask(TransitTask transitTask) {
		this.transitTask = transitTask;
	}

	public TransitTask getTransitTask() {
		return transitTask;
	}
	

}
