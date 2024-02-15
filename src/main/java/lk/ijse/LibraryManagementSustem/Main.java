package lk.ijse.LibraryManagementSustem;

import lk.ijse.LibraryManagementSustem.config.FactoryConfiguration;
import lk.ijse.LibraryManagementSustem.entity.Author;
import lk.ijse.LibraryManagementSustem.entity.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Session session= FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        //save(session);
       // getBooks(session);
        //updatePrice(session);
        //delete(session);
        //getAveragePrice(session);
       // getCountOfBooks(session);
        getAuthor(session);

        transaction.commit();
        session.close();
    }
    public static void save(Session session){
        Book book = new Book();
        book.setTitle("Girls");
        book.setPublicationYear(2014);
        book.setPrice(450);
        Book book1 = new Book();
        book1.setTitle("Jems");
        book1.setPublicationYear(2001);
        book1.setPrice(1800);

        Book book2 = new Book();
        book2.setTitle("Cakes");
        book2.setPublicationYear(2004);
        book2.setPrice(650);

        Author author = new Author();
        author.setName("Jems");
        List<Book>books = new ArrayList<>();
        books.add(book);
        books.add(book1);
        books.add(book2);
        author.setBooks(books);
        book.setAuthor(author);
        book1.setAuthor(author);
        book2.setAuthor(author);

        session.persist(book);
        session.persist(book1);
        session.persist(book2);
        session.persist(author);
    }
    //to get all books published after the year 2010
    //Q1
    public static void getBooks(Session session){
        Query query = session.createQuery("SELECT b FROM Book b WHERE b.publicationYear > 2010");
        List<Book>books = query.list();
        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }
    //Q2
    public static void updatePrice(Session session){
        Query query = session.createQuery("SELECT b FROM Book b WHERE b.author.name = :aut");
        query.setParameter("aut", "Sam");
        List<Book> books = query.list();
        for (Book book : books) {
            int price = book.getPrice();
            int increasedPrice = price-price*10/100;
            //System.out.println(increasedPrice);
           Query query1 = session.createQuery("UPDATE Book set price=:p WHERE id=:i");
            query1.setParameter("p",increasedPrice);
            query1.setParameter("i",book.getId());
            int status=query1.executeUpdate();
            System.out.println(status);
        }
    }
    //Q3
    public static void delete(Session session){
        Query query = session.createQuery("DELETE FROM Author a WHERE a.id = :id");
        query.setParameter("id", 6);
        query.executeUpdate();
    }
    //Q4
    public static void getAveragePrice(Session session){
        Query query = session.createQuery("SELECT AVG(b.price) FROM Book b");
        Double averagePrice = (Double) query.uniqueResult();
        System.out.println(averagePrice);
    }
    //Q5
    public static void getCountOfBooks(Session session){
        //Query query = session.createQuery("SELECT author.name, COUNT(*) FROM Author, Book WHERE author.id = books.Author_id GROUP BY author.id");
        Query query = session.createQuery("SELECT a, COUNT(*) FROM Author a LEFT JOIN a.books b GROUP BY a.id");
        List<Object[]> objects = query.list();
        for (Object[] object : objects) {
            Author author = (Author) object[0];
            Long count = (Long) object[1];
            System.out.println(author.getName()+" "+ count);
        }
    }
    //Q10
    public static void getAuthor(Session session){
       // Query query = session.createQuery("SELECT a FROM Author a WHERE (SELECT a, COUNT(*) FROM Author a LEFT JOIN a.books b GROUP BY a.id)>(SELECT AVG(b.id) FROM Book b) ");
        Query query = session.createQuery(
                "SELECT a " +
                        "FROM Author a " +
                        "WHERE ( " +
                        "   SELECT COUNT(*) " +
                        "   FROM Book b " +
                        "   WHERE b.author = a " +
                        ") > ( " +
                        "   SELECT AVG(books) " +
                        "   FROM ( " +
                        "       SELECT COUNT(b.id) as books " +
                        "       FROM Book b " +
                        "       GROUP BY b.author " +
                        "   ) AS authorBooksCount " +
                        ")"
        );
        List<Author>authors =query.list();
        for (Author author : authors) {
            System.out.println(author.getName());
        }
    }
}