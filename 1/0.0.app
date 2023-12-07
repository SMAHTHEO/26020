#include "library.h"

#include <cstdio>
#include <cstring>

#include <fcntl.h>
#include <unistd.h>

//==============================================================================

// Default Constructor
Document::Document() { }

// Copy Constructor
Document::Document(const Document& doc) {
  _title = new char[strlen(doc._title) + 1];
  strcpy(_title, doc._title);
  _quantity = doc._quantity;
}

// Copy Assignment Operator
Document& Document::operator=(const Document& doc) {
  delete[] _title;
  _title = new char[strlen(doc._title) + 1];
  strcpy(_title, doc._title);
  _quantity = doc._quantity;
  return *this;
}

// Move Constructor
Document::Document(Document&& doc) {
  _title = doc._title;
  _quantity = doc._quantity;
  doc._title = NULL;
}

// Move Assignment Operator
Document& Document::operator=(Document&& doc) {
  delete[] _title;
  _title = doc._title;
  _quantity = doc._quantity;
  doc._title = NULL;
  return *this;
}

Document::~Document() { }

void Document::updateTitle(const char *newTitle) {
  delete[] _title;
  _title = new char[strlen(newTitle) + 1];
  strcpy(_title, newTitle);
}
void Document::updateQuantity(int newQuantity) { _quantity = newQuantity; }

const char *Document::getTitle() { return _title; }
int Document::getQuantity() { return _quantity; }

int Document::borrowDoc() {
  if (_quantity > 0) {
    _quantity--;
    return 1;
  }
  return 0;
}

void Document::returnDoc() { _quantity++; }

//==============================================================================

Novel::Novel(const char *title, const char *author, int quantity) {
  _title = new char[strlen(title) + 1];
  _author = new char[strlen(author) + 1];
  strcpy(_title, title);
  strcpy(_author, author);
  _quantity = quantity;
}

// Explicit Copy Constructor
Novel::Novel(const Novel& n)  {
  _title = new char[strlen(n._title) + 1];
  _author = new char[strlen(n._author) + 1];
  strcpy(_title, n._title);
  strcpy(_author, n._author);
  _quantity = n._quantity;
}

// Explicit Copy Assignment Operator
Novel& Novel::operator=(const Novel& n) {
  delete[] _title;
  delete[] _author;

  _title = new char[strlen(n._title) + 1];
  _author = new char[strlen(n._author) + 1];
  strcpy(_title, n._title);
  strcpy(_author, n._author);
  _quantity = n._quantity;
  return *this;
}

// Explicit Move Constructor
Novel::Novel(Novel&& n) {
  _title = n._title;
  _author = n._author;
  _quantity = n._quantity;
  n._title = NULL;
  n._author = NULL;
}

// Explicit Move Assignment Operator
Novel& Novel::operator=(Novel&& n) {
  delete[] _title;
  delete[] _author;

  _title = n._title;
  _author = n._author;
  _quantity = n._quantity;
  n._title = NULL;
  n._author = NULL;
  return *this;
}

Novel::~Novel() {
  delete[] _title;
  delete[] _author;
}

DocType Novel::getDocType() { return DOC_NOVEL; }

void Novel::print() {
  printf("Novel, title: %s, author: %s, quantity: %d\n", _title, _author,
         _quantity);
}

void Novel::updateAuthor(const char *newAuthor) {
  delete[] _author;
  _author = new char[strlen(newAuthor) + 1];
  strcpy(_author, newAuthor);
}

const char *Novel::getAuthor() { return _author; }

//==============================================================================

Comic::Comic(const char *title, const char *author, int issue, int quantity) {
  _title = new char[strlen(title) + 1];
  _author = new char[strlen(author) + 1];
  strcpy(_title, title);
  strcpy(_author, author);
  _quantity = quantity;
  _issue = issue;
}

// Explicit Copy Constructor
Comic::Comic(const Comic& c)  {
  _title = new char[strlen(c._title) + 1];
  _author = new char[strlen(c._author) + 1];
  strcpy(_title, c._title);
  strcpy(_author, c._author);
  _quantity = c._quantity;
  _issue = c._issue;
}

// Explicit Copy Assignment Operator
Comic& Comic::operator=(const Comic& c) {
  delete[] _title;
  delete[] _author;

  _title = new char[strlen(c._title) + 1];
  _author = new char[strlen(c._author) + 1];
  strcpy(_title, c._title);
  strcpy(_author, c._author);
  _quantity = c._quantity;
  _issue = c._issue;
  return *this;
}

// Explicit Move Constructor
Comic::Comic(Comic&& c) {
  _title = c._title;
  _author = c._author;
  _quantity = c._quantity;
  _issue = c._issue;
  c._title = NULL;
  c._author = NULL;
}

// Explicit Move Assignment Operator
Comic& Comic::operator=(Comic&& c) {
  delete[] _title;
  delete[] _author;

  _title = c._title;
  _author = c._author;
  _quantity = c._quantity;
  _issue = c._issue;
  c._title = NULL;
  c._author = NULL;
  return *this;
}

Comic::~Comic() {
  delete[] _title;
  delete[] _author;
}

DocType Comic::getDocType() { return DOC_COMIC; }

void Comic::print() {
  printf("Comic, title: %s, author: %s, issue: %d, quantity: %d\n", _title,
         _author, _issue, _quantity);
}

