Consegna:
1. Struttura Server-Multiclient TCP in cui il client può collegarsi al server per richiedere
un servizio. Questo servizio potrebbe essere una raccolta di dati particolari, tipo
archivio libri, documenti aziendali, videoteche, oppure iscrizioni a corsi online ecc.
2. Ci deve essere un file XML con all’interno dei dati. Il documento deve avere le
conseguenti caratteristiche:
a. almeno 3 livelli oltre l’elemento radice,
b. almeno una restrizione per un tipo di elemento
c. almeno un elemento deve avere un attributo
3. Ci deve comunque essere un file XSD che valida il file XML.
4. Il client ha la possibilità di richiedere la visualizzazione di un tipo di elemento del file
XML al server (Es: chiedo l’elemento titolo e il server risponde con tutti i titoli dei
libri/album). Il server creerà un nuovo file XML con elemento radice = <richiesta>,
che avrà come figli i contenuti dell’elemento richiesto.
(Es
<richiesta>:
<titolo>ABC</titolo>
<titolo>DCE</titolo>
……
</richiesta>
5. Il client può aggiungere dati alla raccolta del file XML (Es: aggiungo un nuovo libro),
che dovrà comunque rispettare la struttura e le eventuali restrizioni dettate dallo
schema. Il server dovrà poi salvare il documento modificato.

Blog: Gestione di articoli e commenti.

TODO:
- [X] Creare un file XML con almeno 3 livelli oltre l'elemento radice
- [X] Creare un file XSD che valida il file XML
- [X] Implementare la richiesta di visualizzazione di un tipo di elemento del file XML
- [X] Aggiungere login
- [X] !Modifica dei post
- [X] CSS
- [X] Rimozione dei post
~~- [X] !Aggiunta di categorie e tag~~
- [X] !Aggiunta di commenti
- [X] Ricerca dei post
- [X] Paginazione
- [X] !Link che portano al post
- [X] Pagina per modificare i post
- [X] !Pagina per aggiungere i post (solo CSS)
- [X] Implementare la ricerca
- [ ] !Link che porta alla home

Determining workspace structure

Deciding which workspace information to collect

Gathering workspace info

# Blog Application

This is a simple blog application built with Spring Boot and Maven.



## Project structure
The project uses the Spring Boot framework to manage the server and client.
The project is made up of 7 classes:
- `BlogApplication` is the main class of the project and contains the main method
- `BlogController` is the class that handles HTTP requests and returns HTML pages to the client
- `Comment` is the class that represents a comment
- `Post` is the class that represents a post
- `Posts` is the class that represents the list of posts and contains the methods to manage posts (add, remove, edit, search) and to save posts to an XML file
- `XML` is the class that reads and writes XML files
- `WebSecurityConfig` is the class that handles the security of the application such as login and logout

In the `resources` folder there are the folders:
- `static` which contains the CSS and JS files
- `templates` which contains the HTML files
- `xml` which contains the XML file and the XSD file
- `application.properties` which contains the configuration of the application

## XML file structure
The XML file is structured as follows:
```xml
<blog>
    <post id="0">
        <title>Title</title>
        <content>Content</content>
        <author>Author</author>
        <date>2021-12-12</date>
        <comment>
            <author>Author</author>
            <content>Content of the comment</content>
            <date>2021-12-12</date>
        </comment>
    </post>
</blog>
```
Where `blog` is the root element, `post` is a child element of `blog` and `comment` is a child element of `post`. The `post` element has an `id` attribute that is an integer.
For each post there can be multiple comments but there can't be two posts with the same id.

## Building 
To build the project you need to have installed:
- Java 21
- Maven 3.8.1

To build the project:
- Clone the project
- Run the command `./mvnw verify`
This command compiles the code, runs the tests, and packages the resulting binaries in a JAR file.


## Running
To run the project you need to have installed:
- Java 21
- The JAR file built in the previous step or downloaded from the release

To run the project:
- Run the command `java -jar Blog.jar --xml.path="path"` where `path` is the path of the XML file
Or:
- Run the command `./mvnw spring-boot:run --xml.path="path"` where `path` is the path of the XML file
The server will start and you can access it by opening a browser and going to the address `http://localhost:8080`


## Development
To run the project locally:
- Clone the project
- Run the command `./mvnw clean install`
- Run the command `./mvnw spring-boot:run`
- Open the browser and go to the address `http://localhost:8080`


### Application Properties
The application.properties file in the src/main/resources directory contains configuration properties for the application. You can modify this file to change the application's configuration.

### Static Resources
The src/main/resources/static directory contains static resources for the application, such as CSS files and icons.

### Templates
The src/main/resources/templates directory contains the HTML templates for the application's views.

### XML Data
The src/main/resources/xml directory contains XML data used by the application. The posts.xml file contains the blog posts, and the schema.xsd file contains the XML schema for the blog posts.


## Contributing
For contributing to the project it is necessary to:
- Fork the project
- Create a branch with your name
- Run the command `git checkout -b name-branch`
- Run the command `git commit -m "message"`
- Run the command `git push origin name-branch`
- Create a pull request
- Wait for the review


## To create a new release (for maintainers only (litteraly only me))
To create a new release:
- Create a new tag with the command `git tag -a v1.0.0 -m "Release 1.0.0"`
- Push the tag with the command `git push origin v1.0.0`