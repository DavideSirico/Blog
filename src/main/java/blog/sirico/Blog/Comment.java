package blog.sirico.Blog;

import java.time.*;

public class Comment {
    private String author;
    private String content;
    private String email;
    private LocalDate date;

    public Comment(String author, String content, String email, LocalDate date){
        this.author = author;
        this.content = content;
        this.email = email;
        this.date = date;
    }
    
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public String getEmail() {
        return email;
    }
    public LocalDate getDate() {
        return date;
    }
    
}
