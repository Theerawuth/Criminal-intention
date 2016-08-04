package com.augmentis.ayp.crimin;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.UUID;

/**
 * Created by Theerawuth on 7/18/2016.
 */
public class Crime {
    private UUID id;
    private String title;
    private Date crimeDate;
    private boolean solved;
    private String suspect;

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid){
        this.id = uuid;
        crimeDate = new Date();

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCrimeDate() {

        return crimeDate;

    }

    public void setCrimeDate(Date crimeDate) {
        this.crimeDate = crimeDate;
    }

    public boolean getSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    ///

    public String getPhotoFilename(){
        return  "IMF_" + getId().toString() + " .jpg";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UUID=").append(id);
        builder.append(",Title=").append(title);
        builder.append(",Crime Date=").append(crimeDate);
        builder.append(", Solved=").append(solved);
        builder.append(", Suspect=").append(suspect);
        return builder.toString();
    }
}
