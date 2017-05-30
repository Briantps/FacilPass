package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetail;
import jpa.TbSystemUser;
import util.TableProcess;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

@Remote
public interface Process {
	
	/**
	 * @param processTrackTypeId
	 * @param id
	 * @return Created Object TbProcessTrack Id.
	 */
	public Long createProcessTrack(ProcessTrackType processTrackTypeId, Long id, String ip, Long creationUser);
	

	/**
	 * 
	 * @param processTrackId
	 * @param detailType
	 * @param message
	 * @param creationUser
	 * @param ip
	 * @param state 0/open - 1/Closed
	 * @param messageError
	 * @return
	 */
	public Long createProcessDetail(Long processTrackId, ProcessTrackDetailType detailType,
			String message, Long creationUser, String ip, Integer state, String messageError,
			int processError,String fileNameRq,String fileNameRp,String filePse,Long pseId, Long typeId);
	
	/**
	 * 
	 * @return List of created potential clients.
	 */
	public List<TbSystemUser> getPotentialClients();
	
	/**
	 * 
	 * @param id
	 * @param creationUser
	 * @param ip
	 * @return
	 */
	public boolean affiliationDocsCheck(Long id, Long creationUser, String ip);
	
	/**
	 * 
	 * @return List of clients with active process.
	 */
	public List<TbSystemUser> getClientsWithActiveProcess();
	
	/**
	 * 
	 * @return List of client By process detail and state
	 */
	public List<TbSystemUser> getClientByProcessDetailType(ProcessTrackDetailType type);
	
	/**
	 * 
	 * @param idClient
	 * @param isVerificationOk
	 * @param observation
	 * @param creationUser
	 * @param ip
	 * @return
	 */
	public boolean saveVerification(Long idClient, boolean isVerificationOk,
			String observation, Long creationUser, String ip);
	
	/**
	 * 
	 * @param type
	 * @return {@link TbDevice} {@link List} by Device Type.
	 */
	public List<TbDevice> getDevicesWithProcessByType(Long type);
	
	/**
	 * 
	 * @param plate
	 * @return {@link TbProcessTrackDetail} {@link List}
	 */
	public List<TbProcessTrackDetail> getCustomizationProcessDetailByPlate(
			String plate);
	
	/**
	 * 
	 * @param processType
	 * @param referencedId
	 * @return {@link TbProcessTrack}
	 */
	public TbProcessTrack searchProcess(ProcessTrackType processType,
			Long referencedId);
	
	/**
	 * 
	 * @param inclusionId
	 * @return {@link TbProcessTrackDetail} {@link List}
	 */
	public List<TbProcessTrackDetail> getProcessDetails(Long referencedId,
			ProcessTrackType processType);


	public List<TbAccount> getAccountWithActiveProcess();


	public Long createProcessDetail(Long processTrackId,
			ProcessTrackDetailType detailType, String message,
			Long creationUser, String ip, Integer state, String messageError,
			Long typeId,Integer processError,String fileNameRq,String fileNameR);


	public List<TableProcess> getProcessDetailsClient(Long referencedId,
			ProcessTrackType processType,Date dateStart,Date dateEnd, int page, int rows);

	public String getResponseDescByCode(Long responseCode,Long type);
	
	/**
	 * @author JGomez
	 * @param Codigo de respuesta
	 * @param tipo
	 * @param Codigo TB_UMBRAL
	 */
	public void insertRespuAval (Long id, Long type, Long umbralId);
}