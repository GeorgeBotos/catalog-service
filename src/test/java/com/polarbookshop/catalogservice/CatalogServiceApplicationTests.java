package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
		var expectedBook = Book.builder()
		                       .isbn("1234567890")
		                       .title("Title")
		                       .author("Author")
		                       .price(9.90)
		                       .build();
		webTestClient.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(Book.class)
				.value(book -> {
					assertNotNull(book);
					assertEquals(expectedBook.isbn(), book.isbn());
				});
	}

}
