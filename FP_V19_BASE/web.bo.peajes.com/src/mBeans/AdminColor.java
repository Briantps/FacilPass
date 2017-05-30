package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jpa.TbColor;
import ejb.Color;

public class AdminColor implements Serializable {
	private static final long serialVersionUID = -6842390579501496662L;
	private Context context;
	private Color colorHandler;
	
	public AdminColor(){
		try {
			context = new InitialContext();
			colorHandler = (Color) context.lookup("ejb/Color");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public List<SelectItem> getListColors(){
		List<TbColor> colors = colorHandler.getColors();
		List<SelectItem> list = new ArrayList<SelectItem>();
		
		for (TbColor color : colors) {
			list.add(new SelectItem(color.getColorId(), color.getColorName()));
		}
		
		return list;
	}
}
