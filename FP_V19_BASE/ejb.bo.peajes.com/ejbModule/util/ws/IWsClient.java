package util.ws;

/**
 * Interface para las clases clientes de WebService externos
 * @author r.bautista
 *
 */
public interface IWsClient<K> {

	/**
	 * Realiza el consumo del web service acorde a los parametros definidos para el mismo.
	 * @return resultado del consumo en el tipo parametrizado.
	 */
	public K execute();

	/**
	 * modifica la URL a la cual se debe conectar
	 * @param url url a la cual se conecta
	 */
	public void setWSUrl(String url);

	/**
	 * Retorna la URL de invocación del WEB-service
	 * @return url de invocación del WEB-Service
	 */
	public String getWSUrl();

}
