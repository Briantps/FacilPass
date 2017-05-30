/**
 * 
 */
package process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.DataScrollerEvent;

import jpa.TbSystemUser;
import sessionVar.SessionUtil;
import util.TableProcess;
import constant.ProcessTrackType;

/**
 * @author tmolina
 * 
 */
public class MyProcessBean implements Serializable {
	private static final long serialVersionUID = -782765665696861585L;

	@EJB(mappedName = "ejb/Process")
	private ejb.Process process;

	private List<Integer> listaScroll;
	private List<TableProcess> details;
	private TbSystemUser client= SessionUtil.sessionUser();

	// Control Visibility --------------- //

	private String message;

	private boolean showMessage;

	private boolean showData = true;

	private String fileNameXMLRq;

	private String fileNameXMLRp;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.


	private String title;

	private Long pseId = -1L;

	private String filePse = "";
	private Date dateStart;
	private Date dateEnd;

	private boolean searchok = false;
	private boolean refreshData = true;
	
	private int page=1;
	private int valuesFor;

	/**
	 * Constructor.
	 */
	public MyProcessBean() {
	}

	@PostConstruct
	public void init() {
		this.hideModal();
	}

	
	
	public void searchFilter() {
		setTitle("Procesos de Cliente");
		if (dateStart != null && dateEnd != null) {
			if (dateEnd.getTime() >= dateStart.getTime()) {
				if (dateEnd.getTime() <= (new Date()).getTime()) {
					setPage(1);
					List<TableProcess> listfilter = null;    
					listfilter = process.getProcessDetailsClient(
							client.getUserId(),ProcessTrackType.MY_CLIENT_PROCESS, dateStart, dateEnd, page, 20);
					if (listfilter.size() > 0) {
						this.details= listfilter;
						this.setSearchok(true);
						setMessage("");
						setShowMessage(false);
						setShowData(true);
					} else {
						this.setShowMessage(true);
						this.setMessage("No hay información para las fechas seleccionadas.");
						this.setShowData(true);
						this.details.clear();
						this.setSearchok(false);
					}
				} else {
					setShowMessage(true);
					setMessage("Error: La fecha final no debe ser mayor a la fecha actual.");
					setShowData(false);
				}
			} else {
				setShowMessage(true);
				setMessage("Error: La fecha final no debe ser menor a la fecha inicial.");
				setShowData(false);
			}
		} else if((dateEnd == null && dateStart!=null) || (dateEnd != null && dateStart==null)){
				setShowMessage(true);
				setMessage("Error: La fecha inicial y la fecha final no pueden estar vacías.");
				setShowData(false);
		}
	}