void Comic::updateAuthor(const char *newAuthor) {
  delete[] _author;
  _author = new char[strlen(newAuthor) + 1];
  strcpy(_author, newAuthor);
}

void Comic::updateIssue(int newIssue) { _issue = newIssue; }

const char *Comic::getAuthor() { return _author; }
int Comic::getIssue() { return _issue; }

//==============================================================================

Magazine::Magazine(const char *title, int issue, int quantity) {
  _title = new char[strlen(title) + 1];
  strcpy(_title, title);
  _quantity = quantity;
  _issue = issue;
}

// Explicit Copy Constructor
Magazine::Magazine(const Magazine& m)  {
  _title = new char[strlen(m._title) + 1];
  strcpy(_title, m._title);
  _quantity = m._quantity;
  _issue = m._issue;
}

// Explicit Copy Assignment Operator
Magazine& Magazine::operator=(const Magazine& m) {
  delete[] _title;

  _title = new char[strlen(m._title) + 1];
  strcpy(_title, m._title);
  _quantity = m._quantity;
  _issue = m._issue;
  return *this;
}

// Explicit Move Constructor
Magazine::Magazine(Magazine&& m) {
  _title = m._title;
  _quantity = m._quantity;
  _issue = m._issue;
  m._title = NULL;
}

// Explicit Move Assignment Operator
Magazine& Magazine::operator=(Magazine&& m) {
  delete[] _title;

  _title = m._title;
  _quantity = m._quantity;
  _issue = m._issue;
  m._title = NULL;
  return *this;
}

Magazine::~Magazine() {
  delete[] _title;
}

DocType Magazine::getDocType() { return DOC_MAGAZINE; }

void Magazine::print() {
  printf("Magazine, title: %s, issue: %d, quantity: %d\n", _title, _issue,
         _quantity);
}

void Magazine::updateIssue(int newIssue) { _issue = newIssue; }
int Magazine::getIssue() { return _issue; }

//==============================================================================

Library::Library() { _docs_sz = 0; };

Library::~Library() {
  for (int i = 0; i < _docs_sz; i++)
    delete _docs[i];
}

int Library::addDocument(DocType t, const char *title, const char *author,
                         int issue, int quantity) {
  Document *d;
  switch (t) {
  case DOC_NOVEL: {
    d = (Document *)new Novel(title, author, quantity);
    break;
  }

  case DOC_COMIC: {
    d = (Document *)new Comic(title, author, issue, quantity);
    break;
  }

  case DOC_MAGAZINE: {
    d = (Document *)new Magazine(title, issue, quantity);
    break;
  }

  default:
    return 0;
  }
  return addDocument(d);
}

int Library::addDocument(Document *d) {
  if (searchDocument(d->getTitle()) != nullptr)
    return 0;

  _docs[_docs_sz++] = d;
  return 1;
}

int Library::delDocument(const char *title) {
  int index = -1;
  for (int i = 0; i < _docs_sz; i++)
    if (!strcmp(_docs[i]->getTitle(), title)) {
      index = i;
      break;
    }

  if (index != -1) {
    delete _docs[index];
    for (int i = index + 1; i < _docs_sz; i++)
      _docs[i - 1] = _docs[i];
    _docs_sz--;
    return 1;
  }

  return 0;
}

int Library::countDocumentOfType(DocType t) {
  int res = 0;

  for (int i = 0; i < _docs_sz; i++)
    if (_docs[i]->getDocType() == t)
      res++;

  return res;
}

Document *Library::searchDocument(const char *title) {
  for (int i = 0; i < _docs_sz; i++)
    if (!strcmp(_docs[i]->getTitle(), title))
      return _docs[i];

  return NULL;
}

void Library::print() {
  for (int i = 0; i < _docs_sz; i++)
    _docs[i]->print();
}

int Library::borrowDoc(const char *title) {
  Document *d = searchDocument(title);
  if (d)
    return d->borrowDoc();
  return 0;
}

int Library::returnDoc(const char *title) {
  Document *d = searchDocument(title);
  if (d) {
    d->returnDoc();
    return 1;
  }
  return 0;
}

int Library::dumpCSV(const char *filename) {
  char line[128];
  int bytes_written;
  int fd = open(filename, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
  if (fd == -1)
    return 0;

  for (int i = 0; i < _docs_sz; i++) {
    Document *d = _docs[i];
    switch (d->getDocType()) {
    case DOC_NOVEL: {
      Novel *n = (Novel *)d;
      sprintf(line, "novel,%s,%s,,%d\n", n->getTitle(), n->getAuthor(),
              n->getQuantity());
      break;
    }

    case DOC_COMIC: {
      Comic *c = (Comic *)d;
      sprintf(line, "comic,%s,%s,%d,%d\n", c->getTitle(), c->getAuthor(),
              c->getIssue(), c->getQuantity());
      break;
    }

    case DOC_MAGAZINE: {
      Magazine *m = (Magazine *)d;
      sprintf(line, "magazine,%s,,%d,%d\n", m->getTitle(), m->getIssue(),
              m->getQuantity());

      break;
    }

    default:
      return 0;
    }

    bytes_written = write(fd, line, strlen(line));
    if (bytes_written != strlen(line))
      return 0;
  }

  close(fd);
  return 1;
}
