/**
 * 
 */
package converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author tmolina
 * 
 */
public class BigDecimalToStringFormat implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value == null || !(value instanceof BigDecimal)){
			System.out.println("enters");
			return null;
		}
		
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		
		Long t = ((BigDecimal)value).longValue();
		return "$"+nf.format(t);
	}
}