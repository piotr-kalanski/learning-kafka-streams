package com.datawizards.kafka;

import java.util.Date;

public class CustomerBalance {
    private String name;
    private Double balance;
    private Date lastDate;

    public CustomerBalance(String name, Double balance, Date lastDate) {
        this.name = name;
        this.balance = balance;
        this.lastDate = lastDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}
