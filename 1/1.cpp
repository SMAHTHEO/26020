#include "library.h"

#include <iostream>
#include <fstream>
#include <algorithm>

//==============================================================================

// 自定义构造函数实现
Document::Document(std::string title, int quantity)
    : _title(std::move(title)), _quantity(quantity)
{
}

// 设置标题
void Document::updateTitle(const std::string &newTitle)
{
  _title = newTitle;
}

// 获取标题
const std::string &Document::getTitle() const
{
  return _title;
}

// 设置数量
void Document::updateQuantity(int newQuantity)
{
  _quantity = newQuantity;
}

// 获取数量
int Document::getQuantity() const
{
  return _quantity;
}

// 借出文档
int Document::borrowDoc()
{
  if (_quantity > 0)
  {
    _quantity--;
    return 1; // 成功借出
  }
  return 0; // 借出失败
}

// 归还文档
void Document::returnDoc()
{
  _quantity++;
}

//==============================================================================

// 构造函数实现
Novel::Novel(const std::string &title, const std::string &author, int quantity)
    : Document(title, quantity), _author(author)
{
}

// Novel 复制构造函数
Novel::Novel(const Novel &other)
    : Document(other), _author(other._author)
{
}

// Novel 复制赋值运算符
Novel &Novel::operator=(const Novel &other)
{
  if (this != &other)
  {
    Document::operator=(other);
    _author = other._author;
  }
  return *this;
}

// 获取文档类型
DocType Novel::getDocType() const
{
  return DOC_NOVEL;
}

// 打印小说信息
void Novel::print() const
{
  std::cout << "Novel, title: " << getTitle() << ", author: " << _author
            << ", quantity: " << getQuantity() << std::endl;
}

// 设置作者
void Novel::updateAuthor(const std::string &newAuthor)
{
  _author = newAuthor;
}

// 获取作者
const std::string &Novel::getAuthor() const
{
  return _author;
}

//==============================================================================

// 构造函数实现
Comic::Comic(const std::string &title, const std::string &author, int issue, int quantity)
    : Document(title, quantity), _author(author), _issue(issue)
{
}

// Comic 复制构造函数
Comic::Comic(const Comic &other)
    : Document(other), _author(other._author), _issue(other._issue)
{
}

// Comic 复制赋值运算符
Comic &Comic::operator=(const Comic &other)
{
  if (this != &other)
  {
    Document::operator=(other);
    _author = other._author;
    _issue = other._issue;
  }
  return *this;
}

// 获取文档类型
DocType Comic::getDocType() const
{
  return DOC_COMIC;
}

// 打印漫画信息
void Comic::print() const
{
  std::cout << "Comic, title: " << getTitle() << ", author: " << _author
            << ", issue: " << _issue << ", quantity: " << getQuantity() << std::endl;
}

// 设置作者
void Comic::updateAuthor(const std::string &newAuthor)
{
  _author = newAuthor;
}

// 获取作者
const std::string &Comic::getAuthor() const
{
  return _author;
}

// 设置期号
void Comic::updateIssue(int newIssue)
{
  _issue = newIssue;
}

// 获取期号
int Comic::getIssue() const
{
  return _issue;
}

//==============================================================================

// 构造函数实现
Magazine::Magazine(const std::string &title, int issue, int quantity)
    : Document(title, quantity), _issue(issue)
{
}

// Magazine 复制构造函数
Magazine::Magazine(const Magazine &other)
    : Document(other), _issue(other._issue)
{
}

// Magazine 复制赋值运算符
Magazine &Magazine::operator=(const Magazine &other)
{
  if (this != &other)
  {
    Document::operator=(other);
    _issue = other._issue;
  }
  return *this;
}

// 获取文档类型
DocType Magazine::getDocType() const
{
  return DOC_MAGAZINE;
}

// 打印杂志信息
void Magazine::print() const
{
  std::cout << "Magazine, title: " << getTitle() << ", issue: " << _issue
            << ", quantity: " << getQuantity() << std::endl;
}

// 设置期号
void Magazine::updateIssue(int newIssue)
{
  _issue = newIssue;
}

// 获取期号
int Magazine::getIssue() const
{
  return _issue;
}

//==============================================================================

// 添加文档
bool Library::addDocument(std::unique_ptr<Document> doc)
{
  if (searchDocument(doc->getTitle()) != nullptr)
  {
    return false;
  }
  _docs.push_back(std::move(doc));
  return true;
}

bool Library::addDocument(Document *doc)
{
  return addDocument(std::unique_ptr<Document>(doc));
}

bool Library::addDocument(DocType type, const std::string &title, const std::string &author, int issue, int quantity)
{
  if (searchDocument(title) != nullptr)
  {
    return false;
  }

  std::unique_ptr<Document> doc;
  switch (type)
  {
  case DOC_NOVEL:
    doc = std::make_unique<Novel>(title, author, quantity);
    break;
  case DOC_COMIC:
    doc = std::make_unique<Comic>(title, author, issue, quantity);
    break;
  case DOC_MAGAZINE:
    doc = std::make_unique<Magazine>(title, issue, quantity);
    break;
  default:
    return false;
  }

  _docs.push_back(std::move(doc));
  return true;
}

// 删除文档
bool Library::delDocument(const std::string &title)
{
  auto it = std::find_if(_docs.begin(), _docs.end(),
                         [&title](const std::unique_ptr<Document> &doc)
                         {
                           return doc->getTitle() == title;
                         });
  if (it != _docs.end())
  {
    _docs.erase(it);
    return true;
  }
  return false;
}

// 搜索文档
Document *Library::searchDocument(const std::string &title) const
{
  for (const auto &doc : _docs)
  {
    if (doc->getTitle() == title)
    {
      return doc.get();
    }
  }
  return nullptr;
}

// 打印图书馆中的所有文档
void Library::print() const
{
  for (const auto &doc : _docs)
  {
    doc->print();
  }
}

// 借阅文档
bool Library::borrowDoc(const std::string &title)
{
  Document *doc = searchDocument(title);
  if (doc && doc->borrowDoc())
  {
    return true;
  }
  return false;
}

// 归还文档
bool Library::returnDoc(const std::string &title)
{
  Document *doc = searchDocument(title);
  if (doc)
  {
    doc->returnDoc();
    return true;
  }
  return false;
}

int Library::countDocumentOfType(DocType type) const
{
  return std::count_if(_docs.begin(), _docs.end(), [type](const std::unique_ptr<Document> &doc)
                       { return doc->getDocType() == type; });
}
bool Library::dumpCSV(const std::string &filename) const
{
  std::ofstream file(filename);
  if (!file.is_open())
  {
    return false;
  }

  for (const auto &doc : _docs)
  {
    switch (doc->getDocType())
    {
    case DOC_NOVEL:
      file << "Novel," << doc->getTitle() << "," << static_cast<const Novel *>(doc.get())->getAuthor() << ",," << doc->getQuantity() << "\n";
      break;
    case DOC_COMIC:
      file << "Comic," << doc->getTitle() << "," << static_cast<const Comic *>(doc.get())->getAuthor() << "," << static_cast<const Comic *>(doc.get())->getIssue() << "," << doc->getQuantity() << "\n";
      break;
    case DOC_MAGAZINE:
      file << "Magazine," << doc->getTitle() << ",," << static_cast<const Magazine *>(doc.get())->getIssue() << "," << doc->getQuantity() << "\n";
      break;
    }
  }

  file.close();
  return true;
}
