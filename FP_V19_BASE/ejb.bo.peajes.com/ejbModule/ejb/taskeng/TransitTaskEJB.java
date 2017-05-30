package ejb.taskeng;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbTareaTrans;
import constant.TypeTask;
import crud.ObjectEM;

@Stateless(mappedName = "ejb/TransitTask")
public class TransitTaskEJB implements TransitTask{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private ObjectEM objectEM;
	
	public TransitTaskEJB(){
		
	}

	@Override
	public void createTask(TypeTask tipoTarea, String idTarea) {			
			TbTareaTrans tt = new TbTareaTrans();
			tt.setNombreTabla(tipoTarea.getTable());
			tt.setIdPkTabla(idTarea);
			tt.setEstado(0);
			objectEM = new ObjectEM(em);
			objectEM.create(tt);
			System.out.println("Tipo Tarea: "+tipoTarea.toString());
			System.out.println("Id Tarea: "+idTarea);		
	}
	
	public boolean createTaskAccount(TypeTask tipoTarea, String idTarea) {
		try {
			Query q = em.createNativeQuery("INSERT INTO TAREA_TRANS VALUES (TAREA_TRANS_SEQ.NEXTVAL,?1,?2,0)");
			q.setParameter(1, tipoTarea.getTable());
			q.setParameter(2, idTarea);
			q.executeUpdate();
			em.flush();			
		} catch (Exception e) {
			System.out.println("Error al intentar insertar tarea");			
		}
		return true;
		
	}
}
