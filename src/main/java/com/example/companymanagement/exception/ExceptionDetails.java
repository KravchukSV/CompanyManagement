package com.example.companymanagement.exception;

import java.util.Date;

public class ExceptionDetails {
    private Date date;
    private String messageException;
    private String detailsException;

    public ExceptionDetails(Date date, String messageException, String detailsException) {
        this.date = date;
        this.messageException = messageException;
        this.detailsException = detailsException;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageException() {
        return messageException;
    }

    public void setMessageException(String messageException) {
        this.messageException = messageException;
    }

    public String getDetailsException() {
        return detailsException;
    }

    public void setDetailsException(String detailsException) {
        this.detailsException = detailsException;
    }
}
