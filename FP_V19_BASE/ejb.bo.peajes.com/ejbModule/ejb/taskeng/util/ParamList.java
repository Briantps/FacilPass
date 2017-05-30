package ejb.taskeng.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ejb.taskeng.workflow.ActionParam;
import ejb.taskeng.workflow.Step;

public class ParamList {
	public static Map<String, Object> getMap(Step data){
		List<ActionParam> params = data.getActions().get(0).getParams();
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (ActionParam param : params) {
			map.put(param.getName(), param.getValue());
		}
		
		return map;
	}
}
