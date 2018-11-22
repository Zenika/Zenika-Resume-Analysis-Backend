package fr.zenika.search.zenikaresume.backend;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.boot.SpringApplication;
import fr.zenika.search.zenikaresume.backend.parsing.ElasticsearchUserService;
import fr.zenika.search.zenikaresume.backend.parsing.ImportRemoteDataFromApiService;

@Service
public class ImportResumeJobMain {

    public static void main(String[] args) {
        SpringApplication.run(ZenikaSearchResumeApplication.class, args);
    }

    @Autowired
    private ImportRemoteDataFromApiService importRemoteDataFromApiService;

    @Autowired
    private ElasticsearchUserService elasticsearchUserService;

    @PostConstruct
    private void runImportJob(){
        elasticsearchUserService.indexUsers(importRemoteDataFromApiService.fetchDatas());
    }
}