package com.amboucheba.soatp2.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "users") // create the table in public schema
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "Field 'username' is required")
    @Size( min = 6, max = 255, message = "Username length must be between 6 and 255")
    private String username;

    @Column(name = "email")
    @NotBlank(message = "Field 'email' is required")
    @Email
    private String email;

    @Column(name = "created_at")
    private Date created_at = new Date();

    public User(Long id, String username, String email, Date created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.created_at = created_at;
    }

    public User( String username,  String email) {
        this.username = username;
        this.email = email;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                username.equals(user.username) &&
                email.equals(user.email) &&
                Objects.equals(created_at, user.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, created_at);
    }
}









