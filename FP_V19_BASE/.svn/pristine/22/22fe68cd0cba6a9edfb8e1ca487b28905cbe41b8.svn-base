package ejb.email;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbTransaction;
import constant.DistributionType;
import constant.EmailProcess;
import ejb.TypeDistribution;


@Stateless(mappedName = "ejb/TimerEmailTransaction")
public class TimerEmailTransactionEJB implements TimerEmailTransaction,SessionBean,TimedObject{
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@SuppressWarnings("unused")
	private InitialContext context;
	
	private TimerHandle timerHandle = null;
	
	private Timer timer;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	private SessionContext sc;
	
	public TimerEmailTransactionEJB() {
	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("TimerEmailTransactionEJB.init");
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void createTimer() {
		try {
			TimerService ts = sc.getTimerService();			
			if (timer == null) {
				timer = ts.createTimer(0, 600000L, null);
				timerHandle = timer.getHandle();
				System.out.println("TimerEmailTransaction inicializado se ejecutara cada 10 minutos");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSessionContext(SessionContext sc) throws EJBException,
			RemoteException {
		this.sc = sc;
	}

	@Override
	public void ejbTimeout(Timer timer) {
		System.out.println("TimerBalance Execution "+new Timestamp(System.currentTimeMillis()));
			
		//this.sendEmailTrasaction();
		
	}
	
	public void cancelTimer(String timerName) {
		try {
			TimerService ts = sc.getTimerService();
			Iterator<?> it = ts.getTimers().iterator();
			while (it.hasNext()) {
				Timer myTimer = (Timer) it.next();
				if ((myTimer.getInfo().equals(timerName))) {
					myTimer.cancel();
				}
			}
		} catch (Exception e) {
			System.out.println("Exception after cancelling timer : "
					+ e.toString());
		}
		return;
	}

	public void getTimerInfo() {
		if (timerHandle != null) {
			Timer timer = timerHandle.getTimer();
			System.out.println("Timer information: " + timer.getInfo());
		}
	}
	
	/**
	 * Metodos para envio de email por transacciones
	 */
	

	public void sendEmailTrasaction(){
		/*Se buscan los correos pendientes por enviar de acuerdo a transacciones*/
		try{
		Query q = em.createNativeQuery("select transaction_id from tb_outbox_transaction where outbox_transaction_state=0");		
		List<?> listTran = q.getResultList();
		
		if(listTran !=null){
			for (Object object : listTran){	
			  try{	
				Long tranId =  ((BigDecimal)object).longValue();
				System.out.println("tranId: "+tranId);
				Query upd = em.createNativeQuery("update tb_outbox_transaction set outbox_transaction_state = 1 where transaction_id=?1");
				upd.setParameter(1, tranId);
				upd.executeUpdate();
				
				TbTransaction tt = em.find(TbTransaction.class, tranId);
				
				Query q2 = em.createQuery("Select rad from ReAccountDevice rad where rad.tbDevice.deviceId = ?1 and rad.relationState=0");
				q2.setParameter(1, tt.getTbDevice().getDeviceId());
				
				ReAccountDevice rad = (ReAccountDevice) q2.getSingleResult();
				
				outbox.insertMessageOutbox(rad.getTbAccount().getTbSystemUser().getUserId(), 
			             EmailProcess.UTILIZATION,
			             rad.getTbAccount().getAccountId(),
			             null,			             
			             tt.getTransactionId(),
			             tt.getTbDevice().getDeviceId(),
			             tt.getTbVehicle().getVehicleId(),
			             null,
			             null,
			             null,
			             null,
			             null,
			             null,
			             null,
			             true,
		                   null);
				
				validaSaldoCta(rad.getTbAccount().getAccountId());
			  }
			  catch(Exception e){
				  System.out.println("error en correo de transacciones");
			  }
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void validaSaldoCta(Long acountId){
		try{
			//se valida cual es el tipo de distribucion de la cuenta
			TbAccount ta = em.find(TbAccount.class, acountId);
			if(ta != null){
				Long saldo = this.saldoActualCta(ta.getAccountId(),ta.getDistributionTypeId().getDistributionTypeId().intValue());
				Long umbral = this.umbralActual(ta.getAccountId());
				
				if(saldo < umbral ){				  	
					outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
				             EmailProcess.LOW_BALANCE,
				             ta.getAccountId(),
				             null, 
				             null, 
				             null,
				             null,
				             null,
				             null,
				             null,
				             null,
			                 null,
			                 null,
			                 null,
				             true,
			                   null);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Long umbralActual(Long accountId){
		Long result = 0L;
		try{
		 Query q = em.createNativeQuery("select max(Val_Min_Alarm) from Tb_Alarm_Balance where account_id=?1 and Id_Tip_Alarm=4");
		 q.setParameter(1, accountId);
		 result = ((BigDecimal) q.getSingleResult()).longValue();
		}catch(NoResultException n){
			result = 0L;
		}
		return result;
	}
	
	public Long saldoActualCta(Long accountId, int tipoCta){
		Long result =0L;
		Query q = null;
		Query qd = null;
		try{
			if(tipoCta == DistributionType.BAGMONEY.getDistributionID().intValue()){
				q = em.createNativeQuery("select Account_Balance from Tb_Account where Account_Id=?1");
				q.setParameter(1, accountId);
				
				result = ((BigDecimal) q.getSingleResult()).longValue();
			}else if((tipoCta == DistributionType.MANUAL.getDistributionID().intValue()) || 
					 (tipoCta == DistributionType.AUTOMATIC.getDistributionID().intValue())){
				
				q = em.createNativeQuery("select Account_Balance from Tb_Account where Account_Id=?1");
				q.setParameter(1, accountId);
				
				Long saldoCta = ((BigDecimal) q.getSingleResult()).longValue();
				
				qd = em.createNativeQuery("Select sum(td.device_current_balance)"+
										 " From Tb_Device Td"+
										 " Inner Join Re_Account_Device Rad On Rad.Device_Id=Td.Device_Id"+
										 " where rad.account_id=?1");
				qd.setParameter(1, accountId);
				
				Long saldoDisp = ((BigDecimal) qd.getSingleResult()).longValue();
				
				result = (saldoCta+saldoDisp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
