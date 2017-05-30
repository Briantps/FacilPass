/**
 * 
 */
package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author tmolina
 * 
 */
public class EMailValidation implements Validator {

	public EMailValidation() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.validator.Validator#validate(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		// Validates one or more email addresses separated by commas (,) 
			
		String mail = value.toString();
		Pattern p = Pattern.compile("\\D\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([,;]\\S\\D\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*");
	    Matcher m = p.matcher(mail);
	    boolean matchFound = m.matches();
		
	    FacesMessage message = new FacesMessage();
		if(!matchFound){
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("Correo no válido");
			throw new ValidatorException(message);	
		}
		context.addMessage(component.getClientId(context), message);
	}

}