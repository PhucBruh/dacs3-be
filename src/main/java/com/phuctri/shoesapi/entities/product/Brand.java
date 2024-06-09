package com.phuctri.shoesapi.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String img_url;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Product> products;

    public Brand(String name, String img_url) {
        this.name = name;
        this.img_url = img_url;
    }
}
