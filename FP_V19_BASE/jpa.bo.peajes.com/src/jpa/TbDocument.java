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
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_DOCUMENT")
public class TbDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_DOCUMENT_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_DOCUMENT_SEQ", sequenceName = "TB_DOCUMENT_SEQ",allocationSize=1)
	
	@Column(name="DOCUMENT_ID")
	private Long documentId;

	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount accountId;
	
	@ManyToOne
	@JoinColumn(name="TYPE_DOCUMENT_ID")
	private TbTypeDocument TbTypeDocument;
	
	@ManyToOne
	@JoinColumn(name="VEHICLE_PLATE")
	private TbVehicle plateId;
	
	@Column(name="STATE_DOCUMENT")
	private Integer stateDocument;
	
	@Column(name="URL_DOCUMENT")
	private String urlDocument;
	
	@Column(name="NAME_DOCUMENT")
	private String nameDocument;
	
	@Column(name="USER_ID_UPLOAD")
	private Long userIdUpload;
	
	@Column(name="PROCESS_DATE_DOCUMENT")
	private Timestamp processDateDocument;

	@Column(name="UPLOAD_DATE_DOCUMENT")
	private Timestamp uploadDateDocument;
	
	@Column(name="DESCRIPTION")
	private String description;
	/**
	 * Constructor
	 */
    public TbDocument() {
    	super();
    }
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return the documentId
	 */
	public Long getDocumentId() {
		return documentId;
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
	 * @param accountId the accountId to set
	 */
	public void setAccountId(TbAccount accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the accountId
	 */
	public TbAccount getAccountId() {
		return accountId;
	}
	/**
	 * @param tbTypeDocument the tbTypeDocument to set
	 */
	public void setTbTypeDocument(TbTypeDocument tbTypeDocument) {
		TbTypeDocument = tbTypeDocument;
	}
	/**
	 * @return the tbTypeDocument
	 */
	public TbTypeDocument getTbTypeDocument() {
		return TbTypeDocument;
	}

	/**
	 * @param stateDocument the stateDocument to set
	 */
	public void setStateDocument(Integer stateDocument) {
		this.stateDocument = stateDocument;
	}
	/**
	 * @return the stateDocument
	 */
	public Integer getStateDocument() {
		return stateDocument;
	}

	/**
	 * @param uploadDateDocument the uploadDateDocument to set
	 */
	public void setUploadDateDocument(Timestamp uploadDateDocument) {
		this.uploadDateDocument = uploadDateDocument;
	}
	/**
	 * @return the uploadDateDocument
	 */
	public Timestamp getUploadDateDocument() {
		return uploadDateDocument;
	}

	/**
	 * @param urlDocument the urlDocument to set
	 */
	public void setUrlDocument(String urlDocument) {
		this.urlDocument = urlDocument;
	}
	/**
	 * @return the urlDocument
	 */
	public String getUrlDocument() {
		return urlDocument;
	}
	/**
	 * @param processDateDocument the processDateDocument to set
	 */
	public void setProcessDateDocument(Timestamp processDateDocument) {
		this.processDateDocument = processDateDocument;
	}
	/**
	 * @return the processDateDocument
	 */
	public Timestamp getProcessDateDocument() {
		return processDateDocument;
	}
	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(TbVehicle plateId) {
		this.plateId = plateId;
	}
	/**
	 * @return the plateId
	 */
	public TbVehicle getPlateId() {
		return plateId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setNameDocument(String nameDocument) {
		this.nameDocument = nameDocument;
	}
	public String getNameDocument() {
		return nameDocument;
	}
	public void setUserIdUpload(Long userIdUpload) {
		this.userIdUpload = userIdUpload;
	}
	public Long getUserIdUpload() {
		return userIdUpload;
	}

   

}