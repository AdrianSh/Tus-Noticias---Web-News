package es.fdi.iw.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
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

	@RequestMapping(value = { "/", "/home", "/index" }, method = RequestMethod.GET)
	public String homePage(Locale locale, Model model, HttpSession session) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Home");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("actividades", entityManager.createNamedQuery("allActividad").getResultList());
		model.addAttribute("lastarticulos",
				entityManager.createNamedQuery("allArticulosOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		
		
		model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

		if (UserController.ping(session)) {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("user", u);
		}
		return "articulos/articulos";
	}

	/*
	 * @RequestMapping(value = "/error", method = RequestMethod.GET) public
	 * String errorPage(HttpSession session) { String returnn = "error";
	 * if(UserController.isAdmin(session)) returnn = "error_admin"; return
	 * returnn; }
	 */

	@RequestMapping(value = "/noregistro", method = RequestMethod.GET)
	public String noRegistro(Locale locale, Model model) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "No Registro");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		
		model.addAttribute("mMensaje", "Debes estar registrado.");
		return "noregistro";
	}

	@RequestMapping(value = "/inicio_sesion", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Login");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		
		return "login";
	}

	/*
	 * @RequestMapping(value = "/actividad/{id}", method = RequestMethod.GET)
	 * public String actividad(@PathVariable("id") long id, HttpServletResponse
	 * response, Model model, Locale locale) {
	 * model.addAllAttributes(basic(locale)); model.addAttribute("prefix",
	 * "../"); model.addAttribute("pageTitle", "Actividad");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Actividad act = entityManager.find(Actividad.class, id); if (act == null)
	 * { response.setStatus(HttpServletResponse.SC_NOT_FOUND); logger.error(
	 * "No such actividad: {}", id); } else { model.addAttribute("actividad",
	 * act); }
	 * 
	 * return "actividad"; }
	 * 
	 * @RequestMapping(value = "/comentario/{id}", method = RequestMethod.GET)
	 * public String comentario(@PathVariable("id") long id, HttpServletResponse
	 * response, Model model, Locale locale) {
	 * model.addAllAttributes(basic(locale)); model.addAttribute("prefix",
	 * "../"); model.addAttribute("pageTitle", "Comentario");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Comentario com = entityManager.find(Comentario.class, id); if (com ==
	 * null) { response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	 * logger.error("No such comentario: {}", id); } else {
	 * model.addAttribute("comentario", com); } return "comentario"; }
	 * 
	 * @RequestMapping(value = "/puntuacion/{id}", method = RequestMethod.GET)
	 * public String puntuacion(@PathVariable("id") long id, HttpServletResponse
	 * response, Model model, Locale locale) {
	 * model.addAllAttributes(basic(locale)); model.addAttribute("prefix",
	 * "../"); model.addAttribute("pageTitle", "Puntuacion");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Puntuacion pun = entityManager.find(Puntuacion.class, id); if (pun ==
	 * null) { response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	 * logger.error("No such puntuacion: {}", id); } else {
	 * model.addAttribute("puntuacion", pun); } return "puntuacion"; }
	 * 
	 * @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET) public
	 * String tag(@PathVariable("id") long id, HttpServletResponse response,
	 * Model model, Locale locale) { model.addAllAttributes(basic(locale));
	 * model.addAttribute("prefix", "../"); model.addAttribute("pageTitle",
	 * "Tag"); model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Tag tag = entityManager.find(Tag.class, id); if (tag == null) {
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND); logger.error(
	 * "No such tag: {}", id); } else { model.addAttribute("tag", tag); }
	 * 
	 * return "tag"; }
	 * 
	 */

	/**
	 * A not-very-dynamic view that shows an "about us".
	 */
	@RequestMapping(value = { "/about", "/sobre", "/nosotros" }, method = RequestMethod.GET)
	@Transactional
	public String about(Locale locale, Model model) {

		model.addAllAttributes(basic(locale));
		model.addAttribute("pageTitle", "Quienes somos");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		
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
		atributos.put("siteURL", "http://localhost:8080/tusnoticias/");

		return atributos;
	}
}
