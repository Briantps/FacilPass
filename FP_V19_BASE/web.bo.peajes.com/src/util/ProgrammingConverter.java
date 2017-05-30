package util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ProgrammingConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
		return "My new value 2 : " + value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value)
	{
		String result=">"+value+"<";
		try
		{
			Integer idFrecuency=Integer.parseInt(value.toString().trim());
			switch(idFrecuency)
			{
			 case 1: result="Frecuencia"; break;
			 case 2: result="Saldo Mínimo"; break;
			}
		}
		catch(Exception e)
		{ }
		return result;
	}

}
