/**
 * 
 */
package auxiliar;

import java.sql.Timestamp;

import org.json.JSONObject;

/**
 * @author root
 * 
 */
public class JSONHandler {

	private static final String BO_TRAILER = "192.168.3.199";
	private static final String PEAJE_TRAILER = "192.168.1.70";

	/**
	 * 
	 */
	public JSONHandler() {
	}

	/**
	 * @param sm
	 * @return
	 */
	public static String serializeMessage(SerializedMessage sm) {
		JSONObject message = new JSONObject();
		try {
			message.put("header", sm.getHeader());
			message.put("operationType", sm.getOperationType());
			message.put("tran", serializeTransaction(sm.getTransaction()));
			message.put("errorCode", sm.getErrorCode());
			message.put("info", sm.getInfo() != null ? sm.getInfo() : "null");
			message.put("priority", sm.getPriority());
			message.put("stamp", sm.getStamp().getTime()); // Timestamp
			message.put("trailer", sm.getTrailer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message.toString();
	}

	/**
	 * @param transaction
	 * @return
	 */
	public static String serializeTransaction(SerializedTransaction transaction) {
		String ret = "null";
		try {
			if (transaction != null) {
				JSONObject object = new JSONObject();
				object.put("deviceCode", transaction.getDeviceCode());
				object.put("transactionType", transaction.getTransactionType());
				object.put("previousBalance", transaction.getPreviousBalance()
						.toString());
				object
						.put("newBalance", transaction.getNewBalance()
								.toString());
				object.put("transactionValue", transaction
						.getTransactionValue().toString());
				object.put("transactionTime", transaction.getTransactionTime()
						.getTime());
				object.put("laneId", transaction.getLaneId());
				ret = object.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * @return
	 */
	public static String defaultErrorMessage() {
		SerializedMessage errorMessage = new SerializedMessage((short) 0,
				(short) 0, (SerializedTransaction) null, (short) 3,
				"TRANSACCION NO ENTENDIDA", (short) 1, new Timestamp(System
						.currentTimeMillis()), BO_TRAILER);
		return serializeMessage(errorMessage);
	}

	/**
	 * @return
	 */
	public static String errorMessage(int errorCode, String errorInfo) {
		SerializedMessage errorMessage = new SerializedMessage((short) 0,
				(short) 0, (SerializedTransaction) null, (short) errorCode,
				errorInfo, (short) 1,
				new Timestamp(System.currentTimeMillis()), BO_TRAILER);
		return serializeMessage(errorMessage);
	}

	/**
	 * @return
	 */
	public static String observerMessage(long observerCount) {
		SerializedMessage errorMessage = new SerializedMessage((short) 0,
				(short) 0, (SerializedTransaction) null, (short) 0, Long
						.toString(observerCount), (short) 1, new Timestamp(
						System.currentTimeMillis()), BO_TRAILER);
		return serializeMessage(errorMessage);
	}

	/**
	 * @return
	 */
	public static String sampleMessage() {

		/*
		 * short header, short operationType, SerializedTransaction transaction,
		 * short errorCode, String info, short priority, Timestamp stamp, String
		 * trailer
		 */
		SerializedMessage errorMessage = new SerializedMessage((short) 2,
				(short) 3, new SerializedTransaction("50918521",  1L,
						new Long("95000"), new Long("90000"),
						new Long("5000"), new Timestamp(System
								.currentTimeMillis()), (short) 1), (short) 0,
				null, (short) 1, new Timestamp(System.currentTimeMillis()),
				PEAJE_TRAILER);
		return serializeMessage(errorMessage);
	}

	/**
	 * @return
	 */
	public static String returnTransactionMessage(SerializedTransaction tran) {
		SerializedMessage returnTransactionMessage = new SerializedMessage(
				(short) 0, (short) 0, tran, (short) 0, null, (short) 1,
				new Timestamp(System.currentTimeMillis()), BO_TRAILER);
		return serializeMessage(returnTransactionMessage);
	}

	/**
	 * @return
	 */
	public static String sampleObserverMessage() {

		/*
		 * short header, short operationType, SerializedTransaction transaction,
		 * short errorCode, String info, short priority, Timestamp stamp, String
		 * trailer
		 */
		SerializedMessage errorMessage = new SerializedMessage((short) 2,
				(short) 1, null, (short) 0, null, (short) 1, new Timestamp(
						System.currentTimeMillis()), PEAJE_TRAILER);
		return serializeMessage(errorMessage);
	}

	/**
	 * @return
	 */
	public static String askTransactionMessage() {

		/*
		 * short header, short operationType, SerializedTransaction transaction,
		 * short errorCode, String info, short priority, Timestamp stamp, String
		 * trailer
		 */
		SerializedMessage askTransactionMessage = new SerializedMessage(
				(short) 1, (short) 2, null, (short) 0, null, (short) 1,
				new Timestamp(System.currentTimeMillis()), BO_TRAILER);
		return serializeMessage(askTransactionMessage);
	}

	/**
	 * @param in
	 * @return SerializedMessage
	 * 
	 *         create interoperability object based on JSON string incoming from
	 *         the Web Service
	 */
	public static SerializedMessage getSerializedMessage(String in) {
		SerializedMessage message = null;
		try {
			JSONObject object = new JSONObject(in);
			message = new SerializedMessage(Short.parseShort(object
					.getString("header")), Short.parseShort(object
					.getString("operationType")),
					getSerializedTransaction(object.getString("tran")), Short
							.parseShort(object.getString("errorCode")), !object
							.getString("info").equals("null") ? object
							.getString("info") : null, Short.parseShort(object
							.getString("priority")), new Timestamp(object
							.getLong("stamp")), object.getString("trailer"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * @param jsonObject
	 * @return SerializedTransaction
	 */
	private static SerializedTransaction getSerializedTransaction(
			String transaction) {
		SerializedTransaction tran = null;
		try {
			if (!transaction.equalsIgnoreCase("null")) {
				JSONObject object = new JSONObject(transaction);
				tran = new SerializedTransaction(
						object.getString("deviceCode"), Long.parseLong(object
								.getString("transactionType")), new Long(
								object.getString("previousBalance")),
						new Long(object.getString("newBalance")),
						new Long(object.getString("transactionValue")),
						new Timestamp(object.getLong("transactionTime")),
						object.getLong("laneId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tran;
	}
}
