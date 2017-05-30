package process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import constant.ProcessTrackType;

import jpa.TbAccount;
import jpa.TbProcessTrackDetail;

/**
 * @author ablanco
 *
 */
public class AccountProcessBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	private String idAccount;
	
	private List<TbAccount> listAccounts;
	
	private List<String> list;
	
	private boolean showData;
	
	private List<TbProcessTrackDetail> details;
	
	private String message;
	
	private boolean showMessage;
	
	public AccountProcessBean(){
		super();
		this.setMessage(null);
		this.setShowMessage(false);
		this.setShowData(false);
	}
	
	/**
	 * 
	 * @param suggest
	 * @return
	 */
	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		if (list == null) {
			list = new ArrayList<String>();
		}
		list = getList();
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}

	
	private boolean postValidationSearch(){
		if(idAccount!="" && !idAccount.matches("([0-9])+")){
			this.setShowData(false);
			this.setMessage("El campo Número de Cuenta tiene caracteres inválidos. Verifique.");
			this.setShowMessage(true);
			return false;
		}else if(idAccount.trim().length()==0){
			this.setShowData(false);
			this.setMessage("Debe digitar el Número de Cuenta.");
			this.setShowMessage(true);
			return false;
		}
	return true;	
	}
	
	
	public void search(){
		if(postValidationSearch()){
			details = new ArrayList<TbProcessTrackDetail>();
			details = process.getProcessDetails(Long.parseLong(idAccount.trim()), ProcessTrackType.ACCOUNT1);	
			if(details.size()>0){
				this.setMessage("");
				this.setShowMessage(false);
				this.setShowData(true);			
			}else{
				this.setShowData(false);
				this.setMessage("No existe información para la cuenta ingresada");
				this.setShowMessage(true);
			}
		}
	}
	
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	public String getIdAccount() {
		return idAccount;
	}

	public void setListAccounts(List<TbAccount> listAccounts) {
		this.listAccounts = listAccounts;
	}

	public List<TbAccount> getListAccounts() {
		listAccounts= new ArrayList<TbAccount>();
		listAccounts= process.getAccountWithActiveProcess();

		return listAccounts;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public List<String> getList() {
		list= new ArrayList<String>();
		for(TbAccount t:this.getListAccounts()){
			list.add(t.getAccountId().toString());
		}
		return list;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setDetails(List<TbProcessTrackDetail> details) {
		this.details = details;
	}

	public List<TbProcessTrackDetail> getDetails() {
		return details;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowMessage() {
		return showMessage;
	}
	
	public void hideModal(){
		this.setShowMessage(false);
		this.setMessage("");
	}

}
