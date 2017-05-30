/**
 * 
 */
package transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import report.dataSource.AccountTrantactionDataSource;
import security.UserLogged;
import util.ConnectionData;

import ejb.Device;
import ejb.Transaction;
import ejb.User;

import jpa.TbAccount;
import jpa.TbDevice;

/**
 * @author tmolina
 *
 */
public class AccountTransactionBean extends AbstractBaseReportBean  implements Serializable {
	private static final long serialVersionUID = -3099986808739679757L;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;
	
	@EJB(mappedName = "ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName = "ejb/Device")
	private Device device;
	
	// Attributes --- //
	
	private Long idClientAccount;
	
	private List<SelectItem> clientAccounts;
	
	private List<SelectItem> myClientAccounts;
	
	private TbAccount account;
	
	private Date initDate;
	
	private Date endDate;
	
	private List<jpa.TbTransaction> transactionList;
	
	private UserLogged usrs;
	
	// Modal --- //
			
	private boolean showData;
		
	private boolean showMessageError;
	
	private String messageError;

	/**
	 * Constructor.
	 */
	public AccountTransactionBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		initDate = new Date();
		endDate = new Date();
	}
	
	// Actions --- //
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		setShowData(false);
		setShowMessageError(false);
		transactionList = null;
		setIdClientAccount(-1L);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String search() {
		if (idClientAccount.longValue() != -1L) {
			if(initDate != null) {
				if(endDate != null) {
					if (endDate.after(initDate) || initDate.compareTo(endDate) == 0) {
						
						transactionList = transaction.getTransactionByAccountAndDate(
										idClientAccount, initDate, endDate);
						System.out.println(transactionList.size());
						
						if (transactionList.size() > 0) {
							setShowData(true);
							setShowMessageError(false);
							this.account = transactionList.get(0).getTbAccount();
						} else {
							setMessageError("No se encontraron resultados.");
							setShowData(false);
							setShowMessageError(true);
						}
					} else {
						setMessageError("La fecha final debe ser después de la fecha inicial. Verifique el Rango.");
						setShowData(false);
						setShowMessageError(true);
					}
				} else {
					setMessageError("Seleccione la fecha Final.");
					setShowData(false);
					setShowMessageError(true);
				}
			} else {
				setMessageError("Seleccione la fecha Inicial.");
				setShowData(false);
				setShowMessageError(true);
			}
		} else {
			setMessageError("Seleccione una cuenta de cliente.");
			setShowData(false);
			setShowMessageError(true);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String printPdf() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityReportBean");
			e.printStackTrace();
		}
		return null;
	}
	
	// Getters and Setters --- //
	
	/**
	 * @param clientAccounts the clientAccounts to set
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * @return the clientAccounts
	 */
	public List<SelectItem> getClientAccounts() {
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una Cuenta--"));
		for(TbAccount su : userEJB.getClientAccount(null)){
			String name = su.getTbSystemUser().getUserNames();
			if(su.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3L){
				name += " " + su.getTbSystemUser().getUserSecondNames();
			}
			String c="Cuenta No. " + su.getAccountId() + " " + name;
			clientAccounts.add(new SelectItem(su.getAccountId(),c));
		}
		return clientAccounts;
	}

	/**
	 * @param idClientAccount the idClientAccount to set
	 */
	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}

	/**
	 * @return the idClientAccount
	 */
	public Long getIdClientAccount() {
		return idClientAccount;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(TbAccount account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public TbAccount getAccount() {
		return account;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param showMessageError the showMessageError to set
	 */
	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the showMessageError
	 */
	public boolean isShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	// * ---------- Override Methods ------------ * //
	
	/*
	 * 
	 */
	@Override
	protected String getCompileFileName() {
		return "accountMove";
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Movimiento_Cuenta_" + System.currentTimeMillis();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		String name = account.getTbSystemUser().getUserNames();
		if (account.getTbSystemUser().getTbCodeType().getCodeTypeId() != 3L) {
			name += " " + account.getTbSystemUser().getUserSecondNames();
		}
		reportParameters.put("TITTLE", "Movimiento de Cuenta FacilPass");
		reportParameters.put("INIT_DATE", initDate);
		reportParameters.put("END_DATE", endDate);
		reportParameters.put("ACCOUNT_NO", String.valueOf(account.getAccountId()));
		reportParameters.put("CLIENT_NAME", name);
		Long dt = null;
		BigDecimal balance = new BigDecimal(0);
		if(account!=null){
		   dt=account.getDistributionTypeId().getDistributionTypeId();	
		   if(dt==1){
			   balance= account.getAccountBalance();
		   }
		   else{
				balance=account.getAccountBalance();
				List<TbDevice> lis = new ArrayList<TbDevice>();
				lis=device.getDevicesByAccount(account.getAccountId());
				if(lis.size()>0){
					for(TbDevice t: lis){
						System.out.println("t: " +t);
						balance= balance.add(t.getDeviceCurrentBalance());
					}	
				}
			}
		}
		
		
		reportParameters.put("BALANCE", balance);
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		Object[][] dataSource = new Object[transactionList.size()][6];
		int i = 0;
		for (jpa.TbTransaction a : transactionList) {
			dataSource[i][0] = a.getTransactionTime();					
			dataSource[i][1] = a.getTransactionValue();
			dataSource[i][2] = a.getTransactionDescription();
			dataSource[i][3] = a.getPreviousBalance();
			dataSource[i][4] = a.getNewBalance();
			dataSource[i][5] = a.getTbTransactionType().getTransactionTypeName();
			i++;
		}
		AccountTrantactionDataSource ds = new AccountTrantactionDataSource(dataSource);
		return ds;
	}

	/**
	 * @param myClientAccounts the myClientAccounts to set
	 */
	public void setMyClientAccounts(List<SelectItem> myClientAccounts) {
		this.myClientAccounts = myClientAccounts;
	}

	/**
	 * @return the myClientAccounts
	 */
	public List<SelectItem> getMyClientAccounts() {
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		if(myClientAccounts == null){
			myClientAccounts = new ArrayList<SelectItem>();
		} else {
			myClientAccounts.clear();
		}
		myClientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		for(TbAccount su : userEJB.getClientAccount(userId)){
			myClientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
		}
		return myClientAccounts;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
	
	public void ocult(){
		this.setShowData(false);
	}
}