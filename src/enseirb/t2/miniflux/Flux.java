package enseirb.t2.miniflux;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Query;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Entity("flux")
public class Flux {
	@Id ObjectId id;
	@Transient
	private String website;
	private String link;       //lien vers le flux
	@Transient
	private List<Item> items;  //les differents articles du flux
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	public Flux() {
		this.link=null;
		this.items=null;
	}
	
	
	public Flux(String website, String link, List<Item> items) {
		super();
		this.website = website;
		this.link = link;
		this.items = items;
	}

	public Flux(String link) {
		 this.link=link;
		 Item item=null;
	     this.items=new LinkedList<Item>();
		 URL url  = null;    //url du flux rss
		 XmlReader reader = null;

		 
	    try {
	        url=new URL(link); 
	        reader = new XmlReader(url);           
	        SyndFeed feed;  
	        feed = new SyndFeedInput().build(reader);  //flux rss converti en objet java
	      
	        List<SyndEntry> entries=feed.getEntries();     //list des articles dans le flux rss
	     
	        //construction liste des articles du flux
	        for (SyndEntry entry:entries) {
	        item=new Item(this.link, entry.getTitle().trim(), entry.getDescription().getValue().trim(), 
	        		entry.getUri().trim(), entry.getPublishedDate());
	        this.items.add(item);
	            }
	        } 
	    
	    	catch(Exception e) {
	    		 e.printStackTrace();
	             System.out.println("ERROR: "+e.getMessage());
	    	}
	    }
	
	public String getLink() {
		return this.link;
	}
	
	public List<Item> getItems() {
		return this.items;
	}
	
	public void saveInDb() {
		Datastore ds=ConnectToDatabase.connect();
		ds.save(this);
		for(Item i:this.items) {
			ds.save(i);
		}
	}
	
	public void updateInDb() {
		boolean find=false;
		Datastore ds=ConnectToDatabase.connect();
		Query<Item> q=ds.createQuery(Item.class).field("linkFlux").equal(this.getLink());
		List<Item> itemsInDb=q.asList();
		for(Item i:this.items) {
			find=false;
			for(Item item:itemsInDb) {
				if(item.getTitle().equalsIgnoreCase(i.getTitle())){
					find=true;
					break;
				}	
			}
			if(find==false) {
				//ci un article dans le nouveau flux rss n'était pas déjà dans la Db
				ds.save(i);
			}
		}
	}
}
