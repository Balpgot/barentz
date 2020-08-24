package com.barentzconnection.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table
public class LinkDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;

    public LinkDAO(String name, String link) {
        this.name = name;
        this.link = link;
    }
}
