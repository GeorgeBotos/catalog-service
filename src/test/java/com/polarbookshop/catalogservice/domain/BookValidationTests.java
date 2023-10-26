package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookValidationTests {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("When all fields are correct, then validation should succeed.")
	void validateAllFields() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("Title")
		               .author("Author")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("When ISBN defined but it is incorrect, then validation should fail.")
	void validateInvalidIsbn() {
		var book = Book.builder()
		               .isbn("a234567890")
		               .title("Title")
		               .author("Author")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertEquals(1, violations.size());
		assertEquals("The ISBN format must be valid: a 10 or 13 digit number.", violations.iterator()
		                                                                                  .next()
		                                                                                  .getMessage());
	}
}
