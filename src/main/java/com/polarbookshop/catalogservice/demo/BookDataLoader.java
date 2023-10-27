package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata")
@RequiredArgsConstructor
public class BookDataLoader {

	private final BookRepository bookRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void loadBookTestData() {
		var book1 = Book.builder()
		                .isbn("1234567890")
		                .title("Northern Lights")
		                .author("Lyra Silverstar")
		                .price(9.9)
		                .build();
		var book2 = Book.builder()
		                .isbn("1234567891")
		                .title("Polar Journey")
		                .author("Iorek Polarson")
		                .price(12.90)
		                .build();
		bookRepository.save(book1);
		bookRepository.save(book2);
	}
}
