package com.farukgenc.boilerplate.springboot.service;
import com.farukgenc.boilerplate.springboot.model.Books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookService {
    Books createBook(Books book);
    Page<Books> getAllBooks(Pageable pageable);
    Books getBookByIsbn(String isbn);
    Books updateBook(String isbn, Books book);
    void deleteBook(String isbn);

}
