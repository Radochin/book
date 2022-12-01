package telran.java2022.book.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import telran.java2022.book.dto.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import telran.java2022.book.dao.AuthorRepository;
import telran.java2022.book.dao.BookRepository;
import telran.java2022.book.dao.PublisherRepository;
import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.dto.BookDto;
import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    final BookRepository bookRepository;
    final AuthorRepository authorRepository;
    final PublisherRepository publisherRepository;
    final ModelMapper modelMapper;

    @Override
    @Transactional
    public boolean addBook(BookDto bookDto) {
	if (bookRepository.existsById(bookDto.getIsbn())) {
	    return false;
	}

//Publisher
	Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
		.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
// Author
	Set<Author> authors = bookDto.getAuthors()
		.stream()
		.map(a -> authorRepository.findById(a.getName())
			.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
		.collect(Collectors.toSet());
	Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
	bookRepository.save(book);
	return true;
    }

    @Override
    public BookDto findBookByIsbn(String isbn) {
	Book book = bookRepository.findById(isbn)
		.orElseThrow(EntityNotFoundException::new);
	return modelMapper.map(book, BookDto.class);
    }

    @Override
    public BookDto removeBook(String isbn) {
	Book book = bookRepository.findById(isbn)
		.orElseThrow(() -> new EntityNotFoundException());
	bookRepository.delete(book);
	return modelMapper.map(book, BookDto.class);
    }
    

    @Override
    public BookDto updateBook(String isbn, String title) {
	Book book = bookRepository.findById(isbn)
		.orElseThrow(() -> new EntityNotFoundException());
	book.setIsbn(title);
	return modelMapper.map(book, BookDto.class);

    }

    @Override
    public Iterable<BookDto> findBooksByAuthor(String authorName) {

	return  bookRepository.findBooksByAuthor(authorName)
		.map(p->modelMapper.map(p, BookDto.class))
		.collect(Collectors.toList());
    }
			
		
    @Override
    public Iterable<BookDto> findBooksByPublisher(String publisherName) {
 	return bookRepository.findBooksByPublisher(publisherName)
 		.map(p->modelMapper.map(publisherName, BookDto.class))
 		.collect(Collectors.toList());
    }

    @Override
    public Iterable<AuthorDto> findBookAuthor(String isbn) {
	return authorRepository.findBookAuthor(isbn)
		.map(p -> modelMapper.map(isbn, AuthorDto.class))
		.collect(Collectors.toList());
    }

    @Override
    public Iterable<String> findPublishersByAuthor(String authorName) {
 	return null;
    }

    @Override
    public AuthorDto removeAuthor(String authorName) {
	Author author=authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
	
	authorRepository.delete(author);
	return modelMapper.map(author, AuthorDto.class);
    }
}
    
    
    
 
