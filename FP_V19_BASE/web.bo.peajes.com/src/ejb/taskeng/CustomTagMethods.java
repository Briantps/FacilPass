package ejb.taskeng;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CustomTagMethods {

	public static String getTaskTypeName(Integer id){
		try {
			Context ctx = new InitialContext();
			ConfigTaskEng configTaskEng = (ConfigTaskEng) ctx.lookup("ejb/ConfigTaskEng");
			return configTaskEng.getTaskTypeName(id);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
