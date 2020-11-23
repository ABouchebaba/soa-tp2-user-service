package com.amboucheba.soatp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@RestController
@EnableSwagger2
public class SoaTp2Application {

	public static void main(String[] args) {
		SpringApplication.run(SoaTp2Application.class, args);
	}

	@GetMapping(produces = "text/html")
	public String home(@RequestParam(value = "user", required = false, defaultValue = "") String user){
		return "<html><body><h1>Welcome " + user + "</h1><p> User service: Home page </p></body></html>";
	}

	@GetMapping("/documentation")
	public ModelAndView redirectToSwaggerUI(ModelMap model) {
		model.addAttribute("attribute", "redirectWithRedirectPrefix");
		return new ModelAndView("redirect:/swagger-ui/", model);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
