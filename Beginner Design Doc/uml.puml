# Document Editor UML

```mermaid
classDiagram

class DocumentElement {
    <<interface>>
    +render() String
}

class TextElement {
    -text : String
    +render() String
}

class ImageElement {
    -imagePath : String
    +render() String
}

class NewLineElement {
    +render() String
}

class TabSpaceElement {
    +render() String
}

class Document {
    -documentElements : List~DocumentElement~
    +addElement(DocumentElement)
    +render() String
}

class Persistence {
    <<interface>>
    +save(String)
}

class FileStorage {
    +save(String)
}

class DBStorage {
    +save(String)
}

class DocumentEditor {
    -document : Document
    -storage : Persistence
    -renderedDocument : String

    +addText(String)
    +addImage(String)
    +addNewLine()
    +addTabSpace()
    +renderDocument() String
    +saveDocument()
}

class DocumentEditorClient

DocumentElement <|.. TextElement
DocumentElement <|.. ImageElement
DocumentElement <|.. NewLineElement
DocumentElement <|.. TabSpaceElement

Persistence <|.. FileStorage
Persistence <|.. DBStorage

Document *-- DocumentElement
DocumentEditor --> Document
DocumentEditor --> Persistence
DocumentEditorClient --> DocumentEditor
```

## Design Patterns Used

### Strategy Pattern

* `Persistence` interface defines the saving strategy.
* `FileStorage` and `DBStorage` provide different implementations.

### Composition

* `Document` is composed of multiple `DocumentElement` objects.

### Dependency Injection

* `DocumentEditor` receives `Document` and `Persistence` through its constructor.

### Polymorphism

* Different document elements (`TextElement`, `ImageElement`, etc.) implement the same `DocumentElement` interface.
