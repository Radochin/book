package telran.java2022.book.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.model.Author;

public interface AuthorRepository extends CrudRepository<Author, String> {
    @Query("delete  a from  Author a where a.name=?1 ")
    Stream<Author>removeAuthor(String authorName);
    
   
    Stream<Author> findBookAuthor(String isbn);
}
