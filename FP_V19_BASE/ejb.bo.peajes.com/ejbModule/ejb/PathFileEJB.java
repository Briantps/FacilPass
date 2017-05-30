package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbPathFile;

/**
 * Session Bean implementation class PathFileEJB
 */
@Stateless(mappedName = "ejb/PathFile")
public class PathFileEJB implements PathFile {

	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PathFileEJB() {
    }

    /*
     * (non-Javadoc)
     * @see ejb.PathFile#getFilePaths()
     */
	@Override
	public List<TbPathFile> getFilePaths() {
		List<TbPathFile> list = new ArrayList<TbPathFile>();
		try {
			for (Object obj: em.createQuery("SELECT pf FROM TbPathFile pf").getResultList()) {
				list.add((TbPathFile) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PathFileEJB.getFilePaths ] ");
		}
		return list;
	}
	
	@Override
	public List<TbPathFile> getFilePathsClient() {
		List<TbPathFile> list = new ArrayList<TbPathFile>();
		try {
			for (Object obj: em.createQuery("SELECT pf FROM TbPathFile pf WHERE pf.pathFileId = 1").getResultList()) {
				list.add((TbPathFile) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PathFileEJB.getFilePathsClient] ");
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.PathFile#getPath(java.lang.String)
	 */
	@Override
	public String getPath(String param) {
		try {
			Query q = em
					.createQuery("SELECT ph FROM TbPathFile ph WHERE ph.pathFileName = ?1");
			q.setParameter(1, param);
			TbPathFile p = (TbPathFile) q.getSingleResult();
			return p.getPathFile();
		} catch (Exception e) {
			System.out.println(" [ Error en PathFileEJB.getPath ] ");
		}
		return null;
	}
}