package constant;

public enum DistributionType {
	
	BAGMONEY(1L),
	AUTOMATIC(2L),
	MANUAL(3L);
	
	private Long distributionID;
	
	
	private DistributionType(Long t) {
	   this.distributionID = t;
	}
	
	public Long getDistributionID() {
	  	 return distributionID;
	 }
}
