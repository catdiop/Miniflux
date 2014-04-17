package enseirb.t2.miniflux;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("items")
public class Item {
	@Id ObjectId id;
	private String linkFlux;
	private String title;
	private String description;
	private String uri;
	private Date publishedDate;
	
	public Item() {
		this.linkFlux=null;
		this.title=null;
		this.description=null;
		this.uri=null;
		this.publishedDate=null;
	}
	
	public Item(String linkFlux, String title, String description, String uri, Date publishedDate) {
		this.linkFlux=linkFlux;
		this.title=title;
		this.description=description;
		this.uri=uri;
		this.publishedDate=publishedDate;
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
	
	public Date getPublishedDate() {
		return this.publishedDate;
	}
}
