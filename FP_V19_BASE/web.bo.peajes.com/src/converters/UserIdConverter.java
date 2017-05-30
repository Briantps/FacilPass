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
 * Converts a user id to string value (name of user)
 * @author Mauricio Gil
 */
public class UserIdConverter implements Converter {

	private User user;
	private Context context;
	
	public UserIdConverter(){
		try {
			context = new InitialContext();
			user = (User)context.lookup("ejb/User");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getUserName(String value){
		String[] userId = value.indexOf(',') != -1 ? value.split("\\s*,\\s*") : new String[]{value};
		String res = "";
		
		for (int i = 0; i < userId.length; i++) {
			TbSystemUser sysUser = user.getSystemUser(Long.valueOf(userId[i]));
			if (sysUser != null) {
				String userName = sysUser.getUserNames();
				res += i == 0 ? userName : ", " + userName;
			}
		}
		
		return res;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return getUserName(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return getUserName(value.toString());
	}

}
