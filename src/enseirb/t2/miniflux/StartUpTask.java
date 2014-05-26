package enseirb.t2.miniflux;

import java.io.IOException;
import java.util.LinkedList;
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
import javax.ws.rs.QueryParam;

import org.glassfish.jersey.server.Uri;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

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
			t.schedule(new MonAction(), 0, 8*3600*1000);
		}

		class MonAction extends TimerTask {

			public void run() {
				//on prend ts les flux dans la base de données
				List<String> allFlux=getAllFluxName();
				//Mise à jour périodique des flux dans la base de données
				if(allFlux.isEmpty()==false) {
					for(String f:allFlux) {
						refresh(f);
					}
					
				}
			}
		}
	}

	private static List<String> getAllFluxName() {
		Datastore ds=ConnectToDatabase.connect();
		Query<Flux> q=ds.createQuery(Flux.class);
		List<Flux> fluxInDb=q.asList();
		List<String> allFluxName=new LinkedList<String>();
		for(Flux f:fluxInDb) {
			if(allFluxName.contains(f.getLink())==false)
			allFluxName.add(f.getLink());
		}
		return allFluxName;
	}
	
	
	private static void refresh(String link) {
		boolean find=false;
		Datastore ds=ConnectToDatabase.connect();
		//récupération du lien de flux à rafraîchir
		Flux flux=new Flux(link);
		
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(link);
		List<Item> itemsInDb=q.asList();
		
		for(Item item:flux.getItems()) {
			find=false;
			for(Item i:itemsInDb) {
				//item dans flux rss plus récent et même titre 
				if(i.getPublishedDate().compareTo(item.getPublishedDate())<0 && i.getTitle().equalsIgnoreCase(item.getTitle())) {
					//supprimer item présent dans la base de données faire un break
					Query<Item> q1 = ds.createQuery(Item.class);
					q1.and(
					    q1.criteria("title").equal(i.getTitle()),
					    q1.criteria("publishedDate").equal(i.getPublishedDate())
					);
					ds.delete(q1);
					break;
				}
				
				else if(i.getPublishedDate().compareTo(item.getPublishedDate())==0 && i.getTitle().equalsIgnoreCase(item.getTitle())) {
					find=true;
					break;
				}
			}
			
			if(find==false) { //item n'était pas déjà présent dans la base de données
				ds.save(item);
			}
		}
		
	}
	
}
