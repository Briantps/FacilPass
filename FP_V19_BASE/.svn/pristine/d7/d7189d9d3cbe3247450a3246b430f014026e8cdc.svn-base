package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbCodeImagesRejected;
import jpa.TbTransactionState;
import util.Images;
import util.NotificationsList;

@Remote
public interface Image {

	public List<Images> getImages(String url, String url2);
	
	public List<TbCodeImagesRejected> getCodesImagesRejected();

	public boolean createNotification(String idPhoto, Date date, String name,
			String notification, Long typeId, String usrs, Long idTran, Long idClient, String dateTransaction);

	public List<NotificationsList> getNotifications(Long transactionId2);

	public List<TbCodeImagesRejected> getCodesImagesRejected2();

	public List<NotificationsList> getNotifications2(Long transactionId2);

	public boolean createObjection(Date date, Date dateTransaction, String objection, String usrs, Long accountId, String ip, Long chargeId, Long companyId, Long stationId, Long laneId);

	public String createNotificationRejected(String idPhoto, Date date, String name,
			String notification, Long typeId, String usrs,Long idTran);

	public List<TbTransactionState> getStateTransaction();

	public boolean crateValidation(Long typeId, String notification, Long userId, Long transactionId);
	
	public List<String> getInfoTransactionValidate(Long transactionId);
}
