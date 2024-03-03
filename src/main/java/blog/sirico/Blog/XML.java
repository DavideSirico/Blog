package blog.sirico.Blog;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.time.*;

public class XML {

    private String filename;
    private Document doc;

    public XML(String filename) {
        this.filename = filename;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            this.doc = documentBuilder.parse(new File(filename));
        }
        catch (Exception e) {
            System.out.println("Error parsing XML file");
            e.printStackTrace();
        }
    }

    public ArrayList<Post> getPosts() {
        ArrayList<Post> posts = new ArrayList<Post>();
        try {
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
        return posts;
    }
    public void updateViews(String id, int views) {
        try {
            // Get the post with the matching id
            NodeList nList = doc.getElementsByTagName("post");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("id").equals(id)) {
                        // Update the views element
                        eElement.getElementsByTagName("views").item(0).setTextContent(Integer.toString(views));
                    }
                }
            }
            write();
        } catch (Exception e) {
            System.out.println("Error updating the post.xml file");
            e.printStackTrace();
        }
    }
    private void write() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Error writing the post.xml file");
            e.printStackTrace();
        }
    }
    public void addPost(Post post) {
        try {
            // get the root element
            Element root = doc.getDocumentElement();
            // create a new post element
            Element postElement = doc.createElement("post");
            postElement.setAttribute("id", post.getId());
            // create a title element
            Element titleElement = doc.createElement("title");
            titleElement.appendChild(doc.createTextNode(post.getTitle()));
            postElement.appendChild(titleElement);
            // create a content element
            Element contentElement = doc.createElement("content");
            contentElement.appendChild(doc.createTextNode(post.getContent()));
            postElement.appendChild(contentElement);
            // create a category element
            Element categoryElement = doc.createElement("category");
            categoryElement.appendChild(doc.createTextNode(post.getCategory()));
            postElement.appendChild(categoryElement);
            // create a tags element
            Element tagsElement = doc.createElement("tags");
            for(String tag : post.getTags()) {
                Element tagElement = doc.createElement("tag");
                tagElement.appendChild(doc.createTextNode(tag));
                tagsElement.appendChild(tagElement);
            }
            postElement.appendChild(tagsElement);
            // create a comments element
            Element commentsElement = doc.createElement("comments");
            for(Comment comment : post.getComments()) {
                Element commentElement = doc.createElement("comment");
                // create an author element
                Element authorElement = doc.createElement("author");
                authorElement.appendChild(doc.createTextNode(comment.getAuthor()));
                commentElement.appendChild(authorElement);
                // create a content element
                Element commentContentElement = doc.createElement("content");
                commentContentElement.appendChild(doc.createTextNode(comment.getContent()));
                commentElement.appendChild(commentContentElement);
                // create an email element
                Element emailElement = doc.createElement("email");
                emailElement.appendChild(doc.createTextNode(comment.getEmail()));
                commentElement.appendChild(emailElement);
                // create a date element
                Element dateElement = doc.createElement("date");
                dateElement.appendChild(doc.createTextNode(comment.getDate().toString()));
                commentElement.appendChild(dateElement);
                commentsElement.appendChild(commentElement);
            }
            postElement.appendChild(commentsElement);
            // create a views element
            Element viewsElement = doc.createElement("views");
            viewsElement.appendChild(doc.createTextNode(Integer.toString(post.getViews())));
            postElement.appendChild(viewsElement);
            // create a date element
            Element dateElement = doc.createElement("date");
            dateElement.appendChild(doc.createTextNode(post.getDate().toString()));
            postElement.appendChild(dateElement);
            // append the post element to the root element
            root.appendChild(postElement);
            write();
        } catch (Exception e) {
            System.out.println("Error adding a post to the post.xml file");
            e.printStackTrace();
        }
    }
    public void addView(Post post) {
        try {
            Element postElement = findPost(post.getId());
            if (postElement != null) {
                int views = post.getViews();
                views++;
                postElement.getElementsByTagName("views").item(0).setTextContent(Integer.toString(views));
                write();
            }
        } catch (Exception e) {
            System.out.println("Error adding a view to the post.xml file");
            e.printStackTrace();
        }
    }
    public void addView(String id) {
        try {
            Element postElement = findPost(id);
            if (postElement != null) {
                int views = Integer.parseInt(postElement.getElementsByTagName("views").item(0).getTextContent());
                views++;
                postElement.getElementsByTagName("views").item(0).setTextContent(Integer.toString(views));
                write();
            }
        } catch (Exception e) {
            System.out.println("Error adding a view to the post.xml file");
            e.printStackTrace();
        }
    }

    public void addComment(String id, Comment comment) {
        try {
            Element postElement = findPost(id);
            if (postElement != null) {
                Element commentsElement = (Element)postElement.getElementsByTagName("comments").item(0);
                Element commentElement = doc.createElement("comment");
                // create an author element
                Element authorElement = doc.createElement("author");
                authorElement.appendChild(doc.createTextNode(comment.getAuthor()));
                commentElement.appendChild(authorElement);
                // create a content element
                Element commentContentElement = doc.createElement("content");
                commentContentElement.appendChild(doc.createTextNode(comment.getContent()));
                commentElement.appendChild(commentContentElement);
                // create an email element
                Element emailElement = doc.createElement("email");
                emailElement.appendChild(doc.createTextNode(comment.getEmail()));
                commentElement.appendChild(emailElement);
                // create a date element
                Element dateElement = doc.createElement("date");
                dateElement.appendChild(doc.createTextNode(comment.getDate().toString()));
                commentElement.appendChild(dateElement);
                commentsElement.appendChild(commentElement);
                
                write();
            }
        } catch (Exception e) {
            System.out.println("Error adding a view to the post.xml file");
            e.printStackTrace();
        }
    }

    public void removePost(String id) {
        try {
            Element postElement = findPost(id);
            if (postElement != null) {
                postElement.getParentNode().removeChild(postElement);
                write();
            }
        } catch (Exception e) {
            System.out.println("Error removing a post from the post.xml file");
            e.printStackTrace();
        }
    }


    private Element findPost(String id) {
        NodeList nList = doc.getElementsByTagName("post");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("id").equals(id)) {
                    return eElement;
                }
            }
        }
        return null;
    }
}