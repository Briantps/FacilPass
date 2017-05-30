package ejb.taskeng.email;

import java.io.Serializable;

public interface SubjectField extends Serializable {

	public void setId(int id);

	public int getId();

	public void setContent(String content);

	public String getContent();

}