package ejb.crud;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.ReAccountBank;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbProcessTrack;
import jpa.TbProductType;
import jpa.TbSystemUser;

import crud.ObjectEM;

import ejb.Log;
import ejb.Process;
import ejb.email.Outbox;

/**
 * Session Bean implementation class BankAccountClientEJB
 */
@Stateless(mappedName = "ejb/BankAccountClient")
public class BankAccountClientEJB implements BankAccountClient {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	private ObjectEM emObj;
	
	
    /**
     * Default constructor. 
     */
    public BankAccountClientEJB() {
    }
    
    /**
	 * Método encargado de validar la existencia del producto bancario -cliente
	 * @author psanchez
	 */
    @SuppressWarnings("unchecked")
	public boolean existBankAccountClient(Long userId, Long idBank, String bankAccountNumber, Long bankAccountType) {
	    boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT bank_account_id FROM tb_bank_account " +
									 "WHERE product=?1 " +
									 "AND bank_account_type=?2 " +
									 "AND bank_account_number=?3 ");
				query.setParameter(1, idBank);
				query.setParameter(2, bankAccountType);
				query.setParameter(3, bankAccountNumber);

				List<Object> list= new ArrayList<Object>();
				list=query.getResultList();
				if(list.size()>0){
					for(Object o : list ){
						Long bankAccountId = ((BigDecimal) o).longValue();
						TbBankAccount tba = em.find(TbBankAccount.class, bankAccountId);
						if(tba.getUsrs().getUserId().equals(userId)){
							result= false;
							break;
						}else{
							TbSystemUser tsu = em.find(TbSystemUser.class, userId);
							if((tsu.getTbCodeType().getCodeTypeId().equals(3L) && !tba.getUsrs().getTbCodeType().getCodeTypeId().equals(3L)) 
									|| (!tsu.getTbCodeType().getCodeTypeId().equals(3L) && tba.getUsrs().getTbCodeType().getCodeTypeId().equals(3L))){
								result= true;
							}else{
								result= false;
								break;
							}
						}
					}
				}else{
				  result=true; 
				}
		}catch(NoResultException ex){
			System.out.println(" [ Error en BankAccountClientEJB.existBankAccountClient. ] ");
			result=false;
		}
		return result;
	}
    
    
    /**
	 * Método encargado de crear producto bancario al cliente en sesión
	 * @author psanchez
	 */
    @SuppressWarnings("unused")
	@Override
    public boolean insertBankAccountClient(Long UserId, Long idBank, String bankAccountNumber, 
			Long bankAccountType, Long initValue, String ip, Long creationUser) {	
		try {
			Query q = em.createNativeQuery("SELECT bank_account_id FROM tb_bank_account " +
										   "WHERE usrs=?1 " +
										   "AND product=?2 " +
										   "AND bank_account_type=?3 " +
										   "AND bank_account_number=?4 ");
			 q.setParameter(1, UserId);
			 q.setParameter(2, idBank);
			 q.setParameter(3, bankAccountType);
			 q.setParameter(4, bankAccountNumber);
			 q.getSingleResult();		
			 return false;
		   } catch (NoResultException e) {
			// creating the bank account.
			TbProductType tp= em.find(TbProductType.class, bankAccountType);
			TbBank tb=em.find(TbBank.class, idBank);
			TbSystemUser usrs= em.find(TbSystemUser.class,UserId );

			TbBankAccount tba = new TbBankAccount();
			
			tba.setBankAccountHolder(usrs.getUserNames());
			tba.setBankAccountHolderNit(usrs.getUserCode());
			tba.setBankAccountNumber(String.valueOf(bankAccountNumber));
			tba.setBankAccountType(tp);	
			tba.setDate(new Timestamp(System.currentTimeMillis()));
			tba.setDescription("CLIENTE");
			tba.setProduct(tb);
			tba.setUsrs(usrs);
			tba.setInitValueRecharge(initValue);
													
			emObj = new ObjectEM(em);
			if(emObj.create(tba)){
				log.insertLog("Definir Producto Bancario | No se ha definido el producto bancario No."+tba.getBankAccountNumber()+ 
							  " correspondiente al "+ tb.getBankName()+".", LogReference.BANKACCOUNT, LogAction.CREATE, ip, creationUser);
				
				outbox.insertMessageOutbox(usrs.getUserId(), 
		                   EmailProcess.BANK_PRODUCT_REGISTRATION,
		                   null,
		                   tba.getBankAccountId(), 
		                   null, 
		                   null,
		                   null,
		                   null,
		                   tb.getBankId(),
		                   null,
		                   creationUser,
			               null,
			               null,
			               null,
		                   true,
		                   null);
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, UserId);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  UserId, ip, creationUser);
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
						    "Se ha definido el Producto Bancario No "+tba.getBankAccountNumber()+ " del "+ tb.getBankName()+
						    " con tipo de producto "+tp.getProductTypeDescription()+".", creationUser, ip, 1, "", null,null,null,null);
				
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,UserId);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, UserId, ip, creationUser);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
						 "Se ha definido el Producto Bancario No "+tba.getBankAccountNumber()+ " del "+ tb.getBankName()+
						 " con tipo de producto "+tp.getProductTypeDescription()+".", creationUser, ip, 1, "", null,null,null,null);
				
				return true;				
			} else {
				log.insertLog("Definir Producto Bancario | No se ha definido el producto bancario No."+tba.getBankAccountNumber()+ 
							  " correspondiente al "+ tb.getBankName()+".", LogReference.BANKACCOUNT, LogAction.CREATE, ip, creationUser);
				return false;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountClientEJB.insertBankAccountClient. ] ");
			e.printStackTrace();
			return false;
		}
	}
    

    /**
	 * Método encargado de listar las entidades bancarias
	 * @author psanchez
	 */
	@Override
	public List<TbBank> getBanks() {
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			for (Object obj : em.createQuery("SELECT tb FROM TbBank tb")
					.getResultList()) {
				list.add((TbBank) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountClientEJB.getBanks ] ");
			e.printStackTrace();
		}
		return list;
	}

		
	/**
	 * Método encargado de listar los tipos de producto bancario (debito o credito)
	 * @author psanchez
	 */	
	@Override
	public List<TbProductType> getProductType() {
		List<TbProductType> list= new ArrayList<TbProductType>();
		Query q= em.createQuery("select t from TbProductType t");
		List<?> lis=q.getResultList();
		for(Object o: lis){
			TbProductType tt=(TbProductType)o;
			list.add(tt);
		}
		return list;
	}


	/**
	 * Método encargado de listar los producto bancario -cliente
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReAccountBank> getBankAccounts(Long userId) {
		List<ReAccountBank> list = new ArrayList<ReAccountBank>();
		try {
			Query q= em.createNativeQuery("SELECT DATE_CREATION,BANK_NAME,BANK_AVAL,BANK_ACCOUNT_NUMBER," +
					                      "PRODUCT_TYPE_DESCRIPTION, DESCRIPTION,ACCOUNT_ID,STATE_ACCOUNT_BANK " +
										  "FROM VW_PRODUCTS_ASSOCIATED WHERE USRS= ?1");
										  q.setParameter(1, em.find(TbSystemUser.class,userId));
			
			List<Object> lis= (List<Object>)q.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				ReAccountBank r= new ReAccountBank();
				
				r.setDate(o[0]!=null?o[0].toString():"-");
				r.setBank(o[1]!=null?o[1].toString():"-");
				r.setBankAval(Long.parseLong(o[2].toString()));
				r.setAccount(o[3]!=null?o[3].toString():"-");
				r.setTypeProduct(o[4]!=null?o[4].toString():"");
				r.setDesc(o[5]!=null?o[5].toString():"-");
				
				if(o[7]==null || o[7].toString().equals("3")){
					r.setAccountId("Sin Asociar");
				}else if(o[7].toString().equals("1")){
					r.setAccountId("Asociada a la Cuenta FacilPass "+ o[6].toString());
				}else if(o[7].toString().equals("2")){
					r.setAccountId("En proceso de Disociación de la Cuenta FacilPass "+ o[6].toString());
				}else if(o[7].toString().equals("4")){
					r.setAccountId("Pendiente de Aprobación de Disociación de la Cuenta FacilPass "+ o[6].toString());
				}
				
				list.add(r);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountClientEJB.getBankAccounts. ] ");
			e.printStackTrace();
		}
		return list;
	}

}
	
