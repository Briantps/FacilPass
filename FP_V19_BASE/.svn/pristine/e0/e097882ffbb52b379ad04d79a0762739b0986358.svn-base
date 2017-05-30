package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbAgreement;
import jpa.TbChanel;

import security.UserLogged;
import sessionVar.SessionUtil;

import util.ReAgreementChannel;

import ejb.ReAgreementChanelI;

import ejb.User;

public class ReAgreementChannelBean implements Serializable {
	private static final long serialVersionUID = 199399153082288754L;

	@EJB(mappedName = "ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/ReAgreementChanelI")
	private ReAgreementChanelI reAgreementChannel;

	private List<SelectItem> channelsList;
	private List<SelectItem> agreementsList;

	private Long typeValueChannel;
	private Long typeValueAgreement;
	private Long id_agreementChannel;
	private String stateReAgreementChannel;
	private String nameChannel;
	private String nameAgreement;
	private String codeAgreement;
	private boolean viewColumDelete = false;
	private boolean permissionActiveInactive = false;
	private boolean permissionCreate = false;

	private ArrayList<ReAgreementChannel> listReAgreementChannel;

	private UserLogged usrs;
	private boolean viewFormCreate = false;
	private String message;

	private boolean modal;
	private boolean modal1;
	private boolean modal11;
	private boolean modalUpdate;

