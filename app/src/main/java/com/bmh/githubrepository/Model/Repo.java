package com.bmh.githubrepository.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Repo {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("fork")
    @Expose
    private boolean fork;

    @SerializedName("owner")
    @Expose
    private Owner owner;

    @SerializedName("html_url")
    @Expose
    private String HTML_URL;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }
    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getHTML_URL() {
        return HTML_URL;
    }

    public void setHTML_URL(String HTML_URL) {
        this.HTML_URL = HTML_URL;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public class Owner {
        @SerializedName("login")
        private String login;

        @SerializedName("html_url")
        private String ownerURL;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getOwnerURL() {
            return ownerURL;
        }

        public void setOwnerURL(String ownerURL) {
            this.ownerURL = ownerURL;
        }

    }
}
