package com.test.ertugrulemre.htmlparsing;

public class Comment {

    private String name;
    private String comment;
    private String rating;
    private String date;

    public Comment(String name, String comment, String rating, String date) {
        super();
        this.name = name;
        this.comment = comment;
        this.rating = rating;
        this.date = date;

    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String isComment() {
        return comment;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String isRating() {
        return rating;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String isDate() {
        return date;
    }
}
