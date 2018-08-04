package fr.zenika.search.zenikaresume.backend.parsing;

 import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
 import org.joda.time.format.DateTimeFormat;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jurio on 04/12/17.
 */

@Service
public class ParsingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final List<String> TODAY_KEYWORD_DATE = Arrays.asList("a ce jour","maintenant","aujourd'hui", "now", "courant", "en cours");
    // http://www.ocpsoft.org/tutorials/regular-expressions/java-visual-regex-tester/

    public static final String F = "yyyy-MM-dd'T'HH:mm:ss.SSS";

   // 2017-01-04T09:53:05.000Z

    public ParsedUser parseFromContent(String fileContent) throws IOException {

        ParsedUser parsedUser = new ParsedUser();

        List<String> lines = IOUtils.readLines(new StringReader(fileContent));

        int currentPos = 0;
        Pattern p = Pattern.compile("([\\wàâçéèêëîïôûùü]+-?[\\wàâçéèêëîïôûùü]+) ([\\wàâçéèêëîïôûùü]+) (\\d+) an(s)? d'expérience");
        Matcher m = p.matcher(lines.get(currentPos));
        logger.debug(lines.get(0));
        if(m.find()){
            parsedUser.setFirstname(m.group(1));
            parsedUser.setLastname(m.group(2));
            parsedUser.setNbAnneeExp(Integer.valueOf(m.group(3)));
        }else{
            p = Pattern.compile("([\\wôéè]+-?[\\wàâçéèêëîïôûùü]+) ([\\wàâçéèêëîïôûùü]+)");
            m = p.matcher(lines.get(currentPos).trim());
            if(m.find()){
                parsedUser.setFirstname(m.group(1));
                parsedUser.setLastname(m.group(2));
            }
        }

        currentPos++;
        currentPos++;

        String roles = lines.get(currentPos);
        roles = roles.substring(1,roles.length());
        parsedUser.setRoles(Arrays.asList(roles.split("[-+*/,-]")).stream()
                .map(v -> v.trim()).collect(Collectors.toList()));

        currentPos++;

        if(!lines.get(currentPos).isEmpty() && lines.get(currentPos).contains("@")){
            parsedUser.setEmail(lines.get(currentPos));
        }

        currentPos++;
        currentPos++;
        currentPos++;

        while(!lines.get(currentPos).contains("--expertise-end")){
            if(!lines.get(currentPos).contains("--expertise") && !lines.get(currentPos).isEmpty()){
                parsedUser.getGlobalSkills().addAll(
                        Arrays.asList(lines.get(currentPos).split("[-+*/,-]")).stream()
                                .map(v -> v.trim()).collect(Collectors.toList())
                );
            }
            currentPos++;
        }


        while(currentPos <= lines.size() && !lines.get(currentPos).startsWith("# Langues")
                || !lines.get(currentPos).startsWith("# Loisirs")) {

            while (currentPos < lines.size() && !lines.get(currentPos).contains("--section-start")) {

                if(currentPos == lines.size() ||
                        lines.get(currentPos).startsWith("# Langues")
                        || lines.get(currentPos).startsWith("# Loisirs")
                        || lines.get(currentPos).trim().startsWith("# Loisirs")
                        || lines.get(currentPos).startsWith("# Formation suivis")){

                       if(lines.get(currentPos).startsWith("# Loisirs")
                               || lines.get(currentPos).trim().startsWith("# Loisirs")){
                            currentPos++;

                         while(currentPos < lines.size() && !lines.get(currentPos).startsWith("# Langues")){
                              currentPos++;
                             if(currentPos >= lines.size()){
                                 return parsedUser;
                             }
                             String val = lines.get(currentPos).replace("#", "").replace("*", "").trim();

                             if(!val.isEmpty()){
                                 parsedUser.getHobbies().add(val);
                             }
                           }
                        }

                    return parsedUser;
                }
                currentPos++;
            }

            if(currentPos >= lines.size()){
                return parsedUser;
            }

            currentPos++;

            while (!lines.get(currentPos).trim().startsWith("#")) {
                currentPos++;
            }
            if(lines.get(currentPos).startsWith("# Formation suivis")){
                //remplir les formations
                return parsedUser;
            }
            Mission mission = new Mission();
            mission.setRole(lines.get(currentPos).replace("#", "").trim());

            currentPos++;

            if(lines.get(currentPos).startsWith("## ")
                    || lines.get(currentPos).trim().startsWith("## ")) {
                mission.setLocation(lines.get(currentPos).replace("#", "").trim());
             }else{
                //pas de lieu
            }

            currentPos++;

            if (lines.get(currentPos).startsWith("### ") ||
                    lines.get(currentPos).trim().startsWith("### ")) {
                String fullDate = lines.get(currentPos).replace("#", "").trim();
                RangeDate rangeDateFromMission = createRangeDateFromMission(fullDate);
                if(rangeDateFromMission != null){
                    mission.setStartDate(rangeDateFromMission.getStartDate());
                    mission.setEndDate(rangeDateFromMission.getEndDate());
                }
                currentPos++;
            }else{
                //no date
            }

            while (!lines.get(currentPos).contains("--section-end")
                    && !lines.get(currentPos).trim().startsWith("# Loisirs")
                    && !lines.get(currentPos).trim().startsWith("# Langues")) {
                mission.setDescription(mission.getDescription() + lines.get(currentPos));
                currentPos++;
            }
            parsedUser.getMissions().add(mission);

        }


        return parsedUser;

    }

    private RangeDate createRangeDateFromMission(String fullDate) {

        // System.out.println(fullDate);
        fullDate = fullDate.replace(".","");

        DateTime dateTimeStart = createDateTimeInit();
        DateTime dateTimeEnd = createDateTimeInit();


        //Septembre 2014 - Décembre 2016
        Pattern p = Pattern.compile("(\\D+) (\\d+) [-a] (\\D+) (\\d+)");
        Matcher m = p.matcher(fullDate);

        if(m.find()){
            dateTimeStart = fillDateMonth(dateTimeStart,m.group(1));
            dateTimeStart = fillDateYear(dateTimeStart,m.group(2));
            dateTimeEnd = fillDateMonth(dateTimeEnd,m.group(3));
            dateTimeEnd = fillDateYear(dateTimeEnd,m.group(4));
            return new RangeDate(DateTimeFormat.forPattern(F).print(dateTimeStart),
                    DateTimeFormat.forPattern(F).print(dateTimeEnd));

        }


        //Octobre 2015 - Aujourd'hui
        //Octobre 2015 - now
        //...
        p = Pattern.compile("(\\D+) (\\d+) [-a] (\\D+)");
        m = p.matcher(fullDate);

        if(m.find()){
            dateTimeStart = fillDateMonth(dateTimeStart,m.group(1));
            dateTimeStart = fillDateYear(dateTimeStart,m.group(2));
            dateTimeEnd = fillDateMonth(dateTimeEnd,m.group(3));
            dateTimeEnd = fillDateYear(dateTimeEnd,m.group(3));
            return new RangeDate(DateTimeFormat.forPattern(F).print(dateTimeStart),
                    DateTimeFormat.forPattern(F).print(dateTimeEnd));
        }

        //Mars - Septembre 2010
        p = Pattern.compile("(\\D+) [-a] (\\D+) (\\d+)");
        m = p.matcher(fullDate);

        if(m.find()){
            dateTimeStart = fillDateMonth(dateTimeStart,m.group(1));
            dateTimeStart = fillDateYear(dateTimeStart,m.group(3));
            dateTimeEnd = fillDateMonth(dateTimeEnd,m.group(2));
            dateTimeEnd = fillDateYear(dateTimeEnd,m.group(3));
            return new RangeDate(DateTimeFormat.forPattern(F).print(dateTimeStart),
                    DateTimeFormat.forPattern(F).print(dateTimeEnd));
        }

        //Janvier 2017
            p = Pattern.compile("([a-zA-Zàâçéèêëîïôûùü]+)[^-](\\d+)$");
        m = p.matcher(fullDate);

        if(m.find()){
            dateTimeStart = fillDateMonth(dateTimeStart,m.group(1));
            dateTimeStart = fillDateYear(dateTimeStart,m.group(2));
            return new RangeDate(DateTimeFormat.forPattern(F).print(dateTimeStart),null);
        }


        //2012
        p = Pattern.compile("(\\d+)$");
        m = p.matcher(fullDate);

        if(m.find()){
            dateTimeStart = fillDateYear(dateTimeStart,m.group(1));
            dateTimeEnd = fillDateYear(dateTimeEnd,m.group(1));
            dateTimeEnd = dateTimeEnd.withMonthOfYear(12);
            return new RangeDate(DateTimeFormat.forPattern(F).print(dateTimeStart),
                    DateTimeFormat.forPattern(F).print(dateTimeEnd));
        }



        return null;
    }


    private DateTime fillDateMonth(DateTime dateTime,String val){

        if(TODAY_KEYWORD_DATE.contains(StringUtils.stripAccents(val.toLowerCase()))){
            return dateTime.withMonthOfYear(new DateTime().getMonthOfYear());
        }

        SimpleDateFormat sdfFullMonth = new SimpleDateFormat("MMMM");
        Calendar instance = Calendar.getInstance();

        try {
            Date dateMonth = sdfFullMonth.parse(val);
            // System.out.println(dateMonth.getMonth());
             instance.setTime(dateMonth);
            return dateTime.withMonthOfYear(instance.get(Calendar.MONTH)+1);
        } catch (ParseException e) {
            if(val.toLowerCase().contains("jan")){
                return dateTime.withMonthOfYear(1);
            }
            if(StringUtils.stripAccents(val.toLowerCase()).contains("fev")){
                return dateTime.withMonthOfYear(3);
            }
            if(val.toLowerCase().contains("mar")){
                return dateTime.withMonthOfYear(3);
            }
            if(val.toLowerCase().contains("avr")){
                return dateTime.withMonthOfYear(4);
            }
            if(val.toLowerCase().contains("mai")){
                return dateTime.withMonthOfYear(5);
            }
            if(val.toLowerCase().contains("juin")){
                return dateTime.withMonthOfYear(6);
            }
            if(val.toLowerCase().contains("juil")){
                return dateTime.withMonthOfYear(7);
            }
            if(StringUtils.stripAccents(val.toLowerCase()).contains("aou")){
                return dateTime.withMonthOfYear(8);
            }
            if(val.toLowerCase().contains("sept")){
                return dateTime.withMonthOfYear(9);
            }
            if(val.toLowerCase().contains("oct")){
                return dateTime.withMonthOfYear(10);
            }
            if(val.toLowerCase().contains("nov")){
                return dateTime.withMonthOfYear(11);
            }
            if(StringUtils.stripAccents(val.toLowerCase()).contains("dec")){
                return dateTime.withMonthOfYear(12);
            }
            return dateTime;
        }

    }


    private DateTime createDateTimeInit(){
         return new DateTime().withDayOfMonth(1).withMonthOfYear(1).withTime(0,0,0,0);
    }

    private DateTime fillDateYear(DateTime dateTime,String val) {

        if(TODAY_KEYWORD_DATE.contains(StringUtils.stripAccents(val.toLowerCase()))){
            return dateTime.withYear(new DateTime().getYear());
        }

        SimpleDateFormat sdfFullYear = new SimpleDateFormat("yyyy");

        try {
            Date dateYear = sdfFullYear.parse(val);
           //// System.out.println(dateYear.getYear());
            Calendar instance = Calendar.getInstance();
            instance.setTime(dateYear);
            return dateTime.withYear(instance.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return dateTime;
        }
    }

   // public static DateTime createDateFromDateISO8601(String date) throws ParseException {
    //    return new DateTime(dateISO8601.parse(date));
   // }
}
