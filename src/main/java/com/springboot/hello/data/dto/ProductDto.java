package com.springboot.hello.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductDto {
    @Setter
    private String name;

    @Setter
    private int price;

    @Setter
    private int stock;

    public ProductDto(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

}
