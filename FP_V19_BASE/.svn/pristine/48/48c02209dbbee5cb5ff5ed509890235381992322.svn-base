/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *  The persistent class for the TB_PROCESS_TRACK_DETAIL database table.
 * @author tmolina
 *
 */
/**
 * @author arivera
 *
 */
@Entity
@Table(name="TB_PROCESS_TRACK_DETAIL")
public class TbProcessTrackDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="TB_PROCESS_TRACK_DETAIL_PROCESSTRACKDETAILID_GENERATOR")
	@SequenceGenerator(name="TB_PROCESS_TRACK_DETAIL_PROCESSTRACKDETAILID_GENERATOR", sequenceName="TB_PROCESS_TRACK_DETAIL_SEQ",allocationSize=1)
	@Column(name="PROCESS_TRACK_DETAIL_ID")
	private Long processTrackDetailId;
	
	@ManyToOne
	@JoinColumn(name="PROCESS_TRACK_ID")
	private TbProcessTrack tbProcessTrack;
	
	@Column(name="PROCESS_TRACK_DETAIL_STATE")
	private Integer processTrackDetailState;
	
	@Column(name="PROCESS_TRACK_ERROR")
	private Long processTrackDetailError;
	
	@Column(name="PROCESS_TRACK_DETAIL_DATE")
	private Timestamp processTrackDetailDate;
	
	@Column(name="PROCESS_TRACK_DETAIL_MESSAGE")
	private String processTrackDetailMessage;
	

	@Column(name="TYPE_ID")
	private Long typeNoteId;
	
	@Column(name="PROCES_TRACK_NAME_FILE_RQ")
	private String processTrackDetailNameFileRq;
	
	@Column(name="PROCES_TRACK_NAME_FILE_RP")
	private String processTrackDetailNameFileRp;
	
	@ManyToOne
	@JoinColumn(name="PROCESS_TRACK_DETAIL_TYPE_ID")
	private TbProcessTrackDetailType tbProcessTrackDetailType;
	
	@Column(name="PROCESS_TRACK_TRANS_PSE")
	private String processTrackTransPse;
	
	@Column(name="TRANSACTION_ID")
	private Long transactionId;
	
	
	/**
	 * Detail Creation User
	 */
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
	
	@Column(name="VIEWPROC")
	private Long verProcesoComo;
	
	@ManyToOne
	@JoinColumn(name="BANK_ID")
	private TbBank bankId;

	/**
	 * Constructor.
	 */
	public TbProcessTrackDetail() {
		super();
	}

	/**
	 * @param processTrackDetailId the processTrackDetailId to set
	 */
	public void setProcessTrackDetailId(Long processTrackDetailId) {
		this.processTrackDetailId = processTrackDetailId;
	}

	/**
	 * @return the processTrackDetailId
	 */
	public Long getProcessTrackDetailId() {
		return processTrackDetailId;
	}

	/**
	 * @param tbProcessTrack the tbProcessTrack to set
	 */
	public void setTbProcessTrack(TbProcessTrack tbProcessTrack) {
		this.tbProcessTrack = tbProcessTrack;
	}

	/**
	 * @return the tbProcessTrack
	 */
	public TbProcessTrack getTbProcessTrack() {
		return tbProcessTrack;
	}

	/**
	 * @param processTrackDetailState the processTrackDetailState to set
	 */
	public void setProcessTrackDetailState(Integer processTrackDetailState) {
		this.processTrackDetailState = processTrackDetailState;
	}

	/**
	 * @return the processTrackDetailState
	 */
	public Integer getProcessTrackDetailState() {
		return processTrackDetailState;
	}

	/**
	 * @param processTrackDetailDate the processTrackDetailDate to set
	 */
	public void setProcessTrackDetailDate(Timestamp processTrackDetailDate) {
		this.processTrackDetailDate = processTrackDetailDate;
	}

	/**
	 * @return the processTrackDetailDate
	 */
	public Timestamp getProcessTrackDetailDate() {
		return processTrackDetailDate;
	}

	/**
	 * @param processTrackDetailMessage the processTrackDetailMessage to set
	 */
	public void setProcessTrackDetailMessage(String processTrackDetailMessage) {
		this.processTrackDetailMessage = processTrackDetailMessage;
	}

	/**
	 * @return the processTrackDetailMessage
	 */
	public String getProcessTrackDetailMessage() {
		return processTrackDetailMessage;
	}

	/**
	 * @param tbSystemUser the tbSystemUser to set
	 */
	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	/**
	 * @return the tbSystemUser
	 */
	public TbSystemUser getTbSystemUser() {
		return tbSystemUser;
	}

	/**
	 * @param tbProcessTrackDetailType the tbProcessTrackDetailType to set
	 */
	public void setTbProcessTrackDetailType(TbProcessTrackDetailType tbProcessTrackDetailType) {
		this.tbProcessTrackDetailType = tbProcessTrackDetailType;
	}

	/**
	 * @return the tbProcessTrackDetailType
	 */
	public TbProcessTrackDetailType getTbProcessTrackDetailType() {
		return tbProcessTrackDetailType;
	}

	public void setTypeNoteId(Long typeNoteId) {
		this.typeNoteId = typeNoteId;
	}

	public Long getTypeNoteId() {
		return typeNoteId;
	}

	public void setVerProcesoComo(Long verProcesoComo) {
		this.verProcesoComo = verProcesoComo;
	}

	public Long getVerProcesoComo() {
		return verProcesoComo;
	}

	public void setBankId(TbBank bankId) {
		this.bankId = bankId;
	}

	public TbBank getBankId() {
		return bankId;
	}

	public Long getProcessTrackDetailError(){
		return processTrackDetailError;
	}

	public void setProcessTrackDetailError(Long processTrackDetailError) {
		this.processTrackDetailError = processTrackDetailError;
	}

	public String getProcessTrackDetailNameFileRq() {
		return processTrackDetailNameFileRq;
	}

	public void setProcessTrackDetailNameFileRq(String processTrackDetailNameFileRq) {
		this.processTrackDetailNameFileRq = processTrackDetailNameFileRq;
	}

	public String getProcessTrackDetailNameFileRp() {
		return processTrackDetailNameFileRp;
	}

	public void setProcessTrackDetailNameFileRp(String processTrackDetailNameFileRp) {
		this.processTrackDetailNameFileRp = processTrackDetailNameFileRp;
	}

	public String getProcessTrackTransPse() {
		return processTrackTransPse;
	}

	public void setProcessTrackTransPse(String processTrackTransPse) {
		this.processTrackTransPse = processTrackTransPse;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	
	
}