package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.Books;
import com.farukgenc.boilerplate.springboot.service.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.farukgenc.boilerplate.springboot.exceptions.BookValidationException;
import com.farukgenc.boilerplate.springboot.exceptions.ErrorResponse;

import java.time.LocalDateTime;



@RestController
@RequestMapping("/api")
public class CoreController {
    @Autowired
    private BookService bookService;

    
    @PostMapping("/book")
    public ResponseEntity<Books> createBook(@RequestBody Books book) {
        Books createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

	@GetMapping("/books")
    public ResponseEntity<Page<Books>> getAllBooks(
		@RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {
		
		Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/book")
    public ResponseEntity<Books> getBook(
		@RequestParam(value="isbn", required=true) String isbn) {
        Books book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/book/{isbn}")
    public ResponseEntity<Books> updateBook(@PathVariable String isbn, @RequestBody Books book) {
        Books updatedBook = bookService.updateBook(isbn, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/book/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

	@ExceptionHandler(BookValidationException.class)
    public ResponseEntity<ErrorResponse> handleBookValidationException(BookValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                ex.getErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

