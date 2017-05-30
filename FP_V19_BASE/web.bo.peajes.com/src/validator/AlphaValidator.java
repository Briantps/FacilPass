package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;

public class AlphaValidator implements Validator {
	
	/*
	 * (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		if (!StringUtils.isAlphaSpace((String) value)) {
			HtmlInputText htmlInputText = (HtmlInputText) component;
			FacesMessage facesMessage = new FacesMessage(htmlInputText
					.getLabel()
					+ ": Sólo se permiten Letras y Espacios.");
			throw new ValidatorException(facesMessage);
		}
	}
}