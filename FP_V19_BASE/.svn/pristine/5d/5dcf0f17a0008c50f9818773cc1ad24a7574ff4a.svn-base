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

import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbProcessTrack;
import jpa.TbProductType;
import jpa.TbPse;
import jpa.TbSystemUser;
import util.AllInfoAccount;
import util.ReAccountBank;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.Log;
import ejb.Process;
import ejb.SendMail;
import ejb.email.Outbox;

/**
 * Session Bean implementation class BankAccountEJB
 */
@Stateless(mappedName = "ejb/BankAccount")
public class BankAccountEJB implements BankAccount {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public BankAccountEJB() {
    }

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.BankAccount#getBankAccounts()
	 */
    @Override
	public List<TbBankAccount> getBankAccounts() {
		List<TbBankAccount> list = new ArrayList<TbBankAccount>();
		try {
			Query q = em.createQuery("SELECT tbb FROM TbBankAccount tbb");
			for (Object obj : q.getResultList()) {
				list.add((TbBankAccount) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountEJB.getBankAccounts. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.BankAccount#insertBankAccount(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
    /*	@Override
	public Long insertBankAccount(String bankAccountHolder,
			String bankAccountHolderNit, String ip, Long creationUser,
			String product, String bankAccountNumber, String bankAccountType) {
		try {
			Query q = em
					.createQuery("SELECT tbb FROM TbBankAccount tbb WHERE tbb.bankAccountNumber = ?1");
			q.setParameter(1, bankAccountNumber.toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the bank account.
			TbBankAccount ta = new TbBankAccount();
			ta.setBankAccountHolder(bankAccountHolder);
			ta.setBankAccountHolderNit(bankAccountHolderNit);
			ta.setBankAccountNumber(bankAccountNumber);
			ta.setBankAccountType(bankAccountType);
			//ta.setProduct(product);
			
			emObj = new ObjectEM(em);
			
			if(emObj.create(ta)){
				log.insertLog("Creación de Cuenta De Banco | Se ha creado la cuenta de banco ID: " + ta.getBankAccountId()+ ".",
						LogReference.BANKACCOUNT, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Cuenta De Banco | No se pudo crear la cuenta de banco No.: " + bankAccountNumber + ".",
						LogReference.BANKACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountEJB.insertBankAccount. ] ");
			e.printStackTrace();
		}
		return null;
	}*/
		

	@Override
	public List<jpa.TbBankAccount> getProductsByClient(Long idBank, String userCode) {
		List<TbBankAccount> list = new ArrayList<jpa.TbBankAccount>();
		try {
			Query q = em.createQuery("SELECT tb FROM TbBankAccount tb where tb.product.bankId=?1 and tb.bankAccountHolderNit=?2")
			.setParameter(1, idBank).setParameter(2, userCode);
			for (Object obj : q.getResultList()) {
				list.add((jpa.TbBankAccount) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountEJB.getBankAccounts. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReAccountBank> getProductsByClient2(Long idClient) {
		List<ReAccountBank> list = new ArrayList<ReAccountBank>();
		try {
			Query q= em.createNativeQuery("SELECT DATE_CREATION,BANK_ACCOUNT_HOLDER,BANK_NAME,BANK_AVAL,BANK_ACCOUNT_NUMBER,PRODUCT_TYPE_DESCRIPTION,DESCRIPTION,ACCOUNT_ID,STATE_ACCOUNT_BANK "
										+" FROM VW_PRODUCTS_ASSOCIATED WHERE USRS= ?1").setParameter(1, idClient);
			
			List<Object> lis= (List<Object>)q.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				util.ReAccountBank r= new util.ReAccountBank();
				
				r.setDate(o[0]!=null?o[0].toString():"-");
				r.setClient(o[1]!=null?o[1].toString():"-");
				r.setBank(o[2]!=null?o[2].toString():"-");
				r.setBankAval(Long.parseLong(o[3].toString()));
				r.setAccount(o[4]!=null?o[4].toString():"-");
				r.setTypeProduct(o[5]!=null?o[5].toString():" ");
				r.setDesc(o[6]!=null?o[6].toString():"-");
				
				if(o[8]==null || o[8].toString().equals("3")){
					r.setAccountId("Sin Asociar");
				}else if(o[8].toString().equals("1")){
					r.setAccountId("Asociada a la Cuenta FacilPass "+ o[7].toString());
				}else if(o[8].toString().equals("2")){
					r.setAccountId("En proceso de Disociación de la Cuenta FacilPass "+ o[7].toString());
				}else if(o[8].toString().equals("4")){
					r.setAccountId("Pendiente de Aprobación de Disociación de la Cuenta FacilPass "+ o[7].toString());
				}
				
				list.add(r);
			}
		
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountEJB.getBankAccounts. ] ");
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public boolean insertBankAccountAdmin(Long clientId, Long bankId, Long typeProducts, String accountNumber, String ip, Long creationUser, Long initValue) {
		boolean res=false;
		try{
			boolean existe = this.existBankAccount(clientId, bankId, accountNumber, typeProducts);
			if(existe){
				TbSystemUser usrs= em.find(TbSystemUser.class,clientId );
				TbBank b= em.find(TbBank.class, bankId);
				
				Long idPT,idpT2;
				TbProductType tp= em.find(TbProductType.class, typeProducts);

				TbBankAccount tb= new TbBankAccount();
				tb.setBankAccountHolder(usrs.getUserNames());
				tb.setBankAccountHolderNit(usrs.getUserCode());
				tb.setBankAccountNumber(accountNumber);
				tb.setBankAccountType(tp);
				tb.setDate(new Timestamp(System.currentTimeMillis()));
				tb.setDescription("ADMINISTRADOR");
				tb.setProduct(b);
				tb.setUsrs(usrs);
				tb.setInitValueRecharge(initValue);
				
	            emObj = new ObjectEM(em);
				
				if(emObj.create(tb)){
					String email="Estimado Usuario, \r\n \r\nSe ha creado el producto bancario vinculado al banco " + b.getBankName() + " Nro. " +
							accountNumber + ". \r\n \r\nCordialmente \r\nFacilpass";
					
					//sendMail.sendMail(EmailType.CLIENT, usrs.getUserEmail(), EmailSubject.BANK_ACCOUNT, "" +email);
					outbox.insertMessageOutbox(usrs.getUserId(), 
			                   EmailProcess.BANK_PRODUCT_REGISTRATION,
			                   null,
			                   tb.getBankAccountId(), 
			                   null, 
			                   null,
			                   null,
			                   null,
			                   b.getBankId(),
			                   null,
			                   creationUser,
				               null,
				               null,
				               null,
			                   true,
			                   null);
					log.insertLog("Creación de Cuenta De Banco | Se ha creado la cuenta de banco ID: " + tb.getBankAccountId()+ ".",
							LogReference.BANKACCOUNT, LogAction.CREATE, ip, creationUser);
					
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
					TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
					if(pt==null){
						idPT=process.createProcessTrack(ProcessTrackType.CLIENT,  clientId, ip, creationUser);
						idpT2=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ip, creationUser);
						System.out.println("pt es null");
					}
					else{
						idPT=pt.getProcessTrackId();
						idpT2=ptClient.getProcessTrackId();
						System.out.println("pt no es null");
					}
					System.out.println("idPT "+ idPT);
					
					process.createProcessDetail(idPT,ProcessTrackDetailType.BANK_ACCOUNT, 
							"Se ha definido el Producto Bancario No " +accountNumber+ " del "+b.getBankName() +" con tipo de producto "
							                   +tp.getProductTypeDescription(), creationUser, ip, 1, "", null,null,null,null);
					
					process.createProcessDetail(idpT2,ProcessTrackDetailType.BANK_ACCOUNT, 
							"Se ha definido el Producto Bancario No " +accountNumber+ " del "+b.getBankName() +" con tipo de producto "
			                   +tp.getProductTypeDescription(), creationUser, ip, 1, "", null,null,null,null);
					
					
					res=true;
				} else {
					log.insertLog("Creación de Cuenta De Banco | No se pudo crear la cuenta de banco No.: " + accountNumber + ".",
							LogReference.BANKACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
					res=false;
				}
			}else{
				res=false;
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			res=false;
		}
		
		
		return res;
	}

	
	@Override
	public boolean insertBankAccountAdminPSE(Long clientId, String banking, Long idAccount, String ip, Long creationUser) {
		boolean res=false;
		try{
			System.out.println("clientId " + clientId);
			System.out.println("banking " + banking);
			System.out.println("idAccount " + idAccount);
			System.out.println("ip " + ip);
			System.out.println("creationUser " + creationUser);
			
			
			emObj = new ObjectEM(em);
			TbSystemUser usrs= em.find(TbSystemUser.class,clientId );

			Query q = em.createNativeQuery("select bank_id  from tb_bank where bank_name = ?1 ");
			q.setParameter(1, banking);
			BigDecimal bankId = (BigDecimal) q.getSingleResult();
			TbBank b= em.find(TbBank.class, bankId.longValue());
			
			Long idPT,idpT2;
			
			TbPse tp = new TbPse();
			tp.setDateCreator(new Timestamp(System.currentTimeMillis()));
			tp.setUserId(clientId);
			tp.setUserCreator(creationUser);	
			emObj.create(tp);
			
			TbBankAccount tb= new TbBankAccount();
			tb.setBankAccountHolder(usrs.getUserNames());
			tb.setBankAccountHolderNit(usrs.getUserCode());
			System.out.println("PseId " + tp.getPseId());
			tb.setBankAccountNumber(tp.getPseId().toString());
			tb.setBankAccountType(null);
			tb.setDate(new Timestamp(System.currentTimeMillis()));
			tb.setDescription("ADMIN FACILPASS");
			tb.setProduct(b);
			tb.setUsrs(usrs);
			tb.setInitValueRecharge(null);
			emObj.create(tb);
			
			
			
			TbAccount ta = em.find(TbAccount.class, idAccount);
			ta.setAccountState(7L);
			emObj.update(ta);
			
			jpa.ReAccountBank rab = new jpa.ReAccountBank();
			rab.setAccountId(ta);
			rab.setBankAccountId(tb);
			rab.setDate(new Timestamp(System.currentTimeMillis()));
			rab.setDigits(0L);
			rab.setPriority(1L);
			rab.setState_account_bank(1);
			rab.setDateDissociation(null);
			rab.setDateSendBank(null);
			rab.setDateDissosiationBank(null);
			rab.setCodeResult(null);
			rab.setCodeMoneyBack(null);

			
			if(emObj.create(rab)){
				@SuppressWarnings("unused")
				String email="Estimado Usuario, \r\n \r\nSe ha creado el producto bancario vinculado al banco " + b.getBankName() + " Nro. " +
				idAccount + ". \r\n \r\nCordialmente \r\nFacilpass";
				
				//sendMail.sendMail(EmailType.CLIENT, usrs.getUserEmail(), EmailSubject.BANK_ACCOUNT, "" +email);
				outbox.insertMessageOutbox(usrs.getUserId(), 
		                   EmailProcess.BANK_PRODUCT_REGISTRATION,
		                   null,
		                   tb.getBankAccountId(), 
		                   null, 
		                   null,
		                   null,
		                   null,
		                   b.getBankId(),
		                   null,
		                   creationUser,
			               null,
			               null,
			               null,
		                   true,
		                   null);
				log.insertLog("Creación de Cuenta De Banco | Se ha creado la cuenta de banco ID: " + tb.getBankAccountId()+ ".",
						LogReference.BANKACCOUNT, LogAction.CREATE, ip, creationUser);
				
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
				TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
				if(pt==null){
					idPT=process.createProcessTrack(ProcessTrackType.CLIENT,  clientId, ip, creationUser);
					idpT2=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ip, creationUser);
					System.out.println("pt es null");
				}
				else{
					idPT=pt.getProcessTrackId();
					idpT2=ptClient.getProcessTrackId();
					System.out.println("pt no es null");
				}
				System.out.println("idPT "+ idPT);
				
				process.createProcessDetail(idPT,ProcessTrackDetailType.BANK_ACCOUNT, 
						"La cuenta FacilPass No. " +idAccount+ " se vinculó a la entidad "+b.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
				
				process.createProcessDetail(idpT2,ProcessTrackDetailType.BANK_ACCOUNT, 
						"La cuenta FacilPass No. " +idAccount+ " se vinculó a la entidad "+b.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
				
				
				res=true;
			} else {
				log.insertLog("Creación de Cuenta De Banco | No se pudo crear la cuenta de banco No.: " + idAccount + ".",
						LogReference.BANKACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
				res=false;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			ex.getMessage();
			Long idPT,idpT2;
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
			TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
			if(pt==null){
				idPT=process.createProcessTrack(ProcessTrackType.CLIENT,  clientId, ip, creationUser);
				idpT2=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ip, creationUser);
				System.out.println("pt es null");
			}
			else{
				idPT=pt.getProcessTrackId();
				idpT2=ptClient.getProcessTrackId();
				System.out.println("pt no es null");
			}
			System.out.println("idPT "+ idPT);
			
			process.createProcessDetail(idPT,ProcessTrackDetailType.BANK_ACCOUNT, 
					"No fue posible Asociar la cuenta FacilPass " +idAccount+ "a la entidad PSE ya que se presenta el error "+ex.getMessage(), creationUser, ip, 1, "", null,null,null,null);
			
			process.createProcessDetail(idpT2,ProcessTrackDetailType.BANK_ACCOUNT, 
					"No fue posible Asociar la cuenta FacilPass " +idAccount+ "a la entidad PSE ya que se presenta el error "+ex.getMessage(), creationUser, ip, 1, "", null,null,null,null);
			
			res=false;
		}
		
		
		return res;
	}
	
	
	/*@Override
	public boolean haveAccountAssociation(Long idProduct) {
		BigDecimal id=null;
		boolean res=false;
		try{
			Query q= em.createNativeQuery("select account_bank_id from re_account_bank where bank_account_id=?1")
			.setParameter(1, idProduct);
			
			id= (BigDecimal) q.getSingleResult();
			
			if(id!=null){
				res=true;
			}
			else{
				res=false;
			}
		}catch(NoResultException ex){
			res=false;
		}
	
		return res;
	}*/
	
	@Override
	public String haveProductAssociation(Long idProduct) {
		BigDecimal accountAssociate;
		String result= null;
		try{
			
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where BANK_ACCOUNT_ID=?1 order by DATE_ASSOCIATION desc");
			query.setParameter(1, idProduct);
			accountAssociate= (BigDecimal) query.getResultList().get(0);
			if(accountAssociate!=null){
				jpa.ReAccountBank rab = em.find(jpa.ReAccountBank.class, accountAssociate.longValue());
				//Se valida si la cuenta esta en proceso de disociación
				if(rab.getState_account_bank()==2){
					if(rab.getDateSendBank() !=null){
						// el proceso se ha enviado al banco y no se ha recibio respuesta del banco
						if(rab.getDateDissosiationBank()==null){
							result="La cuenta FacilPass está en proceso de disociación, por favor intente más tarde.";
						}
						
					}
				}else if(rab.getState_account_bank()==1){
					result="Este producto ya está asociado a una cuenta Facilpass.";
				}				
				
			}else{
				return null;
			}			
			
		}catch(NoResultException ex){
			ex.printStackTrace();
			result= null;
		}catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			result= null;
		}
		return result;
	}
	
	

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

	public String getProductNumber(Long idProduct){
		String product=null;
		
		Query q= em.createNativeQuery("select bank_account_number from tb_bank_account where bank_account_id=?1");
		q.setParameter(1, idProduct);
		
		product= (String) q.getSingleResult();
		System.out.println("product" + product);
		return product;
	}

	@Override
	public List<jpa.TbBankAccount> getProductsByClient2(Long idBank, String userCode, Long codeType) {
		List<TbBankAccount> list = new ArrayList<jpa.TbBankAccount>();
		try {
			
			Query q=em.createNativeQuery("select tb.user_id from tb_system_user tb where tb.user_code=?1 and tb.code_type_id=?2");
			q.setParameter(1, userCode);
			q.setParameter(2, codeType);
			
			BigDecimal userId= (BigDecimal) q.getSingleResult();
			Long user= (userId!=null?userId.longValue():null);
			Query q1 = em.createNativeQuery("select distinct BANK_ACCOUNT_ID "
					+" from VW_PRODUCTS_ASSOCIATED_USER "
					+" where USRS=?2 "
					+" and ( "
					+"   case  "
					+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
					+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
					+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' "
					+"   end "
					+" ) = 'S' "
					+" and BANK_ID=?1 "
					+" and PRODUCT_TYPE_ID in (select distinct PRODUCT_TYPE_ID "
									+" from VW_PRODUCTS_ASSOCIATED_USER "
									+" where USRS=?2 "
									+" and ( "
									+"   case  "
									+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' "
									+"   end "
									+" ) = 'S' "
									+" and BANK_ID=?1)")
			.setParameter(1, idBank).setParameter(2,user );
			for (Object obj : q1.getResultList()) {
				list.add(em.find(TbBankAccount.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Metodo encargado de validar si el producto bancario a asociar se encuentra en proceso de disociación
	 * @author ablasquez
	 * @return true cuando el producto bancario esta en proceso de disociación, false cuando se puede asociar sin inconveniente
	 */
	@Override
	public boolean isProcessDisociationById(Long bankAccountId){
		boolean result=false;
		try{
			/** Se valida si el producto bancario a asociar se encuentra o no en proceso de disociación	 */
			Query q = em.createNativeQuery("Select account_bank_id from VW_LAST_PRODUCT_BANK where bank_account_id=?1 order by date_association desc");
			q.setParameter(1, bankAccountId);
			
			List<?> lst = q.getResultList();
			
			if(lst==null || lst.size()==0){
				result = false; 
			}
			
			BigDecimal rabId = (BigDecimal) lst.get(0);
			
			jpa.ReAccountBank rab =  em.find(jpa.ReAccountBank.class, rabId.longValue());
			
			if(rab.getState_account_bank().equals(2)){
				if(rab.getDateDissosiationBank()== null){ /* aun no se ha recibido respuesta del banco no se puede asociar*/
					 result = true;
				}else{
					if(rab.getCodeResult().equals(0L)){
						result = false;
					}else{
						result = true;				
					}
				}
			}
			return result;
		}catch(NoResultException e){
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return result;
		}
	}
	
	
	/**
	 * Método encargado de listar cuentas del cliente seleccionado 
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoAccount> getClientAccount(Long userId) {
		List<AllInfoAccount> list = new ArrayList<AllInfoAccount>();
		try {
			
			Query query = 
				 em.createNativeQuery("select DISTINCT ta.account_id FROM tb_bank_account tba " +
				 		"INNER JOIN re_account_bank rab ON tba.bank_account_id = rab.bank_account_id " +
				 		"LEFT JOIN tb_account ta ON rab.account_id=ta.account_id " +
				 		"INNER JOIN tb_bank tb ON tba.product = tb.bank_id " +
				 		"LEFT JOIN tb_account_type tat ON ta.account_type_id = tat.account_type_id " +
						"WHERE ta.user_id=?1 " +
						"AND tb.bank_aval=0 " +
						"AND rab.state_account_bank=1 ");
			    query.setParameter(1, userId);
			    List<?> query1 = (List<?>)query.getResultList();

				String associatedAccount = query1.toString().replace('[', '(').replace(']', ')');
				System.out.println("associatedAccount " + associatedAccount);
				if (associatedAccount.equals("()")){
					Query query2 = 
						  em.createNativeQuery("SELECT DISTINCT ta.account_id, ta.ACCOUNT_BALANCE, " +
						  		"decode(ta.ACCOUNT_OPENING_DATE,null,'-',TO_CHAR(ta.ACCOUNT_OPENING_DATE, 'DD/MM/YYYY HH:MI AM')), " +
						  		"tat.NAME_TYPE||' - '|| ta.PREFERENCE, tat.MAX_DEVICE, ta.ACCOUNT_STATE_TYPE_ID, 0 " +
						  		"FROM tb_bank_account tba " +
						  		"INNER JOIN re_account_bank rab ON tba.bank_account_id = rab.bank_account_id " +
						  		"LEFT JOIN tb_account ta ON rab.account_id=ta.account_id " +
						  		"INNER JOIN tb_bank tb ON tba.product = tb.bank_id " +
						  		"LEFT JOIN tb_account_type tat ON ta.account_type_id = tat.account_type_id " +
						  		"WHERE ta.user_id=?1 " +
						  		"AND tb.bank_aval=0 " +
						  		"AND rab.state_account_bank=1 " +
						  		"UNION " +
						  		"SELECT DISTINCT ta.account_id, ta.ACCOUNT_BALANCE, " +
						  		"decode(ta.ACCOUNT_OPENING_DATE,null,'-',TO_CHAR(ta.ACCOUNT_OPENING_DATE, 'DD/MM/YYYY HH:MI AM')), " +
						  		"tat.NAME_TYPE||' - '|| ta.PREFERENCE, tat.MAX_DEVICE, ta.ACCOUNT_STATE_TYPE_ID, 1 " +
						  		"FROM tb_account ta " +
						  		"LEFT JOIN tb_account_type tat ON ta.account_type_id = tat.account_type_id " +
						  		"WHERE ta.user_id=?1 " +
						  		"ORDER BY 1 ASC ");
					query2.setParameter(1, userId);
					List<Object > lis = (List<Object>) query2.getResultList();
					
					for (int i=0;i<lis.size();i++) {	
						Object[] obj= (Object[]) lis.get(i);
						AllInfoAccount ta= new AllInfoAccount();
						ta.setAccountIdU(obj[0]!=null?obj[0].toString():"-");
						ta.setAccountBalanceU(obj[1]!=null?Long.valueOf(obj[1].toString()):0);
						ta.setAccountOpeningDateU(obj[2].toString());
						ta.setNameTypeU(obj[3]!=null?obj[3].toString():"-");
						ta.setMaxDeviceU(obj[4]!=null?Long.valueOf(obj[4].toString()):0);
						ta.setAccountStateTypeU(obj[5]!=null?obj[5].toString():"-");
						ta.setBankU(obj[6]!=null?Long.valueOf(obj[6].toString()):0);
						list.add(ta);
					}
				}else{
					Query query2 = 
						  em.createNativeQuery("SELECT DISTINCT ta.account_id, ta.ACCOUNT_BALANCE, " +
						  		"decode(ta.ACCOUNT_OPENING_DATE,null,'-',TO_CHAR(ta.ACCOUNT_OPENING_DATE, 'DD/MM/YYYY HH:MI AM')), " +
						  		"tat.NAME_TYPE||' - '|| ta.PREFERENCE, tat.MAX_DEVICE, ta.ACCOUNT_STATE_TYPE_ID, 0 " +
						  		"FROM tb_bank_account tba " +
						  		"INNER JOIN re_account_bank rab ON tba.bank_account_id = rab.bank_account_id " +
						  		"LEFT JOIN tb_account ta ON rab.account_id=ta.account_id " +
						  		"INNER JOIN tb_bank tb ON tba.product = tb.bank_id " +
						  		"LEFT JOIN tb_account_type tat ON ta.account_type_id = tat.account_type_id " +
						  		"WHERE ta.user_id=?1 " +
						  		"AND tb.bank_aval=0 " +
						  		"AND rab.state_account_bank=1 " +
						  		"UNION " +
						  		"SELECT DISTINCT ta.account_id, ta.ACCOUNT_BALANCE, " +
						  		"decode(ta.ACCOUNT_OPENING_DATE,null,'-',TO_CHAR(ta.ACCOUNT_OPENING_DATE, 'DD/MM/YYYY HH:MI AM')), " +
						  		"tat.NAME_TYPE||' - '|| ta.PREFERENCE, tat.MAX_DEVICE, ta.ACCOUNT_STATE_TYPE_ID, 1 " +
						  		"FROM tb_account ta " +
						  		"LEFT JOIN tb_account_type tat ON ta.account_type_id = tat.account_type_id " +
						  		"WHERE ta.user_id=?1 " +
						  		"AND ta.account_id NOT IN " + associatedAccount +
						  		"ORDER BY 1 ASC ");
					query2.setParameter(1, userId);
					List<Object > lis = (List<Object>) query2.getResultList();
					
					for (int i=0;i<lis.size();i++) {	
						Object[] obj= (Object[]) lis.get(i);
						AllInfoAccount ta= new AllInfoAccount();
						ta.setAccountIdU(obj[0]!=null?obj[0].toString():"-");
						ta.setAccountBalanceU(obj[1]!=null?Long.valueOf(obj[1].toString()):0);
						ta.setAccountOpeningDateU(obj[2].toString());
						ta.setNameTypeU(obj[3]!=null?obj[3].toString():"-");
						ta.setMaxDeviceU(obj[4]!=null?Long.valueOf(obj[4].toString()):0);
						ta.setAccountStateTypeU(obj[5]!=null?obj[5].toString():"-");
						ta.setBankU(obj[6]!=null?Long.valueOf(obj[6].toString()):0);
						list.add(ta);
					}
				}
		} catch (Exception e) {
			System.out.println(" [ Error en BankAccountEJB.getClientAccount ] "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean existBankAccount(Long userId, Long idBank, String bankAccountNumber, Long bankAccountType) {
	    boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT bank_account_id FROM tb_bank_account " +
									 "WHERE product=?1 " +
									 "AND bank_account_type=?2 " +
									 "AND bank_account_number=?3 ");
			    //query.setParameter(1, userId);
			System.out.println("idBank: "+idBank);
			System.out.println("bankAccountType: "+bankAccountType);
			System.out.println("bankAccountNumber: "+bankAccountNumber);
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
				  //result=false;
				}else{
				  result=true; 
				}
		}catch(NoResultException ex){
			System.out.println(" [ Error en BankAccountEJB.existBankAccount. ] ");
			result=false;
		}
		return result;
	}
	
}
