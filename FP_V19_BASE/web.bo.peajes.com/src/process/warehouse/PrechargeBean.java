/**
 * 
 */
package process.warehouse;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import constant.TagManufacturer;

import sessionVar.SessionUtil;
import upload.FileUtil;

import jpa.TbTagType;

import ejb.Warehouse;
import ejb.crud.TagType;

/**
 * @author tmolina
 *
 */
public class PrechargeBean implements Serializable {
	private static final long serialVersionUID = 2476355036952034643L;
	
	@EJB(mappedName = "ejb/TagType")
	private TagType tagType;
	
	@EJB(mappedName="ejb/Warehouse")
	private Warehouse warehouse;
	
	// Attributes --- //
	
	private List<SelectItem> typeList;
	
	private Long tagTypeId;
	
	private boolean showPanel;
	
	private String message;
	
	private List<String> listToProcess;
	
	private List<String> list;
	
	private TbTagType type;
	
	private boolean showUpload;
	
	private boolean showModal;	

	/**
	 * Constructor
	 */
	public PrechargeBean(){
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void prechargeBeanInit(){
		init();
	}
	
	/**
	 * 
	 */
	public String init() {
		setTypeList(null);
		setTagTypeId(null);
		listToProcess = null;
		return null;
	}
	
	/**
	 * Hides modal Panel.
	 */
	public String hideModal(){
		//setModalMessage("");
		setShowModal(false);
		//setShowCreate(false);
		//setShowModificate(false);
		//setShowModalMod(false);
		return null;
	}	
	
	// Helpers --------- //
	
	/**
	 * 
	 * @param fileName
	 * @return List string that contain identifiers of cards reported in the
	 *         blacklist
	 */
	public List<String> loadFile(String fileName) {
		list = new ArrayList<String>();
		try {
			File f = new File(fileName);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				list.add(sc.nextLine());
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 
	 */
	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = application.getViewHandler().createView(context,
				context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public String validateFile(String fileName) {
		String validation = "";
		try {
			File f = new File(fileName);
			Scanner sc = new Scanner(f);
			
			if (f.length() <= 0) {
				validation = "El Archivo No contiene Información.";
			}
			
			// Getting the list to do validation 
			List<String> list = this.loadFile(fileName);
			
			try {
				Long numberOfTags = Long.valueOf(list.get(list.size()-1));
				
				if (numberOfTags.intValue() == list.size()-1){
					
					// validate split number
					if (validateSplit()) {
						// validate serial length
						if(validateSerialLength()) {
							// validate manufacturer 
							if(type.getTagTypeId().longValue() == TagManufacturer.QFREE.getId().longValue()
									|| type.getTagTypeId().longValue() == TagManufacturer.KAPSCH.getId().longValue()){
								if (validateManufacturer()) {
								} else {
									validation = "El fabricante del archivo no corresponde con el seleccionado.";
								}
							}
						} else {
							validation = "El Campo serial no tiene el número de caracteres parametrizado: " + type.getSerialLength() + ".";
						}
					} else {
						validation = "Los registros del archivo no tienen la cantidad de campos del estándar: " + type.getNumberOfSplit() + ".";
					}
				} else {
					validation = "El número de registros que se encuentra indicado al final del archivo no corresponde a la totalidad registros." +
							" Total de registros escaneados: " + (list.size()- 1) + ". Número de registros indicados en el archivo: " + numberOfTags + "." ;
				}
			} catch (NumberFormatException ex) {
				validation = "En el Archivo no se encontró el total de dispositivos a ingresar en la última línea. ";
			}
			sc.close();
		} catch (Exception e) {
			validation = " Error al leer la ruta del Archivo.";
			e.printStackTrace();
		}
		return validation;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validateSplit(){
		for (int i = 0; i < list.size() - 1; i++) {
			String cad[] = list.get(i).split(";");
			if (cad.length != type.getNumberOfSplit().intValue()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validateSerialLength(){
		try {
			if (listToProcess == null) {
				listToProcess = new ArrayList<String>();
			} else {
				listToProcess.clear();
			}
			for (int i = 0; i < list.size() - 1; i++) {
				String cad[] = list.get(i).split(";");
				if (cad[type.getNumberOfSplit().intValue() - 1].length() != type
						.getSerialLength().intValue()) {
					return false;
				}
				listToProcess.add(cad[type.getNumberOfSplit().intValue() - 1]);
			}
		} catch (Exception e) {
			System.out.println(" Error en PrechargeBean.validateSerialLength ");
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validateManufacturer() {
		for (int i = 0; i < listToProcess.size(); i++) {
			if (!listToProcess.get(i).substring(11, 13).equals(
					type.getTagTypeCode())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * For  value updates
	 */
	public String reload(){
		FacesContext.getCurrentInstance().renderResponse();
		return null;
	}
	
	// Listener -------- //
	
	public void listener(UploadEvent event) {
		try {
			// Searching the path where file will be saved.
			String path = warehouse.getPrechargePath();

			if (path == null) {
				path = "/var/www/uploadTag/";
			}

			UploadItem item = event.getUploadItem();

			if (item.getData() != null) {
				String uploadedFileName = FileUtil.trimFilePath(item
						.getFileName());
				File file = new File(path, uploadedFileName);
				FileUtil.write(file, item.getData());
				
				String filename = path + item.getFileName();
				
				// Searching the tag type, manufacturer 
				for (TbTagType tt : tagType.getTagType()) {
					if (tt.getTagTypeId().longValue() == tagTypeId.longValue()) {
						this.type = tt;
						break;
					}
				}
				
				/* VALIDATION */
				String resValidation = validateFile(filename);
				
				if (resValidation.trim().length() > 0) {
					this.setMessage("Falló la carga del archivo. " + resValidation );		
				} else {
					String res = warehouse.processPrecharge(listToProcess,
							SessionUtil.ip(), SessionUtil.sessionUser().getUserId(), tagTypeId);
					this.setMessage("La Validación fue Correcta. " + res);
				}
			} else {
				this.setMessage("Falló la carga del archivo.");
			}
			setShowModal(true);
			//setShowPanel(true);
			//refresh();

		} catch (IOException ex) {
			System.out
					.println(" [ Error en PrechargeBean.listener : IOException] ");
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getters and Setters ---- //

	/**
	 * @param tagTypeId the tagTypeId to set
	 */
	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	/**
	 * @return the tagTypeId
	 */
	public Long getTagTypeId() {
		return tagTypeId;
	}

	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<SelectItem> getTypeList() {
		if (typeList == null) {
			typeList = new ArrayList<SelectItem>();
			typeList.add(new SelectItem(-1, "-Seleccione un Fabricante-"));
			for(TbTagType tt : tagType.getTagType()) {
				typeList.add(new SelectItem(tt.getTagTypeId(), tt.getTagTypeName()));
			}
		}
		return typeList;
	}

	/**
	 * @param showPanel the showPanel to set
	 */
	public void setShowPanel(boolean showPanel) {
		this.showPanel = showPanel;
	}

	/**
	 * @return the showPanel
	 */
	public boolean isShowPanel() {
		return showPanel;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param message the message to set
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
	 * @param showUpload the showUpload to set
	 */
	public void setShowUpload(boolean showUpload) {
		this.showUpload = showUpload;
	}

	/**
	 * @return the showUpload
	 */
	public boolean isShowUpload() {
		if(tagTypeId != null && tagTypeId != -1L) {
			showUpload = true;
		} else {
			showUpload = false;
		}
		return showUpload;
	}
}
