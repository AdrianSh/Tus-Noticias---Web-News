package es.fdi.iw.model;

import java.util.ArrayList;
import java.util.Date; 
import java.util.Iterator;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class Articulo {

	private long id;
	private String titulo;
	private List<String> contenido;
	private User autor;
	private List<Tag> tags;
	private List<Integer> puntuacionesId;
	private List<Comentario> comentario;
	private Date fecha;
	private int ranking;
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@ElementCollection
	public List<String> getContenido() {
		return contenido;
	}

	public void setContenido(List<String> contenido) {
		this.contenido = contenido;
	}
	@ManyToOne(targetEntity=User.class)
	public User getAutor() {
		return autor;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}
	
	@ManyToMany(targetEntity=Tag.class, fetch=FetchType.EAGER, mappedBy="articulo")
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	@ElementCollection
	public List<Integer> getPuntuacionesId() {
		//aqui consulta para encontrar puntuacion
		return puntuacionesId;
	}

	public void setPuntuacionesId(List<Integer> puntuacionesId) {
		this.puntuacionesId = puntuacionesId;
		//aqui consulta para guardar puntuacion
	}
	
	@OneToMany(targetEntity=Comentario.class, fetch=FetchType.EAGER, mappedBy="articulo")
	public List<Comentario> getComentario(){
		return comentario;
	}
	
	public void setComentario(List<Comentario> comentario) {
		this.comentario = comentario;
	}

	public List<String> ponerComentariosEnArticulo(){
		List<String> list=new ArrayList<String>();
		Comentario coment;
		String texto;
		Iterator<Comentario> iterator = comentario.iterator();
		while (iterator.hasNext()) {
			coment=iterator.next();
			texto =coment.getComment();
			StringBuilder builder = new StringBuilder(texto);
			builder.substring(3, texto.indexOf("..."+'"'));
			list.add(builder.toString());
		}
		return null;
	}
}
