package jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_LANE database table.
 * 
 */
@Entity
@Table(name="TB_LANE")
public class TbLane implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_LANE_LANEID_GENERATOR", sequenceName="TB_LANE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_LANE_LANEID_GENERATOR")
	@Column(name="LANE_ID", unique=true, nullable=false, precision=19)
	private long laneId;

	@Column(name="LANE_CODE", nullable=false, length=20)
	private String laneCode;

	@Column(name="LANE_NAME", nullable=false, length=20)
	private String laneName;

	//bi-directional many-to-one association to TbStation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATION_ID", nullable=false)
	private TbStationBO tbStation;

	//bi-directional many-to-one association to TbTransaction
	@OneToMany(mappedBy="tbLane")
	private List<TbTransaction> tbTransactions;

	/**
	 * Constructor
	 */
    public TbLane() {
    }

    /**
     * 
     * @return laneId
     */
	public long getLaneId() {
		return this.laneId;
	}

	/**
	 * Setter for laneId
	 * @param laneId
	 */
	public void setLaneId(long laneId) {
		this.laneId = laneId;
	}

	/**
	 * 
	 * @return laneCode
	 */
	public String getLaneCode() {
		return this.laneCode;
	}

	/**
	 * Setter for laneCode
	 * @param laneCode
	 */
	public void setLaneCode(String laneCode) {
		this.laneCode = laneCode;
	}

	/**
	 * 
	 * @return laneName
	 */
	public String getLaneName() {
		return this.laneName;
	}

	/**
	 * Setter for laneName
	 * @param laneName
	 */
	public void setLaneName(String laneName) {
		this.laneName = laneName;
	}

	/**
	 * 
	 * @return tbStation
	 */
	public TbStationBO getTbStation() {
		return this.tbStation;
	}

	/**
	 * Setter for tbStation
	 * @param tbStation
	 */
	public void setTbStation(TbStationBO tbStation) {
		this.tbStation = tbStation;
	}
	
	/**
	 * 
	 * @return tbTransactions
	 */
	public List<TbTransaction> getTbTransactions() {
		return this.tbTransactions;
	}

	/**
	 * Setter for tbTransactions
	 * @param tbTransactions
	 */
	public void setTbTransactions(List<TbTransaction> tbTransactions) {
		this.tbTransactions = tbTransactions;
	}
}