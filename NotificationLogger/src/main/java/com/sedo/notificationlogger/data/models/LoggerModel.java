package com.sedo.notificationlogger.data.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.qbusict.cupboard.annotation.Index;

public class LoggerModel {

    public enum Status {
        Requested(1),
        Complete(2),
        Failed(3);

        private int typeID;

        Status(int i) {
            this.typeID = i;
        }
        public static Status fromInt(int i) {
            for (Status type : Status.values()) {
                if (type.getMode() == i) {
                    return type;
                }
            }
            return null;
        }

        public int getMode() {
            return typeID;
        }
    }

    public static final String[] PARTIAL_PROJECTION = new String[] {
            "_id",
            "date",
            "title",
            "subtitle",
            "description",
            "status",
            "key",
            "data",
            "keyToSearch",
    };

    private static final SimpleDateFormat TIME_ONLY_FMT = new SimpleDateFormat("HH:mm:ss", Locale.US);

    private Long _id;
    private String title;
    private String subtitle;
    private String description;
    private String key;
    private String data;
    private String status;
    private String keyToSearch;
    @Index
    private Date date;

    public Long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return (status);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getNotificationText() {
        return title;
    }

    public String getRequestStartTimeString() {
        return (date != null) ? TIME_ONLY_FMT.format(date) : null;
    }

    public String getRequestEndTimeString() {
        return (date != null) ? TIME_ONLY_FMT.format(date) : null;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getDurationString() {
        return "0";
    }

    public String getKey_to_search() {
        return keyToSearch;
    }

    public void setKeyToSearch(String keyToSearch) {
        this.keyToSearch = keyToSearch;
    }
}
