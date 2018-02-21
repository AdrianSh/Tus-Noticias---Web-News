package es.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "allAmigos", query = "select a from Amigos a where a.user = :userParam")})
public class Amigos {
	private long id;
	private User user;
	private User amigo;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(targetEntity = User.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToOne(targetEntity=User.class)
	public User getAmigo() {
		return amigo;
	}

	public void setAmigo(User amigo) {
		this.amigo = amigo;
	}

}
