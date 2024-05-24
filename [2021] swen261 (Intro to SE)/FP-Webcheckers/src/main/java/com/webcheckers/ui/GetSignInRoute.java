package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.webcheckers.application.PlayerLobby;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the SIGNIN Page.
 *
 * @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class GetSignInRoute implements Route {

  private static final Message SIGN_IN_MSG = Message.info("Sign in with a name.");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetSignInRoute(PlayerLobby playerLobby, final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
  }

  /**
   * Render the WebCheckers Sign In page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Sign In page
   */
  @Override
  public Object handle(Request request, Response response) {

    //RENDER DATA
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Sign In");
    vm.put("message", SIGN_IN_MSG);
    return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
  }
}