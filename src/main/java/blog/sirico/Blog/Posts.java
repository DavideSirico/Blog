package blog.sirico.Blog;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.time.*;

public class Posts implements Iterable<Post> {
    private List<Post> posts;
    
    public Posts(){
        posts = new ArrayList<Post>();
    }

    public void parseXML(String filename) {
        Document doc = null; 

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new File(filename));
        } catch (Exception e) {
            System.out.println("Error parsing XML file");
            e.printStackTrace();
            System.exit(-1);
        }
        @SuppressWarnings("null")
        Element root = doc.getDocumentElement();
        NodeList postNodes = root.getElementsByTagName("post");

        for(int i = 0; i < postNodes.getLength(); i++) {
            if(postNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            System.out.println("Parsing post " + i);
            Element postElement = (Element)postNodes.item(i);
            String id = Integer.toString(i);
            String title = postElement.getElementsByTagName("title").item(0).getTextContent();
            String content = postElement.getElementsByTagName("content").item(0).getTextContent();
            String category = postElement.getElementsByTagName("category").item(0).getTextContent();
            NodeList tagNodes = postElement.getElementsByTagName("tag");
            ArrayList<String> tags = new ArrayList<String>();
            for(int j = 0; j < tagNodes.getLength(); j++) {
                tags.add(tagNodes.item(j).getTextContent());
            }
            NodeList commentNodes = postElement.getElementsByTagName("comment");
            ArrayList<Comment> comments = new ArrayList<Comment>();
            for(int j = 0; j < commentNodes.getLength(); j++) {
                Element commentElement = (Element)commentNodes.item(j);
                String author = commentElement.getElementsByTagName("author").item(0).getTextContent();
                String comment = commentElement.getElementsByTagName("content").item(0).getTextContent();
                String email = commentElement.getElementsByTagName("email").item(0).getTextContent();
                String dateStr = commentElement.getElementsByTagName("date").item(0).getTextContent();
                LocalDate date = LocalDate.parse(dateStr);
                
                comments.add(new Comment(author, comment, email, date));
            }
            int views = Integer.parseInt(postElement.getElementsByTagName("views").item(0).getTextContent());
            String dateStr = postElement.getElementsByTagName("date").item(0).getTextContent();
            LocalDate date = LocalDate.parse(dateStr);
            posts.add(new Post(id, title, content, category, tags, comments, views, date));
        }

    }

    public void addPost(Post post){
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

    @Override
    public Iterator<Post> iterator() {
        return posts.iterator();
    }

}
