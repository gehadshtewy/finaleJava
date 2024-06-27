package com.example.finalejava.data;

public class Favorite {
   private Root root;
    private String userId;

    public Favorite(Root root, String userId) {
        this.root = root;
        this.userId = userId;
    }

    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
