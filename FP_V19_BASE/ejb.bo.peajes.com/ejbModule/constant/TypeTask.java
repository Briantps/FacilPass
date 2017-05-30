package constant;

public enum TypeTask{
	
	ACCOUNT(1,"TB_ACCOUNT"),
	DEVICE(2,"TAG"),
	RECHARGEDEVICE(3,"TB_TRAN_TAG"),
	THRESHOLD(0,"TB_UMBRAL"),
	AUTOMATICRECHARGE(4,"TB_AUTOMATIC_RECHARGE");
	
	
	private int tipTask;
	private String table;
	
	/**
	  * 
	  * @param id
	  */
	 private TypeTask(int t, String table) {
	   this.tipTask = t;
	   this.table = table;
	 }

	 /**
	  * 
	  * @return id
	  */
	 public int getTipTask() {
	  	 return tipTask;
	 }

		public String getTable() {
		return table;
	}


	
}
