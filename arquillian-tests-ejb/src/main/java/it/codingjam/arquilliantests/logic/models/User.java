package it.codingjam.arquilliantests.logic.models;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 */
@Entity
@Table(name = "users")
@NamedQuery(name = User.COUNT_USERS, query = "select count(u) from User u")
public class User {

    public static final String COUNT_USERS = "Users.count";

    @Id
    @NotNull
    @Column(name = "user_name")
    private String userName;

    @NotNull
    private String password;


    private String firstName;

    private String lastName;

    public User() {
        // empty contructor for JPA
    }

    public User(String userName) {
        this.userName = userName;
    }

    @PrePersist
    void encryptPassword() {
        this.password = DigestUtils.sha1Hex(this.password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return userName.equals(user.userName);

    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
}
