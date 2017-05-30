package jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TbFrequency
 *
 */
@Entity
@Table(name="TB_FREQUENCY")
public class TbFrequency implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FREQUENCY_ID")
	private Long frequencyId;
	
	@Column(name="FREQUENCY_DESCRIPTION")
	private String frequencyDescript;
	
	@Column(name="FREQUENCY_EQUIVALENT")
	private Long frequencyEquiv;
	
	@Column(name = "FREQUENCY_STATE")
	private Integer frequencyState;

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyDescript(String frequencyDescript) {
		this.frequencyDescript = frequencyDescript;
	}

	public String getFrequencyDescript() {
		return frequencyDescript;
	}

	public void setFrequencyEquiv(Long frequencyEquiv) {
		this.frequencyEquiv = frequencyEquiv;
	}

	public Long getFrequencyEquiv() {
		return frequencyEquiv;
	}

	public void setFrequencyState(Integer frequencyState) {
		this.frequencyState = frequencyState;
	}

	public Integer getFrequencyState() {
		return frequencyState;
	}
   
}
