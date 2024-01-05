package cgroup2.cadmycode.user;

import cgroup2.cadmycode.content.Course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int userID;
    private String name;
    private String email;
    private String address;
    /**
     * use 2-letter ISO country codes
     */
    private String country;
    private String city;
    private LocalDate dateOfBirth;
    private Sex sex;


    // courses list
    private List<Course> courses = new ArrayList<Course>();

    // om de user uit de database te halen
    public User(int userID,
                String name,
                String email,
                String address,
                String country,
                String city,
                LocalDate dateOfBirth,
                Sex sex
    ) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.address = address;
        this.country = country;
        this.city = city;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public User(String name,
                String email,
                String address,
                String country,
                String city,
                LocalDate dateOfBirth,
                Sex sex) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.country = country;
        this.city = city;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void addCourse(Course c) {
        this.courses.add(c);
    }

    public void removeCourse(Course c) {
        this.courses.remove(c);
    }
}
