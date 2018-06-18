package fr.zenika.search.zenikaresume.backend.parsing;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by jurio on 07/12/17.
 */
public class Mission {

    private String role;
    private String location;
    private String startDate;
    private String endDate;
    private String description = "";


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "role='" + role + '\'' +
                ", location='" + location + '\'' +
                ", date='" + startDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
