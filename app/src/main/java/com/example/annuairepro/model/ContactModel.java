package com.example.annuairepro.model;

import java.io.Serializable;

public class ContactModel implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private String job;


    public ContactModel(String firstName, String lastName, String number, String email, String job) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.email = email;
        this.job = job;
    }
    public ContactModel(Integer id, String firstName, String lastName, String number, String email, String job) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.email = email;
        this.job = job;
    }

    public ContactModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getJob() {
        return job;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
