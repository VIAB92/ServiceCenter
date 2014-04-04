package com.sgsoft.servicer.entity;

/**
 * Created by Viktor Rotar on 03.04.14.
 */
public class State {
    private Integer id;
    private String name;
    private int rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
