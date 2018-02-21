package es.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.fdi.iw.ArticleRipper;
import es.fdi.iw.ContextInitializer;
import es.fdi.iw.model.Actividad;
import es.fdi.iw.model.Articulo;
import es.fdi.iw.model.User;
import es.fdi.iw.model.Periodico;
import es.fdi.iw.model.Tag;

@Controller
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	@Transactional
	public String admin(Locale locale, Model model, HttpSession session) {
		String returnn = "admin";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
			Actividad atv = Actividad.createActividad("Ha entrado a la administración", u, new Date());
			u.getActividad().add(atv);
			model.addAttribute("mensajes",
					entityManager.createNamedQuery("allMensajesByUser").setParameter("userParam", u).getResultList());
			model.addAttribute("actividades",
					entityManager.createNamedQuery("allActividad").setMaxResults(10).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/flot", method = RequestMethod.GET)
	public String adminFlot(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/flot";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/morris", method = RequestMethod.GET)
	public String adminMorris(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/morris";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}
	
	@RequestMapping(value = "/admin/tables/addTag", method = RequestMethod.POST)
	@Transactional
	public String adminTablesAddTag(@RequestParam("tag") String tag, Locale locale, Model model, HttpSession session) {
		String returnn = "redirect:/admin/tables";
		
		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			
			Tag nTag = new Tag();
			nTag.setArticulo(new ArrayList<Articulo>());
			nTag.setNombre(tag);
			
			entityManager.persist(nTag);

			logger.info("Tag added {}", nTag.getNombre());
		}

		return returnn;
	}
	
	@RequestMapping(value = "/admin/tables/addPeriodico", method = RequestMethod.POST)
	@Transactional
	public String adminTablesAddPeriodico(@RequestParam("periodico") String nombre, @RequestParam("url") String url, Locale locale, Model model, HttpSession session) {
		String returnn = "redirect:/admin/tables";
		
		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			
			Periodico periodico = new Periodico();
			periodico.setNombre(nombre);
			periodico.setUrl(url);
			
			entityManager.persist(periodico);

			logger.info("Periodico added {}", periodico.getNombre());
		}

		return returnn;
	}
	
	@RequestMapping(value = "/admin/tables", method = RequestMethod.GET)
	public String adminTables(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/tables";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
			model.addAttribute("mensajes",
					entityManager.createNamedQuery("allMensajesByUser").setParameter("userParam", u).getResultList());
			model.addAttribute("actividades",
					entityManager.createNamedQuery("allActividad").setMaxResults(10).getResultList());
			model.addAttribute("tabla_users",
					entityManager.createNamedQuery("allUsers").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_comentarios",
					entityManager.createNamedQuery("allComentarios").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_comentarios_perfil",
					entityManager.createNamedQuery("allComentarioPerfil").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_tags",
					entityManager.createNamedQuery("allTags").setMaxResults(10000).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/forms", method = RequestMethod.GET)
	public String adminForms(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/forms";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Ripper articles interface Opened by {}", u.getLogin());
			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/forms/publicar", method = RequestMethod.POST)
	@Transactional
	public String adminRipPublicar(@RequestParam("articulo") String articulo, @RequestParam("tags") String tags, @RequestParam("titulo") String titulo,
			Locale locale, Model model, HttpSession session) {
		String returnn = "admin/forms";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		articulo = Encode.forHtmlContent(articulo);
		
		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());
			logger.info("Article ripp public by {}", u.getLogin());

			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());

			List<String> contenido = new ArrayList<String>();
			String[] arrayS = articulo.split("\\r?\\n");
			
			String[] arrayTags = tags.split(",");

			for (String s : arrayS){
				if(s.length() > 50){
					String[] subss = s.split("a");
					for(String subs : subss)
						contenido.add('a' + subs);
				} else {
					contenido.add(s);
				}
			}

			if (contenido.isEmpty())
				contenido.add(articulo);

			List<Tag> nTags = new ArrayList<Tag>();
			nTags.add(Tag.newTag("administrativo"));
			
			
			for (String tg : arrayTags) {
				List<Tag> ta = entityManager.createNamedQuery("allByTag").setParameter("tagParam", tg).getResultList();
				if (!ta.isEmpty()) {
					for(Tag taa: ta)
						nTags.add(taa);
				} else {
					Tag tagN = Tag.newTag(tg);
					entityManager.persist(tagN); 
					entityManager.flush();
					nTags.add(tagN);
				}
			}
			
			Articulo article = Articulo.crearArticuloAdministrativo(u, contenido, titulo, nTags);
			entityManager.persist(article);
			entityManager.flush();
			long ids = article.getId();
			String idSs = Long.toString(ids);
			/*
			 * @RequestParam("artimagen") MultipartFile imagen
			 
			if (!imagen.isEmpty()) {
				try {

					byte[] bytes = imagen.getBytes();
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(ContextInitializer.getFile("articulos", idSs)));
					stream.write(bytes);
					stream.close();
					
					article.setImage(ContextInitializer.getFile("articulos", idSs).getAbsolutePath());
					
					model.addAttribute("imagen", Encode.forUriComponent(ContextInitializer.getFile("articulos", idSs).getAbsolutePath()));
					// ContextInitializer.getFile("user", id).getAbsolutePath();

				} catch (Exception e) {
					return "You failed to upload for Article " + idSs + " => " + e.getMessage();
				}
			}
			*/
			
			Actividad atv = Actividad.createActividad("Ha publicado un articulo administrativo titulado:" + '"' + titulo + '"', u, new Date());
			u.getActividad().add(atv);

			returnn = "redirect:/articulo/" + ids;
			
			for (Tag tagf : nTags)
				tagf.getArticulo().add(article);
		}
		return returnn;
	}

	@RequestMapping(value = "/admin/forms", method = RequestMethod.POST)
	@Transactional
	public String adminRip(@RequestParam("periodico") long periodicoId, @RequestParam("tipo") String tipo,
			@RequestParam("urlarticulo") String url, @RequestParam("identificador") String identificador, Locale locale,
			Model model, HttpSession session) {
		String returnn = "admin/forms";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Article ripp load by {}", u.getLogin());
			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
			Periodico p = (Periodico) entityManager.find(Periodico.class, periodicoId);
			if (p != null) {
				ArticleRipper ar = new ArticleRipper(url);
				Document doc = ar.getDocument();
				Elements els = null;
				Element el = null;

				if (tipo.equals("classs")) {
					els = doc.getElementsByClass(identificador);
					// logger.info("Elementos identificado por class,
					// indentificado con {}", identificador);
				} else if (tipo.equals("id")) {
					el = doc.getElementById(identificador);
					// logger.info("Elemento identificado por id, indentificado
					// con {}", identificador);
				} else if (tipo.equals("attribute")) {
					els = doc.getElementsByAttribute(identificador);
					// logger.info("Elementos identificado por atributo,
					// indentificado con {}", identificador);
				} else {
					logger.info("Tipo no existente {}, indentificado con {}", tipo, identificador);
				}

				if (els == null && el == null) {
					returnn = "redirect:/admin/forms?error=Articulo%20no%20encontrado";
				} else {
					if (els != null) {
						List<String> articles = new ArrayList<String>();
						for (int i = 0; i < els.size(); i++) {
							Element artc = els.get(i);
							String arts = Jsoup.parse(artc.toString()).text();
							articles.add(arts);
						}
						model.addAttribute("articulos", articles);

					}
					if (el != null)
						model.addAttribute("articulo", Jsoup.parse(el.toString()).text());
				}

			} else {
				returnn = "redirect:/admin/forms?error=Periodico%20No%20Valido";
			}
		}
		return returnn;
	}

	@RequestMapping(value = "/admin/panels-wells", method = RequestMethod.GET)
	public String adminPanelWells(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/panels-wells";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/buttons", method = RequestMethod.GET)
	public String adminButtons(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/buttons";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/notifications", method = RequestMethod.GET)
	public String adminNotifications(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/notifications";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/typography", method = RequestMethod.GET)
	public String adminTypography(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/typography";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/icons", method = RequestMethod.GET)
	public String adminIcons(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/icons";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/grid", method = RequestMethod.GET)
	public String adminGrid(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/grid";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/blank", method = RequestMethod.GET)
	public String adminBlank(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/blank";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/loginSettings", method = RequestMethod.GET)
	public String adminLoginSettings(Locale locale, Model model, HttpSession session) {
		String returnn = "admin/login";

		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Administracion");
		model.addAttribute("prefix", "./../");

		if (!UserController.isAdmin(session)) {
			returnn = "redirect:/admin/login";
		} else {
			User u = (User) session.getAttribute("user");
			logger.info("Administration loaded by {}", u.getLogin());
		}

		return returnn;
	}

	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public String adminlogin(Locale locale, Model model, HttpSession session) {
		String returnn = "admin_login";
		model.addAllAttributes(HomeController.basic(locale));
		model.addAttribute("pageTitle", "Login");
		model.addAttribute("prefix", "./../");

		if (UserController.isAdmin(session))
			returnn = "redirect:/admin";

		return returnn;
	}

	/**
	 * Delete a user; return JSON indicating success or failure
	 */
	@RequestMapping(value = "/delUser", method = RequestMethod.POST)
	@ResponseBody
	@Transactional // needed to allow DB change
	public ResponseEntity<String> bookAuthors(@RequestParam("id") long id, @RequestParam("csrf") String token,
			HttpSession session) {
		if (!UserController.isAdmin(session) || !UserController.isTokenValid(session, token)) {
			return new ResponseEntity<String>("Error: no such user or bad auth", HttpStatus.FORBIDDEN);
		} else if (entityManager.createNamedQuery("delUser").setParameter("idParam", id).executeUpdate() == 1) {
			return new ResponseEntity<String>("Ok: user " + id + " removed", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error: no such user", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/articulo/{id}/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] articuloPhoto(@PathVariable("id") long id) throws IOException {
		try {
			String st = Long.toString(id);
			File f = ContextInitializer.getFile("articulos", st);
			InputStream in = null;
			if (f.exists()) {
				in = new BufferedInputStream(new FileInputStream(f));
			} else {
				in = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("unknown-user.jpg"));
			}

			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			logger.warn("Error cargando la imagen del articulo id:  " + id, e);
			throw e;
		}
	}
}
