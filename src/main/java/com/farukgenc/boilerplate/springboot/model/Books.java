package com.farukgenc.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOKS")
public class Books {

    @Id
    @Column(unique = true)
	private String isbn;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 50, message = "Title should not be more than 50 characters")
    private String title;
    private String subTitle;
    private String author;
    private LocalDate publishDate;
    private String publisher;
    private int pages;
    private String description;
    private String website;
    

}
