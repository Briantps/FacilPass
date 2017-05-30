package util.ws;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.ejb.Remote;

import util.ObjectRechage;


@Remote
public interface WSRecharge {
	
	public ObjectRechage createListObj (Long idToken);
	
	public Long createUmbral(Long account, BigDecimal average, BigDecimal value, Timestamp dateTransaction,Long state, Long message, String ip, Long user );
	
	public Long createUmbral(Long account, BigDecimal average, BigDecimal value, Timestamp dateTransaction,Long state, Long message);
	
	public String getBankNameByAccount(Long accountId);
	
	public Long getTransactionByUmbral(Long umbralId);

}
