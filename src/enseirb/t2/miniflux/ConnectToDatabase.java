package enseirb.t2.miniflux;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class ConnectToDatabase {
		private static String databaseName="myDb";
		
	public static Datastore connect() {
		try {
			
			List<MongoCredential> mongoCredential=new LinkedList<MongoCredential>();
			String password=new String("rokheya");
			
			ServerAddress serverAddress=new ServerAddress("localhost", 27017);
			
			MongoCredential m=MongoCredential.createMongoCRCredential("catdiop", databaseName, password.toCharArray());
			Mongo mongoClient = new MongoClient(serverAddress, mongoCredential);
			
			Morphia morphia=new Morphia();
			morphia.map(Item.class);
			morphia.map(Flux.class);
			Datastore ds=morphia.createDatastore(mongoClient, databaseName);
			return ds;
		}
		catch(UnknownHostException u) {
			System.out.println("Error: "+u.getMessage());
		}
		return null;
	}
	
}
