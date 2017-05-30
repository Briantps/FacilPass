package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbSystemParameter;

@Remote
public interface Config {

	public List<TbSystemParameter> getParameters();

	public boolean updateParameter(Long parameterId, String valueParameter, String nameParameter, Long userId, String ip);
	
	public String getParameter(Long idParameter);
	
}
