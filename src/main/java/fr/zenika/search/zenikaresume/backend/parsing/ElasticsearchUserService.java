package fr.zenika.search.zenikaresume.backend.parsing;

import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by jurio on 04/12/17.
 */

@Service
public class ElasticsearchUserService {


    public void indexUsers(List<ParsedUser> users) {

        Validate.notNull(users);

        users.forEach(parsedUser -> {


        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut postRequest = new HttpPut(
                    "http://localhost:9200/formation-elastic-alias/doc/"+parsedUser.getIdFunct());

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
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        });
    }
}
