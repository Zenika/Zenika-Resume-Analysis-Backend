package fr.zenika.search.zenikaresume.backend.parsing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ImportRemoteDataFromApiService {


    @Autowired
    private ParsingService parsingService;

    public List<ParsedUser> fetchDatas()  {
        final String uri = System.getenv("zenika.writing.resume.url")+"/resumes/complete";

        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

        String plainCreds = System.getenv("USER_AUTH_API_USERNAME")+":"+System.getenv("USER_AUTH_API_PASSWORD");
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> request = new HttpEntity<>(headers);


        ResponseEntity<List<ResumeApiResponse>> rateResponse =
                    rt.exchange(uri,
                        HttpMethod.GET, request, new ParameterizedTypeReference<List<ResumeApiResponse>>() {
                        });
        List<ResumeApiResponse> resumeApiResponses = rateResponse.getBody().stream()
                .filter(resumeApiResponse -> StringUtils.isNotEmpty(resumeApiResponse.getPath())).collect(Collectors.toList());

        System.out.println(resumeApiResponses);
        System.out.println("nombre d'utilisateurs recupérés avant traitement : "+resumeApiResponses.size());

        List<ParsedUser> parsedUsers = resumeApiResponses.stream()
                .map(resumeApiResponse -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(resumeApiResponse.getMetadata().getName());
            stringBuilder.append(" ");
            stringBuilder.append(resumeApiResponse.getMetadata().getExperience());
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(resumeApiResponse.getMetadata().getDescription());
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(resumeApiResponse.getContent());
            try {
                ParsedUser parsedUser1 = parsingService.parseFromContent(stringBuilder.toString());
                parsedUser1.setFullname(resumeApiResponse.getMetadata().getName());
                parsedUser1.setFullContent(stringBuilder.toString());
                parsedUser1.setLastUpdate(resumeApiResponse.getLast_modified().toString());
                parsedUser1.setIdFunct(resumeApiResponse.getUuid());
                return parsedUser1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        System.out.println("utilisateurs traités "+parsedUsers);
        System.out.println("nombre d'utilisateurs recupérés aprés traitement : "+parsedUsers.size());

        return parsedUsers;

    }


}
