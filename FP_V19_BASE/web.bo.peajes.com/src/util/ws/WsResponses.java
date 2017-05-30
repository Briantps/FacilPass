package util.ws;

import java.util.Map;

import javax.ejb.Remote;

import constant.EWebServices;

import jpa.TbWebServices;
import jpa.TbWsResponses;

/**
 * Interface que ofrece servicios para manejo de las respuestas y codigos de respuestas de los web services consumidos
 * @author r.bautista
 *
 */
@Remote
public interface WsResponses {

	/**
	 * Servicio que retorna un mapa con los TbWsResponses asociados al wsType indicado.
	 * El mapa tiene como clave el id de la respuesta y valor el TbWsResponses con el mismo id
	 * @param wsType tipo de web service del cual se cargan las respuestas
	 * @return Map<Long, TbWsResponses> con las respuestas parametrizadas
	 */
	 public Map<Long, TbWsResponses> getResponses(EWebServices wsType);

		/**
		 * Retorna el web-service con el identificador indicado
		 * @param id identificador del web-service a retornar
		 * @return TbWebServices con el id indicado o null
		 */
		public TbWebServices getWebService(Long id);

		/**
		 * Retorna la homologación del tipo de identificación con el id indicado
		 * @param id identificador del tipo de documento del bos
		 * @return Valor homologado para facilpass
		 */
		public String getHomologation(Long id);
}
