package mBeans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.Charges;

import jpa.TbCharges;


public class ChargesBean implements Serializable{
	
	private static final long serialVersionUID = 3L;
	
	@EJB(mappedName="ejb/Charges")
	private Charges cargos;
	
	private Long chargeId;
	
	private String nameCharge="";
	
	private String nameCharge1="";
	
	private int textTypeValue;
	
	private List<SelectItem> typeValues;
	
	private Long valueCharge;
	
	private String valueChargeTxt;
	
	private boolean typeValue;
	
	private List<TbCharges> listCargos;
	
	private boolean showModal;

	private String msgModal;
	
	private String idCargo;
	
	private String msgError;
	
	private boolean showError;
	
	private boolean showEdit;
	
	private boolean showModalEdit;
	
	private boolean showModalDelete;
	
	public ChargesBean(){
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
		textTypeValue = -1;
	}

	public String getValueChargeTxt() {
		return valueChargeTxt;
	}

	public void setValueChargeTxt(String valueChargeTxt) {
		this.valueChargeTxt = valueChargeTxt;
	}

	public void setCargos(Charges cargos) {
		this.cargos = cargos;
	}

	public Charges getCargos() {
		return cargos;
	}

	public void setNameCharge(String nameCharge) {
		this.nameCharge = nameCharge;
	}

	public String getNameCharge() {
		return nameCharge;
	}

	public void setValueCharge(Long valueCharge) {
		this.valueCharge = valueCharge;
	}

	public Long getValueCharge() {
		return valueCharge;
	}

	public void setTypeValue(boolean typeValue) {
		this.typeValue = typeValue;
	}

	public boolean isTypeValue() {
		return typeValue;
	}

	public void setListCargos(List<TbCharges> listCargos) {
		this.listCargos = listCargos;
	}

