package com.springboot.hello.data.dto;

import lombok.Getter;
import lombok.Setter;

public class ProductResponseDto {
    @Getter
    @Setter
    private Long number;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int price;

    @Getter
    @Setter
    private int stock;

    public ProductResponseDto() {

    }

    public  ProductResponseDto(Long number, String name, int price, int stock) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
