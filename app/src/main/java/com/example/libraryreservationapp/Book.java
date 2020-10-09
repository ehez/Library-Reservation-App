package com.example.libraryreservationapp;

class Book
{
    //private member variables
    public String course;
    public String title;
    public String author;
    public String isbn;
    //empty constructor
    public Book()
    {

    }

    //constructor for Book
    public Book(String course, String title, String author, String isbn)
    {
        this.course = course;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    //gets course
    public String getCourse() {
        return course;
    }

    //sets course
    public void setCourse(String course) {
        this.course = course;
    }

    //gets title
    public String getTitle() {
        return title;
    }

    //sets title
    public void setTitle(String title) {
        this.title = title;
    }

    //gets author
    public String getAuthor() {
        return author;
    }

    //sets author
    public void setAuthor(String author) {
        this.author = author;
    }

    //gets isbn
    public String getIsbn() {
        return isbn;
    }

    //sets isbn
    public void setISBN(String isbn) { this.isbn = isbn; }


}


