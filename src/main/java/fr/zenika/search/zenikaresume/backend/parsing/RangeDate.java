package fr.zenika.search.zenikaresume.backend.parsing;

import org.joda.time.DateTime;


public class RangeDate {

    private String startDate;
    private String endDate;

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

    public RangeDate(String startDate, String endDate) {

        this.startDate = startDate;
        this.endDate = endDate;
    }
}
