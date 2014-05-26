package enseirb.t2.miniflux;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

@Path("flux")
public class FluxResource {
	
	//Obtenir tous les article d'un flux
	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getFlux(@QueryParam("link") String link) {
		
		String s=null;
		try {
		s=URLDecoder.decode(link, "utf-8");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		Datastore ds=ConnectToDatabase.connect();
		if(ds.createQuery(Flux.class).field("link").equal(s).asList().isEmpty()==true) {
			Flux flux=new Flux(s);
			flux.saveInDb();
			List<Item> items=flux.getItems();
			Collections.sort(items);
			return items;
		}
		
		else {
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(s);
		List<Item> itemsInDb=q.asList();
		Collections.sort(itemsInDb);
		return itemsInDb;
		}
	}
	
	@GET
	@Path("refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> refresh(@QueryParam("link") String link) {
		boolean find=false;
		List<Item> newItemsToShow=new LinkedList<Item>();
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
				newItemsToShow.add(item);
			}
		}
		Collections.sort(newItemsToShow);
		return newItemsToShow;
	}
	
	@GET
	@Path("favorites")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getFavorites(@QueryParam("link") String link) {
		Datastore ds=ConnectToDatabase.connect();
		//récupération du lien de flux à rafraîchir		
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(link).field("favorite").equal(true);
		List<Item> itemsInDb=q.asList();
		
		return itemsInDb;
	}
	
	
}
