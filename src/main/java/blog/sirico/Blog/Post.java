package blog.sirico.Blog;

import java.util.*;
import java.time.*;

public class Post {
    private String id;
    private String category;
    private ArrayList<String> tags;
    private String title;
    private String content;
    private ArrayList<Comment> comments;
    private int views;
    private LocalDate date;

    public Post(String id){
        this.id = id;
        this.category = "";
        this.tags = new ArrayList<String>();
        this.title = "";
        this.content = "";
        this.comments = new ArrayList<Comment>();
        this.views = 0;
        this.date = null;
    }
    
    public Post(String id, String title, String content, String category, ArrayList<String> tags, ArrayList<Comment> comments, int views, LocalDate date){
        this.id = id;
        this.category = category;
        this.tags = tags;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.views = views;
        this.date = date;
    }

    public void addView(){
        this.views++;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }

    public String getId(){
        return this.id;
    }

    public String getCategory(){
        return this.category;
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }

    public ArrayList<Comment> getComments(){
        return this.comments;
    }

    public int getViews(){
        return this.views;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public String getDescription(){
        return this.content.substring(0, Math.min(this.content.length(), 150));
    }


}
