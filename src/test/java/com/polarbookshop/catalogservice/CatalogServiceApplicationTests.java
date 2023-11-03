package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	@DisplayName("If a correct GET request is sent with an existing ISBN, the book is returned.")
	void getBookEndpointTest() {
		var isbn = "1234567890";
		var bookToCreate = Book.builder()
		                       .isbn(isbn)
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		Book expectedBook = webTestClient.post()
		                                 .uri("/books")
		                                 .bodyValue(bookToCreate)
		                                 .exchange()
		                                 .expectStatus()
		                                 .isCreated()
		                                 .expectBody(Book.class)
		                                 .value(book -> assertThat(book).isNotNull())
		                                 .returnResult()
		                                 .getResponseBody();
		webTestClient.get()
		             .uri("/books/" + isbn)
		             .exchange()
		             .expectStatus()
		             .is2xxSuccessful()
		             .expectBody(Book.class)
		             .value(book -> {
			             assertThat(book).isNotNull();
			             assertThat(book.isbn()).isEqualTo(expectedBook.isbn());
		             });
	}

	@Test
	@DisplayName("If a correct POST request is sent, then book is created.")
	void postBookEndpointTest() {
		var bookToCreate = Book.builder()
		                       .isbn("1234567891")
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		webTestClient.post()
		             .uri("/books")
		             .bodyValue(bookToCreate)
		             .exchange()
		             .expectStatus()
		             .isCreated()
		             .expectBody(Book.class)
		             .value(book -> {
			             assertThat(book).isNotNull();
			             assertThat(book.isbn()).isEqualTo(bookToCreate.isbn());
		             });
	}

	@Test
	@DisplayName("If a correct PUT request is sent, then book is updated.")
	void putBookEndpointTest() {
		var isbn = "1234567892";
		var bookToCreate = Book.builder()
		                       .isbn(isbn)
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		Book bookCreated = webTestClient.post()
		                                .uri("/books")
		                                .bodyValue(bookToCreate)
		                                .exchange()
		                                .expectStatus()
		                                .isCreated()
		                                .expectBody(Book.class)
		                                .value(book -> assertThat(book).isNotNull())
		                                .returnResult()
		                                .getResponseBody();
		var bookToUpdate = Book.builder()
		                       .isbn(bookCreated.isbn())
		                       .title(bookCreated.title())
		                       .author(bookCreated.author())
		                       .price(7.45)
		                       .createdDate(bookCreated.createdDate())
		                       .lastModifiedDate(bookCreated.lastModifiedDate())
		                       .version(bookCreated.version())
		                       .build();
		webTestClient.put()
		             .uri("/books/" + isbn)
		             .bodyValue(bookToUpdate)
		             .exchange()
		             .expectStatus()
		             .isOk()
		             .expectBody(Book.class)
		             .value(book -> {
			             assertThat(book).isNotNull();
			             assertThat(book.price()).isEqualTo(bookToUpdate.price());
		             });
	}

	@Test
	@DisplayName("If correct DELETE request is sent, then book is deleted.")
	void deleteBookEndpointTest() {
		var isbn = "1234567893";
		var bookToCreate = Book.builder()
		                       .isbn(isbn)
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		webTestClient.post()
		             .uri("/books")
		             .bodyValue(bookToCreate)
		             .exchange()
		             .expectStatus()
		             .isCreated();
		webTestClient.delete()
		             .uri("/books/" + isbn)
		             .exchange()
		             .expectStatus()
		             .isNoContent();
		webTestClient.get()
		             .uri("/books/" + isbn)
		             .exchange()
		             .expectStatus()
		             .isNotFound()
		             .expectBody(String.class)
		             .value(errorMessage -> assertThat(errorMessage).isEqualTo(String.format("The book with ISBN %s was not found.", isbn)));
	}
}
