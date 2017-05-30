package ejb.crud;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.LogAction;
import constant.LogReference;

import jpa.TbCompany;
import jpa.TbCompanyType;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class CompanyEJB
 */
@Stateless(mappedName = "ejb/Company")
public class CompanyEJB implements Company {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public CompanyEJB() {
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Company#getCompany()
	 */
	@Override
	public List<TbCompany> getCompany() {
		List<TbCompany> list = new ArrayList<TbCompany>();
		try {
			Query q = em.createQuery("SELECT tc FROM TbCompany tc ORDER By tc.companyName");
			for (Object obj : q.getResultList()) {
				list.add((TbCompany) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.getCompany. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Company#insertCompany(java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	/*@Override
	public Long insertCompany(String companyNit,String company, Long companyTypeId, String ip,
			Long creationUser,  String fideicomiso, String nitFideicomiso, String nroContrato) {
		try {
			Query q = em
					.createQuery("SELECT tc FROM TbCompany tc WHERE tc.companyName = ?1 or tc.companyNIT=?2");
			q.setParameter(1, company.toUpperCase());
			q.setParameter(2, companyNit);
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			
			System.out.println("Creando concesión");

			
			System.out.println("company: " + company);
            System.out.println("fideicomiso: " + fideicomiso);
			 
			Query q1= em.createNativeQuery("insert into tb_company values (TB_COMPANY_SEQ.NEXTVAL, SUBSTRB(?1,0,80),?2,SUBSTRB(?3,0,80),?4,SUBSTRB(?5,0,50),?6)");
			q1.setParameter(1, company.toUpperCase());
			q1.setParameter(2,companyTypeId );
			q1.setParameter(3, fideicomiso);
			q1.setParameter(4, nitFideicomiso);
			q1.setParameter(5, nroContrato);
			q1.setParameter(6, companyNit);
			q1.executeUpdate();
			em.flush();

			return 0L;
			
			// creating the company.
//			TbCompany tc = new TbCompany();
//			tc.setCompanyNIT(companyNit);
//			tc.setCompanyName(com.toUpperCase());
//			tc.setTbCompanyType(em.find(TbCompanyType.class, companyTypeId));
//			tc.setFideicomiso(fid);
//			tc.setNitFideicomiso(nitFideicomiso);
//			tc.setNroContrato(nroContrato);
//            System.out.println("com: " + com);
//            System.out.println("fid: " + fid);
//
//			emObj = new ObjectEM(em);
//			if (emObj.create(tc)) {
//				log.insertLog("Creación de Empresa | Se ha creado la empresa con nombre: " + company + ".",
//						LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
//				return 0L;
//			} else {
//				log.insertLog("Creación de Empresa | No se pudo crear la empresa: " + company + ".",
//						LogReference.COMPANY, LogAction.CREATEFAIL, ip, creationUser);
//			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.insertCompany. ] ");
			e.printStackTrace();
		}
		return null;
	}*/

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.Company#getConcession()
	 */
	@Override
	public List<TbCompany> getConcession() {
		List<TbCompany> list = new ArrayList<TbCompany>();
		try {
			Query q = em.createQuery("SELECT tc FROM TbCompany tc WHERE tc.tbCompanyType.companyTypeId = 1" +
					"  ORDER By tc.companyName");
			for (Object obj : q.getResultList()) {
				list.add((TbCompany) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.getConcession. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.Company#getCompanytype()
	 */
	@Override
	public List<TbCompanyType> getCompanytype() {
		List<TbCompanyType> list = new ArrayList<TbCompanyType>();
		try {
			Query q = em.createQuery("SELECT tc FROM TbCompanyType tc ");
			for (Object obj : q.getResultList()) {
				list.add((TbCompanyType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.getCompanytype. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*@Override
	public Long updateCompany(Long companyId,String newCompanyNit, String newCompanyName,
			Long newCompanyTypeId, String ip, Long userId,
			String newFideicomiso, String newNitFideicomiso,
			String newNroContrato) {
		// TODO Auto-generated method stub
		
		
		try {
			Query q = em
					.createQuery("SELECT tc FROM TbCompany tc WHERE tc.companyId <> ?1 and (tc.companyName = ?2 or tc.companyNIT=?3) ");
			q.setParameter(1, companyId);
			q.setParameter(2, newCompanyName.toUpperCase());
			q.setParameter(3, newCompanyNit);
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the company.
			TbCompany tc = em.find(TbCompany.class, companyId);
			
			if(tc!=null){

				System.out.println("company: " + newCompanyName);
	            System.out.println("fideicomiso: " + newFideicomiso);
				 
				Query q1= em.createNativeQuery(
						" update tb_company set " +
						" company_name=SUBSTRB(?1,0,80), company_type_id=?2, fideicomiso=SUBSTRB(?3,0,80), nit_fideicomiso=?4, nro_contrato=SUBSTRB(?5,0,50), company_nit=?6 " +
						" where company_id=?7 ");
				q1.setParameter(1, newCompanyName.toUpperCase());
				q1.setParameter(2, newCompanyTypeId );
				q1.setParameter(3, newFideicomiso);
				q1.setParameter(4, newNitFideicomiso);
				q1.setParameter(5, newNroContrato);
				q1.setParameter(6, newCompanyNit);
				q1.setParameter(7, companyId);
				q1.executeUpdate();
				em.flush();

				return 0L;
				
//				tc.setCompanyNIT(newCompanyNit);
//				tc.setCompanyName(newCompanyName.toUpperCase());
//				tc.setTbCompanyType(em.find(TbCompanyType.class, newCompanyTypeId));
//				tc.setFideicomiso(newFideicomiso);
//				tc.setNitFideicomiso(newNitFideicomiso);
//				tc.setNroContrato(newNroContrato);
				
//				emObj = new ObjectEM(em);
//				
//				if (emObj.update(tc)) {
//					log.insertLog("Modificar Empresa | Se ha modificado la empresa ID: " + tc.getCompanyId() + ".",
//							LogReference.COMPANY, LogAction.UPDATE, ip, userId);
//					return 0L;
//				} else {
//					log.insertLog("Modificar Empresa | No se pudo modificar la empresa: " + newCompanyName + ".",
//							LogReference.COMPANY, LogAction.UPDATEFAIL, ip, userId);
//				}
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.updateCompany. ] ");
			e.printStackTrace();
			log.insertLog("Modificar Empresa | No se pudo modificar la empresa: " + newCompanyName + ".",
					LogReference.COMPANY, LogAction.UPDATEFAIL, ip, userId);
		}
		return null;
	}*/
	
	@Override
	public Long insertCompany(String companyNit,String company, Long companyTypeId, String ip,
							  Long creationUser, String fideicomiso, String nitFideicomiso, 
							  String nroContrato, String disclaimer) {
		try {
			Query q = em
					.createNativeQuery("SELECT tc.company_id FROM Tb_Company tc WHERE tc.company_Name = ?1 or tc.company_NIT=?2" );
			q.setParameter(1, company.toUpperCase().trim());
			q.setParameter(2, companyNit);
			q.getSingleResult();

			return -1L;
		} catch (NoResultException nre) {
			// creating the company.
			TbCompany tc = new TbCompany();
			tc.setCompanyNIT(companyNit);
			tc.setCompanyName(company.toUpperCase().trim());
			tc.setTbCompanyType(em.find(TbCompanyType.class, companyTypeId));
			tc.setFideicomiso(fideicomiso.toUpperCase());
			tc.setNitFideicomiso(nitFideicomiso);
			tc.setNroContrato(nroContrato);
			
			if(companyTypeId==1){
				tc.setDisclaimer(disclaimer);
			    emObj = new ObjectEM(em);
				if(emObj.create(tc)){
					try {
						log.insertLog("Creación de Empresa | Se ha creado la empresa con NIT: "+tc.getCompanyNIT() + 
								      " Empresa:"+ tc.getCompanyName() + " tipo:" + tc.getTbCompanyType().getCompanyTypeDescription() + 
								      " Fideicomiso:"+ tc.getFideicomiso() + " NIT Fideicomiso:"+ tc.getNitFideicomiso() + " Nro Contrato:"+ tc.getNroContrato() + 
								      " Disclaimer:"+ tc.getDisclaimer(), LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
						return 0L;
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}else{
					try {
						log.insertLog("Creación de Empresa | No se ha creado la empresa con NIT: "+tc.getCompanyNIT() + 
							      " Empresa:"+ tc.getCompanyName() + " tipo:" + tc.getTbCompanyType().getCompanyTypeDescription() + 
							      " Fideicomiso:"+ tc.getFideicomiso() + " NIT Fideicomiso:"+ tc.getNitFideicomiso() + " Nro Contrato:"+ tc.getNroContrato() + 
							      " Disclaimer:"+ tc.getDisclaimer(), LogReference.COMPANY, LogAction.CREATEFAIL, ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}
			}else{
				String oldNewDisclaimer="";
				emObj = new ObjectEM(em);
				if(emObj.create(tc)){
					try {
						log.insertLog("Creación de Empresa | Se ha creado la empresa con NIT: "+tc.getCompanyNIT() + 
							      " Empresa:"+ tc.getCompanyName() + " tipo:" + tc.getTbCompanyType().getCompanyTypeDescription() + 
							      " Fideicomiso:"+ tc.getFideicomiso() + " NIT Fideicomiso:"+ tc.getNitFideicomiso() + " Nro Contrato:"+ tc.getNroContrato() + 
							      " Disclaimer:"+ oldNewDisclaimer, LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
						return 0L;
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}else{
					try {
						log.insertLog("Creación de Empresa | No se ha creado la empresa con NIT: "+tc.getCompanyNIT() + 
							      " Empresa:"+ tc.getCompanyName() + " tipo:" + tc.getTbCompanyType().getCompanyTypeDescription() + 
							      " Fideicomiso:"+ tc.getFideicomiso() + " NIT Fideicomiso:"+ tc.getNitFideicomiso() + " Nro Contrato:"+ tc.getNroContrato() + 
							      " Disclaimer:"+ oldNewDisclaimer, LogReference.COMPANY, LogAction.CREATEFAIL, ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.insertCompany. ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long updateCompany(Long companyId, String newCompanyNit, String newCompanyName,
			Long newCompanyTypeId, String ip, Long creationUser,
			String newFideicomiso, String newNitFideicomiso,
			String newNroContrato, String newDisclaimer) {
	 try {
			Query q = em
					.createNativeQuery("SELECT tc.company_id FROM Tb_Company tc WHERE tc.company_Id <> ?1 and (tc.company_Name = ?2 or tc.company_NIT=?3) ");
			q.setParameter(1, companyId);
			q.setParameter(2, newCompanyName.toUpperCase().trim());
			q.setParameter(3, newCompanyNit);		
			q.getSingleResult();

			return -1L;
		} catch (NoResultException nre) {
		    TbCompany tc = em.find(TbCompany.class, companyId);
		    String oldNewCompanyNit = tc.getCompanyNIT();
		    tc.setCompanyNIT(newCompanyNit);
		    String oldNewCompanyName = tc.getCompanyName();
		    tc.setCompanyName(newCompanyName.toUpperCase().trim());
		    String oldNewCompanyTypeId = tc.getTbCompanyType().getCompanyTypeDescription();
		    tc.setTbCompanyType(em.find(TbCompanyType.class, newCompanyTypeId));
		    String oldNewFideicomiso = tc.getFideicomiso();
		    tc.setFideicomiso(newFideicomiso);
		    String oldNitFideicomiso = tc.getNitFideicomiso();
		    tc.setNitFideicomiso(newNitFideicomiso);
		    String oldNewNroContrato = tc.getNroContrato();
		    tc.setNroContrato(newNroContrato);

		   if(newCompanyTypeId==1){
		       String oldNewDisclaimer = tc.getDisclaimer();
			   tc.setDisclaimer(newDisclaimer);
			   emObj = new ObjectEM(em);
				if(emObj.update(tc)){
					try{
					log.insertLog("Editar Empresa | Se ha actualizado la Empresa ID: " + tc.getCompanyId()+ 
								". Antes NIT - Empresa - Tipo - Fideicomiso - NIT Fideicomiso - Nro Contrato - Disclaimer: " + 
								oldNewCompanyNit +" - "+ oldNewCompanyName +" - "+ oldNewCompanyTypeId +" - "+ 
								oldNewFideicomiso +" - "+oldNitFideicomiso +" - "+ oldNewNroContrato +" - "+ 
								oldNewDisclaimer, LogReference.COMPANY, LogAction.UPDATE, ip, creationUser);
					return 0L;
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}else{
					try {
						log.insertLog("Editar Empresa | No se ha actualizado la Empresa ID: " + tc.getCompanyId()+ 
								". Antes NIT - Empresa - Tipo - Fideicomiso - NIT Fideicomiso - Nro Contrato - Disclaimer: " + 
								oldNewCompanyNit +"-"+ oldNewCompanyName +"-"+ oldNewCompanyTypeId +"-"+ oldNewFideicomiso +
								oldNitFideicomiso +"-"+ oldNewNroContrato +"-"+ oldNewDisclaimer,
								LogReference.COMPANY, LogAction.UPDATEFAIL, ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
				}
			}else {
				tc.setDisclaimer("");
				String oldNewDisclaimer="";
				emObj = new ObjectEM(em);
					if(emObj.update(tc)){
						try{
						log.insertLog("Editar Empresa | Se ha actualizado la Empresa ID: " + tc.getCompanyId()+ 
									". Antes NIT - Empresa - Tipo - Fideicomiso - NIT Fideicomiso - Nro Contrato - Disclaimer: " + 
									oldNewCompanyNit +" - "+ oldNewCompanyName +" - "+ oldNewCompanyTypeId +" - "+ 
									oldNewFideicomiso +" - "+oldNitFideicomiso +" - "+ oldNewNroContrato +" - "+ 
									oldNewDisclaimer, LogReference.COMPANY, LogAction.UPDATE, ip, creationUser);
						return 0L;
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
					}else{
						try {
							log.insertLog("Editar Empresa | No se ha actualizado la Empresa ID: " + tc.getCompanyId()+ 
									". Antes NIT - Empresa - Tipo - Fideicomiso - NIT Fideicomiso - Nro Contrato - Disclaimer: " + 
									oldNewCompanyNit +"-"+ oldNewCompanyName +"-"+ oldNewCompanyTypeId +"-"+ oldNewFideicomiso +
									oldNitFideicomiso +"-"+ oldNewNroContrato +"-"+ oldNewDisclaimer,
									LogReference.COMPANY, LogAction.UPDATEFAIL, ip, creationUser);
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
					}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyEJB.updateCompany. ] ");
			e.printStackTrace();
		}
		return null;
	}
}