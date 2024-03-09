package blog.sirico.Blog;

import java.time.*;

public class Comment {
    private String author;
    private String content;
    private LocalDate date;

    public Comment(String author, String content, LocalDate date){
        this.author = author;
        this.content = content;
        this.date = date;
    }
    
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public LocalDate getDate() {
        return date;
    }
    
}
