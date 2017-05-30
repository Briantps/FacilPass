package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import util.AllInfoTagType;

import jpa.TbTagType;

@Remote
public interface TagType {
	
	/**
	 * 
	 * @return {@link TbTagType} {@link List}
	 */
	public List<TbTagType> getTagType();
	
	public List<AllInfoTagType> getFactoryName(String tagTypeName);
	
	public List<AllInfoTagType> getAllInfoFactories();
	
	public Long lengDeviceId (Long typeDevice);
	
	public String insertFactory(String tagTypeCode, String tagTypeName, 
			String serialLength, String ip, Long userId);

	public boolean existTagTypeU(Long tagTypeId, String tagTypeName);

	public boolean removeTagType(Long tagTypeId, String ip, Long userId);

	public String editTagType(Long tagTypeId, String tagTypeCode,
			String tagTypeName, String serialLength, String ip, Long userId);

	public boolean existTagTypeCode(String tagTypeCode);

	public boolean existTagTypeName(String tagTypeName);
	
	public String getFactoryName(Long tagTypeId);
	
}