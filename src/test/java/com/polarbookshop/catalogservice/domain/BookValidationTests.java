package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
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
	void allFieldsCorrectValidationTest() {
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
	@DisplayName("When ISBN is null, then validation should fail.")
	void missingIsbnValidationTest() {
		var book = Book.builder()
		               .title("Title")
		               .author("Author")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book ISBN must be defined.");
	}

	@Test
	@DisplayName("When ISBN defined but it is incorrect, then validation should fail.")
	void invalidIsbnValidationTest() {
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

	@Test
	@DisplayName("When ISBN is not defined, then validation should fail.")
	void notDefinedIsbnValidationTest() {
		var book = Book.builder()
		               .isbn("")
		               .title("Title")
		               .author("Author")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(2);
		List<String> constrainViolationMessages = violations.stream()
		                                                    .map(ConstraintViolation::getMessage)
		                                                    .toList();
		assertThat(constrainViolationMessages).contains("The book ISBN must be defined.")
		                                      .contains("The ISBN format must be valid: a 10 or 13 digit number.");
	}

	@Test
	@DisplayName("When title is not defined, then validation should fail.")
	void notDefinedTitleValidationTest() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("")
		               .author("Author")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book title must be defined.");
	}

	@Test
	@DisplayName("When author is not defined, then validation should fail.")
	void notDefinedAuthorValidationTest() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("Title")
		               .author("")
		               .price(9.9)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book author must be defined.");
	}

	@Test
	@DisplayName("When price is not defined, then validation should fail.")
	void notDefinedPriceValidationTest() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("Title")
		               .author("Author")
		               .price(null)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book price must be defined.");
	}

	@Test
	@DisplayName("When price is defined but set to zero, then validation should fail.")
	void zeroPriceValidationTest() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("Title")
		               .author("Author")
		               .price(0.0)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book price must be greater than zero.");
	}

	@Test
	@DisplayName("When price is defined but set to negative number, then validation should fail.")
	void negativePriceValidationTest() {
		var book = Book.builder()
		               .isbn("1234567890")
		               .title("Title")
		               .author("Author")
		               .price(-2.0)
		               .build();
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator()
		                     .next()
		                     .getMessage()).isEqualTo("The book price must be greater than zero.");
	}
}
