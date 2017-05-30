package ejb;


import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbTypeDocument;

import util.AllInfoVerification;



@Remote
public interface Document {
	
	public boolean getValidateDocument(Long documentId, Long typeDocumentId, 
			String state, String description, Long userId, String ip, Long creationUser);
		
	public List<AllInfoVerification> getStateDocumentByFilters(Long idIds, String numberDoc, String name, 
		String secondName, String email, String accountId, String state, String fechaCargue, Date fechaInicial, Date fechaFinal, int page, int rows);
	
	public List<AllInfoVerification> getDocumentbyClient(Long userId);
	
	public List<TbTypeDocument> getDocumentList();
	
	public boolean insertDocument(Long userId, Long documentTypeId, Long documentState, 
			String string, String uploadedFileName, String ip, Long creationUser);

	public Long insertDocumentDisk(long userId, Long documentTypeId, Long documentState, 
			String uploadedFileName);

	public boolean getValidateSearchDocument(List<AllInfoVerification> listfilter, String documentState, String ip, Long creationUser);

}
