package com.theo.json.bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private List<String> phoneNumberList = new ArrayList<>();

    public Employee(){}

    public Employee(int id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void addPhoneNumber(String phoneNumber) {
        phoneNumberList.add(phoneNumber);
    }
    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id: " + id);
        stringBuilder.append(", firstName: " + firstName);
        stringBuilder.append(", lastName: " + lastName);
        stringBuilder.append(", birthDate: " + dateTimeFormatter.format(birthDate));
        stringBuilder.append(", phoneNumber:");

        for (String phoneNumber : phoneNumberList) {
            stringBuilder.append(" " + phoneNumber);
        }

        return stringBuilder.toString();
    }
}
