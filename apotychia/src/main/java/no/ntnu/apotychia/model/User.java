package no.ntnu.apotychia.model;

//import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class User {
    private final long userID;
    private String username;
    private String encodedPassword;
    private String firstName;
    private String lastName;
    private String email;

    public User(long id) {
        this.userID = id;
    }

    public long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

//    /**
//     * Sets and encodes password. Use StandardPasswordEncoder.matches() to compare.
//     * @param password
//     */
//    public void setPassword(String password) {
//        this.encodedPassword = new StandardPasswordEncoder().encode(password);
//    }

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
}
