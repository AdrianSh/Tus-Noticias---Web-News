package es.fdi.iw.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import es.fdi.iw.model.Actividad;
import es.fdi.iw.model.Articulo;
import es.fdi.iw.model.Comentario;
import es.fdi.iw.model.Puntuacion;
import es.fdi.iw.model.Tag;
import es.fdi.iw.model.User;
import java.util.Collections;
import java.util.Comparator;

@Controller
public class ArticuloController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/articulo/nuevo/publicar", method = RequestMethod.POST)
	@Transactional
	public String adminRipPublicar(@RequestParam("articulo") String articulo, @RequestParam("tags") String tags,
			@RequestParam("titulo") String titulo, Locale locale, Model model, HttpSession session) {
		String returnn = "articulos/nuevo";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			logger.info("Article ripp public by {}", u.getLogin());

			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());

			List<String> contenido = new ArrayList<String>();

			String[] arrayS = Encode.forHtmlContent(articulo).split("\\r?\\n");

			String[] arrayTags = Encode.forHtmlContent(tags).split(",");

			for (String s : arrayS)
				contenido.add(s);

			if (contenido.isEmpty())
				contenido.add(Encode.forHtmlContent(articulo));

			List<Tag> nTags = new ArrayList<Tag>();
			nTags.add(Tag.newTag("administrativo"));

			for (String tg : arrayTags) {
				@SuppressWarnings("unchecked")
				List<Tag> ta = entityManager.createNamedQuery("allByTag").setParameter("tagParam", tg).getResultList();
				if (!ta.isEmpty()) {
					for (Tag taa : ta)
						nTags.add(taa);
				} else {
					Tag tagN = Tag.newTag(tg);
					entityManager.persist(tagN);
					entityManager.flush();
					nTags.add(tagN);
				}
			}

			Articulo article = Articulo.crearArticuloNormal(u, contenido, Encode.forHtmlContent(titulo), nTags);
			entityManager.persist(article);
			entityManager.flush();
			long ids = article.getId();

			Actividad atv = Actividad.createActividad(
					"Ha publicado un articulo normal titulado:" + '"' + Encode.forHtmlContent(titulo) + '"', u,
					new Date());
			u.getActividad().add(atv);

			returnn = "redirect:/articulo/" + ids;

			for (Tag tagf : nTags)
				tagf.getArticulo().add(article);
		} else {
			returnn = "redirect:/noregistro/";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulo/{id}", "/articulo/mostrar/{id}" }, method = RequestMethod.GET)
	public String articulo(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale,
			HttpSession session) {
		model.addAllAttributes(HomeController.basic(locale));
		ponderRanking();
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		Articulo art = entityManager.find(Articulo.class, id);
		if (art == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such articulo: {}", id);
		} else {
			model.addAttribute("articulo", art);

			model.addAttribute("articuloComentarios", entityManager.createNamedQuery("allComentariosByArticulo")
					.setParameter("articuloParam", art).setMaxResults(100).getResultList());
		}
		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("user", u);
			for (int pid : art.getPuntuacionesId()) {
				if (u.getPuntuacionesId().contains(pid)) {
					Puntuacion pp = (Puntuacion) entityManager.find(Puntuacion.class, pid);

					if (pp.getPositivos() > 0) {
						model.addAttribute("puntuacionP", true);
					}
					if (pp.getNegativos() > 0) {
						model.addAttribute("puntuacionN", true);
					}
					break;
				}
			}
		}
		return "articulos/articulo";
	}

	@RequestMapping(value = { "/articulo/tag/{tag}", "/articulos/tag/{tag}" }, method = RequestMethod.GET)
	public String articulosbytag(@PathVariable("tag") String tagName, HttpServletResponse response, Model model,
			Locale locale, HttpSession session) {
		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "./");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		Tag tag = entityManager.find(Tag.class, Encode.forHtmlContent(tagName));
		if (tag == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such tag: {}", tagName);
		} else {
			model.addAttribute("articulos", tag.getArticulo());
		}

		if (UserController.ping(session))
			model.addAttribute("user", true);

		return "articulos/bytag";
	}

	@RequestMapping(value = { "/articulo/nuevo", "/articulo/new" }, method = RequestMethod.GET)
	@Transactional
	public String nuevoArticulo(HttpServletResponse response, Model model, Locale locale, HttpSession session) {
		String returnn = "articulos/nuevo";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

		} else {
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder publicar un articulo.");
			returnn = "noregistro";
		}

		return returnn;
	}

	@RequestMapping(value = { "/articulo/nuevo/articulos", "/articulo/new" }, method = RequestMethod.POST)
	@Transactional
	public String nuevoArticuloLoadArticulos(@RequestParam("tag") String tag, HttpServletResponse response, Model model,
			Locale locale, HttpSession session) {
		String returnn = "articulos/nuevo";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			@SuppressWarnings("unchecked")
			List<Tag> tags = entityManager.createNamedQuery("allByTag").setParameter("tagParam", tag).getResultList();
			if (!tags.isEmpty()) {
				for (Tag tg : tags) {
					List<Articulo> articulos = tg.getArticulo();
					// Falta implementar que los articulos que le son pasados al
					// usuario sean ordenados por fecha
					if (articulos.size() > 10)
						for (int i = 10; i < articulos.size(); i++)
							articulos.remove(i);

					model.addAttribute("articulos", articulos);
				}
			} else {
				returnn = "redirect:/articulo/nuevo";
			}

		} else
			returnn = "redirect:/";

		return returnn;
	}

	@RequestMapping(value = { "/articulos", "/mis/articulos" }, method = RequestMethod.GET)
	@Transactional
	public String misArticulos(HttpServletResponse response, Model model, Locale locale, HttpSession session) {
		String returnn = "articulos/articulos";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos", entityManager.createNamedQuery("allArticulosByAutor")
					.setParameter("autorParam", u).setMaxResults(10000).getResultList());
			model.addAttribute("user", u);
		} else {
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Mis articulos");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver tus articulos.");

			returnn = "noregistro";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulos/ranking" }, method = RequestMethod.GET)
	@Transactional
	public String rankingArt(HttpServletResponse response, Model model, Locale locale, HttpSession session) {
		String returnn = "articulos/articulos";
		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Ranking");
		ponderRanking();
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10000).getResultList());
			model.addAttribute("user", u);
		} else {
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver el ranking.");

			returnn = "noregistro";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulos/favoritos", "/mis/articulosfavoritos" }, method = RequestMethod.GET)
	@Transactional
	public String misArticulosFav(HttpServletResponse response, Model model, Locale locale, HttpSession session) {
		String returnn = "articulos/articulos";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulos favoritos");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos", u.getFavoritos());
			model.addAttribute("user", u);
		} else {
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Articulos favoritos");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver tus articulos favoritos.");

			returnn = "noregistro";
		}
		return returnn;
	}

	/**
	 * Borra un articulo
	 */

	@RequestMapping(value = "/articulo/borrar/{id}", method = RequestMethod.GET)
	@Transactional
	public String borrarArticulo(@PathVariable("id") long id, HttpServletResponse response, Model model,
			HttpSession session, Locale locale) {
		String returnn;
		if (UserController.ping(session)) {
			try {
				User u = (User) session.getAttribute("user");
				u = (User) entityManager.find(User.class, u.getId());

				Articulo art = entityManager.find(Articulo.class, id);

				if (art.getAutor() == u) {
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
				} else {
					returnn = "redirect:/home";
				}

				returnn = "redirect:/mis/articulos";
			} catch (NoResultException nre) {
				logger.error("No existe tal articulo: {}", id, nre);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				returnn = "redirect:/articulos";
			}
		} else {
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../../");
			model.addAttribute("mMensaje",
					"Debes estar registrado y ser el dueño de dicho articulo, para poder borrarlo.");

			returnn = "noregistro";
		}
		return returnn;
	}

	/**
	 * Añadir articulo a favoritos
	 */
	@RequestMapping(value = "/articulo/favorito", method = RequestMethod.POST)
	@Transactional
	public String anadirArticuloFavorito(@RequestParam("idArticulo") long idArticulo, Model model,
			HttpSession session) {

		Articulo art = entityManager.find(Articulo.class, idArticulo);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			user.getFavoritos().add(art);

			entityManager.persist(user);

			entityManager.flush();
			logger.info("Articulo " + art.getId() + " añadido a favoritos de " + user.getLogin());
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/favorito", method = RequestMethod.GET)
	@Transactional
	public String anadirArticuloFavoritoById(@PathVariable("id") long idArticulo, Model model, HttpSession session) {

		Articulo art = entityManager.find(Articulo.class, idArticulo);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			if (user.getFavoritos().contains(art)) {
				user.getFavoritos().remove(art);
			} else {
				user.getFavoritos().add(art);
			}

			entityManager.persist(user);

			entityManager.flush();
			logger.info("Articulo " + art.getId() + " añadido a favoritos de " + user.getLogin());
		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo positivo
	 */

	@RequestMapping(value = "/articulo/puntuarP", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloPositivo(@RequestParam("id") long id, HttpSession session) {
		Articulo art = entityManager.find(Articulo.class, id);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion positiva retirada");
			} else {
				Puntuacion p = new Puntuacion(1, 0);
				p.setUsuario(user.getId());
				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado positivo");
			}
			entityManager.persist(art);
			entityManager.flush();
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/puntuarP", method = RequestMethod.GET)
	@Transactional
	public String puntuarArticuloPositivob(@PathVariable("id") long id, HttpSession session) {
		Articulo art = entityManager.find(Articulo.class, id);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion positiva retirada");
			} else {
				Puntuacion p = new Puntuacion(1, 0);
				p.setUsuario(user.getId());

				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado positivo");
			}
			entityManager.persist(art);
			entityManager.flush();

		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo negativo
	 */

	@RequestMapping(value = "/articulo/puntuarN", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloNegativo(@RequestParam("id") long id, HttpSession session) {
		Articulo art = entityManager.find(Articulo.class, id);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion negativa retirada");
			} else {
				Puntuacion p = new Puntuacion(0, 1);
				p.setUsuario(user.getId());

				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado negativo");
			}
			entityManager.persist(art);
			entityManager.flush();
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/puntuarN", method = RequestMethod.GET)
	@Transactional
	public String puntuarArticuloNegativoB(@PathVariable("id") long id, HttpSession session) {
		Articulo art = entityManager.find(Articulo.class, id);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());

			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion negativa retirada");
			} else {
				Puntuacion p = new Puntuacion(0, 1);
				p.setUsuario(user.getId());

				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado negativo");
			}
			entityManager.persist(art);
			entityManager.flush();
		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Añadir tag a un articulo
	 */
	@RequestMapping(value = "/articulo/anadirTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String anadirTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag, Model model,
			HttpSession session) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);

		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());
			if (art.getAutor() == user) {
				if (t == null) {
					t = Tag.newTag(Encode.forHtmlContent(tag));
				}

				t.getArticulo().add(art);
				art.getTags().add(t);

				entityManager.persist(t);
				entityManager.persist(art);

				entityManager.flush();
				logger.info("Tag " + t.getNombre() + " puesto a articulo " + art.getId());
			}
		}
		return "redirect:/mis/articulos";
	}

	/**
	 * Quitar tag de un articulo
	 */
	@RequestMapping(value = "/articulo/eliminarTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String eliminarTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag, Model model,
			HttpSession session) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);
		if (UserController.ping(session)) {
			User user = (User) session.getAttribute("user");
			user = (User) entityManager.find(User.class, user.getId());
			if (art.getAutor() == user) {
				t.getArticulo().remove(art);
				art.getTags().remove(t);

				entityManager.persist(t);
				entityManager.persist(art);

				entityManager.flush();
				logger.info("Tag " + t.getNombre() + " eliminado de articulo " + art.getId());
			}
		}
		return "redirect:/mis/articulos";
	}

	int sumaPuntuaciones(Articulo art) {
		Iterator<Integer> itera = art.getPuntuacionesId().iterator();
		int total = 0;
		while (itera.hasNext()) {
			Integer control = itera.next();
			Number num =control;
			Long control2=num.longValue();
			Puntuacion pun =  entityManager.find(Puntuacion.class, control2);
			int suma = pun.getPositivos()-pun.getNegativos();
		 	total = total + suma;	
		}
		return total;
	}
	
	

	void ponderRanking() {
		
		try {
			List<Articulo> art = new ArrayList<Articulo>();
			art = entityManager.createNamedQuery("allArticulos").setMaxResults(10000).getResultList();
			Comparator<Articulo> comparador = Collections.reverseOrder();
			Comparator<Articulo> prueba2 = new Comparator<Articulo>() {
				public int compare(Articulo s1, Articulo s2) {
				   Integer x = sumaPuntuaciones(s1);
				   Integer y = sumaPuntuaciones(s2);
				   return y.compareTo(x);
			    }};
			    Collections.sort(art, prueba2);
			int ranking = 1;
			Iterator<Articulo> itera = art.iterator();
			while (itera.hasNext()){
				 Articulo arp =itera.next();
				 arp.setRanking(ranking);
				ranking=ranking+1;
				entityManager.merge(arp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


