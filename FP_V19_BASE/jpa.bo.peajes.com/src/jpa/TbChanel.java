package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="TB_CHANEL")
/*@NamedQueries(
				@NamedQuery(
							name="findChannelId",
				            query="select chanelId from TbChanel where cod_channel : paramChanelCod"
				     		)
     		)
     		*/
public class TbChanel implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_CHANNEL_TBCHANELID_GENERATOR")
	@SequenceGenerator(name = "TB_CHANNEL_TBCHANELID_GENERATOR", sequenceName = "TB_CHANNEL_SEQ",allocationSize=1)
	@Column (name="CHANEL_ID", unique=true, nullable=false)
	private Long chanelId;
	
	@Column(name = "CHANEL_TYPE")
	private String chanelType;
	
	@Column(name = "CHANEL_STATE")
	private Long chanelState;
	
	@Column(name = "CHANEL_DESCRIPTION")
	private String chanelDescription;
	
	@Column(name="COD_CHANEL")
	private Long cod_channel;
	
	/**
	 * @author TPS r.bautista
	 */
	@Column(name="CHANEL_MINIMUM_ACTIVE")
	private Long minimumActive;

	/**
	 * @author TPS r.bautista
	 */
	@Column(name="CHANEL_MINIMUM_ALLOCATION")
	private Long minimumValue;
	
	public TbChanel(){
		super();
	}

	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}

	public Long getChanelId() {
		return chanelId;
	}

	public void setChanelType(String chanelType) {
		this.chanelType = chanelType;
	}

	public String getChanelType() {
		return chanelType;
	}

	public void setChanelState(Long chanelState) {
		this.chanelState = chanelState;
	}

	public Long getChanelState() {
		return chanelState;
	}

	public void setChanelDescription(String chanelDescription) {
		this.chanelDescription = chanelDescription;
	}

	public String getChanelDescription() {
		return chanelDescription;
	}

	public Long getCod_channel() {
		return cod_channel;
	}

	public void setCod_channel(Long cod_channel) {
		this.cod_channel = cod_channel;
	}

	/**
	 * @author TPS r.bautista
	 */
	public Long getMinimumActive() {
		return minimumActive;
	}

	/**
	 * @author TPS r.bautista
	 */
	public void setMinimumActive(Long minimumActive) {
		this.minimumActive = minimumActive;
	}

	/**
	 * @author TPS r.bautista
	 */
	public Long getMinimumValue() {
		return minimumValue;
	}

	/**
	 * @author TPS r.bautista
	 */
	public void setMinimumValue(Long minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * Indica si la recarga tiene activo la minima recarga
	 * @return true si tiene activa la minima recarga
	 * @author TPS r.bautista
	 */
	public boolean isValidaRecAsigMinima(){
		if (this.minimumActive == null){
			return false;
		}

		return this.minimumActive.intValue() == 1;
	}


}
