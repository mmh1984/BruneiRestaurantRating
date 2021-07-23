package com.example.maynard.bruneirestaurantrating;

/**
 * Created by Maynard on 4/8/2017.
 */

public class Reviews {
    String email;
    String rating;
    String comments;

    public Reviews(String email, String rating, String comments) {
        this.email = email;
        this.rating = rating;
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
