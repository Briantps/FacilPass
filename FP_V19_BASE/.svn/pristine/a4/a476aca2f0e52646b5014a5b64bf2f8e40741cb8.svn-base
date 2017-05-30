package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author tmolina
 * 
 */
public class PlateValidator implements Validator{
	
	/**
	 * 
	 */
	public PlateValidator(){
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		String string = (String) value.toString();
		
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
	    Matcher m = p.matcher(string);
	    
	    boolean match = false;
	    
	    if(m.matches()){
	    	match = true;
	    }else{
	    	p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
	    	m = p.matcher(string);
	    	match = m.matches();
	    }	  
		
		if (!match){
			((UIInput) component).setValid(false);
			FacesMessage message = new FacesMessage("Placa inválida. La Placa debe estar compuesta exactamente por 3 letras seguida por 3 números ó 2 letras seguidas de 4 números. Verifique. ");
			context.addMessage(component.getClientId(context), message);
			throw new ValidatorException(message);
		}
	}
}