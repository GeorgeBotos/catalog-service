package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@Builder
public record Book(

		@Id
		Long id,

		@Version
		int version,

		@NotBlank(message = "The book ISBN must be defined.")
		@Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
				 message = "The ISBN format must be valid: a 10 or 13 digit number.")
		String isbn,

		@NotBlank(message = "The book title must be defined.")
		String title,

		@NotBlank(message = "The book author must be defined.")
		String author,

		@NotNull(message = "The book price must be defined.")
		@Positive(message = "The book price must be greater than zero.")
		Double price
) {}
