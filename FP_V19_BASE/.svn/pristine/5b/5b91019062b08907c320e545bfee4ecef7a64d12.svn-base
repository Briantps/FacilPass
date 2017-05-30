package converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

/**
 * Converts a given identifier to a string. Applied to combo boxes and select lists.
 * @author Mauricio Gil
 */
public class RowIdToNameConverter implements Converter {

	public RowIdToNameConverter(){
		
	}
	
	@SuppressWarnings("unchecked")
    private List<SelectItem> retrieveSelectItems(UIComponent uiComponent){
        List<SelectItem> items = null;
        if (!uiComponent.getChildren().isEmpty() && uiComponent.getChildren().get(0) instanceof UISelectItems) {
            items = (List<SelectItem>)((UISelectItems)uiComponent.getChildren().get(0)).getValue();
        }
        return items;
    }
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		List<SelectItem> items = retrieveSelectItems(component);
        if (items != null) {
            for (SelectItem item : items) {
                if (item.getLabel().equals(value)){
                    return item.getValue();
                }
            }
        }

		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		List<SelectItem> items = retrieveSelectItems(component);
        if (items != null) {
            for (SelectItem item : items) {
                if (item.getValue().equals(value)){
                    return item.getLabel();
                }
            }
        }
        return value == null ? null : "";
	}

}
