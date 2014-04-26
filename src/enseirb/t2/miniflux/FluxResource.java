package enseirb.t2.miniflux;

import java.net.URLDecoder;
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
	
	//Obtenir les noms des flux auquels on est abonné
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAllFluxName() {
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
	
	//Obtenir tous les article d'un flux
	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getFlux(@QueryParam("link") String link) {
		String s=null;
		try {
		s=URLDecoder.decode(link, "utf-8");
		System.out.println(s);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		Datastore ds=ConnectToDatabase.connect();
		if(ds.createQuery(Flux.class).field("link").equal(s).asList().isEmpty()==true) {
			Flux flux=new Flux(s);
			flux.saveInDb();
			return flux.getItems();
		}
		
		else {
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(s);
		List<Item> itemsInDb=q.asList();
		return itemsInDb;
		}
	}
	
	@GET
	@Path("refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> refresh(@QueryParam("link") String link) {
		Datastore ds=ConnectToDatabase.connect();
		//récupération du lien de flux à rafraîchir
		Flux flux=new Flux(link);
		ds.delete(ds.createQuery(Item.class).field("linkFlux").equal(link));
		ds.save(flux);
		return flux.getItems();
	}
}
