package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author tmolina
 * 
 */
public class BlankValidator implements Validator{
	
	/**
	 * 
	 */
	public BlankValidator(){
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		String string = (String) value.toString();
		
		if (string == null || string.trim().length()<1){
			((UIInput) component).setValid(false);
			HtmlInputText htmlInputText = (HtmlInputText) component;
			FacesMessage message = new FacesMessage("El Campo: " + htmlInputText
					.getLabel() + " no puede estar en blanco.");
			context.addMessage(component.getClientId(context), message);
		}
	}
}