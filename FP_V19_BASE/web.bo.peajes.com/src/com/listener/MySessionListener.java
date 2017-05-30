package com.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener{

    int newActiveSession=0;
    int totalSessionCreated=0;

    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Nueva Sesi�n Creada");
        newActiveSession++;
        totalSessionCreated++;
        System.out.println("Sesiones Activas  :="+newActiveSession);

        System.out.println("Total No Sesiones Creadas :="+totalSessionCreated);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("Sesi�n es Destruida");
        newActiveSession--;
    }

}