package es.fdi.iw.controller;

import java.util.Date;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.fdi.iw.model.Actividad;
import es.fdi.iw.model.User;

@Controller
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
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
			logger.info("Administration loaded by {}", u.getLogin());
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
		
		if(UserController.isAdmin(session))
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

}
