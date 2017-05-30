package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jpa.TbCategory;
import ejb.Category;

public class AdminCat implements Serializable {

	private static final long serialVersionUID = -8111247002620349070L;

	private Context context;
	private Category catHandler;
	
	public AdminCat(){
		try {
			context = new InitialContext();
			catHandler = (Category)context.lookup("ejb/Category");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public List<SelectItem> getListCategories(){
		List<TbCategory> categories = catHandler.getCategories();
		List<SelectItem> list = new ArrayList<SelectItem>();
		
		for (TbCategory cat : categories) {
			list.add(new SelectItem(cat.getCategoryId(), cat.getCategoryName()));
		}
		return list;
	}
}
