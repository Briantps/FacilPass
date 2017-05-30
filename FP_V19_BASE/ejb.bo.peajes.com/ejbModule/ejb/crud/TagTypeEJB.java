package ejb.crud;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.AllInfoTagType;

import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.Log;
import ejb.Process;

import jpa.TbProcessTrack;
import jpa.TbTagType;

/**
 * Session Bean implementation class TagTypeEJB
 */
@Stateless(mappedName = "ejb/TagType")
public class TagTypeEJB implements TagType {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM objectEM;
	
    /**
     * Default constructor. 
     */
    public TagTypeEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.TagType#getTagType()
	 */
	@Override
	public List<TbTagType> getTagType() {
		List<TbTagType> list = new ArrayList<TbTagType>();
		try {
			Query q = em.createQuery("SELECT tt FROM TbTagType tt " +
					                 "WHERE tt.tagState =1 " +
									 "ORDER BY tt.tagTypeId ");
			for (Object obj : q.getResultList()) {
				list.add((TbTagType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TagTypeEJB.getTagType. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public Long lengDeviceId(Long typeDevice) {
		Long lenghtId = 20L;
		BigDecimal result = null;
		Query q = em.createNativeQuery("Select serial_length from Tb_Tag_Type where TAG_TYPE_ID=?1");
		q.setParameter(1, typeDevice);
		result = (BigDecimal) q.getSingleResult();
		if(result != null){
			lenghtId = result.longValue();
		}
		return lenghtId;
	}
	
	/**
	 * Método creado para guardar fabricante, tamaño tag y tipo de código fabricante.
	 * @author psanchez
	 */
	public String insertFactory(String tagTypeCode, String tagTypeName, String serialLength, String ip, Long creationUser) {
		try {
			// creating the factory.
			TbTagType tt = new TbTagType();
			tt.setTagTypeCode(tagTypeCode.trim());
			tt.setTagTypeName(tagTypeName.toUpperCase());
		    Long textoId = new Long(Long.parseLong(serialLength)); 
			tt.setSerialLength(textoId);
			tt.setNumberOfSplit((long) 4);
			tt.setTagState(1);
			
			em.persist(tt);
			objectEM = new ObjectEM(em);
			if(objectEM.create(tt)){
				log.insertLog("Creación Fabricante| Se ha creado el fabricante ID: " + tt.getTagTypeId()+
						       tt.getTagTypeName() + tt.getSerialLength() + ".",
						LogReference.FACTORY, LogAction.CREATE, ip, creationUser);
				return "El fabricante ha sido creado con éxito.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TagTypeEJB.insertFactory. ] ");
			e.printStackTrace();
			return "El fabricante no ha sido creado.";
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean existTagTypeCode(String tagTypeCode) {
	    boolean result=false;
	    String tagTypeCodeS = tagTypeCode.replaceFirst ("^0*", "");;
		try{
			Query query= 
				em.createNativeQuery("SELECT tag_type_id " +
					                 "FROM tb_tag_type " +
								     "WHERE tag_type_code=?1 " +
									 "AND tag_state=1 ");
				query.setParameter(1, tagTypeCodeS.trim());

				List<Object> list= new ArrayList<Object>();
				list=query.getResultList();
				if(list.size()>0){
					  result=false;
				  }else{
					  result=true; 
				  }
		}catch(NoResultException ex){
			System.out.println(" [ Error en TagTypeEJB.existTagTypeCode. ] ");
			result=false;
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean existTagTypeName(String tagTypeName) {
	    boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT tag_type_id " +
					                 "FROM tb_tag_type " +
					                 "WHERE upper(tag_type_name)= trim(upper(?1)) " +
									 "AND tag_state=1 ");
				query.setParameter(1, tagTypeName.trim());
		
				List<Object> list= new ArrayList<Object>();
				list=query.getResultList();
				if(list.size()>0){
					  result=false;
				  }else{
					  result=true; 
				  }
		}catch(NoResultException ex){
			System.out.println(" [ Error en TagTypeEJB.existTagTypeName. ] ");
			result=false;
		}
		return result;
	}
	
	/**
	 * Método creado para listar datos de fabricante activos.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	public List<AllInfoTagType> getFactoryName(String name) {
	  ArrayList<AllInfoTagType> factories = new ArrayList<AllInfoTagType>();
	  try {
		    String qry = "";
			String endqry = " ORDER BY to_number(tag_type_code) ASC ";
			qry = "SELECT DISTINCT to_char(tag_type_id), tag_type_code, tag_type_name, serial_length, tag_state " +
				  "FROM tb_tag_type " +
				  "WHERE tag_state=1 ";
						
			 if(!name.equals("")){
				qry = qry+"AND Upper(tag_type_name) LIKE '%"+name.toUpperCase()+"%' ";
			 }
			  Query query = em.createNativeQuery(qry+endqry);
			  List<Object> lis= (List<Object>)query.getResultList();
			  if(lis.size()>0){	
				 for(int i=0;i<lis.size();i++){
					Object[] o=(Object[]) lis.get(i);
					
					AllInfoTagType factory = new AllInfoTagType();
					if(factory!=null){	
						factory.setTagTypeIdU(Long.parseLong(o[0].toString()));
						factory.setTagTypeCodeU(o[1].toString());
						factory.setTagTypeNameU(o[2].toString());
						factory.setSerialLengthU(o[3].toString());
						factory.setStateU(Long.parseLong(o[4].toString()));
						factories.add(factory);
					}
				}
	        }	
		} catch (Exception e) {
			System.out.println(" [ Error en TagTypeEJB.getFactoryName. ] ");
			e.printStackTrace();
		}
	  return factories;
	}
	
	
	/**
	 * Método creado para mostrar nombre del fabricante.
	 * @author psanchez
	 */
	public String getFactoryName(Long tagTypeId) {
		try{
			TbTagType tt = em.find(TbTagType.class, tagTypeId);
			if(tt != null){
				return tt.getTagTypeName();
			}else{
				return null;
			}
		}catch(NoResultException e){
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Método creado para listar datos de fabricantes activos.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoTagType> getAllInfoFactories() {
		 List<AllInfoTagType> factories = new ArrayList<AllInfoTagType>();
		  try {
				Query q = em.createNativeQuery("SELECT DISTINCT to_char(tag_type_id), tag_type_code, tag_type_name, serial_length, tag_state " +
											   "FROM tb_tag_type " +
											   "WHERE tag_state = 1 " +
											   "ORDER BY to_number(tag_type_code) ASC ");
					List<Object> lis= (List<Object>)q.getResultList();
					for(int i=0;i<lis.size();i++){
						Object[] o=(Object[]) lis.get(i);                                   					
						AllInfoTagType factory = new AllInfoTagType();
						if(factory!=null){	
							factory.setTagTypeIdU(Long.parseLong(o[0].toString()));
							factory.setTagTypeCodeU(o[1].toString());
							factory.setTagTypeNameU(o[2].toString());
							factory.setSerialLengthU(o[3].toString());
							factory.setStateU(Long.parseLong(o[4].toString()));
							factories.add(factory);
						}
					}		
			  } catch (Exception e) {
				System.out.println(" [ Error en TagTypeEJB.getAllInfoFactories. ] ");
				e.printStackTrace();
			}	
		  return factories;
	}

	/**
	 * Método creado para desactivar fabricante
	 * @return return true si el fabricante fué desactivado (2) o false en otro caso.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeTagType(Long tagTypeId, String ip, Long creationUser) {
	  try {
		  Query query1 = 
				em.createNativeQuery("select tag_type_code from tb_tag_type where tag_type_id=?1 ");
				  query1.setParameter(1, tagTypeId);
				  query1.getSingleResult();
				  String result = (String) query1.getSingleResult();

		  Query query= 
				em.createNativeQuery("select distinct td.device_id from tb_device td " +
						"inner join re_device_tag_type rd on rd.device_id=td.device_id " +
						"left join re_account_device rad on rad.device_id=td.device_id " +
						"inner join tb_tag_type tt ON tt.tag_type_id = rd.tag_type_id " +
						"where (td.device_state_id in (4,3,6) or rad.relation_state=0) " +
						"and tt.tag_state=1 " +
						"and tt.tag_type_code= ?1 ").setParameter(1, result);
			List<Object> list= new ArrayList<Object>();
			list=query.getResultList();
			  if(list.size()>0){
				  return true;
			  }else{
			    TbTagType tt= em.find(TbTagType.class, tagTypeId);
	            tt.setTagState(2);
	            objectEM = new ObjectEM(em);	
				if(objectEM.create(tt)){
					log.insertLog("Desactivación Fabricante | Se ha desactivado el fabricante ID: " + tt.getTagTypeId()+
							tt.getTagTypeCode() +tt.getTagTypeName() + tt.getSerialLength() + ".",
							LogReference.FACTORY, LogAction.CREATE, ip, creationUser);
					  
						//proceso administrador
			        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
						}else{
							idPTA=pt.getProcessTrackId();
						}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.DEVICE_CREATION,  
							     "Desactivación Fabricante "+tt.getTagTypeName()+".", creationUser, ip, 1, "", null,null,null,null);					
					return false;
				} else {
					log.insertLog("Desactivación Fabricante | No se ha desactivado el fabricante ID: " + tt.getTagTypeId()+
							tt.getTagTypeCode() +tt.getTagTypeName() + tt.getSerialLength() + ".",
							LogReference.FACTORY, LogAction.DELETEFAIL, ip, creationUser);
					return false;
				}
			  }
	     } catch (Exception e) {
			System.out.println(" [ Error en TagTypeEJB.removeTagType ] ");
			e.printStackTrace();
			return false;
		}	
	}

	/**
	 * Método creado para editar datos de la fábrica
	 * @return return true si los datos fuerón editados o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public String editTagType(Long tagTypeId, String tagTypeCode,
			String tagTypeName, String serialLength, String ip, Long creationUser) {
	 try {	
		 TbTagType factoria = em.find(TbTagType.class, tagTypeId);
		    String oldTagTypeName = factoria.getTagTypeName();
		    factoria.setTagTypeName(tagTypeName.toUpperCase());
		    Long oldSerialLength = factoria.getSerialLength();
		    Long textoId = new Long(Long.parseLong(serialLength)); 
		    factoria.setSerialLength(textoId);
			//update
			objectEM = new ObjectEM(em);
			if(objectEM.update(factoria)){
				log.insertLog("Editar Fabricante | Se ha editado el fabricante ID: " + factoria.getTagTypeId()+
						factoria.getTagTypeCode() +factoria.getTagTypeName() + factoria.getSerialLength() + 
						". Antes Nombre fabricante - Tamaño ID Tacs: " +  oldTagTypeName + oldSerialLength ,
						LogReference.FACTORY, LogAction.UPDATE, ip, creationUser);
				return "Los datos del fabricante han sido modificados con éxito.";
			} else {
				log.insertLog("Editar Fabricante | No se ha editado el fabricante ID: " + factoria.getTagTypeId()+
						factoria.getTagTypeCode() +factoria.getTagTypeName() + factoria.getSerialLength() + 
						". Antes Nombre fabricante - Tamaño ID Tacs: " +  oldTagTypeName + oldSerialLength ,
						LogReference.FACTORY, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TagTypeEJB.editTagType. ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean existTagTypeU(Long tagTypeId, String tagTypeName) {
		boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT tag_type_name " +
				                     "FROM tb_tag_type " +
									 "WHERE tag_type_id = ?1 " +
									 "AND tag_state=1 ");
			query.setParameter(1, tagTypeId);
			String NameFactory=(String) query.getSingleResult();
			if(NameFactory.equals(tagTypeName.toLowerCase())){
				result=true;
			}else{
				Query query1= 
					em.createNativeQuery("SELECT to_char(tag_type_id) " +
					                     "FROM tb_tag_type " +
										 "WHERE tag_type_id <> ?1 " +
										 "AND (upper(tag_type_name)=upper(?2)) " +
										 "AND tag_state=1 ");
				query1.setParameter(1, tagTypeId);
				query1.setParameter(2, tagTypeName.trim());
				List<Object> list= new ArrayList<Object>();
				list=query1.getResultList();
				  if(list.size()>0){
					  result=false;
				  }else{
					  result=true; 
				  }
			}
		}catch(NoResultException ex){
			System.out.println(" [ Error en TagTypeEJB.existTagTypeU. ] ");
			result=false;
		}
		return result;
	}

	
}
