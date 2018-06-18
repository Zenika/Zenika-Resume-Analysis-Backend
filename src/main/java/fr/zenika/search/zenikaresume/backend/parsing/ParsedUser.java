package fr.zenika.search.zenikaresume.backend.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jurio on 05/12/17.
 */
public class ParsedUser {

    private Integer nbAnneeExp;
    private String fullname;
    private String firstname;
    private String lastname;
    private String lastUpdate;
    private List<String> roles;
    private List<String> globalSkills = new ArrayList<>();
    private String email;
    private List<Mission> missions = new ArrayList<>();
    private String fullContent;
    private String identifierHostedSite;
    private List<String> hobbies = new ArrayList<>();
    private String idFunct;


    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getIdentifierHostedSite() {
        return identifierHostedSite;
    }

    public void setIdentifierHostedSite(String identifierHostedSite) {
        this.identifierHostedSite = identifierHostedSite;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getNbAnneeExp() {
        return nbAnneeExp;
    }

    public void setNbAnneeExp(Integer nbAnneeExp) {
        this.nbAnneeExp = nbAnneeExp;
    }


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getGlobalSkills() {
        return globalSkills;
    }

    public void setGlobalSkills(List<String> globalSkills) {
        this.globalSkills = globalSkills;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "ParsedUser{" +
                "nbAnneeExp=" + nbAnneeExp +
                ", fullname='" + fullname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", roles=" + roles +
                ", globalSkills=" + globalSkills +
                ", email='" + email + '\'' +
                ", missions=" + missions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParsedUser that = (ParsedUser) o;

        return fullname != null ? fullname.equals(that.fullname) : that.fullname == null;
    }

    @Override
    public int hashCode() {
        return fullname != null ? fullname.hashCode() : 0;
    }

    public void setIdFunct(String idFunct) {
        this.idFunct = idFunct;
    }

    public String getIdFunct() {
        return idFunct;
    }
}
