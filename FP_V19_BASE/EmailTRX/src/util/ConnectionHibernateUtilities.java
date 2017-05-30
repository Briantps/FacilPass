package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import main.EmailTransactionMain;

public class ConnectionHibernateUtilities {
	@PersistenceContext(unitName = "emailTransaction")
	private static EntityManager em;
	private static EntityManagerFactory emf;
	private static Properties properties;
	
	public static EntityManager getEntityManager(){
		String res = "";
		try{
			getEntityFactory();
			if(emf!=null){
				if(em==null){
					em = emf.createEntityManager();
					EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Se inicializó la entidad");
					System.out.println("Se inicializó la entidad");
				}else{
					if(!em.isOpen()){						
						em = emf.createEntityManager();
						EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Se inicializó la entidad al estar cerrada");
						System.out.println("Se inicializó la entidad al estar cerrada");
					}else{
						Query q = em.createNativeQuery("select * from dual ");
						res = (String) q.getSingleResult();
						System.out.println("res: " + res);
						if(res != null){
							if(res != ""){
								EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Ya se encontraba abierta la entidad");
								System.out.println("Ya se encontraba abierta la entidad");
							}							
						}else{
							em = emf.createEntityManager();
							EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Se inicializó la entidad al estar cerrada");
							System.out.println("Se inicializó la entidad al estar cerrada");
						}						
					}
				}
				return em;
			}else{
				EmailTransactionMain.logs.getErrorLog().error("getEntityManager().No se creo entidad por factoria nula");
				System.out.println("No se creo entidad por factoria nula");
				return null;
			}
		}catch (Exception e2) {			
			System.out.println("res: " + res);			
			if(res.equals("")){
				//em = emf.createEntityManager();
				EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Se inicializó la entidad al estar cerrada");
				System.out.println("Se inicializó la entidad al estar cerrada");
			}
			//else{
				System.out.println("Error creando la entidad");
				EmailTransactionMain.logs.getErrorLog().error("getEntityManager().Error creando la entidad");
				e2.printStackTrace();
			//}		
			
			return null;
		}
	}

	/*public static void closeEntityManager(){
		try{
			if(em!=null){
				if(em.isOpen()){
					em.close();
					System.out.println("----------Se cerró la entidad");
				}else{
					System.out.println("----------Ya se encontraba cerrada la entidad");
				}
			}else{
				System.out.println("----------Entidad nula");
			}
			if(emf!=null){
				if(emf.isOpen()){
					emf.close();
					System.out.println("----------Se cerró la factoria");
				}else{
					System.out.println("----------Ya se encontraba cerrada la factoria");
				}
			}else{
				System.out.println("----------Factoria nula");
			}
		}catch (Exception e) {
			System.out.println("----------Error al momento de cerrar la entidad o la factoria");
			e.printStackTrace();
		}
	}*/
	
	private static EntityManagerFactory getEntityFactory(){
		String res = "";
		try{
			if(emf==null){
				emf=Persistence.createEntityManagerFactory("emailTransaction",getPersistenceProperties());
				EmailTransactionMain.logs.getNotificationLog().info("getEntityFactory().Se creó factoria por estar nula");
				System.out.println("Se creó factoria por estar nula");
			}else{
				if(!emf.isOpen()){
					emf=Persistence.createEntityManagerFactory("emailTransaction",getPersistenceProperties());
					EmailTransactionMain.logs.getNotificationLog().info("getEntityFactory().Se creó factoria por estar cerrada");
					System.out.println("Se creó factoria por estar cerrada");
				}else{
					Query q = em.createNativeQuery("select * from dual ");
					res = (String) q.getSingleResult();
					if(res != null){
						if(res != ""){
							EmailTransactionMain.logs.getNotificationLog().info("getEntityFactory().Ya se encontraba abierta la factoria");
							System.out.println("Ya se encontraba abierta la factoria");
						}							
					}else{
						emf=Persistence.createEntityManagerFactory("emailTransaction",getPersistenceProperties());
						EmailTransactionMain.logs.getNotificationLog().info("getEntityFactory().Se creó factoria por estar cerrada");
						System.out.println("Se creó factoria por estar cerrada");
					}					
				}
			}
			return emf;
		}catch (Exception e) {		
			if(res.equals("")){
				emf=Persistence.createEntityManagerFactory("emailTransaction",getPersistenceProperties());
				EmailTransactionMain.logs.getNotificationLog().info("getEntityManager().Se creó factoria por estar cerrada");
				System.out.println("Se creó factoria por estar cerrada");
			}
			System.out.println("Error al crear la factoria");
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	/**
	 * @return Properties
	 */
	private synchronized static Properties getPersistenceProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(new File(
						"persistence.properties")));
			} catch (Exception e) {

				e.printStackTrace();
			}
			return properties;
		}
		return properties;
	}
	
}