package com.xtracker.backend.jpa;

import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "public", catalog = "xtracker")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    private long userId;
    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 104857)
    private String email = "";
    @Basic
    @Column(name="name")
    private String name = "";
    @Basic
    @XmlTransient
    @Column(name = "private_key", nullable = true, insertable = true, updatable = true, length = 104857)
    private String privateKey = "";

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Track> tracks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private UserStat userStat;

    public User() {
        userStat = new UserStat();
    }

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


    public UserStat getUserStat() {
        return userStat;
    }

    public void setUserStat(UserStat userStat) {
        this.userStat = userStat;
    }

    @PrePersist
    public void initializeUserStat() {
        this.userStat.setUserId(userId);
    }
}
