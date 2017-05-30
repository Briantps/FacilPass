/**
 * 
 */
package converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author jromero
 *
 */
public class StringToStringDateConverter implements Converter{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
			Date date = (Date)formatter.parse(value);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
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
		try {
			if(value == null || !(value instanceof String)){
				return null;
			}
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String t = (String)value;
			Date d = (Date)sf.parse(t);
			return new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}