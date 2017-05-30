package ejb;

import java.util.List;

import javax.ejb.Remote;
import jpa.TbAccount;
import jpa.TbFrequency;

@Remote
public interface AutomaticRecharge {
	
  public TbAccount getAccountbyCode(Long accountId);
  
  public List<TbFrequency> getListFrequency();
}
