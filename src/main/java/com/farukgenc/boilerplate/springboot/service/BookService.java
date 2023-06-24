package com.farukgenc.boilerplate.springboot.service;
import com.farukgenc.boilerplate.springboot.model.Books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface BookService {
    Books createBook(Books book);
    Page<Books> getAllBooks(Pageable pageable);
    Books getBookByIsbn(String isbn);
    Books updateBook(String isbn, Books book);
    void deleteBook(String isbn);
    List<Books> findAllPublishedAfter(LocalDate date);
    List<Books> findAllByAuthor(String author);
    List<Books> findByTitleContainingIgnoreCase(String title);
    Long countByPagesGreaterThan(int pages);
    Page<Books> findAllByPublisherOrderByPublishDateDesc(String publisher, Pageable pageable);


}
