package enseirb.t2.miniflux;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

@Path("flux")
public class FluxResource {
	
	//Obtenir les noms des flux auquels on est abonné
	@GET
	@Path("allFluxName")
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
	
	//Obtenir tous les articld'un flux
	@GET
	@Path("{fluxName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Item> getFlux(@PathParam("fluxName") String fluxName) {
		Flux newFlux=new Flux("http://geopolis.francetvinfo.fr/la-une-rss");
		newFlux.saveInDb();
		Datastore ds=ConnectToDatabase.connect();
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal("http://geopolis.francetvinfo.fr/la-une-rss");
		List<Item> itemsInDb=q.asList();
		return itemsInDb;
	}
}
