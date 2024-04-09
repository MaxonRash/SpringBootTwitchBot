package com.bot.springboottwitchbot.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "\"User\"")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "twitch_id")
    private int twitchId;

    @Column(name = "login")
    private String login;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "following_since")
    @Temporal(TemporalType.DATE)
    private Date followingSince;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(int twitchId) {
        this.twitchId = twitchId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getFollowingSince() {
        return followingSince;
    }

    public void setFollowingSince(Date followingSince) {
        this.followingSince = followingSince;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && twitchId == user.twitchId && login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, twitchId, login);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", twitchId=" + twitchId +
                ", login='" + login + '\'' +
                ", createdAt=" + createdAt +
                ", dateOfBirth=" + dateOfBirth +
                ", followingSince=" + followingSince +
                '}';
    }
}
