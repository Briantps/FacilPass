package com.facilpass.util;

import java.util.Map;

public interface MessageParser {

	public String parseMessage(String template);

	public Map<String, Object> getParams();
}
