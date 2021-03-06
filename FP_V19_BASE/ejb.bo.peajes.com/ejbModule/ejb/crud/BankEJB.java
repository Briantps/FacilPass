package ejb.crud;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReAgreementBank;
import jpa.TbAgreement;
import jpa.TbBank;
import jpa.TbPaymentMethod;
import jpa.TbProcessTrack;
import jpa.TbState;
import jpa.reAgreementChannel;
import util.BankTb;
import util.UListbank;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.Log;
import ejb.Process;

/**
 * Session Bean implementation class BankEJB
 */
@Stateless(mappedName = "ejb/Bank")
public class BankEJB implements Bank {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	private ObjectEM objectEM;
	
    /**
     * Default constructor. 
     */
    public BankEJB() {
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Bank#getBanks()
	 */
	@Override
	public List<TbBank> getBanks() {
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			Query q = em.createQuery("SELECT tbb FROM TbBank tbb ORDER BY bankId");
			
			for (Object obj : q.getResultList()) {			
				list.add((TbBank) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.getBanks. ] ");
			e.printStackTrace();
		}
		return list;
	}

	
	public String checAval (Long bankId){
		String bankAval = null;
		try{
			Query q = em.createNativeQuery("select bank_aval from tb_bank where bank_id = ?1 ");
			q.setParameter(1, bankId);
			BigDecimal aval = (BigDecimal) q.getSingleResult();
			if(aval.longValue() == 1L){
				bankAval = "1";
			}else {
				bankAval = "0";
			}
		}catch (Exception e) {
			bankAval = null;
			e.printStackTrace();
		}
		return bankAval;
	}
	
	
	/**
	 * M�todo encargado de listar registros de la tabla Tb_bank que tengan el campo bank_aval: 1
	 * @author psanchez
	 */
	@Override
	public List<TbBank> getBanksAval() {
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			Query q = em.createQuery("SELECT tbb FROM TbBank tbb WHERE bankAval=1");
			for (Object obj : q.getResultList()) {
				list.add((TbBank) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.getBanksAval. ] "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * M�todo encargado de verificar si el banco no pertenece al grupo Aval
	 * @author psanchez
	 */
	@Override
	public Long getOtherBanks(Long idBank) {
		Long bankId=null;
		try {
			Query query = em.createNativeQuery("SELECT bank_id FROM Tb_Bank " +
											   "WHERE bank_Aval=0 " +
											   "AND bank_id=?1 ").setParameter(1, idBank);
			bankId = ((BigDecimal) query.getSingleResult()).longValue();	
			
		} catch (NoResultException nre) {
			 return bankId;
		}
	   return bankId;
	}
	
	/**
	 * M�todo encargado de listar registros de la tabla Tb_bank
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BankTb> getBankTb() {
		List<BankTb> list = new ArrayList<BankTb>();
		try {
			
			Query query = 
				 em.createNativeQuery("SELECT DISTINCT tb.bank_id " +
				 		              "FROM re_account_bank rab " +
				 		              "INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id " +
				 		              "INNER JOIN tb_bank tb ON tba.product = tb.bank_id " +
				 		              "WHERE rab.state_account_bank = 2 " +
				 		              "AND tb.bank_aval = 1 ");
			    List<?> query1 = (List<?>)query.getResultList();

				String associatedAccount = query1.toString();
				associatedAccount=associatedAccount.replace('[', '(').replace(']', ')');
				
				Query query2 = 
					  em.createNativeQuery("SELECT DISTINCT tb.bank_id, tb.bank_name, tb.bank_code, tb.bank_aval, 0 "+ 
									  		"FROM re_account_bank rab "+
									  		"INNER JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id "+
									  		"INNER JOIN tb_bank tb ON tba.product=tb.bank_id "+
									  		"WHERE rab.state_account_bank!=3 "+
									  		"AND tb.bank_aval in(0,1) "+ 
									  		"UNION "+
	                                        "SELECT DISTINCT tb.bank_id, tb.bank_name, tb.bank_code, tb.bank_aval, 1 "+
					 		                "FROM re_account_bank rab "+
					 		                "INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id "+ 
					 		                "INNER JOIN tb_bank tb ON tba.product = tb.bank_id "+
					 		                "WHERE rab.state_account_bank = 3"+                       
					 		                "AND tb.bank_aval in(0,1) "+           
	                                        "AND tb.bank_id not in (SELECT DISTINCT tb.bank_id "+
	                                                  "FROM re_account_bank rab "+
	                                                  "INNER JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id "+
	                                                  "INNER JOIN tb_bank tb ON tba.product=tb.bank_id  "+
	                                                  "WHERE rab.state_account_bank!=3 "+
	                                                  "AND tb.bank_aval in(0,1)) "+
	                                         "UNION "+
	                                "SELECT  tb.bank_id, tb.bank_name, tb.bank_code, tb.bank_aval, 1 "+ 
	                                "from tb_bank tb "+
	                                "LEFT OUTER join  TB_BANK_ACCOUNT tba on (tb.BANK_ID=tba.PRODUCT) "+
	                                "where tba.PRODUCT is null "+
	                                "union "+
	                                " SELECT DISTINCT tb.bank_id, tb.bank_name, tb.bank_code, tb.bank_aval, 1 "+
	                                                  "FROM re_account_bank rab "+ 
	                                                  "RIGHT outer JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id "+
	                                                  "RIGHT outer JOIN tb_bank tb ON tba.product=tb.bank_id "+ 
	                                                  "where tba.PRODUCT in ( "+
	                               "select DISTINCT tb.BANK_ID from  "+
	                               "RE_ACCOUNT_BANK rab "+
	                               "RIGHT outer join tb_bank_account tba on (rab.ACCOUNT_BANK_ID=tba.BANK_ACCOUNT_ID) "+
	                               "LEFT OUTER JOIN TB_BANK tb ON (tb.BANK_ID=tba.PRODUCT ) "+
	                               "where rab.ACCOUNT_BANK_ID is null) "+
	                            "and tb.BANK_ID not in (SELECT DISTINCT tb.bank_id "+
					 		              "FROM re_account_bank rab "+
					 		              "INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id  "+
					 		              "INNER JOIN tb_bank tb ON tba.product = tb.bank_id)");
			List<Object > lis = (List<Object>) query2.getResultList();
			for (int i=0;i<lis.size();i++) {	
				Object[] obj= (Object[]) lis.get(i);
				BankTb tb = new BankTb();
				tb.setBankId(obj[0]!=null?Long.valueOf(obj[0].toString()):0);
				tb.setBankName(obj[1]!=null?obj[1].toString():"");
				tb.setBankCode(obj[2]!=null?obj[2].toString():"");
				tb.setBankAval(obj[3]!=null?Long.valueOf(obj[3].toString()):0);
				tb.setBankAvalCheck(obj[4]!=null?Long.valueOf(obj[4].toString()):0);
				list.add(tb);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.getBankTb ] "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * M�todo encargado de validar registros en la tabla Tb_bank 
	 * @author psanchez
	 */
	@Override
	public String existBank(Long bankId, String bankName, String bankLetter, int i) {
	    String result=null;
		try {
			if(i==1){
				Query query= 
					em.createNativeQuery("SELECT count(bank_id) " +
						                 "FROM tb_bank " +
									     "WHERE bank_id = ?1 ").setParameter(1, bankId);
				    BigDecimal idBank = (BigDecimal) query.getSingleResult();
	
				Query query1= 
					em.createNativeQuery("SELECT count(bank_id) " +
							             "FROM tb_bank " +
										 "WHERE upper(bank_name) = upper(?1) ").setParameter(1, bankName.trim());
			        BigDecimal nameBank = (BigDecimal) query1.getSingleResult();
	
				Query query2 = 
					em.createNativeQuery("SELECT count(bank_id) " +
							             "FROM tb_bank " +
										 "WHERE upper(bank_code) = upper(?1) ").setParameter(1, bankLetter.trim());
				    BigDecimal letterBank = (BigDecimal) query2.getSingleResult();	
	
				if(idBank.intValue()>=1){
					result="El c�digo de la entidad ya existe en el sistema.";
				}else if(letterBank.intValue()>=1){
					result="La letra de la entidad ya existe en el sistema.";	
				}else if(nameBank.intValue()>=1){
				    result="El nombre de la entidad ya existe en el sistema."; 
				}
			}else if(i==2){
				Query query= 
					em.createNativeQuery("SELECT count(bank_id) " +
							             "FROM tb_bank " +
										 "WHERE bank_id <> ?1 " +
										 "AND (upper(bank_name)=upper(?2)) ");
				query.setParameter(1, bankId);
				query.setParameter(2, bankName.trim());
				
				BigDecimal idBank =  (BigDecimal) query.getSingleResult();
				if(idBank.intValue()>=1){
			       result="El nombre de la entidad ya existe en el sistema."; 
				}
			}
		} catch (Exception e) {
			System.out.println("Error bankEJB.existBank:"+e.getMessage());
			result= "La entidad no ha sido creada.";
		}
		return result;
	}
	
	/**
	 * M�todo encargado de editar registros en la tabla Tb_bank 
	 * @author psanchez
	 */
	@Override
	public String editBank(Long bankId, String bankName, Long bankAval, String ip, Long creationUser) {
	 String result=null;
	   try {
			TbBank tb = em.find(TbBank.class, Long.valueOf(bankId));
		    tb.setBankName(bankName.trim());
		    tb.setBankAval(bankAval);
		    
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tb)){	
				log.insertLog("Editar Entidad | Se ha actualizado la Entidad ID: " + tb.getBankId()+ 
						". Antes Entidad - AVAL: " +tb.getBankName() +" - "+ tb.getBankAval(), 
						LogReference.BANK, LogAction.UPDATE, ip, creationUser);
				result= "La entidad ha sido editada con �xito.";
			} else {
				log.insertLog("Editar Entidad | No se ha actualizado la Entidad ID: " + tb.getBankId()+ 
						". Antes Entidad - AVAL: " +tb.getBankName() +" - "+ tb.getBankAval(), 
						LogReference.BANK, LogAction.UPDATE, ip, creationUser);
				result= "La entidad no ha sido editada.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.editBank ] "+e.getMessage());
			result= "La entidad no ha sido editada.";
		}
		return result;
	}
	
	/**
	 * M�todo creado para obtener nombre del banco
	 * @author psanchez
	 */
	@Override
	public String haveBankName(Long idBank) {
		String bankName = null;
		try {
			Query query= em.createNativeQuery("SELECT bank_name FROM tb_bank WHERE bank_id=?1 ");
			query.setParameter(1, idBank);
			bankName = (String) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(" [ Error en bankEJB.haveBankName] "+e.getMessage());
			e.printStackTrace();
		}
		return bankName;
	}

	/**
	 * M�todo encargado de insertar registros en la tabla Tb_bank
	 * @author psanchez
	 */
	@Override
	public String insertBank(Long bankId, String bankName, String bankLetter,
			Long bankAval, String ip, Long creationUser) {
		String result=null;
		try {
			TbBank tb = new TbBank();	
			tb.setBankId(Long.valueOf(bankId));
			tb.setBankName(bankName.trim());
			tb.setBankCode(bankLetter.toUpperCase().trim());		
			tb.setBankAval(bankAval);
			tb.setBankProgramming((long) 0);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tb)){		
				log.insertLog("Creaci�n de Banco | Se ha creado el banco ID: " + tb.getBankId() + ".",
					LogReference.BANK, LogAction.CREATE, ip, creationUser);
			    result= "La entidad ha sido creada con �xito.";
			}else{
				log.insertLog("Creaci�n de Banco | No se ha creado el banco ID: " + tb.getBankId() + ".",
						LogReference.BANK, LogAction.CREATE, ip, creationUser);
				result= "La entidad no ha sido creada.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en bankEJB.insertBank ] "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	
	
	//-----------frd140-recarga online-->Administrar Convenio
		@SuppressWarnings("unused")
		@Override
		/**
		 * M�todo crea el convenio 
		 * @author psanchez
		 */
		public boolean insertAgreement(Long codeAgreement, String nameAgreement, Long paymentMethodId, String descriptionAgreement, 
				Long bankId, Long agreementMinimumActive, Long agreementMinimumAllocation, String ip, Long creationUser) {
			Long agreemnetChannelId=-1L, count=0L;

			try {
				ReAgreementBank reAgreementBank = new ReAgreementBank();
				TbAgreement tbAgreement = new TbAgreement();

				tbAgreement.setCodeAgreement(codeAgreement);
				tbAgreement.setNameAgreement(nameAgreement);
				tbAgreement.setTbPaymentMethod(em.find(TbPaymentMethod.class, Long.valueOf(paymentMethodId)));
				tbAgreement.setDescriptionAgreement(descriptionAgreement.trim());
				tbAgreement.setTbState(em.find(TbState.class, 1L));
				tbAgreement.setMinimumActive(agreementMinimumActive);
				tbAgreement.setMinimumValue(agreementMinimumAllocation);
				
				reAgreementBank.setAgreementId(tbAgreement);
				reAgreementBank.setBankId(em.find(TbBank.class, bankId));
				reAgreementBank.setStateId(em.find(TbState.class, 1L));
				reAgreementBank.setDateCreateRelation(new Timestamp(System.currentTimeMillis()));
				reAgreementBank.setDateInactiveRelation(null);

				if(new ObjectEM(em).create(tbAgreement) && new ObjectEM(em).create(reAgreementBank)){
				log.insertLog("Creaci�n de Convenio | Se ha creado el Convenio ID: " + tbAgreement.getAgreementId() + ".",
					LogReference.AGREEMENT, LogAction.CREATE, ip, creationUser);
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AGREEMENT, 
						"Se ha creado el convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
						creationUser, ip,1,"Error al crear el convenio",null,null,null,null);			
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AGREEMENT, 
						"Se ha creado el convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
						creationUser, ip, 1, "Error al crear el convenio",null,null,null,null);
				return true;
				}else{
					log.insertLog("Creaci�n de Convenio | No se ha creado el Convenio ID: " + tbAgreement.getAgreementId() + ".",
							LogReference.AGREEMENT, LogAction.CREATE, ip, creationUser);
					return false;
				}
			} catch (Exception e) {
				System.out.println(" [ Error en bankEJB.insertAgreement ] "+e.getMessage());
				e.printStackTrace();
			}
			return false;
	}
	
	
		/**
		 * M�todo crea registro convenio cuando al modificar la entidad es diferente a la existente en bd
		 * @author psanchez
		 */
		public boolean validateRelationAgreementBank(Long idAgreement, Long codeAgreement, Long bankId) {
			try {		
				ReAgreementBank reAgreementBank = new ReAgreementBank();
				reAgreementBank.setAgreementId(em.find(TbAgreement.class, idAgreement));
				reAgreementBank.setBankId(em.find(TbBank.class, bankId));
				reAgreementBank.setStateId(em.find(TbState.class, 1L));
				reAgreementBank.setDateCreateRelation(new Timestamp(System.currentTimeMillis()));
				reAgreementBank.setDateInactiveRelation(null);
				if(new ObjectEM(em).create(reAgreementBank)){
					return true;
				} else {
					return false;	
				}
			} catch (Exception e) {
				System.out.println(" [ Error en bankEJB.validateRelationAgreementBank ] "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}

	/**
	 * M�todo lista los convenio 
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BankTb> getListAllAgreement() {
		List<BankTb> list = new ArrayList<BankTb>();
		try {
				Query query = 
					  em.createNativeQuery("select rab.AGREEMENT_BANK_ID, " +
					  		               "ta.AGREEMENT_ID, " +
					  		               "ta.CODE_AGREEMENT, " +
					  		               "ta.NAME_AGREEMENT, " +
					  		               "tp.PAYMENT_METHOD_ID, " +
					  		               "tp.PAYMENT_METHOD, " +
					  		               "tb.BANK_ID, " +
					  		               "tb.BANK_NAME, " +
					  		               "ta.DESCRIPTION_AGREEMENT, " +
					  		               "ta.AGREEMENT_MINIMUM_ACTIVE, " +
					  		               "ta.AGREEMENT_MINIMUM_ALLOCATION, " +
					  		               "rab.STATE_ID, " +
					  		               "ts.NAME_STATE, " +
					  		               "count(tu.AGREEMENT_CHANEL_ID) " +
					  		               "from RE_AGREEMENT_BANK rab " +
					  		               "inner join TB_AGREEMENT ta on rab.AGREEMENT_ID = ta.AGREEMENT_ID " +
					  		               "inner join TB_BANK tb on rab.BANK_ID = tb.BANK_ID " +
					  		               "inner join TB_STATE ts on rab.STATE_ID = ts.STATE_ID " +
					  		               "inner join TB_PAYMENT_METHOD tp on ta.PAYMENT_METHOD_ID = tp.PAYMENT_METHOD_ID " +
					  		               "left join RE_AGREEMENT_CHANEL rac on rab.AGREEMENT_ID = rac.AGREEMENT_ID " +
					  		               "left join TB_UMBRAL tu on rac.AGREEMENT_CHANEL_ID = tu.AGREEMENT_CHANEL_ID " +
					  		               "inner join " +
					  		               "(select rab.AGREEMENT_ID, max(rab.DATE_CREATE_RELATION) fecha " +
			                               "from RE_AGREEMENT_BANK rab " +
			                               "group by rab.AGREEMENT_ID) rel " +
			                               "on rab.DATE_CREATE_RELATION = rel.fecha " +
			                               "where rab.STATE_ID <> 2 " +
					  		               "group by rab.AGREEMENT_BANK_ID, ta.AGREEMENT_ID, ta.CODE_AGREEMENT, ta.NAME_AGREEMENT, tp.PAYMENT_METHOD_ID, " +
					  		               "tp.PAYMENT_METHOD, tb.BANK_ID, tb.BANK_NAME, ta.DESCRIPTION_AGREEMENT, ta.AGREEMENT_MINIMUM_ACTIVE, " +
					  		               "ta.AGREEMENT_MINIMUM_ALLOCATION, rab.STATE_ID, ts.NAME_STATE " +
					  		               "order by 3 ");
			List<Object > lis = (List<Object>) query.getResultList();
			for (int i=0;i<lis.size();i++) {	
				Object[] obj= (Object[]) lis.get(i);
				BankTb tb = new BankTb();
				tb.setIdAgreementBank(obj[0]!=null?Long.valueOf(obj[0].toString()):0);
				tb.setIdAgreement(obj[1]!=null?Long.valueOf(obj[1].toString()):0);
				tb.setCodeAgreement(obj[2]!=null?Long.valueOf(obj[2].toString()):0);
				tb.setNameAgreement(obj[3]!=null?obj[3].toString():"");
				tb.setPaymentMethodId(obj[4]!=null?Long.valueOf(obj[4].toString()):0);
				tb.setNamePaymentMethod(obj[5]!=null?obj[5].toString():"");
				tb.setBankId(obj[6]!=null?Long.valueOf(obj[6].toString()):0);
				tb.setBankName(obj[7]!=null?obj[7].toString():"");
				tb.setDescriptionAgreement(obj[8]!=null?obj[8].toString():"");
				tb.setActiveRecharge(obj[9]!=null?Long.valueOf(obj[9].toString()):0);
				tb.setRechargeValue(formateador((obj[10]!=null?((BigDecimal)obj[10]):new BigDecimal(0))));
				tb.setIdState(obj[11]!=null?Long.valueOf(obj[11].toString()):0);
				tb.setStateReAgreementBank(obj[12]!=null?obj[12].toString():"");
				tb.setDeleteAgreement(obj[13]!=null?Long.valueOf(obj[13].toString()):0);
				list.add(tb);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.getListAllAgreement ] "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * M�todo valida los datos ingresados en el formulario modificaci�n y creaci�n del convenio
	 * @author psanchez
	 */
	@Override
	public String validateAgreement(Long idAgreementBank, Long codeAgreement, String nameAgreement, Long bankId, String descriptionAgreement, int i) {
		Long existCodeAgreement=-1L;
		String message=null;
		try {
			if(i==1){
				Query query = em.createNativeQuery("select count(rab.AGREEMENT_BANK_ID) " +
						"from RE_AGREEMENT_BANK rab " +
						"inner join TB_AGREEMENT ta on rab.AGREEMENT_ID = ta.AGREEMENT_ID " +
						"where rab.STATE_ID in (0,1) " +
						"and ta.CODE_AGREEMENT = ?1 ");		
				
						query.setParameter(1, codeAgreement);
						existCodeAgreement = ((BigDecimal) query.getSingleResult()).longValue();
						
						if(existCodeAgreement>0){
							message = "El c�digo del convenio ya existe.";
						}
			}else if (i==2){
				Query query = em.createNativeQuery("select count(rab.AGREEMENT_BANK_ID) " +
						"from RE_AGREEMENT_BANK rab " +
						"inner join TB_AGREEMENT ta on rab.AGREEMENT_ID = ta.AGREEMENT_ID " +
						"where rab.STATE_ID in (0,1) " +
						"and rab.AGREEMENT_BANK_ID <> ?1 " +
						"and ta.CODE_AGREEMENT = ?2 ");
						
						query.setParameter(1, idAgreementBank);
						query.setParameter(2, codeAgreement);
						existCodeAgreement = ((BigDecimal) query.getSingleResult()).longValue();
						if(existCodeAgreement==1){
							message = "El c�digo del convenio ya existe.";
						}
				}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.validateUpdateAgreement()] ");
			e.printStackTrace();
		}
		return message;
	}
	

	/**
	 * M�todo actualiza el convenio
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean updateAgreement(Long idAgreement, Long idAgreementBank, Long codeAgreement, String nameAgreement,  
			Long paymentMethodId, Long bankId, String descriptionAgreement, Long stateId, Long agreementMinimumActive, 
			Long agreementMinimumAllocation, String ip, Long creationUser) {
		try {
			TbAgreement tbAgreement = em.find(TbAgreement.class, Long.valueOf(idAgreement));
			ReAgreementBank reAgreementBank = em.find(ReAgreementBank.class, Long.valueOf(idAgreementBank));
	
			tbAgreement.setCodeAgreement(codeAgreement);
			tbAgreement.setNameAgreement(nameAgreement);
			tbAgreement.setTbPaymentMethod(em.find(TbPaymentMethod.class, Long.valueOf(paymentMethodId)));
			tbAgreement.setDescriptionAgreement(descriptionAgreement);
			tbAgreement.setMinimumActive(agreementMinimumActive);
			tbAgreement.setMinimumValue(agreementMinimumAllocation);
			
			reAgreementBank.setAgreementId(tbAgreement);
			reAgreementBank.setBankId(em.find(TbBank.class, Long.valueOf(bankId)));
			reAgreementBank.setStateId(em.find(TbState.class, Long.valueOf(stateId)));
		    
			if(new ObjectEM(em).update(tbAgreement) && new ObjectEM(em).update(reAgreementBank)){	
				log.insertLog("Editar Convenio | Se ha modificado el Convenio ID: " + tbAgreement.getAgreementId()+ 
						". Antes: " +tbAgreement.getCodeAgreement() +" - "+tbAgreement.getNameAgreement() +" - " +
					    tbAgreement.getDescriptionAgreement()+" - "+reAgreementBank.getBankId().getBankName(),
						LogReference.AGREEMENT, LogAction.UPDATE, ip, creationUser);
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AGREEMENT, 
						"Se ha modificado el convenio "+nameAgreement+" con el c�digo "+codeAgreement+".",
						creationUser, ip,1,"Error al modificar el convenio",null,null,null,null);			
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AGREEMENT, 
						"Se ha modificado el convenio "+nameAgreement+" con el c�digo "+codeAgreement+".", 
						creationUser, ip, 1, "Error al modificar el convenio",null,null,null,null);					
				return true;
			} else {
				log.insertLog("Editar Convenio | No se ha modificado el Convenio ID: " + tbAgreement.getAgreementId()+ 
						". Antes: " +tbAgreement.getCodeAgreement() +" - "+tbAgreement.getNameAgreement() +" - " +
					    tbAgreement.getDescriptionAgreement()+" - "+reAgreementBank.getBankId().getBankName(),
						LogReference.AGREEMENT, LogAction.UPDATEFAIL, ip, creationUser);
				return false;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en bankEJB.updateAgreement ] "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * M�todo actualiza el estado del convenio
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean updateEstateAgreement(Long idAgreement, Long idAgreementBank, Long idState, String ip, Long creationUser) {
		try {
				TbState tbState = em.find(TbState.class, Long.valueOf(idState));
				ReAgreementBank reAgreementBank = em.find(ReAgreementBank.class, Long.valueOf(idAgreementBank));
				TbAgreement tbAgreement = em.find(TbAgreement.class, Long.valueOf(idAgreement)); 
				
				if(tbState.getStateId().intValue()==1) {
					reAgreementBank.setStateId(em.find(TbState.class, Long.valueOf(0)));
					reAgreementBank.setDateInactiveRelation(new Timestamp(System.currentTimeMillis()));
					tbAgreement.setTbState(em.find(TbState.class, Long.valueOf(0)));
				}
				else if(tbState.getStateId().intValue()==0){
					reAgreementBank.setStateId(em.find(TbState.class, Long.valueOf(1)));
					tbAgreement.setTbState(em.find(TbState.class, Long.valueOf(1)));
			    }
				
				objectEM = new ObjectEM(em);	
				if(objectEM.update(reAgreementBank) && objectEM.update(tbAgreement)){	
					log.insertLog("Editar Convenio | Se ha modificado el estado de la relaci�n Convenio-Banco ID: " +
					reAgreementBank.getAgreementBankId()+". Antes: "+tbState.getNameState(),LogReference.AGREEMENT, LogAction.UPDATE, ip, creationUser);
					//proceso administrador
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AGREEMENT, 
							"Se ha modificado el estado del convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
							creationUser, ip,1,"Error al modificar el estado del convenio",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AGREEMENT, 
							"Se ha modificado el estado del convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
							creationUser, ip, 1, "Error al modificar el estado del convenio",null,null,null,null);
					return true;
				} else {
					log.insertLog("Editar Convenio | No se ha modificado el estado Convenio-Banco ID: " +
					reAgreementBank.getAgreementBankId()+". Antes: " +tbState.getNameState(),LogReference.AGREEMENT, LogAction.UPDATE, ip, creationUser);
					return false;
				}
		} catch (Exception e) {
			System.out.println(" [ Error en bankEJB.updateEstateAgreement ] "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * M�todo elimina el convenio caundo no esta asociado en la tabla tb_umbral
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean deleteAgreement(Long idAgreement, Long idAgreementBank, String ip, Long creationUser) {
		Long existAgreementChannel=-1L;
		Long idAgreementChannel=-1L;
		try {
			TbAgreement tbAgreement = em.find(TbAgreement.class, Long.valueOf(idAgreement)); 
			ReAgreementBank reAgreementBank = em.find(ReAgreementBank.class, Long.valueOf(idAgreementBank));
			TbState tbState = em.find(TbState.class, 2L);
			
			Query query = em.createNativeQuery("select count(rac.AGREEMENT_CHANEL_ID) " +
					"from RE_AGREEMENT_CHANEL rac " +
					"inner join TB_AGREEMENT ta on rac.AGREEMENT_ID = ta.AGREEMENT_ID " +
					"where rac.STATE_ID = 1 " +
					"and rac.AGREEMENT_ID = ?1 ");	
			
    			query.setParameter(1, idAgreement);
    			existAgreementChannel = ((BigDecimal) query.getSingleResult()).longValue();
    			
    			if(existAgreementChannel>0){    
    				Query query2 = em.createNativeQuery("select rac.AGREEMENT_CHANEL_ID " +
    					"from RE_AGREEMENT_CHANEL rac " +
    					"inner join TB_AGREEMENT ta on rac.AGREEMENT_ID = ta.AGREEMENT_ID " +
    					"where rac.STATE_ID = 1 " +
    					"and rac.AGREEMENT_ID = ?1 ");	
    			
        			query2.setParameter(1, idAgreement);
        			idAgreementChannel = ((BigDecimal) query2.getSingleResult()).longValue();
        			
        			reAgreementChannel reAgreementChannel = em.find(reAgreementChannel.class, Long.valueOf(idAgreementChannel));
        			reAgreementChannel.setStateId(tbState.getStateId());
        			new ObjectEM(em).update(reAgreementChannel);
    			}

    			reAgreementBank.setStateId(tbState);
    			tbAgreement.setTbState(tbState);
    			
    		objectEM = new ObjectEM(em);	
    		if(objectEM.update(reAgreementBank) && objectEM.update(tbAgreement)){	
    			log.insertLog("Eliminar Convenio | Se ha eliminado la relaci�n Convenio-Banco ID: " + 
    					reAgreementBank.getAgreementBankId(),LogReference.AGREEMENT, LogAction.UPDATE, ip, creationUser);
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AGREEMENT, 
						"Se ha eliminado el estado del convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
						creationUser, ip,1,"Error al eliminar el estado del convenio",null,null,null,null);			
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AGREEMENT, 
						"Se ha eliminado el estado del convenio "+tbAgreement.getNameAgreement()+" con el c�digo "+tbAgreement.getCodeAgreement()+".",
						creationUser, ip, 1, "Error al eliminar el estado del convenio",null,null,null,null);
				return true;
			} else {
				log.insertLog("Eliminar Convenio | No se ha eliminado la relaci�n Convenio-Banco ID: " + 
						reAgreementBank.getAgreementBankId(),LogReference.AGREEMENT, LogAction.UPDATE, ip, creationUser);
				return false;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en bankEJB.deleteQuestion ] "+e.getMessage());
			e.printStackTrace();
		}
		return false;		
	}
	
	public List<UListbank> getListBankAutomaticProgramming() {

				
		ArrayList<UListbank> list = null;
		
		
		try {
			list = new ArrayList<UListbank>();
			Query q = em.createNativeQuery("select * from  tb_bank");
			
			@SuppressWarnings("unchecked")
			List<Object> lis= (List<Object>)q.getResultList();
			System.out.println("Estoy llenando el objeto " + lis.size());
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				UListbank uLB= new UListbank();
				
				uLB.setBankId(o[0]!=null?Long.parseLong(o[0].toString()):0L);
				uLB.setBankName(o[1]!=null?o[1].toString():"-");
				uLB.setBankAval(Long.parseLong(o[3].toString())!=0?"SI":"NO");
				uLB.setBankProgramming(Long.parseLong(o[4].toString())!=0?true:false);
				uLB.setBankUrl(o[5]!=null?o[5].toString():"-");
								
				list.add(uLB);						 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error a consultar getListBankAutomaticProgramming");
		}
		return list;
	}
	
	@SuppressWarnings("unused")
	public boolean setListBankAutomaticProgramming(List<UListbank> updateList, String modifyEntities, Long userId, String ip) throws Exception
	{
		try {
			
			boolean logDetails=false;
			
			for (UListbank uListbank : updateList)
			{					
				Query update = em.createNativeQuery("update TB_BANK set BANK_URL=?1 , BANK_PROGRAMMING=?2 where BANK_ID = ?3 ");
				update.setParameter(1, uListbank.getBankUrl());
				update.setParameter(2, uListbank.isBankProgramming()?1L:0L);
				update.setParameter(3, uListbank.getBankId().longValue());
				
				if(update.executeUpdate()>0)
				{
				 logDetails=true;					
				}
			}
			
			
			if(logDetails)
			{
			 TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
			 Long idPTA;
			 if(pt==null)
			 {
			  idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, userId);
			 }
			 else
			 {
			  idPTA=pt.getProcessTrackId();
			 }
			 Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AUTOMATIC_PROGRAMMING_ENTITIES, 
						"Se realizaron las siguientes configuraciones para la recarga programada: \n\r" + modifyEntities,
						userId, ip,1,"Error en la Asignacion",null,null,null,null);			

				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, 1L);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailB=process.createProcessDetail(idPTC,ProcessTrackDetailType.AUTOMATIC_PROGRAMMING_ENTITIES, 
						"Se realizaron las siguientes configuraciones para la recarga programada: \n\r" + modifyEntities,
						userId, ip,1,"Error en la Asignacion",null,null,null,null);	
			}
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error en BankEJB.setListBankAutomaticProgramming al actualizar los datos");
			throw new Exception(e);
		}
	}
	
	public Long getPlaymentMethodAccount(Long accountId){
		Long Resp=0L;
		try {
			Resp= ((Long) em.createQuery("Select paymentMethodId.paymentMethodId from ReAccountBank where accountId.accountId = ?1 and state_account_bank = 1").setParameter(1, accountId).getSingleResult());
			return Resp;
		}catch (NoResultException e) {
			System.out.println("Error en getPlaymentMethodAccount NoResultException " + accountId);
		}catch (NullPointerException e) {
			System.out.println("Error en getPlaymentMethodAccount NullPointerException " + accountId);
		}catch (Exception e) {
			System.out.println("Error en getPlaymentMethodAccount Exception " + accountId);
			e.printStackTrace();
		}
		return Resp;
	}
	
	/**
	 * M�todo valida la existencia del convenio antes de crear o modificar el convenio
	 * @author psanchez
	 */
	@Override
	public Long existAgreement(Long idAgreementBank, Long codeAgreement, int i) {
		Long existCodeAgreement=-1L;
		Long count=0L;
		try {
			if(i==1){//crear
				Query query = em.createNativeQuery("select count(rab.AGREEMENT_BANK_ID) " +
						"from RE_AGREEMENT_BANK rab " +
						"inner join TB_AGREEMENT ta on rab.AGREEMENT_ID = ta.AGREEMENT_ID " +
						"where rab.STATE_ID in (0,1) " +
						"and ta.CODE_AGREEMENT = ?1 ");		
				
						query.setParameter(1, codeAgreement);
						count = ((BigDecimal) query.getSingleResult()).longValue();
						
			}else if (i==2){//modificar 
				Query query = em.createNativeQuery("select count(rab.AGREEMENT_BANK_ID) " +
						"from RE_AGREEMENT_BANK rab " +
						"inner join TB_AGREEMENT ta on rab.AGREEMENT_ID = ta.AGREEMENT_ID " +
						"where rab.STATE_ID in (0,1) " +
						"and rab.AGREEMENT_BANK_ID <> ?1 " +
						"and ta.CODE_AGREEMENT = ?2 ");
						
						query.setParameter(1, idAgreementBank);
						query.setParameter(2, codeAgreement);
						count = ((BigDecimal) query.getSingleResult()).longValue();
				}
			if(count>0){
				existCodeAgreement=1L;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BankEJB.existAgreement] "+e.getMessage());
			e.printStackTrace();
			existCodeAgreement = 0L;
		}
		return existCodeAgreement;
	}
	
	private String formateador (BigDecimal number){		 
		 String valueChargeTxt = "";		 
		 if(number != null && number.compareTo(BigDecimal.ZERO) > 0){
			 DecimalFormat formateador = new DecimalFormat("###,###.##");
			 valueChargeTxt = formateador.format(number);
			 valueChargeTxt = "$"+valueChargeTxt.replaceAll(",", ".");
		 }			 
		return valueChargeTxt;		 
	 }
	
	//BM
	@Override
	public boolean getpermission(Long userId, String nameperm) {
		long result = 0L;
		boolean resp = false;
			try {
				
				System.out.println("getpermission EJB. Entre a Validar Permisos " + userId + "," + nameperm);
				Query q = em.createNativeQuery ("select count(*) from re_role_option_action rroa "+
													"inner join re_option_action roa on roa.option_action_id = rroa.option_action_id "+
													"inner join re_user_role ruo on rroa.role_id  = ruo.role_id  "+
													"inner join tb_system_user tu on tu.user_id = ruo.user_id "+
													"where tu.user_id = ?1 "+
													"and roa.behavior = ?2 "+
													"and roa.option_action_state = 2");
				q.setParameter(1, userId);
				q.setParameter(2, nameperm);
				result =((BigDecimal) q.getSingleResult()).longValue();
					if (result > 0){
						resp = true;
					}
			}catch(NoResultException ex){
				resp = false;
			}catch(Exception e){
				e.printStackTrace();
				resp = false;
			}
			return resp;	
		}
  }
