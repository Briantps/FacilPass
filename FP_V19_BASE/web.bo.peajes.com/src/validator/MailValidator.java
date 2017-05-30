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
 * @author choyos
 * 
 */
public class MailValidator implements Validator {

	/**
	 * 
	 */
	public MailValidator() {
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
		
		String mail = value.toString();
		Pattern p = Pattern.compile("^[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[a-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,})$");
	    Matcher m = p.matcher(mail);
	    boolean matchFound = m.matches();	
		
		FacesMessage facesMessage = new FacesMessage();
		if (!matchFound) {
			facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
			facesMessage.setSummary("Correo electrónico no válido");
			throw new ValidatorException(facesMessage);
		}
		context.addMessage(component.getClientId(context), facesMessage);
	}

}
