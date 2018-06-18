package fr.zenika.search.zenikaresume.backend.parsing;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ImportRemoteDataFromApiService {

    @Autowired
    private ParsingService parsingService;

    public List<ParsedUser> fetchDatas()  {

        final String uri = "http://localhost:3000/resumes/complete";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<ResumeApiResponse>> rateResponse =
                restTemplate.exchange(uri,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ResumeApiResponse>>() {
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
