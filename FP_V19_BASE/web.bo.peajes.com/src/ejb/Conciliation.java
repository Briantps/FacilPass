package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbDailyConcDetail;
import jpa.TbDailyConciliation;

/**
 * Defines a set of methods to perform control on contents of Conciliation
 * @author Julián Romero
 */
@Remote
public interface Conciliation {

	public List<TbDailyConcDetail> getDailyConcDetail(Long dailyConcId);
	
	public boolean saveClientConc(Long userId,Long vehicleId,Long accountId,Long valueRecharge,Long dailyConcId,String ip);
	
	public Long createDailyConc(Long userId,Date dateClosing,String ip);
	
	public TbDailyConciliation getDailyConc(Long dailyConcId);
	
	public boolean createLogDailyConc(Long dailyConcId,Long dailyConcStateId,Long valEffe,
			String observation,Long creatorUser,String ip);
	
	public Long calculateDiffDailyConc(Long valBala,Long valueEffec);
	
	public boolean updateStateDailyConc(Long dailyConcId,Long dailyConcStateId,Long valEffe,String ip,Long creatorUser);
	
	public boolean isCloseDayById(Long dailyConcId);
	
	public Long getDailyConcByFilters(Long codeType,String code,String firstName,String lastName,
			String userEmail,Date fechaCierre);
	
	public Long getDailyConcByDate(Long userId,Date dateClosing);
	
	public Long getDailyConcStateById(Long dailyConc);
	
}