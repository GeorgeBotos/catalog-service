package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

	private final BookService bookService;

	@GetMapping
	public Iterable<Book> getBooks() {
		return bookService.getBooks();
	}

	@GetMapping("{isbn}")
	public Book getByIsbn(@PathVariable String isbn) {
		return bookService.getBook(isbn);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Book postBook(@Valid @RequestBody Book book) {
		return bookService.addBookToCatalog(book);
	}

	@DeleteMapping("{isbn}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable String isbn) {
		bookService.deleteBookFromCatalog(isbn);
	}

	@PutMapping("{isbn}")
	public Book editBook(@Valid @PathVariable String isbn, @RequestBody Book book) {
		return bookService.editBook(isbn, book);
	}
}
