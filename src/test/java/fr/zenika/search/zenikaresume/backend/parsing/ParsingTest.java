package fr.zenika.search.zenikaresume.backend.parsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ParsingTest {


    @Test
    public void parseSkills(){
        String rawSkills = "Spring (Core, Batch, Boot), CDI, XML";
        List<String> skills = ParsingService.parseSkillsStyle(rawSkills);
        assertThat(skills).containsAll(Arrays.asList("Spring Core","Spring Batch","Spring Boot","CDI","XML"));
    }
}
