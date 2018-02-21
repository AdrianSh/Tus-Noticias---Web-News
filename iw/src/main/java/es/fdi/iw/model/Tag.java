package es.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {

	private List<Articulo> articulo;
	private String nombre;
	
	
	public Tag(String nombre){
		this.nombre=nombre;
		this.articulo = new ArrayList<Articulo>();
	}
	
	@Id
	public String getNombre(){
		return nombre;
	} 
	
	@ManyToMany(targetEntity=Articulo.class)
	@JoinColumn(name="Tags") 
	public List<Articulo> getArticulo(){
		return articulo;
	}
	
	public void setArticulo(List<Articulo> articulo) {
		this.articulo = articulo;
	}
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}

}
