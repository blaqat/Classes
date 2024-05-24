package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.webcheckers.application.*;

import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to Process the Username Input.
 *
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostSignInRoute implements Route {

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignInRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
    }

    /**
     * Processes the User Sign In
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {

        String playerName = request.queryParams("name");
        Session htmlSession = request.session();
        String signInResult = playerLobby.signIn(playerName, htmlSession);

        //Sends respective error if name is invalid or is taken
        if (signInResult != null) {
            //RENDER DATA
            Map<String, Object> vm = new HashMap<>();
            vm.put("title", "Sign In");
            vm.put("message", Message.error(signInResult));
            return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
        }

        //redirect on success
        response.redirect("/");
        return null;
    }
}