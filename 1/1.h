#pragma once

#include <vector>
#include <memory>
#include <string>

/* The different types of documents stored in the library */
enum DocType
{
  DOC_NOVEL,
  DOC_COMIC,
  DOC_MAGAZINE
};

// 抽象基类 Document
class Document
{
public:
  // 构造函数
  Document() = default;

  // 自定义构造函数
  Document(std::string title, int quantity);

  // 虚析构函数，以确保正确的派生类析构
  virtual ~Document() = default;

  // 禁用复制构造函数和赋值运算符，因为这是抽象类
  Document(const Document &) = default;
  Document &operator=(const Document &) = default;

  // 启用移动构造函数和移动赋值运算符
  Document(Document &&) = default;
  Document &operator=(Document &&) = default;

  // 纯虚函数，返回文档类型
  virtual DocType getDocType() const = 0;

  // 纯虚函数，打印文档信息
  virtual void print() const = 0;

  // 设置和获取标题
  void updateTitle(const std::string &newTitle);
  const std::string &getTitle() const;

  // 设置和获取数量
  void updateQuantity(int newQuantity);
  int getQuantity() const;

  // 借出和归还文档
  int borrowDoc();
  void returnDoc();

protected:
  std::string _title; // 文档标题
  int _quantity;      // 文档数量
};

class Novel : public Document
{
public:
  // 构造函数
  Novel(const std::string &title, const std::string &author, int quantity);

  // 启用默认的析构函数
  ~Novel() override = default;

  // 重新启用复制构造函数和赋值运算符
  Novel(const Novel &) = default;
  Novel &operator=(const Novel &) = default;

  // 启用移动构造函数和移动赋值运算符
  Novel(Novel &&) noexcept = default;
  Novel &operator=(Novel &&) noexcept = default;

  // 覆盖基类的虚函数
  DocType getDocType() const override;
  void print() const override;

  // 设置和获取作者
  void updateAuthor(const std::string &newAuthor);
  const std::string &getAuthor() const;

private:
  std::string _author; // 小说作者
};

class Comic : public Document
{
public:
  // 构造函数
  Comic(const std::string &title, const std::string &author, int issue, int quantity);

  // 启用默认的析构函数
  ~Comic() override = default;

  // 启用复制构造函数和赋值运算符
  Comic(const Comic &) = default;
  Comic &operator=(const Comic &) = default;

  // 启用移动构造函数和移动赋值运算符
  Comic(Comic &&) noexcept = default;
  Comic &operator=(Comic &&) noexcept = default;

  // 覆盖基类的虚函数
  DocType getDocType() const override;
  void print() const override;

  // 设置和获取作者和期号
  void updateAuthor(const std::string &newAuthor);
  void updateIssue(int newIssue);
  const std::string &getAuthor() const;
  int getIssue() const;

private:
  std::string _author; // 漫画作者
  int _issue;          // 漫画期号
};

class Magazine : public Document
{
public:
  // 构造函数
  Magazine(const std::string &title, int issue, int quantity);

  // 启用默认的析构函数
  ~Magazine() override = default;

  // 启用复制构造函数和赋值运算符
  Magazine(const Magazine &) = default;
  Magazine &operator=(const Magazine &) = default;

  // 启用移动构造函数和移动赋值运算符
  Magazine(Magazine &&) noexcept = default;
  Magazine &operator=(Magazine &&) noexcept = default;

  // 覆盖基类的虚函数
  DocType getDocType() const override;
  void print() const override;

  // 设置和获取期号
  void updateIssue(int newIssue);
  int getIssue() const;

private:
  int _issue; // 杂志期号
};

class Library
{
public:
  Library() = default;
  ~Library() = default;

  // 启用复制构造函数和赋值运算符
  Library(const Library &) = default;
  Library &operator=(const Library &) = default;

  // 启用移动构造函数和移动赋值运算符
  Library(Library &&) noexcept = default;
  Library &operator=(Library &&) noexcept = default;

  // 添加文档
  bool addDocument(std::unique_ptr<Document> doc);
  // 新增重载
  bool addDocument(Document* doc);  
  // 添加一个新的重载方法
  bool addDocument(DocType type, const std::string &title, const std::string &author, int issue, int quantity);

  // 删除文档
  bool delDocument(const std::string &title);

  // 搜索文档
  Document *searchDocument(const std::string &title) const;

  // 打印图书馆中的所有文档
  void print() const;

  // 借阅文档
  bool borrowDoc(const std::string &title);

  // 归还文档
  bool returnDoc(const std::string &title);

  int countDocumentOfType(DocType type) const;
  bool dumpCSV(const std::string& filename) const;

private:
  std::vector<std::unique_ptr<Document>> _docs; // 存储文档的向量
  
};
;
