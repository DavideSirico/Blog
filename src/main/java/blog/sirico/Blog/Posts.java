package blog.sirico.Blog;

import java.util.*;


// This class represents a collection of posts 
// I've implemented the Iterable interface so that I can use the for-each loop to iterate over the posts
public class Posts implements Iterable<Post> {
    private List<Post> posts;

    public Posts() {
        posts = new ArrayList<>();
    }
    
    public void addPost(Post post){
        // I add the post to the list of posts and to the xml file
        posts.add(post);
    }

    public List<Post> getPosts(){
        return posts;
    }

    public Post getPost(String id){
        for(Post post : posts){
            if(post.getId().equals(id)){
                return post;
            }
        }
        return null;
    }
    
    public void addView(String id){
        Post post = getPost(id);
        if(post != null){
            post.addView();
        }
    }

    public void addComment(String id, Comment comment) {
        Post post = getPost(id);
        if(post != null){
            post.addComment(comment);
        }
    }

    // implementing the Iterable interface
    @Override
    public Iterator<Post> iterator() {
        return posts.iterator();
    }

    
    public void removePost(String id){
        Post post = getPost(id);
        if(post != null){
            posts.remove(post);
        }
    }

    public void editTitle(String id, String title){
        Post post = getPost(id);
        if(post != null){
            post.setTitle(title);
        }
    }

    public void editContent(String id, String content){
        Post post = getPost(id);
        if(post != null){
            post.setContent(content);
        }
    }
}
