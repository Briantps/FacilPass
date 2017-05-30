package util;

public class AttachmentDef {
	
	private String filePath;
	
	public AttachmentDef(){
	}
	
	public AttachmentDef(String filePath){
		setFilePath(filePath);
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

}
