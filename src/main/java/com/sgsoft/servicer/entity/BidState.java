package com.sgsoft.servicer.entity;

import java.util.Date;

/**
 * Created by Viktor Rotar on 03.04.14.
 */
public class BidState {
    private State state;
    private Date dateChanged;
    private User userChanged;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public User getUserChanged() {
        return userChanged;
    }

    public void setUserChanged(User userChanged) {
        this.userChanged = userChanged;
    }
}