	public String downloadVoucher() {
		setTitle("Procesos de Cliente");
		String p = null;
		String f = null;
		try {
			System.out.println("Muesta PDF");
			filePse = filePse.replace("/", "\\");
			p = filePse.substring(0, filePse.lastIndexOf("\\"));
			f = filePse.substring(filePse.lastIndexOf("\\") + 1).trim();
			System.out.println("filePse: " + filePse);
			System.out.println("p: " + p);
			System.out.println("f: " + f);
			java.io.File directory = new java.io.File(filePse);
			if (directory.exists()) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext
						.getExternalContext();
				HttpServletResponse response = (HttpServletResponse) externalContext
						.getResponse();
				System.out.println("response" + response.toString());
				File file = new File(p, f);
				BufferedInputStream input = null;
				BufferedOutputStream output = null;

				try {
					System.out.println("Entra aqui");
					// Open file.
					input = new BufferedInputStream(new FileInputStream(file),
							DEFAULT_BUFFER_SIZE);
					response.reset();
					response.setHeader("Content-Type", "application/pdf");
					response.setHeader("Content-Length",
							String.valueOf(file.length()));
					System.out.println("file.length() " + file.length());
					response.setHeader("Content-Disposition",
							"inline; filename=\"" + f + "\"");
					output = new BufferedOutputStream(
							response.getOutputStream(), DEFAULT_BUFFER_SIZE);

					// Write file contents to response.
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}

					// Finalize task.
					output.flush();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(" [ Error MyProcessBean.printPdf ]");
				} finally {
					// Gently close streams.
					System.out.println("Finally");
					close(output);
					close(input);
				}

				// Inform JSF that it doesn't need to handle response.
				// This is very important, otherwise you will get the following
				// exception in the logs:
				// java.lang.IllegalStateException: Cannot forward after
				// response has
				// been committed.
				facesContext.responseComplete();
			} else {
				setMessage("No se encontró el archivo solicitado.");
				setShowMessage(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error MyProcessBean.downloadVoucher ] ");
			setMessage("Ha ocurrido un error, intente más tarde.");
			setShowMessage(true);
		}
		return null;
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				// Do your thing with the exception. Print it, log it or mail
				// it. It may be useful to
				// know that this will generally only be thrown when the client
				// aborted the download.
				e.printStackTrace();
			}
		}
	}

	// Getters and Setters ------ //

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(List<TableProcess> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TableProcess> getDetails() {
	    if (details == null) {
			details = new ArrayList<TableProcess>();
		}else if(searchok==false){
			details.clear(); 
		} 
	    if(searchok==false){
			this.getDataForScroll();
			details = process.getProcessDetailsClient(client.getUserId(),
					ProcessTrackType.MY_CLIENT_PROCESS, null, null, page, 20);	
		}
		if (searchok==true){
			this.getDataForScroll();
			details = process.getProcessDetailsClient(client.getUserId(),
					ProcessTrackType.MY_CLIENT_PROCESS, dateStart, dateEnd, page, 20);	
		}
		/*
		 * if (details == null) { String viewProcessFirstTime = (String)
		 * FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("viewProcessFirstTime"); if (viewProcessFirstTime == null ||
		 * viewProcessFirstTime.isEmpty()) { search(); ((HttpSession)
		 * FacesContext
		 * .getCurrentInstance().getExternalContext().getSession(false
		 * )).setAttribute("viewProcessFirstTime", "false"); if (details ==
		 * null) { details = new ArrayList<TableProcess>(); } } }
		 */
		return details;
	}
	
	public void getDataForScroll(){
		try {
			if(searchok==false){
				this.setValuesFor(Integer.parseInt(String.valueOf(process.getProcessDetailsClient(
						client.getUserId(), ProcessTrackType.MY_CLIENT_PROCESS, null, null, 0, 20).get(0))));
			}else{
				this.setValuesFor(Integer.parseInt(String.valueOf(process.getProcessDetailsClient(
						client.getUserId(), ProcessTrackType.MY_CLIENT_PROCESS, dateStart, dateEnd, 0, 20).get(0))));
			}
			listaScroll=new ArrayList<Integer>();
			for(int i=0;i<getValuesFor();i++){	
				listaScroll.add(i);
			}
		} catch (Exception e) {
	     	e.printStackTrace();
		}
	}
	
	
	public void dataScroller(ActionEvent event)throws AbortProcessingException {
		DataScrollerEvent events=(DataScrollerEvent)event;
		page = events.getPage();
		this.setPage(1);
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param showMessage
	 *            the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
	}

	/**
	 * @param showData
	 *            the showData to set
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

	public void hideModal() {
		this.setMessage("");
		this.setShowMessage(false);
	}

	public String getFileNameXMLRq() {
		return fileNameXMLRq;
	}

	public void setFileNameXMLRq(String fileNameXMLRq) {
		this.fileNameXMLRq = fileNameXMLRq;
	}

	public String getFileNameXMLRp() {
		return fileNameXMLRp;
	}

	public void setFileNameXMLRp(String fileNameXMLRp) {
		this.fileNameXMLRp = fileNameXMLRp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPseId() {
		return pseId;
	}

	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}

	public String getFilePse() {
		return filePse;
	}

	public void setFilePse(String filePse) {
		this.filePse = filePse;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public boolean isRefreshData() {
		return refreshData;
	}

	public void setRefreshData(boolean refreshData) {
		this.refreshData = refreshData;
	}

	public boolean isSearchok() {
		return searchok;
	}

	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}
	
	public void setListaScroll(List<Integer> listaScroll) {
		this.listaScroll = listaScroll;
	}

	public List<Integer> getListaScroll() {
		return listaScroll;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setValuesFor(int valuesFor) {
		this.valuesFor = valuesFor;
	}

	public int getValuesFor() {
		return valuesFor;
	}

}