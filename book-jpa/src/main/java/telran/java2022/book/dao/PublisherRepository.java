package telran.java2022.book.dao;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.book.dto.BookDto;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, String> {
Stream<Book> findBooksByPublisher(String publisherName);
}
