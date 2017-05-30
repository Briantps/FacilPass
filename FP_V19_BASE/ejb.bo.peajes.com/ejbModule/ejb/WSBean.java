package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.ReDeRegistration;
import jpa.TbDevice;
import jpa.TbLane;
import jpa.TbTransaction;
import jpa.TrReNotifier;
import auxiliar.JSONHandler;
import auxiliar.SerializedMessage;
import auxiliar.SerializedTransaction;

/**
 * Session Bean implementation class WSBean
 */
@Stateless(mappedName = "ejb/WS")
public class WSBean implements WSBeanRemote {

	/**
	 * DataSource
	 */
	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private static final Long CREDIT_OPERATION = 0L;
	private static final Long DEBIT_OPERATION = 1L;
	private static final String VERSION = "1.0386.1.20100706";

	/**
	 * Default constructor.
	 */
	public WSBean() {
		System.out.println("VERSION - " + VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.WSBeanRemote#getService(java.lang.String)
	 */
	@Override
	public String getService(String in) {
		System.out.println(in);
		String ret = null;
		try {
			SerializedMessage message = JSONHandler.getSerializedMessage(in);
			String trailer = message.getTrailer();
			if (message != null) {
				if (validateReceiver(trailer)) {
					switch (message.getOperationType()) {
					case 1: {/* Ask to Observer */
						if (message.getTransaction() == null) {
							try {
								ret = JSONHandler
										.observerMessage(queryObserver(trailer));
							} catch (Exception e) {
								ret = JSONHandler.errorMessage(4,
										"UNDEFINED ERROR");
							}
						} else {
							ret = JSONHandler.errorMessage(3,
									"NOT UNDERSTOOD TRANSACTION INFO");
						}
					}
						break;
					case 2: {/* Get Transaction for Observer */
						if (message.getTransaction() == null) {
							if (queryObserver(trailer) > 0) {
								TbTransaction tran = getTransaction(trailer);
								ret = JSONHandler
										.returnTransactionMessage(new SerializedTransaction(
												tran.getTbDevice()
														.getDeviceCode(), tran
														.getTbTransactionType().getTransactionTypeId()
														, tran
														.getPreviousBalance(),
												tran.getNewBalance(), tran
														.getTransactionValue(),
												tran.getTransactionTime(), tran
														.getTbLane()
														.getLaneId()));
							} else {
								ret = JSONHandler.errorMessage(3,
										"NO TRANSACTIONS OBSERVED");
							}
						} else {
							ret = JSONHandler.errorMessage(3,
									"MISSING TRANSACTION INFO");
						}
					}
						break;
					case 3: {/* Register a Transaction */
						if (message.getTransaction() != null) {
							if (persistTransaction(message.getTransaction(),
									message.getTrailer())) {
								ret = JSONHandler.errorMessage(0,
										"TRANSACTION ENDED SUCCESSFULLY");
							} else {
								ret = JSONHandler.errorMessage(4,
										"UNDEFINED ERROR");
							}
						} else {
							ret = JSONHandler.errorMessage(3,
									"MISSING TRANSACTION INFO");
						}
					}
						break;
					case 4: {/* ..... */
					}
						break;
					default: {
						ret = JSONHandler.errorMessage(3,
								"OPERATION TYPE NOT RECOGNIZED");
					}
					}
				} else {
					ret = JSONHandler.errorMessage(3, "INVALID RECEIVER");
				}
			} else {
				ret = JSONHandler.defaultErrorMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = JSONHandler.errorMessage(4, "UNDEFINED ERROR");
		}
		return ret;
	}

	/**
	 * @param trailer
	 * @return
	 */
	private TbTransaction getTransaction(String trailer) {
		TrReNotifier notifier = (TrReNotifier) em.createQuery(
				"FROM TrReNotifier noti "
						+ "WHERE noti.notificationStatus = ?1 "
						+ "AND noti.tbReceiver.receiverIpv4 = ?2")
				.setParameter(1, BigDecimal.ZERO).setParameter(2, trailer)
				.setMaxResults(1).getSingleResult();
		notifier.setNotificationStatus(BigDecimal.ONE);
		em.merge(notifier);
		em.flush();
		return notifier.getTbTransaction();
	}

	/**
	 * @param trailer
	 * @return
	 */
	private Long queryObserver(String trailer) throws Exception {
		return (Long) em.createQuery(
				"SELECT COUNT(noti) FROM TrReNotifier noti "
						+ "WHERE noti.notificationStatus = ?1 "
						+ "AND noti.tbReceiver.receiverIpv4 = ?2")
				.setParameter(1, BigDecimal.ZERO).setParameter(2, trailer)
				.getSingleResult();
	}

	/**
	 * @param trailer
	 * @return
	 */
	private boolean validateReceiver(String trailer) {
		boolean ret = false;
		try {
			em.createQuery("FROM TbReceiver rec WHERE rec.receiverIpv4 = ?1")
					.setParameter(1, trailer).getSingleResult();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * @param tran
	 * @param ipV4
	 * @return
	 */
	private boolean persistTransaction(SerializedTransaction tran, String ipv4) {
		boolean ret = false;
		try {
			TbDevice device = (TbDevice) em.createQuery(
					"FROM TbDevice dev WHERE dev.deviceCode = ?1")
					.setParameter(1, tran.getDeviceCode()).getSingleResult();
			TbTransaction transaction = new TbTransaction();
			transaction.setBackOfficeTime(new Timestamp(System
					.currentTimeMillis()));
			transaction.setNewBalance(tran.getNewBalance());
			transaction.setPreviousBalance(tran.getPreviousBalance());
			transaction.setTbDevice(device);
			transaction.setTbLane(em.find(TbLane.class, tran.getLaneId()));
			transaction.setTransactionTime(tran.getTransactionTime());
			transaction.setTbTransactionType(em.find(
					jpa.TbTransactionType.class, tran.getTransactionType()));
			transaction.setTransactionValue(tran.getTransactionValue());
			if (tran.getTransactionType().longValue() == CREDIT_OPERATION.longValue()) {
				device.setDeviceCurrentBalance(device.getDeviceCurrentBalance()
						.add(new BigDecimal(tran.getTransactionValue())));
			} else if (tran.getTransactionType().longValue() == DEBIT_OPERATION.longValue()) {
				device.setDeviceCurrentBalance(device.getDeviceCurrentBalance()
						.subtract(new BigDecimal(tran.getTransactionValue())));
			}
			em.merge(device);
			em.persist(transaction);
			for (Object obj : em.createQuery(
					"FROM ReDeRegistration reg WHERE reg.tbDevice = ?1")
					.setParameter(1, device).getResultList()) {
				ReDeRegistration registry = (ReDeRegistration) obj;
				if (!registry.getTbReceiver().getReceiverIpv4().equals(ipv4)) {
					TrReNotifier notifier = new TrReNotifier();
					notifier.setNotificationStatus(BigDecimal.ZERO);
					notifier.setTbReceiver(registry.getTbReceiver());
					notifier.setTbTransaction(transaction);
					em.persist(notifier);
				}
			}
			em.flush();
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}