package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_INVOICE")
public class TbInvoice implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="INVOICE_ID")
    private Long invoiceId;
    
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
    private TbAccount accountId;
    
	@Column(name="RUT")
    private String rut;
    
	@Column(name="GENERATION_DATE")
    private Date GenerationDate;
    
	@Column(name="NAME_CLIENT")
    private String nameClient;
    
	@Column(name="ADDRESS_CLIENT")
    private String addressClient;
    
	@Column(name="VALUE")
    private BigDecimal value;
    
	@Column(name="TYPE_INVOICE")
    private Long  typeInvoice;
    
	@Column(name="INIT_DATE")
    private Date initData;
	
	@Column(name="SEND_STATE")
    private Long sendState;
    
    public TbInvoice(){
    	super();
    }

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setAccountId(TbAccount accountId) {
		this.accountId = accountId;
	}

	public TbAccount getAccountId() {
		return accountId;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getRut() {
		return rut;
	}

	public void setGenerationDate(Date generationDate) {
		GenerationDate = generationDate;
	}

	public Date getGenerationDate() {
		return GenerationDate;
	}


	public void setAddressClient(String addressClient) {
		this.addressClient = addressClient;
	}

	public String getAddressClient() {
		return addressClient;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setTypeInvoice(Long typeInvoice) {
		this.typeInvoice = typeInvoice;
	}

	public Long getTypeInvoice() {
		return typeInvoice;
	}

	public void setInitData(Date initData) {
		this.initData = initData;
	}

	public Date getInitData() {
		return initData;
	}

	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}

	public String getNameClient() {
		return nameClient;
	}

	/**
	 * @param sendState the sendState to set
	 */
	public void setSendState(Long sendState) {
		this.sendState = sendState;
	}

	/**
	 * @return the sendState
	 */
	public Long getSendState() {
		return sendState;
	}
    
    
    
    
    
}
