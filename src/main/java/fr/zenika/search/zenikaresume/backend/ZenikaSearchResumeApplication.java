package fr.zenika.search.zenikaresume.backend;

import fr.zenika.search.zenikaresume.backend.job.ImportUsersJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;


@SpringBootApplication
@EnableOAuth2Client
public class ZenikaSearchResumeApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(ZenikaSearchResumeApplication.class, args);
    }

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }


    private Filter ssoFilter() {

        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate template = new OAuth2RestTemplate(googleCodeRessourceDetails(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(
                googleResourceServer().getUserInfoUri(), googleCodeRessourceDetails().getClientId());
        userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor(template));
        filter.setTokenServices(userInfoTokenServices);

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new RedirectAuthenticationSuccessHandler();
        filter.setAuthenticationSuccessHandler(successHandler);

        return filter;
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails googleCodeRessourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResourceServer() {
        return new ResourceServerProperties();
    }


    @Bean
    public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
        return map -> {
            if (!map.containsKey("hd") || !map.get("hd").equals("zenika.com")) {
                throw new BadCredentialsException("Not in zenika origanization");
            }
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        };
    }



}