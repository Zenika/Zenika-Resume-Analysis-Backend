package fr.zenika.search.zenikaresume.backend.parsing;

import java.util.Date;

public class IdentityInfo {

    private String dateUpdate;
    private String functionalId;

    public IdentityInfo(String dateUpdate, String idFunct) {
        this.dateUpdate = dateUpdate;
        this.functionalId = idFunct;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public void setFunctionalId(String functionalId) {
        this.functionalId = functionalId;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public String getFunctionalId() {
        return functionalId;
    }
}
