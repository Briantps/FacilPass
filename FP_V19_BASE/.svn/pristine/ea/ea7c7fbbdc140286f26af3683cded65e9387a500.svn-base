package upload;

public class File {

    private String Name;
    private String Filetype;
    private String mime;
    private long length;
    private byte[] data;
    
    /**
	 * Constructor
	 */
	public File(java.io.File strFilesDirs) {
		
	}

	public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("xls".equals(extension)||"XLS".equals(extension)){
                mime="image/xls";
            } else if("xlsx".equals(extension)||"XLSX".equals(extension)){
                mime="image/xlsx";
          /*}else if("bmp".equals(extension)||"BMP".equals(extension)){
                mime="image/bmp";*/
            } else if("jpg".equals(extension)||"JPG".equals(extension)){
                mime="image/jpeg";
            } else if("gif".equals(extension)||"GIF".equals(extension)){
                mime="image/gif";
            } else if("png".equals(extension)||"PNG".equals(extension)){
                mime="image/png";
            } else if("pdf".equals(extension)||"PDF".equals(extension)){
                mime="application/pdf";
            } else if("doc".equals(extension)||"DOC".equals(extension)){
                mime="application/msword";
            }  else if("docx".equals(extension)||"DOCX".equals(extension)){
                mime="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }else if("txt".equals(extension)||"TXT".equals(extension)){
                mime="application/txt";
	        } else if("tif".equals(extension)||"TIF".equals(extension)){
	            mime="application/tif";
	        }else {
                mime = "image/unknown";
            }
        }
    }
    
	/**
	 * @param filetype the filetype to set
	 */
	public void setFiletype(String filetype) {
		Filetype = filetype;
	}

	/**
	 * @return the filetype
	 */
	public String getFiletype() {
		return Filetype;
	}
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getMime(){
        return mime;
    }
}
