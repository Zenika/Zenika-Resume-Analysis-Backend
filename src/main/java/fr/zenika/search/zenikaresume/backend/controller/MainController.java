package fr.zenika.search.zenikaresume.backend.controller;

import fr.zenika.search.zenikaresume.backend.parsing.ElasticsearchUserService;
import fr.zenika.search.zenikaresume.backend.parsing.ImportRemoteDataFromApiService;
import fr.zenika.search.zenikaresume.backend.parsing.ParsedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


//FIXME https://spring.io/blog/2015/06/08/cors-support-in-spring-framework#filter-based-cors-support

@CrossOrigin(origins = {"http://localhost:4200","https://zenika-resume-analysis.herokuapp.com"},allowCredentials = "true")
@Controller
public class MainController {

    @Autowired
    private OAuth2RestOperations restTemplate;

    @Autowired
    private ImportRemoteDataFromApiService importRemoteDataFromApiService;

    @Autowired
    private ElasticsearchUserService elasticsearchUserService;

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/findusers",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<ParsedUser> findUsers() {
        return importRemoteDataFromApiService.fetchDatas();
    }


    @RequestMapping("/index-users")
    public void indexUsers() {
        elasticsearchUserService.indexUsers(importRemoteDataFromApiService.fetchDatas());
    }

}