package org.example;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class Jokes {
    private String type;
    private String setup;
    private String punchline;



    public String getType() {
        return type;
    }

    public String getSetup() {
        return setup;
    }


    public String getPunchline() {
        return punchline;
    }

}
