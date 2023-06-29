package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Numbers {
    private String text;
    private int number;



    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }
}
