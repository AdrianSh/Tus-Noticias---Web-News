package es.fdi.iw.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import es.fdi.iw.ArticleRipper;
import es.fdi.iw.model.Actividad;
import es.fdi.iw.model.Periodico;
import es.fdi.iw.model.User;

@Controller
public class ArticuloController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager; 
	
	@RequestMapping(value = "/articulo/nuevo", method = RequestMethod.GET)
	public String nuevoArticulo(HttpServletResponse response, Model model, HttpSession session, Locale locale) {
		logger.info("Articulo nuevo en: {}.", locale);
		
		User u = (User) session.getAttribute("user");
		if (u != null) {
			Actividad atv = Actividad.createActividad("Desea crear un nuevo articulo", u, new Date());
			u.getActividad().add(atv);
			
			model.addAllAttributes(HomeController.basic(locale));
			model.addAttribute("pageTitle", "Nuevo Articulo");
			model.addAttribute("prefix", "./../");
			
			List<Periodico> periodicos = entityManager.createNamedQuery("allPeriodicos").getResultList();
			model.addAttribute("periodicos", periodicos);
			
			for (int i = 0; i < periodicos.size(); i++) {
				String contenido = "";
				Periodico periodico = periodicos.get(i);
				ArticleRipper articul = new ArticleRipper(periodico.getUrl());
				Document docu = articul.getDocument();
				if(periodico.getIdentificador() == "id"){
					contenido = docu.getElementById(periodico.getIdentiValor()).html();
				} else if(periodico.getIdentificador() == "class"){
					Elements elementos = docu.getElementsByClass(periodico.getIdentiValor());
					for(Element elemento : elementos){
						contenido += elemento.html();
					}
				} else if(periodico.getIdentificador() == "tag") {
					Elements elementos = docu.getElementsByTag(periodico.getIdentiValor());
					for(Element elemento : elementos){
						contenido += elemento.html();
					}
				} else {
					Elements elementos = docu.getElementsByAttributeValue(periodico.getIdentificador(), periodico.getIdentiValor());
					for(Element elemento : elementos){
						contenido += elemento.html();
					}
				}
				periodico.setContenidoHTML(contenido);
			}
	
			return "newarticulo";
		} else {
			return "home";
		}
	}
}
