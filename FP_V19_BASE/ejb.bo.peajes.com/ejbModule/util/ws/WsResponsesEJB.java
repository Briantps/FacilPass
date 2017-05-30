package util.ws;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbWebServices;
import jpa.TbWsResponses;
import constant.ECodeType;
import constant.EWebServices;



@Stateless(mappedName = "util/ws/WsResponses")
public class WsResponsesEJB implements WsResponses {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	/**
	 * @see util.ws.WsResponses#getResponses(EWebServices)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, TbWsResponses> getResponses(EWebServices wsType) {
		Map<Long, TbWsResponses> resp = new HashMap<Long, TbWsResponses>();
		if (wsType == null){
			throw new IllegalArgumentException("El valor del web service no puede ser vacio ");
		}

		Query q = em.createQuery("Select p From TbWsResponses p where p.tbWebService.tbWebServicesId =?1 ");
		q.setParameter(1, wsType.getId());
		
		List<Object> rList = q.getResultList();
		if ( rList != null ){
			for (Object obj : rList) {
				TbWsResponses t = (TbWsResponses)obj;
				resp.put(t.getCode(), t);
			}

		}

		return resp;
	}

	/**
	 * @see util.ws.WsResponses#getWebService(Long)
	 */
	@Override
	public TbWebServices getWebService(Long id) {
		if (id == null){
			throw new IllegalArgumentException("El identificador del web-service a consultar no puede ser vacío");
		}
		
		TbWebServices tResp = (TbWebServices)em.find(TbWebServices.class, id);
		
		if ((tResp.getTbWsResponses() != null) && (!tResp.getTbWsResponses().isEmpty())){
			// Nada por hacer...
		}
		
		return tResp;
	}

	@Override
	public String getHomologation(Long id) {
		
		if (id == null){
			throw new IllegalArgumentException("El identificador del tipo de documento a homologar no puede ser vacío");
		}
		
		String resp = null;
		ECodeType ct = ECodeType.getECodeType(id);
		
		switch(ct){
		case CEDULA_CIUDADANIA :
			resp = "C";
			break;
		case CEDULA_EXTRANJERIA :
			resp = "E";
			break;
		case TARJETA_IDENTIDAD:
			resp = "T";
			break;
		case PASAPORTE:
			resp = "P";
			break;
		case NIT :
			resp = "N";
			break;
		// TODO que homologación se debe hacer a todos los demás documentos??
		// No hay registro civil en el sistema
		}
		
		return resp;
	}

}
