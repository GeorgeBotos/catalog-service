package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

	@Autowired
	private JacksonTester<Book> json;

	@Test
	@DisplayName("When a Book object is serialised, values should be set correctly ensuring serialization was successful.")
	void testSerialize() throws Exception {
		var book = Book.builder()
		               .id(1L)
		               .version(2)
		               .isbn("1234567890123")
		               .title("Title")
		               .author("Author")
		               .price(9.90)
		               .build();
		var jsonContent = json.write(book);
		assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
		                       .isEqualTo(book.id()
		                                      .intValue());
		assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
		                       .isEqualTo(book.version());
		assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
		                       .isEqualTo(book.isbn());
		assertThat(jsonContent).extractingJsonPathStringValue("@.author")
		                       .isEqualTo(book.author());
		assertThat(jsonContent).extractingJsonPathStringValue("@.title")
		                       .isEqualTo(book.title());
		assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
		                       .isEqualTo(book.price());
	}

	@Test
	@DisplayName("When a correct Book Json string is deserialized, the Book object should have all fields correctly set.")
	void testDeserialize() throws Exception {
		var book = Book.builder()
		               .id(1L)
		               .version(2)
		               .isbn("1234567890123")
		               .title("Title")
		               .author("Author")
		               .price(9.90)
		               .build();
		var jsonString = """
		                 {
		                   "id": 1,
		                   "version": 2,
		                   "isbn": "1234567890123",
		                   "title": "Title",
		                   "author": "Author",
		                   "price": 9.9
		                 }
		                 """;
		assertThat(json.parse(jsonString)
		               .getObject()).usingRecursiveComparison()
		                            .isEqualTo(book);
	}
}
