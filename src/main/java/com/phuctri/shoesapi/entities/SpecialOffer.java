package com.phuctri.shoesapi.entities;

import com.phuctri.shoesapi.entities.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SpecialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double value;

    @ManyToOne
    private Product product;

    private Boolean active;
}
