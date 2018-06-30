package fr.zenika.search.zenikaresume.backend.parsing;

import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;


@Service
public class ElasticsearchUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public void indexUsers(List<ParsedUser> users) {

        Validate.notNull(users);

        users.forEach(parsedUser -> {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut postRequest = new HttpPut(
                    System.getenv("elasticsearch.url")+"/formation-elastic-alias/doc/"+parsedUser.getIdFunct());

            Gson gson = new Gson();

            String jsonInString = gson.toJson(parsedUser);

            StringEntity input = new StringEntity(jsonInString,"UTF-8");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200 &&
                    response.getStatusLine().getStatusCode() != 201 ) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            logger.debug("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                logger.debug(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        });
    }

    public String search(String requestDataBody)  {

        logger.debug("Search elastic from req {}",requestDataBody);

        final String uri =
                System.getenv("elasticsearch.url")+"/formation-elastic-alias/doc/_search";

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(requestDataBody, headers);
        ResponseEntity<String> response = rt.postForEntity( uri, request , String.class );

        logger.debug("response of search {} ",response);

        return response.getBody();

    }

}
