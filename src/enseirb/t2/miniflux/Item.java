package enseirb.t2.miniflux;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("items")
public class Item implements Comparable<Item> {
	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		if(this.publishedDate.after(o.publishedDate)) 
			return -1;
		else if(this.publishedDate.before(o.publishedDate)) 
			return 1;
		else
			return 0;
	}

	@Id ObjectId id;
	private String linkFlux;
	private String title;
	private String description;
	private String uri;
	private Date publishedDate;
	private boolean read;
	private boolean favorite;

	public Item() {
		this.linkFlux=null;
		this.title=null;
		this.description=null;
		this.uri=null;
		this.publishedDate=null;
		this.read=false;
		this.favorite=false;
	}
	
	public Item(String linkFlux, String title, String description, String uri, Date publishedDate) {
		this.linkFlux=linkFlux;
		this.title=title;
		this.description=description;
		this.uri=uri;
		this.publishedDate=publishedDate;
		this.read=false;
		this.favorite=false;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getLinkFlux() {
		return linkFlux;
	}

	public void setLinkFlux(String linkFlux) {
		this.linkFlux = linkFlux;
	}

	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public Date getPublishedDate() {
		return this.publishedDate;
	}
}
