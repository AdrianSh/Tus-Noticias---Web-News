package es.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "allPeriodicos", query = "select u from Periodico u") })
public class Periodico {

	private long id;
	private String url;
	private String identificador;
	private String identiValor;
	private String contenidoHTML = null;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setContenidoHTML(String contenido) {
		contenidoHTML = contenido;
	}

	public String getContenidoHTML() {
		return contenidoHTML;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getIdentiValor() {
		return identiValor;
	}

	public void setIdentiValor(String identiValor) {
		this.identiValor = identiValor;
	}
}
