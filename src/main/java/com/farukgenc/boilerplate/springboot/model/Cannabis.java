package com.farukgenc.boilerplate.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CANNABIS")
public class Cannabis {

    @Id
    private Integer id;
    private String uid;
    private String strain;
    private String cannabinoid_abbreviation;
    private String cannabinoid;
    private String terpene;
    private String medical_use;
    private String health_benefit;
    private String category;
    private String type;
    private String buzzword;
    private String brand;
}
