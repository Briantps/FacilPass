package ejb.balance;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbProcessTrack;
import linews.InvokeSqdm;
import util.ObjectRechage;
import util.ws.WSRecharge;
import constant.AccountStateType;
import constant.DistributionType;
import constant.EmailProcess;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Process;
import ejb.SystemParameters;
import ejb.email.Outbox;

@Stateless(mappedName = "ejb/TimerBalance")
public class TimerBalanceEJB implements TimerBalance,SessionBean,TimedObject {
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@SuppressWarnings("unused")
	private InitialContext context;
	
	private TimerHandle timerHandle = null;
	
	private Timer timer;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName = "ejb/Process")
	private Process proceso;
	
	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParameters;
	
	private SessionContext sc;
	
	public TimerBalanceEJB() {
	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("TimerBalanceEJB.init");
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
				timer = ts.createTimer(0, 86400000L, null);
				timerHandle = timer.getHandle();
				System.out.println("TimerBalance inicializado se ejecutara cada "+(this.getTimeCalculation()/86400000L)+" dias");
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
		
		if(calculaUmbral()){
			this.calculoSaldoBajo();
		}
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
	
	
	public Long getTimeCalculation(){
		Long result = 86400000L*15;
		try{
			try{
			 String parameter = SystemParameters.getParameterValueById(9L);
			 result= Long.parseLong(parameter)*86400000L;
			}catch(NoResultException n){
				result = 86400000L*15;
			}
			return result;
			
		}catch(Exception e){
			System.out.println("Error en TimerBalanceEJB.getTimeCalculation");
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * Metodos que manejaran los calculos matematicos
	 */
	
	private boolean calculaUmbral(){
		boolean result = false;
		try{
			Timestamp le = null;
			Query q = em.createNativeQuery("Select Timer_Last_Execution"+
										   " From Tb_Timer"+
										   " where Timer_Name = 'TimerBalance'" +
										   " and timer_state = 0");
			try{
			 le = (Timestamp) q.getSingleResult();
			}
			catch (NoResultException n){
				le = new Timestamp(System.currentTimeMillis());
			}
			if(le==null){
				le = new Timestamp(System.currentTimeMillis());
				Query updTimer = em.createNativeQuery("update tb_timer set timer_state = 1 where Timer_Name = 'TimerBalance'");					
				updTimer.executeUpdate();
				result =  true;
			}else{
				Long hoy =System.currentTimeMillis();
				Long ultEjec = le.getTime();
				Long calculo = getTimeCalculation();
				if((hoy-ultEjec)>=calculo){
					Query updTimer = em.createNativeQuery("update tb_timer set timer_state = 1 where Timer_Name = 'TimerBalance'");					
					updTimer.executeUpdate();
					result =  true;
				}
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private Long promedioTotal(Long idCuenta, int numDias){
		Long result = 0L;
		try{
			Query q = em.createNativeQuery("Select ceil(nvl(sum(tt.transaction_value),0)/decode(count(tt.transaction_id),0,1,count(tt.transaction_id))) promedio"+
							" From Tb_Transaction Tt"+
							" Inner Join Re_Account_Device Rad On Rad.Device_Id=Tt.Device_Id"+
							" Inner Join Tb_Account Ta On Rad.Account_Id=Ta.Account_Id"+
							" Where Ta.Account_Id=?1"+
							" And Tt.Transaction_Type_Id=2"+
							" and tt.transaction_value > 0"+
							" and tt.account_id is not null "+
							" and Tt.Transaction_Time between (sysdate - interval '"+numDias+"' day) and sysdate");
				q.setParameter(1, idCuenta);			
				
			result = ((BigDecimal) q.getSingleResult()).longValue();
		}catch(Exception e){
			e.printStackTrace();
			result = 0L;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void calculoSaldoBajo(){
		System.out.println("Inicio: "+new Timestamp(System.currentTimeMillis()));
		try{
			int diasprom = 45;			
			try{
			 //Query q = em.createNativeQuery("select System_Parameter_Value parametro from Tb_System_Parameter where System_Parameter_Id=8");
			   Query q =em.createQuery("select systemParameterValue from TbSystemParameter  where systemParameterId in (8L)");
			 diasprom = Integer.parseInt((String) q.getSingleResult());
			}catch(NoResultException n){
				diasprom = 45;
			}
			
			Long valMinUmbral = 20000L;
			try{
			  //Query q = em.createNativeQuery("select System_Parameter_Value parametro from Tb_System_Parameter where System_Parameter_Id=4");
			   Query q =em.createQuery("select systemParameterValue from TbSystemParameter  where systemParameterId in (4L)");
			 valMinUmbral = Long.parseLong((String) q.getSingleResult());
			}catch(NoResultException n){
				valMinUmbral = 20000L;
			}
						
			
			Long dias = (this.getTimeCalculation()/86400000L); 
			
			//se listan todas la cuenta activas
			
			Query ctas = em.createQuery("Select ta from TbAccount ta where ta.accountState = ?1");
			ctas.setParameter(1, AccountStateType.ACTIVE.getId());
			List<Object> listCtas = ctas.getResultList();
			Long promTotal = 0L;
			Long promDiario = 0L;
			Long umbralAct = 0L;
			System.out.println(listCtas.size());
			for (Object object : listCtas){		
				System.out.println(object);
				if (object != null && object instanceof TbAccount) {
					TbAccount cta = (TbAccount) object;
					System.out.println("cta: "+cta.getAccountId());
					promDiario = this.promedioTotal(cta.getAccountId(),diasprom);
					promTotal = (promDiario*dias);
					umbralAct = this.umbralActual(cta.getAccountId());
					System.out.println("promDiario: "+promDiario);
					System.out.println("promTotal: "+promTotal);
					System.out.println("umbralAct: "+umbralAct);
					if(promTotal < valMinUmbral){
						promTotal = valMinUmbral;
					}
					
					if(umbralAct<promTotal){
						Query updUmbral = em.createNativeQuery("update tb_alarm_balance set val_min_alarm = ?1 where account_id = ?2 and id_tip_alarm=4");
						updUmbral.setParameter(1, promTotal);
						updUmbral.setParameter(2, cta.getAccountId());
						updUmbral.executeUpdate();
					}
					
					if(enviaraRecarga()){
						Long saldAct = this.saldoActualCta(cta.getAccountId(), cta.getDistributionTypeId().getDistributionTypeId().intValue());
						System.out.println("saldAct: "+saldAct);
						if(saldAct < promTotal){
							this.generarRecarga(cta,promTotal);
						}
					}
				}
			}
			Query updTimer = em.createNativeQuery("update tb_timer set Timer_Last_Execution = ?1,timer_state=0 where Timer_Name = 'TimerBalance'");
			updTimer.setParameter(1, new Timestamp(System.currentTimeMillis()));			
			updTimer.executeUpdate();
			
			System.out.println("Final: "+new Timestamp(System.currentTimeMillis()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean enviaraRecarga(){
		boolean result = false;
		String parametro = "NO";
		try{
			//Query q = em.createNativeQuery("select System_Parameter_Value parametro from Tb_System_Parameter where System_Parameter_Id=23");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (23L)");
			parametro = (String) q.getSingleResult();
			
			if(parametro.equals("SI")){
				result = true;
			}else{
				result = false;
			}
		}catch(NoResultException n){
			result = false;
		}
		return result; 
	}
	
	public void generarRecarga(TbAccount cta, Long valueRecharge){
	  try{	
		  DecimalFormat n = new DecimalFormat("#,###,###,###");
		String msgProcess = "";
		Long umbral = WSrecharge.createUmbral(cta.getAccountId(), 
                new BigDecimal(0), 
                new BigDecimal(valueRecharge),
                new Timestamp(System.currentTimeMillis()),
                0L,
                1L);
		
		ObjectRechage obj = WSrecharge.createListObj(umbral);
		 InvokeSqdm rechargeWS= new InvokeSqdm();
		 String data =  rechargeWS.InvokeRecharge(obj);
		 String arrayData[] = data.split(";");
		   Long id=Long.valueOf(arrayData[0]);
		   ReAccountBank rab = null;
			Query qReAccountBank = em.createQuery("Select rab From ReAccountBank rab where rab.accountId.accountId=?1 and rab.state_account_bank=1 ");
			qReAccountBank.setParameter(1, cta.getAccountId());
			try{
			   rab = (ReAccountBank) qReAccountBank.getSingleResult();
			} catch(NoClassDefFoundError e){
				System.out.println("No se encontro ReAccountBank para la cuenta "+cta.getAccountId());
			}
		 
		String bankName = WSrecharge.getBankNameByAccount(cta.getAccountId());
		Long transactionId = WSrecharge.getTransactionByUmbral(umbral);
		if(id == 0L){
			outbox.insertMessageOutbox(cta.getTbSystemUser().getUserId(), 
			             EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
			             cta.getAccountId(),
			             rab.getBankAccountId().getBankAccountId(), 
			             transactionId, 
			             null,
			             null,
			             null,			             
			             rab.getBankAccountId().getProduct().getBankId(),
			             null,
			             0L,
			             null,
			             null,
			             null,			             
			             true,
		                   null);
			msgProcess = "Asignación de Recursos por valor de $"+n.format(valueRecharge)+" para la Cuenta FacilPass No. "+cta.getAccountId()+" aprobada por "+bankName+".";
		
		} else {
			outbox.insertMessageOutbox(cta.getTbSystemUser().getUserId(),
			EmailProcess.RESOURCE_ALLOCATION_REJECTED,
			cta.getAccountId(),
			rab.getBankAccountId().getBankAccountId(), 
            transactionId, 
            null,
            null,
            null,			             
            rab.getBankAccountId().getProduct().getBankId(),
            null,
            0L,
            null,
            null,
            null,            
			true,
            null);
			
			msgProcess = "Asignación de Recursos por valor de $"+n.format(valueRecharge)+" para la Cuenta FacilPass No. "+cta.getAccountId()+" rechazada por "+bankName+".";
		}
		
		//creacion de proceso de cliente en Mi priceso
		TbProcessTrack idProc = proceso.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, cta.getTbSystemUser().getUserId());
		Long newProc = null;
		if(idProc == null){
			newProc = proceso.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, cta.getTbSystemUser().getUserId(), "localhost", 0L);
		}else{
			newProc = idProc.getProcessTrackId();
		}
		proceso.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
		msgProcess, 0L, "localhost", 1, "Error al Crear Proceso para Recarga Manual de la cuenta "+cta.getAccountId(),null,null,null,null);
		
		//creacion de proceso de cliente
		TbProcessTrack idProcClient = proceso.searchProcess(ProcessTrackType.CLIENT, cta.getTbSystemUser().getUserId());
		Long newProcClient = null;
		if(idProcClient == null){
			newProcClient = proceso.createProcessTrack(ProcessTrackType.CLIENT, cta.getTbSystemUser().getUserId(), "localhost", 0L);
		}else{
			newProcClient = idProcClient.getProcessTrackId();
		}
		proceso.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
				msgProcess, 0L, "localhost", 1, "Error al Crear Proceso para Recarga Manual de la cuenta "+cta.getAccountId(),null,null,null,null);
	  } catch(Exception e){
		  e.printStackTrace();
	  }
	}
	
	@Override
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
	
	@Override
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
