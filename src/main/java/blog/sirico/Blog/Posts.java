package blog.sirico.Blog;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.time.*;

public class Posts implements Iterable<Post> {
    private List<Post> posts;
    
    public Posts(){
        posts = new ArrayList<Post>();
    }

    public void parseXML(String filename) {
        try {
            // parse the XML file
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(filename));
        
            // get the root element
            Element root = doc.getDocumentElement();
            // get all the post elements
            NodeList postNodes = root.getElementsByTagName("post");

            // for each post element
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
        } catch (Exception e) {
            System.out.println("Error parsing XML file");
            e.printStackTrace();
        }
    }

    public void addPost(Post post){
        // add the post to the list
        posts.add(post);
        // add the post to the XML file
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.parse(new File("src/main/resources/xml/posts.xml"));

            Element root = doc.getDocumentElement();
            
            Element postElement = doc.createElement("post");
            postElement.setAttribute("id", post.getId());
            Element titleElement = doc.createElement("title");
            titleElement.setTextContent(post.getTitle());
            postElement.appendChild(titleElement);
            Element contentElement = doc.createElement("content");
            contentElement.setTextContent(post.getContent());
            postElement.appendChild(contentElement);
            Element categoryElement = doc.createElement("category");
            categoryElement.setTextContent(post.getCategory());
            postElement.appendChild(categoryElement);
            for(String tag : post.getTags()){
                Element tagElement = doc.createElement("tag");
                tagElement.setTextContent(tag);
                postElement.appendChild(tagElement);
            }
            for(Comment comment : post.getComments()){
                Element commentElement = doc.createElement("comment");

                Element authorElement = doc.createElement("author");
                authorElement.setTextContent(comment.getAuthor());
                commentElement.appendChild(authorElement);

                Element commentContentElement = doc.createElement("content");
                commentContentElement.setTextContent(comment.getContent());
                commentElement.appendChild(commentContentElement);
                
                Element emailElement = doc.createElement("email");
                emailElement.setTextContent(comment.getEmail());
                commentElement.appendChild(emailElement);
                
                Element dateElement = doc.createElement("date");
                dateElement.setTextContent(comment.getDate().toString());
                commentElement.appendChild(dateElement);
                
                postElement.appendChild(commentElement);
            }
            Element viewsElement = doc.createElement("views");
            viewsElement.setTextContent(Integer.toString(post.getViews()));
            postElement.appendChild(viewsElement);
            
            Element dateElement = doc.createElement("date");
            dateElement.setTextContent(post.getDate().toString());
            postElement.appendChild(dateElement);
            
            root.appendChild(postElement);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // TODO: fix indentation
            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/resources/xml/posts.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Error adding post to XML file");
            e.printStackTrace();
        }
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
