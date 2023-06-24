package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.Books;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Books, String> {

    boolean existsByIsbn(String isbn);

    @Query("SELECT b FROM Books b WHERE b.publishDate > :date")
    List<Books> findAllPublishedAfter(@Param("date") LocalDate date);

    @Query("SELECT b FROM Books b WHERE b.author = :author")
    List<Books> findAllByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Books b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%'))")
    List<Books> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT COUNT(b) FROM Books b WHERE b.pages > :pages")
    Long countByPagesGreaterThan(@Param("pages") int pages);

    @Query("SELECT b FROM Books b WHERE b.publisher = :publisher ORDER BY b.publishDate DESC")
    Page<Books> findAllByPublisherOrderByPublishDateDesc(@Param("publisher") String publisher, Pageable pageable);
}
