package es.fdi.iw.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import es.fdi.iw.model.Actividad;
import es.fdi.iw.model.Articulo;
import es.fdi.iw.model.Comentario;
import es.fdi.iw.model.Puntuacion;
import es.fdi.iw.model.Tag;
import es.fdi.iw.model.User;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	// Enlaces

	@RequestMapping(value = {"/", "/home", "/index"}, method = RequestMethod.GET)
	public String homePage(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Home");
		model.addAttribute("actividades", entityManager.createNamedQuery("allActividad").getResultList()); 
		
		return "home";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String errorPage() {
		return "error";
	}
			

	@RequestMapping(value = "/comentarios", method = RequestMethod.GET)
	public String comentarios(Locale locale, Model model) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Comentarios");
		return "comentarios";
	}

	@RequestMapping(value = "/nosotros", method = RequestMethod.GET)
	public String nosotros(Locale locale, Model model) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Nosotros");
		return "nosotros";
	}

	@RequestMapping(value = "/articulo", method = RequestMethod.GET)
	public String articulo(Locale locale, Model model) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Articulo");
		return "articulo";
	}

	// Funcionalidad

	@RequestMapping(value = "/actividad/{id}", method = RequestMethod.GET)
	public String actividad(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Actividad");
		
		Actividad act = entityManager.find(Actividad.class, id);
		if (act == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such actividad: {}", id);
		} else {
			model.addAttribute("actividad", act);
		}
		
		return "actividad";
	}

	@RequestMapping(value = "/articulo/{id}", method = RequestMethod.GET)
	public String articulo(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		
		Articulo art = entityManager.find(Articulo.class, id);
		if (art == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such articulo: {}", id);
		} else {
			model.addAttribute("articulo", art);
		}
		return "articulos";
	}

	@RequestMapping(value = "/comentario/{id}", method = RequestMethod.GET)
	public String comentario(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Comentario");
		
		Comentario com = entityManager.find(Comentario.class, id);
		if (com == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such comentario: {}", id);
		} else {
			model.addAttribute("comentario", com);
		}
		return "comentario";
	}

	@RequestMapping(value = "/puntuacion/{id}", method = RequestMethod.GET)
	public String puntuacion(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Puntuacion");
		
		Puntuacion pun = entityManager.find(Puntuacion.class, id);
		if (pun == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such puntuacion: {}", id);
		} else {
			model.addAttribute("puntuacion", pun);
		}
		return "puntuacion";
	}

	@RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
	public String tag(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Tag");
		
		Tag tag = entityManager.find(Tag.class, id);
		if (tag == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such tag: {}", id);
		} else {
			model.addAttribute("tag", tag);
		}
		
		return "tag";
	}

	/**
	 * Añadir comentario
	 */
	@RequestMapping(value = "/comentario/anadir", params = { "owner", "comment",
			"articulo" }, method = RequestMethod.POST)
	@Transactional
	public String anadirComentario(@RequestParam("owner") long ownerId, @RequestParam("comment") String comment,
			@RequestParam("articulo") long articuloId, Model model) {
		Comentario com = new Comentario();
		com.setPuntuacionesId(new ArrayList<Integer>());
		com.setRespuestas(new ArrayList<Comentario>());
		com.setUser(entityManager.getReference(User.class, ownerId));
		com.setComment(comment);
		com.setResponde(null);
		entityManager.persist(com);

		Articulo art = entityManager.find(Articulo.class, articuloId);
		art.getComentario().add(com);
		entityManager.persist(art);

		entityManager.flush();
		logger.info("Comment " + com.getId() + " written in " + com.getArticulo().getTitulo() + " written by "
				+ com.getUser().getLogin());

		return "redirect:comentario/" + com.getId();
	}

	/**
	 * Puntuar comentario positivo
	 */

	@RequestMapping(value = "/comentario/puntuarP", params = { "id", "user" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioPositivo(@RequestParam("id") long id, @RequestParam("user") long userId) {
		Comentario com = entityManager.find(Comentario.class, id);
		Puntuacion p = new Puntuacion(1, 0);
		p.setUsuario(userId);

		com.getPuntuacionesId().add((Integer) (int) p.getId());

		com.getUser().getPuntuacionesId().add((Integer) (int) p.getId());

		entityManager.persist(p);
		entityManager.persist(com);
		entityManager.flush();
		logger.info("Articulo " + com.getId() + " puntuado positivo");

		return "redirect:articulo/" + com.getArticulo().getId();
	}

	/**
	 * Puntuar comentario negativo
	 */

	@RequestMapping(value = "/comentario/puntuarN", params = { "id", "user" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioNegativo(@RequestParam("id") long id, @RequestParam("user") long userId) {
		Comentario com = entityManager.find(Comentario.class, id);
		Puntuacion p = new Puntuacion(0, 1);
		p.setUsuario(userId);

		com.getPuntuacionesId().add((Integer) (int) p.getId());

		com.getUser().getPuntuacionesId().add((Integer) (int) p.getId());

		entityManager.persist(p);
		entityManager.persist(com);
		entityManager.flush();
		logger.info("Articulo " + com.getId() + " puntuado negativo");

		return "redirect:articulo/" + com.getArticulo().getId();
	}

	/**
	 * Responder comentario
	 */

	@RequestMapping(value = "/comentario/responder", params = { "owner", "comment", "articulo",
			"comentario original" }, method = RequestMethod.POST)
	@Transactional
	public String responderComentario(@RequestParam("owner") long ownerId, @RequestParam("comment") String comment,
			@RequestParam("articulo") long articuloId, Model model,
			@RequestParam("comentario original") long comentarioOrgId) {
		Comentario com = new Comentario();
		Comentario org = entityManager.find(Comentario.class, comentarioOrgId);

		com.setPuntuacionesId(new ArrayList<Integer>());
		com.setRespuestas(new ArrayList<Comentario>());
		com.setUser(entityManager.getReference(User.class, ownerId));
		com.setComment(comment);
		com.setResponde(org);

		entityManager.persist(com);

		Articulo art = entityManager.find(Articulo.class, articuloId);
		art.getComentario().add(com);
		entityManager.persist(art);

		org.anadirRespuesta(com);
		entityManager.persist(org);

		entityManager.flush();
		logger.info("Comment " + com.getId() + " written in " + com.getArticulo().getTitulo() + " written by "
				+ com.getUser().getLogin());

		return "redirect:comentario/" + com.getId();
	}

	/**
	 * Borra un comentario
	 */
	@RequestMapping(value = "/comentario/borrar/{id}", method = RequestMethod.DELETE)
	@Transactional
	@ResponseBody
	public String borrarComentario(@PathVariable("id") long id, HttpServletResponse response, Model model) {
		try {
			Comentario c = entityManager.find(Comentario.class, id);
			for (Comentario com : c.getRespuestas()) {
				entityManager.remove(com);
			}

			for (Integer p : c.getPuntuacionesId()) {
				Puntuacion pun = entityManager.find(Puntuacion.class, (long) (int) p);
				entityManager.remove(pun);
			}

			if (c.getResponde() != null)
				c.getResponde().getRespuestas().remove(c);

			c.getArticulo().getComentario().remove(c);

			entityManager.remove(c);
			response.setStatus(HttpServletResponse.SC_OK);
			return "OK";
		} catch (NoResultException nre) {
			logger.error("No existe tal comentario: {}", id, nre);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "ERR";
		}
	}

	/**
	 * Añadir un articulo
	 */
	@RequestMapping(value = "/articulo/anadir", params = { "autor", "titulo", "contenido",
			"tags" }, method = RequestMethod.POST)
	@Transactional
	public String anadirArticulo(@RequestParam("autor") long autorId, @RequestParam("titulo") String titulo,
			@RequestParam("contenido") List<String> contenido, @RequestParam("tags") List<Tag> tags, Model model) {
		Articulo art = new Articulo();

		art.setTitulo(titulo);
		art.setContenido(contenido);
		art.setAutor(entityManager.getReference(User.class, autorId));
		art.setTags(tags);
		art.setPuntuacionesId(new ArrayList<Integer>());
		art.setComentario(new ArrayList<Comentario>());
		art.setFecha(new Date());
		art.setRanking(0);
		entityManager.persist(art);

		entityManager.flush();
		logger.info("Articulo " + art.getId() + " puesto por " + art.getAutor().getLogin());

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Borra un articulo
	 */
	@RequestMapping(value = "/articulo/borrar/{id}", method = RequestMethod.DELETE)
	@Transactional
	@ResponseBody
	public String borrarArticulo(@PathVariable("id") long id, HttpServletResponse response, Model model) {
		try {
			Articulo art = entityManager.find(Articulo.class, id);
			for (Comentario com : art.getComentario()) {
				entityManager.remove(com);
			}

			for (Tag tag : art.getTags()) {
				entityManager.remove(tag);
			}

			for (Integer p : art.getPuntuacionesId()) {
				Puntuacion pun = entityManager.find(Puntuacion.class, (long) (int) p);
				entityManager.remove(pun);
			}

			entityManager.remove(art);
			response.setStatus(HttpServletResponse.SC_OK);
			return "OK";
		} catch (NoResultException nre) {
			logger.error("No existe tal articulo: {}", id, nre);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "ERR";
		}
	}

	/**
	 * Añadir articulo a favoritos
	 */
	@RequestMapping(value = "/articulo/favorito", method = RequestMethod.POST)
	@Transactional
	public String anadirArticuloFavorito(@RequestParam("idArticulo") long idArticulo,
			@RequestParam("idUser") long idUser, Model model) {

		Articulo art = entityManager.find(Articulo.class, idArticulo);
		User user = entityManager.find(User.class, idUser);

		user.getFavoritos().add(art);

		entityManager.persist(user);

		entityManager.flush();
		logger.info("Articulo " + art.getId() + " añadido a favoritos de " + user.getLogin());

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo positivo
	 */

	@RequestMapping(value = "/articulo/puntuarP", params = { "id", "user" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloPositivo(@RequestParam("id") long id, @RequestParam("user") long userId) {
		Articulo art = entityManager.find(Articulo.class, id);

		Puntuacion p = new Puntuacion(1, 0);
		p.setUsuario(userId);

		art.getPuntuacionesId().add((Integer) (int) p.getId());

		art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());

		entityManager.persist(p);
		entityManager.persist(art);
		entityManager.flush();
		logger.info("Articulo " + art.getId() + " puntuado positivo");

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo negativo
	 */

	@RequestMapping(value = "/articulo/puntuarN", params = { "id", "user" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloNegativo(@RequestParam("id") long id, @RequestParam("user") long userId) {
		Articulo art = entityManager.find(Articulo.class, id);

		Puntuacion p = new Puntuacion(0, 1);
		p.setUsuario(userId);

		art.getPuntuacionesId().add((Integer) (int) p.getId());

		art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());

		entityManager.persist(p);
		entityManager.persist(art);
		entityManager.flush();
		logger.info("Articulo " + art.getId() + " puntuado negativo");

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Añadir tag a un articulo
	 */
	@RequestMapping(value = "/articulo/anadirTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String anadirTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag, Model model) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);

		if (t == null) {
			t = new Tag(tag);
		}

		t.getArticulo().add(art);
		art.getTags().add(t);

		entityManager.persist(t);
		entityManager.persist(art);

		entityManager.flush();
		logger.info("Tag " + t.getNombre() + " puesto a articulo " + art.getId());

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Quitar tag de un articulo
	 */
	@RequestMapping(value = "/articulo/eliminarTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String eliminarTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag,
			Model model) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);

		t.getArticulo().remove(art);
		art.getTags().remove(t);

		entityManager.persist(t);
		entityManager.persist(art);

		entityManager.flush();
		logger.info("Tag " + t.getNombre() + " eliminado de articulo " + art.getId());

		return "redirect:articulo/" + art.getId();
	}

	/**
	 * Mostrar articulo
	 */
	@RequestMapping(value = "/articulo/mostrar/{id}", method = RequestMethod.GET)
	@Transactional
	public String mostrarArticulo(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		
		Articulo art = entityManager.find(Articulo.class, id);

		if (art == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such articulo: {}", id);
		} else {
			model.addAttribute("articulo", art);
		}
		
		return "articulo";
	}

	//
	// /**
	// * Displays single-book details
	// */
	// @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
	// public String book(@PathVariable("id") long id, HttpServletResponse
	// response, Model model) {
	// Book b = entityManager.find(Book.class, id);
	// if (b == null) {
	// response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	// logger.error("No such book: {}", id);
	// } else {
	// model.addAttribute("book", b);
	// }
	// model.addAttribute("prefix", "../");
	// return "book";
	// }
	//
	// /**
	// * Delete a book
	// */
	// @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
	// @Transactional
	// @ResponseBody
	// public String rmbook(@PathVariable("id") long id, HttpServletResponse
	// response, Model model) {
	// try {
	// Book b = entityManager.find(Book.class, id);
	// for (Author a : b.getAuthors()) {
	// a.getWritings().remove(b);
	// entityManager.persist(a);
	// }
	// entityManager.remove(b);
	// response.setStatus(HttpServletResponse.SC_OK);
	// return "OK";
	// } catch (NoResultException nre) {
	// logger.error("No such book: {}", id, nre);
	// response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	// return "ERR";
	// }
	// }
	//
	// /*
	// * List all books
	// */
	// @RequestMapping(value = "/books", method = RequestMethod.GET)
	// @Transactional
	// public String books(Model model) {
	// model.addAttribute("books",
	// entityManager.createNamedQuery("allBooks").getResultList());
	// model.addAttribute("owners",
	// entityManager.createNamedQuery("allUsers").getResultList());
	// model.addAttribute("authors",
	// entityManager.createNamedQuery("allAuthors").getResultList());
	// return "books";
	// }
	//
	// /*
	// * Add a book
	// */
	// @RequestMapping(value = "/book", method = RequestMethod.POST)
	// @Transactional
	// public String book(@RequestParam("owner") long ownerId,
	// @RequestParam("authors") long[] authorIds,
	// @RequestParam("title") String title,
	// @RequestParam("description") String description, Model model) {
	// Book b = new Book();
	// b.setTitle(title);
	// b.setDescription(description);
	// for (long aid : authorIds) {
	// // adding authors to book is useless, since author is the owning side (=
	// has no mappedBy)
	// Author a = entityManager.find(Author.class, aid);
	// a.getWritings().add(b);
	// entityManager.persist(a);
	// }
	// b.setOwner(entityManager.getReference(User.class, ownerId));
	// entityManager.persist(b);
	// entityManager.flush();
	// logger.info("Book " + b.getId() + " written ok - owned by " +
	// b.getOwner().getLogin()
	// + " written by " + b.getAuthors());
	//
	// return "redirect:book/" + b.getId();
	// }
	// /**
	// * Load book authors for a given book via post; return as JSON
	// */
	// @RequestMapping(value = "/bookAuthors")
	// @ResponseBody
	// @Transactional // needed to allow lazy init to work
	// public ResponseEntity<String> bookAuthors(@RequestParam("id") long id,
	// HttpServletRequest request) {
	// try {
	// Book book = (Book)entityManager.find(Book.class, id);
	// List<Author> authors = book.getAuthors();
	// StringBuilder sb = new StringBuilder("[");
	// for (Author a : authors) {
	// if (sb.length()>1) sb.append(",");
	// sb.append("{ "
	// + "\"id\": \"" + a.getId() + "\", "
	// + "\"familyName\": \"" + a.getFamilyName() + "\", "
	// + "\"lastName\": \"" + a.getLastName() + "\"}");
	// }
	// return new ResponseEntity<String>(sb + "]", HttpStatus.OK);
	// } catch (NoResultException nre) {
	// logger.error("No such book: {}", id, nre);
	// }
	// return new ResponseEntity<String>("Error: libro no existe",
	// HttpStatus.BAD_REQUEST);
	// }
	//
	// /**
	// * Displays author details
	// */
	// @RequestMapping(value = "/author/{id}", method = RequestMethod.GET)
	// public String author(@PathVariable("id") long id, Model model) {
	// try {
	// model.addAttribute("author", entityManager.find(Author.class, id));
	// } catch (NoResultException nre) {
	// logger.error("No such author: {}", id, nre);
	// }
	// model.addAttribute("prefix", "../");
	// return "author";
	// }
	/**
	 * A not-very-dynamic view that shows an "about us".
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	@Transactional
	public String about(Locale locale, Model model) {
		
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "IW: Quienes somos");
		
		logger.info("User is looking up 'about us'");
		@SuppressWarnings("unchecked")
		List<User> us = (List<User>) entityManager.createQuery("select u from User u").getResultList();
		System.err.println(us.size());
		model.addAttribute("users", us);
		return "about";
	}

	/*
	 * Basic for all pages - Let it be like General Page settings
	 */
	public static Map<String, String> basic(Locale locale) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		
		Map<String, String> atributos = new HashMap<String, String>();
		atributos.put("serverTime", formattedDate);
		atributos.put("siteTitle", "Tus Noticias");
		atributos.put("siteName", "Tus Noticias");
		atributos.put("siteURL", "http://localhost:8080/iw/");
		
		return atributos;
	}
}
