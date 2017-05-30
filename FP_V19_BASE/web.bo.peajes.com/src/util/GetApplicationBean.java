/**
 * 
 */
package util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Cristian Avellaneda
 * 
 */
public class GetApplicationBean {

	/**
	 * Constructor
	 */
	public GetApplicationBean() {
	}

	public Object getBeanAplication(String beanName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ExpressionFactory ef = facesContext.getApplication()
				.getExpressionFactory();
		ValueExpression ve = ef.createValueExpression(elContext, "#{"
				+ beanName + "}", Object.class);
		return ve.getValue(elContext);
	}
}