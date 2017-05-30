/**
 * 
 */
package constant;

/**
 * Enum for LogAction 
 * 
 * @author tmolina
 * @since 01-10-2010
 */
public enum LogReference {
	
	ACTION("Acciones"),
	OPTION("Opciones"),
	USER("Usuarios"),
	PERMISSION("Permisos"),
	LOGIN("Login"),
	PURCHASE("Compras"),
	ROLE("Roles"),
	CLIENT("Clientes"),
	PROCESS("Seguimiento de Proceso"),
	PROCESSDETAIL("Detalle de Seguimiento de Proceso"),
	SENDMAIL("Correo"),
	CLIENTDATA("Dependencia del Cliente"),
	PURCHASEORDER("Orden de Pedido"),
	PURCHASEORDERDETAIL("Detalle de Orden de Pedido"),
	ACCOUNT("Cuenta Cliente"),
	CONSIGNMENT("Consignaci�n"),
	DEVICE("Dispositivo"),
	TRANSACTION("Transacciones"),
	ACCOUNT_TRANSACTION("Transacciones de Cuenta"),
	VEHICLE("Veh�culo"),
	CUSTOMIZATION("Personalizaci�n"),
	SPECIAL("Especiales"),
	BLACKLIST("Listas Negras"),
	INCLUSION("Inclusi�n"),
	INSTALLATIONLOCATION("Ubicaciones de Instalaci�n"),
	REPLACEMENTCAUSE("Causas de Reposici�n."),
	INSTALLATIONTIES("Instalaciones TIES"),
	SPECIALEXEMPTTYPE("Tipo de Especial Exento"),
	SPECIALEXEMPTOFFICE("Dependencia de Exentos"),
	BANK("Banco"),
	BANKACCOUNT("Cuenta de Banco"),
	WAREHOUSEDEPENDENCY("Dependencias de Almac�n"),
	WAREHOUSECARDTYPE("Tipo de Tarjeta"),
	COMPANY("Empresas"),
	TASK("Motor de Tareas"),
	RECHARGESTATION("Estaci�n de Recarga"),
	ADMINPARAMETER("Administraci�n de Par�metros"),
	RECHARGEPOINT("Asignaci�n IP Punto Recarga"),
	WAREHOUSE("Almac�n"),
	STATIONBO("Estaciones Backoffice"),
	DEVICEFACIAL("Facial de Dispositivos"),
	INITIALIZATION("Inicializaci�n"),
	CHANGESYSTEMPARAMETER("Cambio en par�metros del sistema"),
	AUTOMATICRECHARGE ("Recarga Autom�tica"),
	REMOVEAUTOMATICRECHARGE ("Eliminar Recarga Autom�tica"),
	INIT_RECHARGE("Recarga Inicial"),
	EMAIL("Gestor de Correo"),
	BRAND("Marca"),
	HOLIDAY("D�a Festivo"),
	FACTORY("F�brica"),
	DOCUMENT_VERIFICATION("Verificaci�n de Documentos"),
	CONCILIATION("Conciliaci�n"),
	MINIMUMBALANCE("Saldo M�nimo"),
	DATA_POLICIES("Creaci�n Pol�tica de Datos"),
	CREATEQUESTION("Creaci�n de preguntas"),
	MODIFYQUESTION("Modificaci�n de preguntas"),
	DELETEQUESTION("Eliminaci�n de preguntas"),
	COURIER("Courier"),
	ROLL("Rollo"),
	RECHARGEPSE("Recarga PSE"),
	AGREEMENT("Convenio"),
	CHANNEL("Canales"),
	RECHANNELAGREEMENT("Relaci�n Canales-Convenios"),
	INFORMATIONBALANCE("Informaci�n saldos"),
	CONFIAUTOMATICPROGRAMMING("Configuraci�n Recarga Programada");
	
	private String logReference;

	private LogReference(String str) {
		this.logReference = str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.logReference;
	}
}