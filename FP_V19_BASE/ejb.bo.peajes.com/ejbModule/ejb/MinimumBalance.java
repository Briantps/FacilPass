package ejb;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import util.TbMinimumBalance;

@Remote
public interface MinimumBalance {

	public boolean getpermission(Long userId, String nameperm);

	public ArrayList<TbMinimumBalance> getCategoryRates();

	public String getcreate(Long categoryId, String valor, String saldoMinimo,String observacion, Long userid, String ip);

	public String getEdit(String valor, String saldoMinimo,
			String observacion, Long userId, String idMinimum, String ip);

	public String getapprovesCategory(String idMinimum, String saldoMinimo, Long userId, String ip);

	public boolean getExitsCategory(Long categoryId);
	
	public Long getMaxCategoryAprob (Long categoryClient);
	
	public boolean setAlarmBalance (Long account,Long idMini,Long userId,Long userIdClient, String ip);
	
	public List<BigDecimal> getRelationDevices(Long account);
	
	public boolean getExistCategoryesAprob();

	public void setCalculateMinimumBalanceAccount(Long accountId,Long creationUser, String ip);
}