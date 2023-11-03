package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private JdbcAggregateTemplate jdbcAggregateTemplate;

	@Test
	@DisplayName("Find a book by its ISBN, when exists.")
	void findBookByIsbnTest() {
		var isbn = "1234567897";
		var book = Book.builder()
		               .isbn(isbn)
		               .title("Title")
		               .author("Author")
		               .price(12.90)
		               .build();
		jdbcAggregateTemplate.insert(book);
		Optional<Book> actualBook = bookRepository.findByIsbn(isbn);

		assertThat(actualBook).isPresent();
		assertThat(actualBook.get()
		                     .isbn()).isEqualTo(book.isbn());
	}

	@Test
	@DisplayName("Find all books return all books from database.")
	void findAllBooksTest() {
		var book1 = Book.builder()
		                .isbn("1234567895")
		                .title("Title")
		                .author("Author")
		                .price(12.90)
		                .build();
		var book2 = Book.builder()
		                .isbn("1234567896")
		                .title("Another Title")
		                .author("Author")
		                .price(12.90)
		                .build();
		jdbcAggregateTemplate.insert(book1);
		jdbcAggregateTemplate.insert(book2);

		Iterable<Book> books = bookRepository.findAll();

		assertThat(StreamSupport.stream(books.spliterator(), true)
		                        .filter(book -> book.isbn()
		                                            .equals(book1.isbn()) || book.isbn()
		                                                                         .equals(book2.isbn()))
		                        .toList()).hasSize(2);

	}

	@Test
	@DisplayName("When book does not exist, then no book is returned.")
	void findNotExistingBookTest() {
		Optional<Book> book = bookRepository.findByIsbn("1234567898");
		assertThat(book).isEmpty();
	}

	@Test
	@DisplayName("When presence of an existing book is requested, true is returned.")
	void existByIsbnTest() {
		var isbn = "1234567899";
		var book = Book.builder()
		               .isbn(isbn)
		               .title("Title")
		               .author("Author")
		               .price(12.90)
		               .build();
		jdbcAggregateTemplate.insert(book);

		boolean existing = bookRepository.existsByIsbn(isbn);
		assertThat(existing).isTrue();
	}

	@Test
	@DisplayName("When presence of a non-existing book is requested, false is returned.")
	void nonExistByIsbnTest() {
		boolean existing = bookRepository.existsByIsbn("1234567900");
		assertThat(existing).isFalse();
	}

	@Test
	@DisplayName("When a book is deleted by ISBN from database, it does not exists any more.")
	void deleteBookTest() {
		var isbn = "1234567901";
		var book = Book.builder()
		               .isbn(isbn)
		               .title("Title")
		               .author("Author")
		               .price(12.90)
		               .build();
		var persistedBook = jdbcAggregateTemplate.insert(book);

		bookRepository.deleteByIsbn(isbn);

		assertThat(jdbcAggregateTemplate.findById(persistedBook.id(), Book.class)).isNull();
	}
}
