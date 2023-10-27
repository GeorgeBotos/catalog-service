package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
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
		assertThat(violations).isEmpty();
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
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The ISBN format must be valid: a 10 or 13 digit number.");
	}
}
