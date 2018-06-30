package fr.zenika.search.zenikaresume.backend.controller;

import fr.zenika.search.zenikaresume.backend.parsing.ElasticsearchUserService;
import fr.zenika.search.zenikaresume.backend.parsing.ImportRemoteDataFromApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;


//FIXME https://spring.io/blog/2015/06/08/cors-support-in-spring-framework#filter-based-cors-support

@CrossOrigin(origins = {"http://localhost:4200","https://zenika-resume-analysis.herokuapp.com"},allowCredentials = "true",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.POST})
@Controller
public class MainController {


    @Autowired
    private ImportRemoteDataFromApiService importRemoteDataFromApiService;

    @Autowired
    private ElasticsearchUserService elasticsearchUserService;

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping("/index-users")
    public void indexUsers() {
        elasticsearchUserService.indexUsers(importRemoteDataFromApiService.fetchDatas());
    }

    @PostMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String search(@RequestBody String requestDataBody) throws IOException {
        return elasticsearchUserService.search(requestDataBody);
    }



}