	public ReAgreementChannelBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}

	@PostConstruct
	public void init() {
		getListChannelAgreement();
		getPermission();
	}

	public void getListChannelAgreement() {
		if (usrs.getUserId() != null) {
			listReAgreementChannel = new ArrayList<ReAgreementChannel>();
			listReAgreementChannel = reAgreementChannel.getAllRelationAgreementChannel();
		}
	}

	public void getPermission() {	
		try {
			permissionCreate = userEJB.getPermission(usrs.getUserId(),"permissionCreateChannelAgreement");
			permissionActiveInactive = userEJB.getPermission(usrs.getUserId(),"permissionAIChannelAgreement");					
		} catch (Exception e) {
			System.out.print("---//" + e.getMessage());
		}
	}

	public void showViewCreate() {		
		getListChannelAgreement();
		setViewFormCreate(true);
	}

	public void confirmationSave() {
		if (typeValueAgreement == -1L) {
			setModal1(true);
			setMessage("El código de convenio es requerido.");
		} else if (typeValueChannel == -1L) {
			setMessage("El código de canal es requerido.");
			setModal1(true);
		} else {
			ReAgreementChannel re = new ReAgreementChannel();
			re = reAgreementChannel.getFindChannelAgreement(typeValueChannel,
					typeValueAgreement);
			setModal(true);
			setMessage("¿Está seguro de crear la relación del convenio "
					+ re.getNameAgreement() + " con código "
					+ re.getCodeAgreement() + " y el canal "
					+ re.getNameChannel() + " ?");
		}
	}

	public void saveReAgreementChannel() {
		try {
			boolean rest = reAgreementChannel.saveReAgreementChannel(
					typeValueChannel, typeValueAgreement, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId());
			setModal(false);
			if (rest) {
				setMessage("Se ha registrado la relación entre canal y convenio Exitosamente");
				setModal11(true);

			} else {
				setMessage("La relación del convenio - canal ya existe");
				setModal1(true);
			}
		} catch (Exception e) {
			setMessage("Se ha presentado un error al momento de crear la relación  del Convenio - canal.");
			setModal1(true);
		}
	}

	public void confirmInactiveActive() {
		setModalUpdate(true);
		setMessage("¿Está seguro de "+getStateReAgreementChannel()+ " la relación del convenio "+nameAgreement+" con el código  "
				+ codeAgreement + " y el canal " + nameChannel + "?");
	}

	public void clear(){
		getListChannelAgreement();
		setMessage("");
		setModal(false);
		setModal1(false);
		setModal11(false);
		setModalUpdate(false);
		setViewFormCreate(false);
		channelsList.clear();
		channelsList=null;
		agreementsList.clear();		
		agreementsList=null;
		typeValueAgreement=null;
		typeValueChannel=null;
	}
	
	public void inactiveActive() {

		boolean result = reAgreementChannel
				.ActiveInactiveRelationChannelAgremeent(id_agreementChannel,
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		setModalUpdate(false);
		if (result) {
			setModal1(true);
			setMessage("El estado la relación entre canal y convenio se ha modificado Exitosamente.");

		} else {
			setModal1(true);
			setMessage("Se ha presentado un error al momento de modificar el estado de la relación entre canal y convenio, porfavor intente nuevamente.");
		}
	}

	public void hiddenModal(){
		getListChannelAgreement();
		setMessage("");
		setModal(false);
		setModal1(false);
		setModal11(false);
		setModalUpdate(false);
	}

	public void ocultFormcreate() {	
		clear();
		setViewFormCreate(false);
	}

	public void setChannelsList(List<SelectItem> channelsList) {
		this.channelsList = channelsList;
	}

	public List<SelectItem> getChannelsList() {
		if (channelsList == null) {
			channelsList = new ArrayList<SelectItem>();
		} else {
			channelsList.clear();
		}
		channelsList.add(new SelectItem(-1L, "--Seleccione Una Opción--"));
		for (TbChanel su : reAgreementChannel.getChannels()) {
			if((su.getCod_channel().toString()+ "-"+ su.getChanelType().toString()).length()<19){
				channelsList.add(new SelectItem(su.getChanelId(), su
						.getCod_channel().toString()
						+ "-"
						+ su.getChanelType().toString()));
			}else{
				channelsList.add(new SelectItem(su.getChanelId(), (su
						.getCod_channel().toString()
						+ "-"
						+ su.getChanelType().toString()).substring(0, 19)+"..."));
			}
		}
		return channelsList;
	}

	public List<SelectItem> getAgreementsList() {
		if (agreementsList == null) {
			agreementsList = new ArrayList<SelectItem>();
		} else {
			agreementsList.clear();
		}
		agreementsList.add(new SelectItem(-1L, "--Seleccione Una Opción--"));
		for (TbAgreement su : reAgreementChannel.getAgreements()) {
			if((su.getCodeAgreement().toString()+ "-"+ su.getNameAgreement().toString()).length()<19){
				agreementsList.add(new SelectItem(su.getAgreementId(), su
						.getCodeAgreement().toString()
						+ "-"
						+ su.getNameAgreement().toString()));
			}else{
				agreementsList.add(new SelectItem(su.getAgreementId(), (su
						.getCodeAgreement().toString()
						+ "-"
						+ su.getNameAgreement().toString()).substring(0,19)+"..."));
			}
		}

		return agreementsList;
	}

	public boolean isViewFormCreate() {
		return viewFormCreate;
	}

	public void setViewFormCreate(boolean viewFormCreate) {
		this.viewFormCreate = viewFormCreate;
	}

	public ArrayList<ReAgreementChannel> getListReAgreementChannel() {
		return listReAgreementChannel;
	}

	public void setListReAgreementChannel(
			ArrayList<ReAgreementChannel> listReAgreementChannel) {
		this.listReAgreementChannel = listReAgreementChannel;
	}

	public Long getTypeValueChannel() {
		return typeValueChannel;
	}

	public void setTypeValueChannel(Long typeValueChannel) {
		this.typeValueChannel = typeValueChannel;
	}

	public Long getTypeValueAgreement() {
		return typeValueAgreement;
	}

	public void setTypeValueAgreement(Long typeValueAgreement) {
		this.typeValueAgreement = typeValueAgreement;
	}

	public void setAgreementsList(List<SelectItem> agreementsList) {
		this.agreementsList = agreementsList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isModal1() {
		return modal1;
	}

	public void setModal1(boolean modal1) {
		this.modal1 = modal1;
	}

	public Long getId_agreementChannel() {
		return id_agreementChannel;
	}

	public void setId_agreementChannel(Long id_agreementChannel) {
		this.id_agreementChannel = id_agreementChannel;
	}

	public String getStateReAgreementChannel() {
		return stateReAgreementChannel;
	}

	public void setStateReAgreementChannel(String stateReAgreementChannel) {
		this.stateReAgreementChannel = stateReAgreementChannel;
	}

	public String getNameChannel() {
		return nameChannel;
	}

	public void setNameChannel(String nameChannel) {
		this.nameChannel = nameChannel;
	}

	public String getNameAgreement() {
		return nameAgreement;
	}

	public void setNameAgreement(String nameAgreement) {
		this.nameAgreement = nameAgreement;
	}

	public String getCodeAgreement() {
		return codeAgreement;
	}

	public void setCodeAgreement(String codeAgreement) {
		this.codeAgreement = codeAgreement;
	}

	public boolean isModalUpdate() {
		return modalUpdate;
	}

	public void setModalUpdate(boolean modalUpdate) {
		this.modalUpdate = modalUpdate;
	}

	public boolean isViewColumDelete() {
		return viewColumDelete;
	}

	public void setViewColumDelete(boolean viewColumDelete) {
		this.viewColumDelete = viewColumDelete;
	}

	public boolean isModal11() {
		return modal11;
	}

	public void setModal11(boolean modal11) {
		this.modal11 = modal11;
	}

	public boolean isPermissionActiveInactive() {
		return permissionActiveInactive;
	}

	public void setPermissionActiveInactive(boolean permissionActiveInactive) {
		this.permissionActiveInactive = permissionActiveInactive;
	}

	public boolean isPermissionCreate() {
		return permissionCreate;
	}

	public void setPermissionCreate(boolean permissionCreate) {
		this.permissionCreate = permissionCreate;
	}

	
	
}