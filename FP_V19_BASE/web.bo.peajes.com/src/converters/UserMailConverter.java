package converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jpa.TbSystemUser;
import ejb.User;

/**
 * Converts a given user id to string mail address
 * @author Mauricio Gil
 */
public class UserMailConverter implements Converter {

	private User user;
	private Context context;
	
	public UserMailConverter(){
		try {
			context = new InitialContext();
			user = (User)context.lookup("ejb/User");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getEmail(String value) {
		String[] userId = value.indexOf(',') != -1 ? value.split("\\s*,\\s*") : new String[]{value};
		String res = "";
		
		for (int i = 0; i < userId.length; i++) {
			String userEmail;
			try {
				Long usrId = Long.valueOf(userId[i]);
				TbSystemUser sysUser = user.getSystemUser(usrId);
				if (sysUser != null) {
					userEmail = sysUser.getUserEmail();
					res += i == 0 ? userEmail : ", " + userEmail;
				}
			} catch (NumberFormatException e) {
				userEmail = userId[i];
				res += i == 0 ? userEmail : ", " + userEmail;
			}
		}
		
		return res;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return getEmail(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		return getEmail(value.toString());
	}

}
