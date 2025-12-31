package com.example.addishiwot;

public class Pharmacy {
    private String name;
    private String username;
    private String password;
    private boolean isActive;

    public Pharmacy(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.isActive = false; // Default to inactive
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return name + (isActive ? "" : " [INACTIVE]");
    }
}
