/**
 * 
 */
package process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.DataScrollerEvent;

import jpa.TbCodeType;
import jpa.TbSystemUser;
import util.TableProcess;
import constant.ProcessTrackType;
import ejb.SystemParameters;
import ejb.User;

/**
 * @author tmolina
 * 
 */

public class ClientProcessBean implements Serializable {
	private static final long serialVersionUID = -782765665696861585L;

	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");
	@EJB(mappedName = "ejb/Process")
	private ejb.Process process;

	@EJB(mappedName = "ejb/User")
	private User userEJB;
	@EJB(mappedName = "ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;

	/**
	 * List of clients with open/active process.
	 */
	private List<TbSystemUser> listClients;

	private String codeClient;

	private List<SelectItem> codeTypeList;
	
	private List<Integer> listaScroll;

	private Long codeTypeId;

	private String userCode = "";

	private List<TableProcess> details;

	private TbSystemUser client;

	// Control Visibility --------------- //

	private String message;
	private String title;

	private boolean showMessage;

	private boolean showData;

	private String fileNameXMLRq;

	private String fileNameXMLRp;
	
	private Long pseId=-1L;
	
	private String filePse="";

	private Date dateStart;
	private Date dateEnd;

	private boolean searchok = false;
	
	private int page=1;
	private int valuesFor;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
	/**
	 * Constructor.
	 */
	public ClientProcessBean() {
	}

	@PostConstruct
	public void init() {
		codeTypeList = new ArrayList<SelectItem>();
		for (TbCodeType c : userEJB.getCodeTypes()) {
			codeTypeList.add(new SelectItem(c.getCodeTypeId(), c
					.getCodeTypeDescription()));
		}
	}

	// Actions --------------- //

	/**
	 * Searches process track info of a client. Modificación realizada para que
	 * el filtro se realice por medio de la selección de tipo de documento y
	 * número de documento por separado.
	 * 
	 * @author arivera
	 */

	public void dowloadFileZipWhitXML() {

		System.out.print("desacargando archivo");
		try{
			String typePath = "Ruta de almacenamiento de archivos XML";
			String systemParametersValue = SystemParametersEJB
					.getParameterName(typePath);
			System.out.print("esta es la rutaa------------>"
					+ systemParametersValue);

			String urlRq = systemParametersValue + fileNameXMLRq.trim();
			String urlRp = systemParametersValue + fileNameXMLRp.trim();

			java.io.File directory = new java.io.File(urlRq);
			java.io.File directory1 = new java.io.File(urlRp);
			System.out.println("urlRq: "+urlRq);  System.out.println("urlRp: "+urlRp);
			Boolean existRq = directory.exists()&& !directory.isDirectory();
			Boolean existRp = directory1.exists()&& !directory1.isDirectory();
			System.out.println("existRq: " + existRq);
			System.out.println("existRp: " + existRp);
			if (existRq) {
				System.out.print("es directorio la ruta de archivo Request ");
			} else {
				System.out.print("NO directorio la ruta de archivo Request ");
			}
			if (existRp) {
				System.out.print("es directorio la ruta de archivo Respons ");
			} else
				System.out.print("NO directorio la ruta de archivo Response ");

			if (systemParametersValue != null) {
				if (existRq && existRp) {
					System.out.print("ruta del archivo response =" + directory);
					System.out.print("ruta del archivo response =" + directory1);
					String[] source = new String[] { urlRq, urlRp };
					String[] nameFile = new String[] { fileNameXMLRq.trim().toString(), fileNameXMLRp.trim().toString() };
					byte[] bs = addToZipFile(nameFile, source);
					prepareDowload(bs);

				} else {
					if (existRq) {
						System.out.print("ruta del archivo response =" + directory);
						String[] source = new String[] { urlRq };
						String[] nameFile = new String[] { fileNameXMLRq.trim().toString() };
						byte[] bs = addToZipFile(nameFile, source);					
						prepareDowload(bs);									   
					} else {
						if (existRp) {
							System.out.print("ruta del archivo response ="
									+ directory);

							String[] source = new String[] { urlRp };
							String[] nameFile = new String[] { fileNameXMLRp.trim().toString() };
							byte[] bs = addToZipFile(nameFile, source);
							prepareDowload(bs);
				

						} else {
							System.out.println("[ Metodo dowloadFileZipWhitXML Error: No se encontraron archivos solicitados.]");
							setShowMessage(true);
							setTitle("Seguimiento de Procesos de Cliente");
							setMessage("Error: No se encontraron archivos solicitados.");

						}
					}
				}
			} else {
				System.out.print("error");
				setShowMessage(true);
				setTitle("Seguimiento de Procesos de Cliente");
				System.out.println("[Metodo dowloadFileZipWhitXML Error: No se tiene acceso a la Ruta de descarga]");
				setMessage("Error: No se tiene acceso a la Ruta de descarga");
			}
		}catch (Exception e) {
			
			System.out.print("error");
			setShowMessage(true);
			setTitle("Seguimiento de Procesos de Cliente");
			System.out.println("[Metodo dowloadFileZipWhitXML Error: No se tiene acceso a la Ruta de descarga]");
			setMessage("Error: No se tiene acceso a la Ruta de descarga");
			e.printStackTrace();
		}
	}
	
	public String downloadVoucher() {
		setTitle("Procesos de Cliente");
		String p=null;
		String f=null;
		try{
			System.out.println("Muesta PDF");
			filePse=filePse.replace("/", "\\");
			p=filePse.substring(0,filePse.lastIndexOf("\\"));
			f=filePse.substring(filePse.lastIndexOf("\\")+1);
			System.out.println("filePse: "+filePse);
			System.out.println("p: "+p);
			System.out.println("f: "+f);
			java.io.File directory = new java.io.File(filePse);
			if(directory.exists()){
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				HttpServletResponse response = (HttpServletResponse) externalContext
						.getResponse();
				File file = new File(p,
						f);
				BufferedInputStream input = null;
				BufferedOutputStream output = null;

				try {
					System.out.println("Entra aqui");
					// Open file.
					input = new BufferedInputStream(new FileInputStream(file),
							DEFAULT_BUFFER_SIZE);
					response.reset();
					response.setHeader("Content-Type", "application/pdf");
					response.setHeader("Content-Length", String.valueOf(file.length()));
					response.setHeader("Content-Disposition", "inline; filename=\""
							+ f + "\"");
					output = new BufferedOutputStream(response.getOutputStream(),
							DEFAULT_BUFFER_SIZE);

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
				// java.lang.IllegalStateException: Cannot forward after response has
				// been committed.
				facesContext.responseComplete();
			}else{
				setMessage("No se encontró el archivo solicitado.");
				setShowMessage(true);
			}
		}catch (Exception e) {
			System.out.println(" [ Error MyProcessBean.downloadVoucher ] ");
			e.printStackTrace();			
			setMessage("Ha ocurrido un error, intente más tarde.");
			setShowMessage(true);
		}
		return null;
	}

	private byte[] addToZipFile(String fileName[], String source[])
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		for (int i = 0; i < source.length; i++) {

			java.io.File f = new java.io.File(source[i]);
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			// Add ZIP entry to output stream.
			ZipEntry zipEntry = new ZipEntry(fileName[i]);
			zos.putNextEntry(zipEntry);

			// Transfer bytes from the file to the ZIP file
			byte[] buf = new byte[2024];
			int len;
			while ((len = bis.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}

			// Complete the entry
			zos.closeEntry();
			bis.close();
			fis.close();
		}
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		System.out.print("Se esta retornando los datos el Array de byte ");
		byte[] compres = baos.toByteArray();
		return compres;
	}

	public void prepareDowload(byte[] bs) throws Exception {

		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();

			System.out.print("datos que son traidos de return--->"
					+ bs.toString().getBytes());
			response.reset();
			String target =codeClient+Calendar.DAY_OF_MONTH+".zip";
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ target + "");

			ServletOutputStream sos = response.getOutputStream();
			ZipOutputStream zipos = new ZipOutputStream(sos);

			try {
				sos.write(bs);
				System.out.print("ESCRIBIENDO ARCHIVO .ZIP EN ZIPOUTPUTSTREAM");
				// TODO: handle exception
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("Error al escribir el byte de array");
			}
			System.out
					.println("exportacion de .Zip en descargas del browser---");
			sos.flush();

			zipos.flush();
			sos.close();
			zipos.closeEntry();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void search() {
		if (userCode != null && !userCode.equals("")) {
			client = userEJB.getUserByCode(userCode, codeTypeId);
			if (client != null) {
				if(dateStart==null && dateEnd==null){
					setPage(1);
					setMessage("");
					setShowData(true);
					setShowMessage(false);
					details = new ArrayList<TableProcess>();
					details = process.getProcessDetailsClient(
							client.getUserId(), ProcessTrackType.CLIENT,dateStart,dateEnd,page,20);
				}else if(dateStart==null && dateEnd!=null){
						setTitle("Seguimiento de Procesos de Cliente");
						setShowMessage(true);
						setMessage("Error: La fecha inicial no pueden estar vacía.");
						setShowData(false);
				}else if(dateStart!=null && dateEnd==null){
						setTitle("Seguimiento de Procesos de Cliente");
						setShowMessage(true);
						setMessage("Error: La fecha Final no pueden estar vacía.");
						setShowData(false);
				}else if(dateEnd.getTime()>= dateStart.getTime()){
					if(dateEnd.getTime()<=(new java.util.Date()).getTime()){
						setMessage("");
						setShowData(true);
						setShowMessage(false);
						details = new ArrayList<TableProcess>();
						details = process.getProcessDetailsClient(
								client.getUserId(), ProcessTrackType.CLIENT,dateStart,dateEnd,page,20);		
						if(details.size() > 0) {
							setMessage("");
							setShowMessage(false);
							setShowData(true);
							setSearchok(true);
						} else {
							setTitle("Seguimiento de Procesos de Cliente");
							setShowMessage(true);
							setMessage("Error: No hay información para el Cliente digitado.");
							setShowData(false);
							setSearchok(false);
						}
					}else{
						setTitle("Seguimiento de Procesos de Cliente");
						setShowMessage(true);
						setMessage("Error: La fecha final no debe ser mayor a la fecha actual.");
						setShowData(false);
					}
				}else{
					setTitle("Seguimiento de Procesos de Cliente");
					setShowMessage(true);
					setMessage("Error: La fecha final no debe ser menor a la fecha inicial.");
					setShowData(false);	
				}
			} else {
				setShowData(false);
				setShowMessage(true);
				setTitle("Seguimiento de Procesos de Cliente");
				setMessage("Error: No hay información para el Cliente digitado.");
			}
		} else {
			setShowData(false);
			setShowMessage(true);
			setTitle("Seguimiento de Procesos de Cliente");
			setMessage("Error: Debe digitar el número de identificación del cliente.");
		}
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

	public void hideModal() {
		setShowData(false);
		setShowMessage(false);
		setMessage("");
		setUserCode("");
	}

	// Getters and Setters ------ //

	/**
	 * @param listClients
	 *            the listClients to set
	 */
	public void setListClients(List<TbSystemUser> listClients) {
		this.listClients = listClients;
	}

	/**
	 * @return the listClients
	 */
	public List<TbSystemUser> getListClients() {
		if (listClients == null) {
			listClients = new ArrayList<TbSystemUser>();
			listClients = process.getClientsWithActiveProcess();
		}
		return listClients;
	}

	/**
	 * @param codeClient
	 *            the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

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
						ProcessTrackType.CLIENT, null, null, page, 20);	
			}
			if (searchok==true){
				this.getDataForScroll();
				details = process.getProcessDetailsClient(client.getUserId(),
						ProcessTrackType.CLIENT, dateStart, dateEnd, page, 20);	
			}
		return details;
	}
	
	public void getDataForScroll(){
		try {
			if(searchok==false){
				this.setValuesFor(Integer.parseInt(String.valueOf(process.getProcessDetailsClient(
						client.getUserId(), ProcessTrackType.CLIENT, null, null, 0, 20).get(0))));
			}else{
				this.setValuesFor(Integer.parseInt(String.valueOf(process.getProcessDetailsClient(
						client.getUserId(), ProcessTrackType.CLIENT, dateStart, dateEnd, 0, 20).get(0))));
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

	/**
	 * @param codeTypeId
	 *            the codeTypeId to set
	 */
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	/**
	 * @return the codeTypeId
	 */
	public Long getCodeTypeId() {
		return codeTypeId;
	}

	/**
	 * @param codeTypeList
	 *            the codeTypeList to set
	 */
	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}

	/**
	 * @return the codeTypeList
	 */
	public List<SelectItem> getCodeTypeList() {
		return codeTypeList;
	}

	/**
	 * @param userCode
	 *            the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
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

	public java.util.Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(java.util.Date dateStart) {
		this.dateStart = dateStart;
	}

	public java.util.Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(java.util.Date dateEnd) {
		this.dateEnd = dateEnd;
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
	
	public boolean isSearchok() {
		return searchok;
	}

	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

}