package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.exceptions.BookValidationException;
import com.farukgenc.boilerplate.springboot.exceptions.BookAlreadyExistsException;
import com.farukgenc.boilerplate.springboot.exceptions.BookNotFoundException;
import com.farukgenc.boilerplate.springboot.model.Books;
import com.farukgenc.boilerplate.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Books createBook(Books book) {
        checkIfBookExists(book);
        validateBook(book);
        return bookRepository.save(book);
    }

    @Override
    public Page<Books> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Books getBookByIsbn(String isbn) {
    Books Book = bookRepository.findById(isbn).orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    return Book;
    }


    @Override
    public Books updateBook(String isbn, Books book) {
        Books existingBook = getBookByIsbn(isbn);
        existingBook.setTitle(book.getTitle());
        existingBook.setSubTitle(book.getSubTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublishDate(book.getPublishDate());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPages(book.getPages());
        existingBook.setDescription(book.getDescription());
        existingBook.setWebsite(book.getWebsite());
        validateBook(book);
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String isbn) {
        Books book = getBookByIsbn(isbn);
        bookRepository.delete(book);
    }

    public void checkIfBookExists(Books book) {
        final String isbn = book.getIsbn();
        final boolean bookInRepository = checkIsbn(isbn);
    
        if (bookInRepository) {
            throw new BookAlreadyExistsException("A book with the given ISBN already exists.");
        }
    }
    public void validateBook(Books book) {
        List<String> errors = new ArrayList<>();
    
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            errors.add("Title should not be empty.");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            errors.add("Author should not be empty.");
        }
        if (book.getTitle().length() > 50) {
            errors.add("Title have a maximum length of 50 characters.");
        }
        if (book.getAuthor().length() > 50) {
            errors.add("Author have a maximum length of 50 characters.");
        }
    
        if (!errors.isEmpty()) {
            throw new BookValidationException(errors);
        }
    }
    

    private boolean checkIsbn(String isbn) {
        final boolean existsByIsbn = bookRepository.existsByIsbn(isbn);

        if (existsByIsbn) {
            return true;
        }
        else {
            return false;
        }
    }
}
