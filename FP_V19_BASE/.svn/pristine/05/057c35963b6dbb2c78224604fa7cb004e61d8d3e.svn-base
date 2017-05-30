/**
 * 
 */
package converters;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author tmolina
 *
 */
public class TimeStampToStringDateConverter implements Converter{

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
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			Date date = (Date)formatter.parse(value);
			return new Timestamp(date.getTime());
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
		if(value == null || !(value instanceof Timestamp)){
			return null;
		}
		
		Timestamp t = (Timestamp)value;
		return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(t);
	}
}