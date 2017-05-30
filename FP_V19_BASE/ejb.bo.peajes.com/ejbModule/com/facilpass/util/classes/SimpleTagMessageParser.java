package com.facilpass.util.classes;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facilpass.util.MessageParser;

/** 
 * 
 * */
public class SimpleTagMessageParser implements MessageParser
{
	private Map<String,Object> data;
	
	public SimpleTagMessageParser(Map<String,Object> tagData)
	{
	 this.data = tagData;
	}
	
	@Override
	public String parseMessage(String template)
	{
	 StringBuilder builder=new StringBuilder();
	 StringBuilder builderPattern=new StringBuilder();
  
	 Set<String> keySet=data.keySet();
  
	 for(String key:keySet)
	 {
	  if (builderPattern.length()>0) builderPattern.append("|");
	  builderPattern.append(key);
	 }
  
	 int lastPosition=0;
	 Pattern pattern=Pattern.compile(builderPattern.toString());
	 Matcher matcher=pattern.matcher(template);
 
	 while(matcher.find())
	 {
	  int patStart=matcher.start();
  
	  if(patStart>lastPosition)
	  {
	   builder.append(template.substring(lastPosition, patStart)); 
	  }
	  builder.append(data.get(matcher.group()));
	  lastPosition=matcher.end();
	 }
  
	 if (lastPosition<template.length()) builder.append(template.substring(lastPosition, template.length()));
	 return builder.toString();
	}
	
	@Override
	public Map<String, Object> getParams()
	{
	 return this.data;
	}

}
