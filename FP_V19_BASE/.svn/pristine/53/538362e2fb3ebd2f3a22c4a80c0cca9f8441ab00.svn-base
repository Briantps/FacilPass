package converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.Color;

/**
 * Converts a color object to a string value (name of color)
 * @author Mauricio Gil
 */
public class ColorIdToNameConverter implements Converter {

	private Color colorHandler;
	private Context context;
	
	public ColorIdToNameConverter(){
		try {
			context = new InitialContext();
			colorHandler = (Color)context.lookup("ejb/Color");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return colorHandler.getColor(Long.valueOf(value)).getColorName();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return colorHandler.getColor(Long.valueOf(value.toString())).getColorName();
	}
	
}
