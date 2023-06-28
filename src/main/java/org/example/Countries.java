package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Countries {

    private String name;
    private String capital;
    private String region;
    private int population;


    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }


    public int getPopulation() {
        return population;
    }

    public String getRegion() {
        return region;
    }
}
