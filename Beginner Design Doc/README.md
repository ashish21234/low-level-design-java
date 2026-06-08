# Document Editor - Low Level Design

## Overview

This project demonstrates the design of a simple **Document Editor** using Object-Oriented Design principles and the **Strategy Pattern**.

The editor supports:

* Adding text
* Adding images
* Adding new lines
* Adding tab spaces
* Rendering the complete document
* Saving the document using different storage mechanisms

---

## Features

### Document Elements

The editor treats every part of the document as a `DocumentElement`.

Supported elements:

* Text
* Image
* New Line
* Tab Space

Each element is responsible for rendering itself.

---

## Class Responsibilities

### DocumentElement (Interface)

Defines the contract for all document elements.

```java
String render();
```

---

### TextElement

Represents text content.

Example:

```text
Hello World
```

---

### ImageElement

Represents an image inside the document.

Example:

```text
[Image: picture.jpg]
```

---

### NewLineElement

Represents a line break.

---

### TabSpaceElement

Represents a tab space.

---

### Document

Acts as a container for all document elements.

Responsibilities:

* Store document elements
* Add new elements
* Render complete document

---

### Persistence (Strategy Interface)

Defines how a document should be stored.

```java
void save(String data);
```

---

### FileStorage

Stores document content inside a file.

```text
document.txt
```

---

### DBStorage

Placeholder implementation for database storage.

Can be extended to save documents in:

* MySQL
* PostgreSQL
* MongoDB
* Redis

---

### DocumentEditor

Main class used by clients.

Responsibilities:

* Add content to document
* Render document
* Save document

Acts as a facade over the entire system.

---

## Design Patterns Used

### Strategy Pattern

The `Persistence` interface defines a family of storage algorithms.

Current implementations:

* FileStorage
* DBStorage

Storage strategy can be changed without modifying `DocumentEditor`.

```text
Persistence
    |
    +---- FileStorage
    |
    +---- DBStorage
```

---

### Composition

A `Document` is composed of multiple `DocumentElement` objects.

```text
Document
    |
    +---- TextElement
    +---- ImageElement
    +---- NewLineElement
    +---- TabSpaceElement
```

---

### Dependency Injection

Dependencies are injected through the constructor.

```java
DocumentEditor editor =
    new DocumentEditor(document, persistence);
```

This keeps the editor loosely coupled.

---

## Project Structure

```text
DocumentEditor/
│
├── DocumentEditorClient.java
├── uml.md
└── README.md
```

---

## Sample Output

```text
Hello, world!
This is a real-world document editor example.
    Indented text after a tab space.
[Image: picture.jpg]
```

Document is then saved to:

```text
document.txt
```

---

## Future Enhancements

* Rich Text Formatting
* Undo / Redo
* PDF Export
* Cloud Storage
* Database Persistence
* Document Versioning
* Search and Replace
* Collaborative Editing

---

## Learning Outcomes

This project demonstrates:

* Interfaces and Abstraction
* Composition
* Strategy Design Pattern
* Dependency Injection
* Separation of Concerns
* Extensible Low-Level Design

It serves as a beginner-friendly example of designing a scalable document editor system using Java.
