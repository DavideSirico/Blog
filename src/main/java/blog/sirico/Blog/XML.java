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

                String id = postElement.getAttribute("id");
                String title = postElement.getElementsByTagName("title").item(0).getTextContent();
                String content = postElement.getElementsByTagName("content").item(0).getTextContent();

                NodeList commentNodes = postElement.getElementsByTagName("comment");
                ArrayList<Comment> comments = new ArrayList<Comment>();
                for(int j = 0; j < commentNodes.getLength(); j++) {
                    Element commentElement = (Element)commentNodes.item(j);
                    String author = commentElement.getElementsByTagName("author").item(0).getTextContent();
                    String comment = commentElement.getElementsByTagName("content").item(0).getTextContent();
                    String dateStr = commentElement.getElementsByTagName("date").item(0).getTextContent();
                    LocalDate date = LocalDate.parse(dateStr);
                    
                    comments.add(new Comment(author, comment, date));
                }

                int views = Integer.parseInt(postElement.getElementsByTagName("views").item(0).getTextContent());
                String dateStr = postElement.getElementsByTagName("date").item(0).getTextContent();
                LocalDate date = LocalDate.parse(dateStr);
                posts.add(new Post(id, title, content, comments, views, date));
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
            // create a comment element
            for (Comment comment : post.getComments()) {
                Element commentElement = doc.createElement("comment");
                Element authorElement = doc.createElement("author");
                authorElement.appendChild(doc.createTextNode(comment.getAuthor()));
                commentElement.appendChild(authorElement);
                Element commentContentElement = doc.createElement("content");
                commentContentElement.appendChild(doc.createTextNode(comment.getContent()));
                commentElement.appendChild(commentContentElement);
                Element dateElement = doc.createElement("date");
                dateElement.appendChild(doc.createTextNode(comment.getDate().toString()));
                commentElement.appendChild(dateElement);
                postElement.appendChild(commentElement);
            }
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
                Element commentElement = doc.createElement("comment");
                Element authorElement = doc.createElement("author");
                authorElement.appendChild(doc.createTextNode(comment.getAuthor()));
                commentElement.appendChild(authorElement);
                Element commentContentElement = doc.createElement("content");
                commentContentElement.appendChild(doc.createTextNode(comment.getContent()));
                commentElement.appendChild(commentContentElement);
                Element dateElement = doc.createElement("date");
                dateElement.appendChild(doc.createTextNode(comment.getDate().toString()));
                commentElement.appendChild(dateElement);
                postElement.appendChild(commentElement);
                System.out.println("Adding comment to post " + id);
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
    
    public void editTitle(String id, String title) {
        try {
            Element postElement = findPost(id);
            if (postElement != null) {
                postElement.getElementsByTagName("title").item(0).setTextContent(title);
                write();
            }
        } catch (Exception e) {
            System.out.println("Error editing the title in the post.xml file");
            e.printStackTrace();
        }
    }

    public void editContent(String id, String content) {
        try {
            Element postElement = findPost(id);
            if (postElement != null) {
                postElement.getElementsByTagName("content").item(0).setTextContent(content);
                write();
            }
        } catch (Exception e) {
            System.out.println("Error editing the content in the post.xml file");
            e.printStackTrace();
        }
    }

    public int getLastId() {
        NodeList nList = doc.getElementsByTagName("post");
        // get the last post element
        if(nList.getLength() > 0) {
            Element eElement = (Element)nList.item(nList.getLength() - 1);
            return Integer.parseInt(eElement.getAttribute("id"));
        }
        return -1;
    }
}