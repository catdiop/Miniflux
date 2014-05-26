package enseirb.t2.miniflux;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("users")
public class User {
	
	@Id ObjectId id;
	private String user;
	private String password;
	
	public User() {
		this.user=null;
		this.password=null;
	}
	
	public User(String user, String password) {
		this.user=user;
		this.password=password;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
