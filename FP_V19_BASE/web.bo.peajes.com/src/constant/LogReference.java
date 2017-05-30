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
	CONSIGNMENT("Consignación"),
	DEVICE("Dispositivo"),
	TRANSACTION("Transacciones"),
	ACCOUNT_TRANSACTION("Transacciones de Cuenta"),
	VEHICLE("Vehículo"),
	CUSTOMIZATION("Personalización"),
	SPECIAL("Especiales"),
	BLACKLIST("Listas Negras"),
	INCLUSION("Inclusión"),
	INSTALLATIONLOCATION("Ubicaciones de Instalación"),
	REPLACEMENTCAUSE("Causas de Reposición."),
	INSTALLATIONTIES("Instalaciones TIES"),
	SPECIALEXEMPTTYPE("Tipo de Especial Exento"),
	SPECIALEXEMPTOFFICE("Dependencia de Exentos"),
	BANK("Banco"),
	BANKACCOUNT("Cuenta de Banco"),
	WAREHOUSEDEPENDENCY("Dependencias de Almacén"),
	WAREHOUSECARDTYPE("Tipo de Tarjeta"),
	COMPANY("Empresas"),
	TASK("Motor de Tareas"),
	RECHARGESTATION("Estación de Recarga"),
	ADMINPARAMETER("Administración de Parámetros"),
	RECHARGEPOINT("Asignación IP Punto Recarga"),
	WAREHOUSE("Almacén"),
	STATIONBO("Estaciones Backoffice"),
	DEVICEFACIAL("Facial de Dispositivos"),
	INITIALIZATION("Inicialización"),
	CHANGESYSTEMPARAMETER("Cambio en parámetros del sistema"),
	AUTOMATICRECHARGE ("Recarga Automática"),
	REMOVEAUTOMATICRECHARGE ("Eliminar Recarga Automática"),
	INIT_RECHARGE("Recarga Inicial"),
	EMAIL("Gestor de Correo"),
	BRAND("Marca"),
	HOLIDAY("Día Festivo"),
	FACTORY("Fábrica"),
	DOCUMENT_VERIFICATION("Verificación de Documentos"),
	CONCILIATION("Conciliación"),
	MINIMUMBALANCE("Saldo Mínimo"),
	DATA_POLICIES("Creación Política de Datos"),
	CREATEQUESTION("Creación de preguntas"),
	MODIFYQUESTION("Modificación de preguntas"),
	DELETEQUESTION("Eliminación de preguntas"),
	COURIER("Courier"),
	ROLL("Rollo"),
	RECHARGEPSE("Recarga PSE"),
	AGREEMENT("Convenio"),
	CHANNEL("Canales"),
	RECHANNELAGREEMENT("Relación Canales-Convenios"),
	INFORMATIONBALANCE("Información saldos"),
	CONFIAUTOMATICPROGRAMMING("Configuración Recarga Programada");
	
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