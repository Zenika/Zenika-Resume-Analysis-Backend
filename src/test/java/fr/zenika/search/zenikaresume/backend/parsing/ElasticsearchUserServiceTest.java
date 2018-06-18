package fr.zenika.search.zenikaresume.backend.parsing;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchUserServiceTest {

    @Autowired
    ElasticsearchUserService elasticsearchUserService;

    @Test
    @Ignore
    public void indexUsers() throws IOException {
        ParsedUser parsedUser  = new ParsedUser();
        parsedUser.setFullname("toto");
        parsedUser.setLastname("ddz");
        parsedUser.setEmail("dazd@Êzz.fr");

        elasticsearchUserService.indexUsers(Arrays.asList(parsedUser));

    }
}
