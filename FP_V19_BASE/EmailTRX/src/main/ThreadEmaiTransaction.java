package main;

import util.ConnectionHibernateUtilities;

public class ThreadEmaiTransaction implements Runnable{
	private volatile boolean running = true;
	@Override
	public void run(){
		System.out.println("running1" + running);
		while(running){
			System.out.println("running2" + running);
			try {
				try{
					ConnectionHibernateUtilities.getEntityManager();
					EmailTransactionMain.logs.getNotificationLog().info("run().conectado");
				}catch (Exception e) {
					ConnectionHibernateUtilities.getEntityManager();
					EmailTransactionMain.logs.getNotificationLog().info("run().conectado por estar cerrado");
					e.printStackTrace();					
				}
				if(ConnectionHibernateUtilities.getEntityManager()!=null&&ConnectionHibernateUtilities.getEntityManager().isOpen()){
					System.out.println("utility");
					EmailTransactionMain.getEntityDAO().getTrasactions();
					EmailTransactionMain.logs.getNotificationLog().info("getTrasactions() procesado");
					
					EmailTransactionMain.getEntityDAO().sendEmailTrasaction();
					EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction() procesado");
					
				}						
				Thread.sleep(EmailTransactionMain.frequency);
				
			} catch(Exception ex){			
				
				EmailTransactionMain.logs.getErrorLog().error("run().error iniciando EmailTransaction" + ex);				
				System.out.println("run().error iniciando EmailTransaction");
				ex.printStackTrace();		

				try {
					Thread.sleep(EmailTransactionMain.frequency);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void terminate() {
        running = false;
        EmailTransactionMain.logs.getErrorLog().error("terminate().Terminación de hilo");
        System.out.println("Terminación Hilo");
    }
}
