package ejb.taskeng.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.ejb.Stateless;

import jpa.TbTask;

import ejb.taskeng.util.ParamList;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

/**
 * Session Bean implementation class CreateFileEJB
 */
@Stateless(mappedName = "ejb/CreateFile")
public class CreateFileEJB implements CreateFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Default constructor. 
     */
    public CreateFileEJB() {
    }

	@Override
	public int execute(TbTask taskReg, Task task, Step data, String params) {
		Map<String, Object> map = ParamList.getMap(data);
		
		try {
			FileOutputStream out = new FileOutputStream("files"
					+ File.separator + (String) map.get("nombre"));
			
			PrintWriter writer = new PrintWriter(out);
			writer.println((String)map.get("cabecera"));
			writer.println(map.get("valor"));
			writer.println((String)map.get("pie"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return 1;
	}

}
