package com.xtracker.jpa;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "xtracker", catalog = "x-tracker")
public class UsersEntity {
    private long userId;
    private String token;
    private String email;
    private Collection<TracksEntity> tracksesByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "token", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (userId != that.userId) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<TracksEntity> getTracksesByUserId() {
        return tracksesByUserId;
    }

    public void setTracksesByUserId(Collection<TracksEntity> tracksesByUserId) {
        this.tracksesByUserId = tracksesByUserId;
    }
}
