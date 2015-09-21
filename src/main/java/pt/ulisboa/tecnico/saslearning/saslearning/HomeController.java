package pt.ulisboa.tecnico.saslearning.saslearning;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String goToHome() {
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value = "/headerFragment")
	public String getHeader(){
		return "headerFragment";
	}
	
	@RequestMapping(value = "/templates")
	public String getTemplates(){
		return "templates";
	}	
	
	
	

}
