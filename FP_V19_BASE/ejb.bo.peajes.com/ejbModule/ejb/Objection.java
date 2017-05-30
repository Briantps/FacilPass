package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import util.Objections;

import constant.AjustmentStateType;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbObjection;
import jpa.TbStationBO;
import jpa.TbSystemUser;

@Remote
public interface Objection {
	
	public List<TbObjection> getObjectionByState(Long state);
	
	public TbObjection getObjectionById(Long idObjection);
	
	public boolean setAjustmentObjection(Long value, Long objectionId, AjustmentStateType tipo, String ip, Long user);

	public boolean createObjection(Date date, Date dateTransaction,
			String objection, TbSystemUser user, Long accountId, String ip,
			Long chargeId, Long companyId, Long stationId, Long laneId, boolean res1);
	
	public TbStationBO getStationById(Long idStation);
	
	public TbCompany getConcesionById(Long idConcesion);
	
	public TbLane getCarrilById(Long idCarril);
	
	public List<Objections> getObjectionByClient(Long userId);
	
	public boolean updateObjection(Long objectionId, Date newDate,
			String newObjection, TbSystemUser tt, Long newAccountId, String ip,
			Long newChargeId, Long newCompanyId, Long newStationId,
			Long newLaneId, boolean b);
	
	public boolean deleteObjection(Long objectionId);

	public boolean validateDateReclamation(Long accountId, Date dateTransaction);
	
}
