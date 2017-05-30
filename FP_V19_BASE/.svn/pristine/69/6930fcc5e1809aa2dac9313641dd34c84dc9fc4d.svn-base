package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbSystemParameter;

@Remote
public interface SystemParameters {

	public String getParameterValueById(Long idParameter);

	public String getParameterName(String typePath);

	public List<TbSystemParameter> getListPath();

	public String validateVigParameter(String valueParameter);

	public String minimumResponses(int valueParameter);
	
}
