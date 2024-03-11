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
- [ ] !Pagina per aggiungere i post (solo CSS)
- [X] Implementare la ricerca
- [ ] !Link che porta alla home


## Struttura del progetto
Il progetto utilizza il framework Spring Boot per la gestione del server e del client.
Il progetto è formato da 7 classi:
- `BlogApplication` è la classe principale del progetto e contiene il metodo main
- `BlogController` è la classe che gestisce le richieste HTTP e restituisce le pagine HTML al client
- `Comment` è la classe che rappresenta un commento
- `Post` è la classe che rappresenta un post
- `Posts` è la classe che rappresenta la lista dei post e contiene i metodi per gestire i post (aggiunta, rimozione, modifica, ricerca) e per salvare i post su un file XML
- `XML` è la classe che si occupa di leggere e scrivere i file XML
- `User` è la classe che rappresenta un utente

Nella cartella `resources` sono presenti le cartelle:
- `static` che contiene i file CSS e JS
- `templates` che contiene i file HTML
- `xml` che contiene il file XML e il file XSD





## Struttura del file XML
Il file XML è strutturato in questo modo:
```xml
<blog>
    <post id="0">
        <title>Titolo</title>
        <content>Contenuto</content>
        <author>Autore</author>
        <date>2021-12-12</date>
        <comment>
            <id>1</id>
            <author>Autore</author>
            <content>Contenuto</content>
            <date>2021-12-12</date>
        </comment>
    </post>
</blog>
```
Dove `blog` è l'elemento radice, `post` è un elemento figlio di `blog` e `comment` è un elemento figlio di `post`. L'elemento `post` ha un attributo `id` che è un intero.
Per ogni post ci possono essere più commenti.
E non ci possono essere due post con lo stesso id.


## Installazione
- scaricare l'ultima versione del file jar dalle release
- eseguire il file jar con il comando `java -jar nomefile.jar --xml.path="prova"`

## Sviluppo
Per sviluppare il progetto è necessario avere installato:
- Java 21
- Maven 3.8.1

Per eseguire il progetto in locale:
- Clonare il progetto
- Eseguire il comando `./mvnw clean install`
- Eseguire il comando `./mvnw spring-boot:run`
- Aprire il browser e andare all'indirizzo `http://localhost:8080`

Se si vuole abilitare il live reload:
- cambiare nel file `application.properties` la riga `spring.devtools.livereload.enabled = false` in `spring.devtools.livereload.enabled = true`, la riga `spring.devtools.restart.enabled = false` in `spring.devtools.restart.enabled = true` e la riga `spring.devtools.add-properties = false` in `spring.devtools.add-properties = true`

il file `application.properties` sarà così:
```properties
spring.thymeleaf.cache = true
spring.devtools.add-properties = false
spring.devtools.restart.enabled = false
spring.devtools.livereload.enabled = false
logging.level.web = DEBUG
```

## Contribuire
Per contribuire al progetto è necessario:
- Forkare il progetto
- Creare un branch con il proprio nome
- Eseguire il comando `git checkout -b nome-branch`
- Eseguire il comando `git commit -m "messaggio"`
- Eseguire il comando `git push origin nome-branch`
- Creare una pull request
- Attendere la revisione

