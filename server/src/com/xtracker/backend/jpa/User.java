package com.xtracker.backend.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "public", catalog = "xtracker")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    private long userId;
    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 104857)
    private String email = "";
    @Basic
    @Column(name = "private_key", nullable = true, insertable = true, updatable = true, length = 104857)
    private String privateKey = "";
    @OneToMany(mappedBy = "user")
    private List<Track> tracks = new ArrayList<>();

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            return user.getUserId() == this.getUserId();
        } else
            return false;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String private_key) {
        this.privateKey = private_key;
    }
}
