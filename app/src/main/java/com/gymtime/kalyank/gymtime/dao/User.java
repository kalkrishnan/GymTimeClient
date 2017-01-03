package com.gymtime.kalyank.gymtime.dao;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by kalyanak on 8/24/2016.
 */
public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private Set<Gym> favorites;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.favorites = new HashSet<Gym>(builder.favorites);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String email;
        private String password;
        private Set<Gym> favorites;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder favorites(Set<Gym> favorites) {
            this.favorites = new HashSet<Gym>(favorites);
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Gym> getFavorites() {
        return favorites;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
