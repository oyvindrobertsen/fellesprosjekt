package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


public class User implements Participant, Serializable {
    private String username;
    private String encodedPassword;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getId(){
        return this.username;
    }

    public String getUsername() {
        return username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets and encodes password.
     * @param password
     */
    public void setPasswordAndEncode(String password) {
        this.encodedPassword = new StandardPasswordEncoder().encode(password);
    }

    public void setPassword(String password) {
        this.encodedPassword = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns true by default for now, could be amended to allow for a user to disable
     * their account.
     * @return boolean
     */
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return this.username + ", " + this.firstName + ", " + this.lastName + ", " + this.email;
    }
}
