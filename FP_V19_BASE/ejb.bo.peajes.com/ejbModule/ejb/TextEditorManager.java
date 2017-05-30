package ejb;



import java.util.ArrayList;

import javax.ejb.Remote;

import util.ReToolbarOptionActionList;

@Remote
public interface TextEditorManager {

	public Long getEditorListCount();

	public ArrayList<ReToolbarOptionActionList> getEditorList();

	public String setUpdateBarEditor(
			ArrayList<ReToolbarOptionActionList> listEditor);
	
	public ArrayList<String> getOptionToolBarrEditorText(int count, String formName);
	
	// TPS FRD152 Cristhian David Buchely
	public String getThemefont();
	public String getThemeTooltip();
		
}