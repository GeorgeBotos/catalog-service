package com.polarbookshop.catalogservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

	private final BookRepository bookRepository;

	public Iterable<Book> getBooks() {
		return bookRepository.findAll();
	}

	public Book getBook(String isbn) {
		return bookRepository.findByIsbn(isbn)
		                     .orElseThrow(() -> new BookNotFoundException(isbn));
	}

	public Book addBookToCatalog(Book book) {
		if (bookRepository.existsByIsbn(book.isbn())) {
			throw new BookAlreadyExistsException(book.isbn());
		}
		bookRepository.save(book);
		return bookRepository.findByIsbn(book.isbn())
		                     .orElse(null);
	}

	public void deleteBookFromCatalog(String isbn) {
		bookRepository.deleteByIsbn(isbn);
	}

	public Book editBook(String isbn, Book book) {
		return bookRepository.findByIsbn(isbn)
		                     .map(existingBook -> {
			                     var bookToUpdate = new Book(existingBook.isbn(),
			                                                 book.title(),
			                                                 book.author(),
			                                                 book.price());
			                     return bookRepository.save(bookToUpdate);
		                     })
		                     .orElseGet(() -> addBookToCatalog(book));
	}
}
