package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
	public Book postBook(@RequestBody Book book) {
		return bookService.addBookToCatalog(book);
	}

	@DeleteMapping("{isbn}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable String isbn) {
		bookService.deleteBookFromCatalog(isbn);
	}

	@PutMapping("{isbn}")
	public Book editBook(@PathVariable String isbn, @RequestBody Book book) {
		return bookService.editBook(isbn, book);
	}
}
