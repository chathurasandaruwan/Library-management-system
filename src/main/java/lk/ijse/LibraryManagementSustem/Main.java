package lk.ijse.LibraryManagementSustem;

import lk.ijse.LibraryManagementSustem.config.FactoryConfiguration;
import lk.ijse.LibraryManagementSustem.entity.Author;
import lk.ijse.LibraryManagementSustem.entity.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Session session= FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        //save(session);
       // getBooks(session);
        updatePrice(session);

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
}