package edu.envdev.springdemo.control;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

  @RequestMapping("/403")
  public String forbidden(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    return "error/403";
  }
}
