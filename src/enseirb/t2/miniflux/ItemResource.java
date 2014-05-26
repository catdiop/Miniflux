package enseirb.t2.miniflux;

import java.net.URLDecoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@Path("item")
public class ItemResource {

	@GET
	@Path("read")
	@Produces(MediaType.TEXT_PLAIN)
	public Response isReadTrue(@QueryParam("link") String link, @QueryParam("title") String title) {
		try {
			link=URLDecoder.decode(link, "utf-8");
			title=URLDecoder.decode(title, "utf-8");
			System.out.println(link);
			System.out.println(title);
		}
		catch(Exception e){

		}
		Datastore ds=ConnectToDatabase.connect();
		UpdateOperations<Item> ops = ds.createUpdateOperations(Item.class).set("read", true);
		ds.update(queryToFindMe(link, title), ops);
		return Response.ok().build();
	}

	@GET
	@Path("favorite")
	@Produces(MediaType.TEXT_PLAIN)
	public Response isFavoriteTrue(@QueryParam("link") String link, @QueryParam("title") String title) {
		try {
			link=URLDecoder.decode(link, "utf-8");
			title=URLDecoder.decode(title, "utf-8");
			System.out.println(link);
			System.out.println(title);
		}
		catch(Exception e){

		}
		Datastore ds=ConnectToDatabase.connect();
		UpdateOperations<Item> ops = ds.createUpdateOperations(Item.class).set("favorite", true);
		ds.update(queryToFindMe(link, title), ops);
		return Response.ok().build();
	}

	private Query<Item> queryToFindMe(String link, String title) {
		Datastore ds=ConnectToDatabase.connect();
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(link).field("title").equal(title);
		return q;
	}
}
