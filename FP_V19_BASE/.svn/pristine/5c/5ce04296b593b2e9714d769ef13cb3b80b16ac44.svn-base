package converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jpa.TbBrand;

import ejb.BrandManager;

/**
 * Converts a brand object to a string value (name of object)
 * @author Mauricio Gil
 */
public class BrandIdToNameConverter implements Converter {

	private Context context;
	private BrandManager brandManager;
	
	public BrandIdToNameConverter(){
		try {
			context = new InitialContext();
			brandManager = (BrandManager)context.lookup("ejb/BrandManager");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		TbBrand brand = null;
		
		try {
			long id = Long.valueOf(value);
			brand = brandManager.getBrand(id);
		} catch (NumberFormatException e) {
			List<TbBrand> brandsByName = brandManager.getBrandsByName(value);
			if(brandsByName.size() > 0)
				brand = brandsByName.get(0);
		}
		return brand;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		TbBrand brand;
		
		if(value instanceof TbBrand){
			brand = (TbBrand)value;
			return brand.getBrandName();
		} else if (value instanceof Number){
			Number id = (Number)value;
			brand = brandManager.getBrand(id.longValue());
			return brand.getBrandName();
		}
		
		return "";
	}

}
