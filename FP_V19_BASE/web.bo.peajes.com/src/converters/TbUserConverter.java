package converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.User;

import jpa.TbSystemUser;

public class TbUserConverter implements Converter {
	private InitialContext ctx;
	
	public TbUserConverter(){
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
	
		TbSystemUser sysUser = null;
		ejb.User userBean = null;
		
		try {
			userBean = (User) ctx.lookup("ejb/User");
			sysUser = userBean.getSystemUser(Long.valueOf(value));
		} catch (NumberFormatException e) {
			System.out.println("Unable to convert ID from value. " + e.getCause().getMessage());
			List<TbSystemUser> usersByName = userBean.getUsersByName(value);
			sysUser = usersByName.get(0);
		} catch (NullPointerException e){
			System.out.println("Value for TbUserConverter is null. " + e.getCause().getMessage());
		} catch (NamingException e){
			System.out.println("Unable to find ejb/User. " + e.getCause().getMessage());
		}
		
		return sysUser;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		TbSystemUser sysUser = (TbSystemUser) value;
		return sysUser.getUserId().toString();
	}

}
