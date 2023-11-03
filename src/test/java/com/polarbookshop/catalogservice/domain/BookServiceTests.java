package com.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@Test
	@DisplayName("When a book already exists, then BookAlreadyExistsException is thrown.")
	void testBookAlreadyExists() {
		var isbn = "1234567892";
		var bookToCreate = Book.builder()
		                       .isbn(isbn)
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		when(bookRepository.existsByIsbn(isbn)).thenReturn(true);
		assertThatThrownBy(() -> bookService.addBookToCatalog(bookToCreate)).isInstanceOf(BookAlreadyExistsException.class)
		                                                                    .hasMessage(String.format("A book with ISBN %s already exists.", isbn));
	}

	@Test
	@DisplayName("When a non existing book is requested, then BookNotFoundException is thrown.")
	void testNotExistingBook() {
		var isbn = "1234567892";
		when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookService.getBook(isbn)).isInstanceOf(BookNotFoundException.class)
		                                                   .hasMessage(String.format("The book with ISBN %s was not found.", isbn));
	}
}
