package enseirb.t2.miniflux;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

import enseirb.t2.miniflux.TimerTaskServlet.RepetAction.MonAction;

/**
 * Servlet implementation class StartUpTaskServlet
 */

//Tâche à lancer au lancement du serveur
public class StartUpTask implements ServletContextListener {
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	System.out.println("Tâche à lancer au lancement du serveur");
    	new RepetAction();
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	class RepetAction {
		Timer t;

		public RepetAction() {
			t = new Timer();
			t.schedule(new MonAction(), 0, 24*60*60/2);
		}

		class MonAction extends TimerTask {
			int nbrRepetitions = 3;

			public void run() {
				Datastore ds=ConnectToDatabase.connect();
				//on prend ts les flux dans la base de données
				Query<Flux> q=ds.createQuery(Flux.class);
				List<Flux> fluxInDb=q.asList();
				//Mise à jour périodique des flux dans la base de données
				if(fluxInDb.isEmpty()==false) {
					for(Flux f:fluxInDb) {
						Flux newFlux=new Flux(f.getLink());
						newFlux.updateInDb();
					}
					
				}
			}
		}
	}

}
