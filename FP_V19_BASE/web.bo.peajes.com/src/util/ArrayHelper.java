package util;

import java.util.List;

public class ArrayHelper {
	public static String implode(List<?> list){
		if(list.size() > 0){
			String res = list.get(0).toString();
			
			for(int i = 1; i < list.size(); i++){
				res += ", " + list.get(i);
			}

			return res;
		}
		return "";
	}
}
