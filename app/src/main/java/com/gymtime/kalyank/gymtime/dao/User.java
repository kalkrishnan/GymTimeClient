package com.gymtime.kalyank.gymtime.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kalyanak on 8/24/2016.
 */
public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private List<Gym> favorites;

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.favorites = new ArrayList<Gym>(builder.favorites);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String email;
        private String password;
        private List<Gym> favorites;

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

        public Builder favorites(List<Gym> favorites) {
            this.favorites = new ArrayList<Gym>(favorites);
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

    public List<Gym> getFavorites() {
        return favorites;
    }
}
