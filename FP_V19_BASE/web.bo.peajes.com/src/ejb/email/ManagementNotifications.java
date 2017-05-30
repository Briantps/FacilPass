package ejb.email;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbSystemUser;

@Remote
public interface ManagementNotifications {
	
	public long validateFilters(String[] filters);
	
	public TbSystemUser getClientByFilters(String[] filters);
	
	public List<util.Notifications> getListNotificationsByClient(Long clientId);
	
	public String getAccountsByClient(Long clientId);
	
	public Long saveNotifications(List<util.Notifications> newList, Long userLogg, String ipLogg);
	
	public TbSystemUser getClientByUserId(Long userId);
}