	public List<TbCharges> getListCargos() {
		if(listCargos !=null){
			listCargos.clear();
		} else {
			listCargos = new ArrayList<TbCharges>();
		}		
		List<TbCharges> listCargos = cargos.getAllCharges();	
		
		return listCargos;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setMsgModal(String msgModal) {
		this.msgModal = msgModal;
	}

	public String getMsgModal() {
		return msgModal;
	}
	
	public void setIdCargo(String idCargo) {
		this.idCargo = idCargo;
	}

	public String getIdCargo() {
		return idCargo;
	}

	public void setTextTypeValue(int textTypeValue) {
		this.textTypeValue = textTypeValue;
	}

	public int getTextTypeValue() {
		return textTypeValue;
	}

	public void setTypeValues(List<SelectItem> typeValues) {
		this.typeValues = typeValues;
	}

	public List<SelectItem> getTypeValues() {
		if(typeValues == null){		
			typeValues = new ArrayList<SelectItem>();
		} else {
			typeValues.clear();
		}		
			typeValues.add(new SelectItem(-1,"--Seleccione una Opción--"));
			typeValues.add(new SelectItem(0,"Variable"));
			typeValues.add(new SelectItem(1,"Fijo"));			
		return typeValues;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public boolean isShowError() {
		return showError;
	}
	
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}

	public Long getChargeId() {
		return chargeId;
	}

	public void setNameCharge1(String nameCharge1) {
		this.nameCharge1 = nameCharge1;
	}

	public String getNameCharge1() {
		return nameCharge1;
	}

	public void setShowModalEdit(boolean showModalEdit) {
		this.showModalEdit = showModalEdit;
	}

	public boolean isShowModalEdit() {
		return showModalEdit;
	}

	public void setShowModalDelete(boolean showModalDelete) {
		this.showModalDelete = showModalDelete;
	}

	public boolean isShowModalDelete() {
		return showModalDelete;
	}

	public void hideError(){
		showError=false;
		valueCharge = null;
	}
	
	public void hideEdit(){
		this.getListCargos();
		msgError = "";
		msgModal = "";
		nameCharge = "";
		nameCharge1 = "";
		valueCharge = null;
		valueChargeTxt="";
		typeValue = false;
		textTypeValue = -1;
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
	}
	
	public void msgSave(){	
	  if((nameCharge1.equals(null)) || (nameCharge1.trim().equals(""))){
		  msgError= "Falta Digitar el Nombre del Cargo";
		  showError = true;
	  } else if(!Validation.isAlphaES(nameCharge1)){
		  msgError= "El campo Nombre de Cargo contiene caracteres inválidos, por favor verifique";
		  showError = true;
	  } else if (textTypeValue==1){
		  if((valueChargeTxt.equals("")) || (valueChargeTxt.equals(null))){
			  msgError= "No se ha registrado el valor para el Cargo";
			  showError = true;
		  } else if(!Validation.isNumericPuntoYComaNoConsecutive(valueChargeTxt)){
			  msgError= "El campo Valor contiene caracteres inválidos, por favor verifique";
			  showError = true;
		  } else {
			  valueCharge = Long.parseLong(valueChargeTxt.replace(".", "").replace(",", ""));
			  showError = false;
			  msgModal= "¿Está seguro de realizar esta operación?";
			  showModal = true;
		  }
	  } else if (textTypeValue==-1){
		  msgError= "No ha Seleccionado tipo de Valor";
		  showError = true;		  
	  } else {
		  showError = false;
		  msgModal= "¿Está seguro de realizar esta operación?";
		  showModal = true;
	  }
	}
	
	public void msgSaveEdit(){
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
	  if((nameCharge.equals(null)) || (nameCharge.trim().equals(""))){
		  msgError= "Falta Digitar el Nombre del Cargo";
		  showError = true;
	  } else if(!Validation.isAlphaES(nameCharge)){
		  msgError= "El campo Nombre de Cargo contiene caracteres inválidos, por favor verifique";
		  showError = true;
	  } else if (textTypeValue == 1){
		  if((valueChargeTxt.equals("")) || (valueChargeTxt.equals(null))){
			  msgError= "No se ha registrado el valor para el Cargo";
			  showError = true;
		  } else if(!Validation.isNumericPuntoYComaNoConsecutive(valueChargeTxt)){
			  msgError= "El campo Valor contiene caracteres inválidos, por favor verifique";
			  showError = true;
		  } else {
			  valueCharge = Long.parseLong(valueChargeTxt.replace(".", "").replace(",", ""));
			  showError = false;
			  msgModal= "¿Está seguro de realizar esta operación?";
			  showModalEdit = true;
		  }
	  } else if (textTypeValue == -1){
		  msgError= "No ha Seleccionado tipo de Valor";
		  showError = true;		  
	  } else {
		  showError = false;
		  msgModal= "¿Está seguro de realizar esta operación?";
		  showModalEdit = true;
	  }
	}
	
	public void hide(){
		showModal = false;
	}
	
	public void apply(){
		showError = false;
		showModal = false;
		showModalEdit = false;
		if(!cargos.existChargeI(nameCharge1.trim())){
			if(cargos.setCharge(nameCharge1.trim(), textTypeValue, valueCharge, SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId())){
				msgError= "Transacción Exitosa";
				showError = true;
				this.getListCargos();
				msgModal = "";
				nameCharge1 = "";
				valueCharge = null;
				valueChargeTxt="";
				typeValue = false;
				textTypeValue = -1;
			} else {
				msgError= "Se ha presentado un Error al Crear el Cargo";
				showError = true;
			}
		}
		else{
			msgError= "El nombre del cargo ya existe.";
			showError = true;
		}
		
	}
	
	public void changeTypeValue(){
		if(textTypeValue == 1 ){
			typeValue = true;
		} else {
			typeValue = false;
			valueCharge = null;
		}
	}
	
	public void panelEdit(){
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
		 
		 TbCharges tbc = cargos.getChargeById(chargeId);
		 nameCharge = tbc.getChargeDescription();
		 textTypeValue = tbc.getChargeTypeValue();
		 valueCharge = tbc.getChargeValue();
		 valueChargeTxt = (valueCharge!=null?valueCharge.toString():"");
		
		 if((valueCharge != null) && (valueCharge >0L)){
			 DecimalFormat formateador = new DecimalFormat("###,###.##");
			 valueChargeTxt = formateador.format(valueCharge);
		 }
		 valueChargeTxt = valueChargeTxt.replaceAll(",", ".");
		 
		 if(textTypeValue == 1){
				typeValue = true;
			} else {
				typeValue = false;
			}
		showEdit = true;
	}
	
	public void edit(){
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
		if(!cargos.existChargeU(chargeId, nameCharge.trim())){
			if(cargos.editCharge(chargeId, nameCharge.trim(), textTypeValue, valueCharge, SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId())){
				msgError= "Transacción Exitosa";
				showError = true;
				this.getListCargos();			
				msgModal = "";
				nameCharge = "";
				valueCharge = null;
				valueChargeTxt="";
				typeValue = false;
				showEdit = false;
				textTypeValue = -1;
			} else {
				msgError= "Se ha presentado un Error al Editar el Cargo"+cargos.nameById(chargeId);
				showError = true;
			}
		}else{
			msgError= "El nuevo nombre del cargo ya existe";
			showError = true;
		}
	
	}
	
	public void panelDelete(){
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
		msgModal= "¿Está seguro de Eliminar el Cargo "+cargos.nameById(chargeId)+"?";
		showModalDelete = true;
	}
	
	public void delete(){
		showEdit = false;
		showError = false;
		showModal =  false;
		showModalDelete =false;
		showModalEdit =false;
		
		msgError=cargos.removeCharge(chargeId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		showError = true;
		
		if(msgError.equals("Transacción Exitosa")){
					showError = true;
					this.getListCargos();			
					msgModal = "";
					nameCharge = "";
					valueCharge = null;
					typeValue = false;
					showEdit = false;
					textTypeValue = -1;
		} 
		
	}
	
	public void hideDelete(){
		showModalDelete = false;
	}
}
