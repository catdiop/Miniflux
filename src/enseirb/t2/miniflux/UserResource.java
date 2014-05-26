package enseirb.t2.miniflux;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;

@Path("user")
public class UserResource {

	@POST
	@Path("register")
	@Produces(MediaType.TEXT_PLAIN)
	public Response userRegister(@FormParam("username") String user, @FormParam("password") String password) {
		User u=new User(user, password);
		Datastore ds=ConnectToDatabase.connect();
		Query<User> q=ds.createQuery(User.class).field("user").equal(user);
		//User us=q.get();
		if(q.countAll()==0) {
			ds.save(u);
			return Response.ok().build();  //bien enregistré
		}
		else 
			return Response.status(Status.FORBIDDEN).build(); //nom utilisateur existe déjà 
	}
	
	@POST
	@Path("sign_in")
	@Produces(MediaType.TEXT_PLAIN)
	public Response signIn(@FormParam("username") String user, @FormParam("password") String password) {
		Datastore ds=ConnectToDatabase.connect();
		Query<User> q=ds.createQuery(User.class).field("user").equal(user).field("password").equal(password);
		if(q.countAll()!=0)
			return Response.ok().build();
		else
			return Response.status(Status.FORBIDDEN).build();
	}
}
