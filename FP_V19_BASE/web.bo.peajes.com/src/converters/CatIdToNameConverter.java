package converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.Category;

/**
 * Converts a category object to a string value (name of category)
 * @author Mauricio Gil
 */
public class CatIdToNameConverter implements Converter {
	private Context context;
	private Category catHandler;
	
	public CatIdToNameConverter(){
		try {
			context = new InitialContext();
			catHandler = (Category)context.lookup("ejb/Category");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		long id = Long.valueOf(value);
		return catHandler.getCategory(id).getCategoryName();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		long id = Long.valueOf(value.toString());
		return catHandler.getCategory(id).getCategoryName();
	}

}
