package fr.zenika.search.zenikaresume.backend;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//We need to handle the redirect from this success handler instead open id google redirect_uri parameter
//because the redirect uri provided to google sign will redirect the user to the front end without
//go to the spring boot authent code which to let record the user as logged user

public class RedirectAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameterValues("redirect_url_additional")[0];
    }
}