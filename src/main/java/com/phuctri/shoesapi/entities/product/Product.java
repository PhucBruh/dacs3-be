package com.phuctri.shoesapi.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phuctri.shoesapi.entities.Inventory;
import com.phuctri.shoesapi.entities.SpecialOffer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double promotionalPrice;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Color> colors = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Size> sizes = new ArrayList<>();

    private String mainImg;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imgs = new ArrayList<>();

    private Long totalSold;

    private Double rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Inventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SpecialOffer> specialOffers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(String name,
                   String description,
                   Double price,
                   Double promotionalPrice,
                   String mainImg,
                   Long totalSold) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.promotionalPrice = promotionalPrice;
        this.mainImg = mainImg;
        this.totalSold = totalSold;
    }
}
