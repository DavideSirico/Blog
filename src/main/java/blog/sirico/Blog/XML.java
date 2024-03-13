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

    public XML(String filename) {
        this.filename = filename;
    }

    public void write(Posts posts) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.getDocumentElement();
            if (root == null) {
                root = doc.createElement("blog");
                doc.appendChild(root);
            }

            for (Post post : posts) {
                Element postElement = doc.createElement("post");
                postElement.setAttribute("id", post.getId());
                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(post.getTitle()));
                postElement.appendChild(title);
                Element content = doc.createElement("content");
                content.appendChild(doc.createTextNode(post.getContent()));
                postElement.appendChild(content);
                Element date = doc.createElement("date");
                date.appendChild(doc.createTextNode(post.getDate().toString()));
                postElement.appendChild(date);
                Element views = doc.createElement("views");
                views.appendChild(doc.createTextNode(Integer.toString(post.getViews())));
                postElement.appendChild(views);
                Element comments = doc.createElement("comments");
                for (Comment comment : post.getComments()) {
                    Element commentElement = doc.createElement("comment");
                    commentElement.setAttribute("author", comment.getAuthor());
                    commentElement.appendChild(doc.createTextNode(comment.getContent()));
                    comments.appendChild(commentElement);
                }
                postElement.appendChild(comments);
                root.appendChild(postElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    public Posts read() {
        Posts posts = new Posts();
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList postNodes = doc.getElementsByTagName("post");
            for (int i = 0; i < postNodes.getLength(); i++) {
                Node postNode = postNodes.item(i);
                if (postNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element postElement = (Element) postNode;
                    String id = postElement.getAttribute("id");
                    String title = postElement.getElementsByTagName("title").item(0).getTextContent();
                    String content = postElement.getElementsByTagName("content").item(0).getTextContent();
                    LocalDate date = LocalDate.parse(postElement.getElementsByTagName("date").item(0).getTextContent());
                    int views = Integer.parseInt(postElement.getElementsByTagName("views").item(0).getTextContent());
                    NodeList commentNodes = postElement.getElementsByTagName("comment");
                    ArrayList<Comment> comments = new ArrayList<>();
                    for (int j = 0; j < commentNodes.getLength(); j++) {
                        Node commentNode = commentNodes.item(j);
                        if (commentNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element commentElement = (Element) commentNode;
                            String author = commentElement.getAttribute("author");
                            String comment = commentElement.getTextContent();
                            comments.add(new Comment(author, comment, LocalDate.now()));
                        }
                    }
                    posts.addPost(new Post(id, title, content, comments, views, date));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public int getLastId() {
        int last_id = 0;
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList postNodes = doc.getElementsByTagName("post");
            for (int i = 0; i < postNodes.getLength(); i++) {
                Node postNode = postNodes.item(i);
                if (postNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element postElement = (Element) postNode;
                    int id = Integer.parseInt(postElement.getAttribute("id"));
                    if (id > last_id) {
                        last_id = id;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return last_id;
    }
}

    