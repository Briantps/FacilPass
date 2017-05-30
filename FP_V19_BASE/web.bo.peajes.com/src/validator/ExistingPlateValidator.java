package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jpa.TbVehicle;
import ejb.Vehicle;

public class ExistingPlateValidator extends PlateValidator {
	public Vehicle vehicleBean;
	public InitialContext context;
	
	public ExistingPlateValidator(){
		try {
			context = new InitialContext();
			vehicleBean = (Vehicle) context.lookup("ejb/Vehicle");
			if(vehicleBean == null){
				//System.out.println("Vehicle Bean not found");
			} else {
				//System.out.println("Vehicle Bean found");
			}
			
		} catch (NamingException e) {
			e.printStackTrace();
		}		
	}
	
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		super.validate(context, component, value);
		
		if (vehicleBean != null) {
			System.out.println("Validating vehicle plate");
			
			String plate = (String) value.toString();
			TbVehicle vehicle = vehicleBean.getVehicleByPlate(plate);
			if (vehicle != null) {
				System.out.println("The specified plate already exists");
				
				((UIInput) component).setValid(false);
				FacesMessage message = new FacesMessage(
						"La placa especificada ya existe");
				context.addMessage(component.getClientId(context), message);
				throw new ValidatorException(message);
			} else {
				System.out.println("The specified plate does not exists. Validation OK.");
			}
		}
	}
}
