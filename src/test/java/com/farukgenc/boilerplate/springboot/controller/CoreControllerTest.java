package com.farukgenc.boilerplate.springboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import com.farukgenc.boilerplate.springboot.model.Books;
import org.hamcrest.Matchers;

import java.time.LocalDate;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class CoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateBookSuccess() throws Exception {

        String isbn = "test_book";
        String title = "Test Book";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example1.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))
        .andExpect(status().isCreated());
    }


    @Test
    public void testCreateBookDuplicate() throws Exception {
        String isbn = "going_to_be_dupe";
        String title = "Test Book";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        // Create the book for the first time (201)
        mockMvc.perform(post("/api/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.isbn", Matchers.is(isbn)))
        .andExpect(jsonPath("$.title", Matchers.is(title)))
        .andExpect(jsonPath("$.author", Matchers.is(author)))
        .andExpect(jsonPath("$.publishDate", Matchers.is(publishDate.toString())))
        .andExpect(jsonPath("$.publisher", Matchers.is(publisher)))
        .andExpect(jsonPath("$.pages", Matchers.is(pages)))
        .andExpect(jsonPath("$.description", Matchers.is(description)))
        .andExpect(jsonPath("$.website", Matchers.is(website)));

        // Tries to create the same book again (409)
        mockMvc.perform(post("/api/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message[0]", Matchers.is("A book with the given ISBN already exists.")));
    }


    @Test
    public void testGetBookNotFound() throws Exception {
        String testIsbn = "isbn_that_does_not_exist";
        mockMvc.perform(get("/book").param("isbn", testIsbn))
        .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBook() throws Exception {
        String isbn = "book";
        String title = "Test Book";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example1.com";
    
        Books book = Books.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .publishDate(publishDate)
                .publisher(publisher)
                .pages(pages)
                .description(description)
                .website(website)
                .build();
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/book").param("isbn", isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", Matchers.is(isbn)))
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.author", Matchers.is(author)))
                .andExpect(jsonPath("$.publishDate", Matchers.is(publishDate.toString())))
                .andExpect(jsonPath("$.publisher", Matchers.is(publisher)))
                .andExpect(jsonPath("$.pages", Matchers.is(pages)))
                .andExpect(jsonPath("$.description", Matchers.is(description)))
                .andExpect(jsonPath("$.website", Matchers.is(website)));
    }
    
    @Test
    public void testGetBooks() throws Exception {
        
        String isbn1 = "book1";
        String title1 = "Test Book 1";
        String author1 = "Test Author 1";
        LocalDate publishDate1 = LocalDate.of(2022, 1, 1);
        String publisher1 = "Test Publisher 1";
        int pages1 = 200;
        String description1 = "Test Description 1";
        String website1 = "https://example1.com";
    
        String isbn2 = "book2";
        String title2 = "Test Book 2";
        String author2 = "Test Author 2";
        LocalDate publishDate2 = LocalDate.of(2022, 1, 2);
        String publisher2 = "Test Publisher 2";
        int pages2 = 250;
        String description2 = "Test Description 2";
        String website2 = "https://example2.com";

        Books book1 = Books.builder()
                .isbn(isbn1)
                .title(title1)
                .author(author1)
                .publishDate(publishDate1)
                .publisher(publisher1)
                .pages(pages1)
                .description(description1)
                .website(website1)
                .build();
        Books book2 = Books.builder()
                .isbn(isbn2)
                .title(title2)
                .author(author2)
                .publishDate(publishDate2)
                .publisher(publisher2)
                .pages(pages2)
                .description(description2)
                .website(website2)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String book1Json = objectMapper.writeValueAsString(book1);
        String book2Json = objectMapper.writeValueAsString(book2);
        
        mockMvc.perform(post("/api/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(book1Json))
        .andExpect(status().isCreated());

        mockMvc.perform(post("/api/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(book2Json))
        .andExpect(status().isCreated());
        

        mockMvc.perform(get("/api/books")
                .param("page", "1")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].isbn", Matchers.is(isbn2)))
                .andExpect(jsonPath("$.content[0].title", Matchers.is(title2)))
                .andExpect(jsonPath("$.content[0].author", Matchers.is(author2)))
                .andExpect(jsonPath("$.content[0]publishDate", Matchers.is(publishDate2.toString())))
                .andExpect(jsonPath("$.content[0].publisher", Matchers.is(publisher2)))
                .andExpect(jsonPath("$.content[0].pages", Matchers.is(pages2)))
                .andExpect(jsonPath("$.content[0].description", Matchers.is(description2)))
                .andExpect(jsonPath("$.content[0].website", Matchers.is(website2)));
    }

    @Test
    public void testCreateBookWithEmptyTitle() throws Exception {

        String isbn = "book";
        String title = "";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Title should not be empty.")));
    }

    @Test
    public void testCreateBookWithEmptyAuthor() throws Exception {

        String isbn = "book";
        String title = "Test Book";
        String author = "";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Author should not be empty.")));
    }

    @Test
    public void testCreateBookWithLongTitle() throws Exception {
        String isbn = "book";
        String title = String.join("", Collections.nCopies(51, "a"));
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Title have a maximum length of 50 characters.")));
    }

    @Test
    public void testCreateBookWithLongAuthor() throws Exception {
        String isbn = "book";
        String title = "Test Book";
        String author = String.join("", Collections.nCopies(51, "a"));
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books book = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Author have a maximum length of 50 characters.")));
    }

    @Test
    public void testUpdateBook() throws Exception {

        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books existingBook = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        String newDescription = "Updated Description";

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(title)
        .author(author)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(newDescription)
        .website(website)
        .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String existingBookJson = objectMapper.writeValueAsString(existingBook);
        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(existingBookJson))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", Matchers.is(isbn)))
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.author", Matchers.is(author)))
                .andExpect(jsonPath("$.publishDate", Matchers.is(publishDate.toString())))
                .andExpect(jsonPath("$.publisher", Matchers.is(publisher)))
                .andExpect(jsonPath("$.pages", Matchers.is(pages)))
                .andExpect(jsonPath("$.description", Matchers.is(newDescription)))
                .andExpect(jsonPath("$.website", Matchers.is(website)));
    }

    //

    @Test
    public void testUpdateBookWithLongTitle() throws Exception {
        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books existingBook = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());

        String newTitle = String.join("", Collections.nCopies(51, "a"));

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(newTitle)
        .author(author)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(description)
        .website(website)
        .build();

        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Title have a maximum length of 50 characters.")));
    }

    @Test
    public void testUpdateBookWithLongAuthor() throws Exception {
        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books existingBook = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());

        String newAuthor = String.join("", Collections.nCopies(51, "a"));;

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(title)
        .author(newAuthor)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(description)
        .website(website)
        .build();

        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Author have a maximum length of 50 characters.")));
    }

    @Test
    public void testUpdateBookWithEmptyTitle() throws Exception {
        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books existingBook = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());

        String newTitle = "";

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(newTitle)
        .author(author)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(description)
        .website(website)
        .build();

        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Title should not be empty.")));
    }

    @Test
    public void testUpdateBookWithEmptyAuthor() throws Exception {
        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";

        Books existingBook = Books.builder()
            .isbn(isbn)
            .title(title)
            .author(author)
            .publishDate(publishDate)
            .publisher(publisher)
            .pages(pages)
            .description(description)
            .website(website)
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookJson = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated());

        String newAuthor = "";

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(title)
        .author(newAuthor)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(description)
        .website(website)
        .build();

        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]", Matchers.is("Author should not be empty.")));
    }


    //
    
    @Test
    public void testUpdateBookIdempotency() throws Exception {

        String isbn = "book";
        String title = "Test Title";
        String author = "Test Author";
        LocalDate publishDate = LocalDate.of(2022, 1, 1);
        String publisher = "Test Publisher";
        int pages = 200;
        String description = "Test Description";
        String website = "https://example.com";
        

        Books updatedBook = Books.builder()
        .isbn(isbn)
        .title(title)
        .author(author)
        .publishDate(publishDate)
        .publisher(publisher)
        .pages(pages)
        .description(description)
        .website(website)
        .build();
    
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(post("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", Matchers.is(isbn)))
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.author", Matchers.is(author)))
                .andExpect(jsonPath("$.publishDate", Matchers.is(publishDate.toString())))
                .andExpect(jsonPath("$.publisher", Matchers.is(publisher)))
                .andExpect(jsonPath("$.pages", Matchers.is(pages)))
                .andExpect(jsonPath("$.description", Matchers.is(description)))
                .andExpect(jsonPath("$.website", Matchers.is(website)));

        mockMvc.perform(put("/api/book/{isbn}", isbn)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", Matchers.is(isbn)))
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.author", Matchers.is(author)))
                .andExpect(jsonPath("$.publishDate", Matchers.is(publishDate.toString())))
                .andExpect(jsonPath("$.publisher", Matchers.is(publisher)))
                .andExpect(jsonPath("$.pages", Matchers.is(pages)))
                .andExpect(jsonPath("$.description", Matchers.is(description)))
                .andExpect(jsonPath("$.website", Matchers.is(website)));
    }
}
