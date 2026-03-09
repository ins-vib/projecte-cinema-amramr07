package com.daw.CinemaDaw.domain.movie;

public class New {
    private String headline;
    private String body;
    public String getHeadline() {
        return headline;
    }
    public void setHeadline(String headline) {
        this.headline = headline;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public New(String headline, String body) {
        this.headline = headline;
        this.body = body;
    }
    
}
