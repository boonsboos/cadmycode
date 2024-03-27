package cgroup2.cadmycode.user;

import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.except.FieldValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class of the Domain
 */
public class User {

    private int userID;
    private String name;
    private String email;
    private String postCode;
    /**
     * use 2-letter ISO country codes
     */
    private String country;
    private String houseNumber;
    private LocalDate dateOfBirth;
    private Sex sex;

    /**
     * creates an instance of User
     * @param userID the ID of a user
     * @param name the name of the user
     * @param email the email of the user
     * @param postCode the address of a user
     * @param country the country of a user
     * @param houseNumber the house number of a user
     * @param dateOfBirth the date of birth of a user
     * @param sex the {@link Sex} of a user
     * @throws FieldValidationException if the postcode is invalid
     */

     public User(int userID,
                String name,
                String email,
                String postCode,
                String country,
                String houseNumber,
                LocalDate dateOfBirth,
                Sex sex
    ) throws FieldValidationException {
         if (!(checkPostcode(postCode))){
             throw new FieldValidationException("postcode onjuist ingevoerd");
         }
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.postCode = postCode;
        this.country = country;
        this.houseNumber = houseNumber;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * creates an instance of a User
     * @param name the name of the user
     *      * @param email the email of the user
     *      * @param postCode the address of a user
     *      * @param country the country of a user
     *      * @param houseNumber the house number of a user
     *      * @param dateOfBirth the date of birth of a user
     *      * @param sex the {@link Sex} of a user
     *      * @throws FieldValidationException if the postcode is invalid
     */
    public User(String name,
                String email,
                String postCode,
                String country,
                String houseNumber,
                LocalDate dateOfBirth,
                Sex sex) throws FieldValidationException {
       if (!(checkPostcode(postCode))){
           throw new FieldValidationException("postcode onjuist ingevoerd");
       };
        this.name = name;
        this.email = email;
        this.postCode = postCode;
        this.country = country;
        this.houseNumber = houseNumber;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    /**
     * checks if the postCode is correct
     * @param postCode the postCode to check
     * @return true if valid or false if invalid
     */
    public static boolean checkPostcode(String postCode) {
        return postCode.matches("[1-9]{1}[0-9]{3}[a-zA-Z]{2}");
    }

    /**
     * gets the userId
     * @return the userId
     */
    public int getUserID() {
        return userID;
    }

    /**
     * gets the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the email
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the postCode
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * gets the country
     * @return the country
     */
    public String getCountry() {
        return country;
    }
  
    /**
     * gets the houseNumber
     * @return
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * gets the date of birth
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * gets the sex
     * @return the sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * sets the name of the user
     * @param name the new name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the email of the user
     * @param email the new email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets the postCode of the user
     * @param postCode the new postCode of the user
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * sets the country of the user
     * @param country the new country of the user
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * sets the house number of the user
     * @param houseNumber the new house number of the user
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * sets the sex of the user
     * @param sex the new sex of the user
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
