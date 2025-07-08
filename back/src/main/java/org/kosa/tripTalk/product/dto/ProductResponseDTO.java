package org.kosa.tripTalk.product.dto;

import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.discount.DiscountDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductResponseDTO {
	private Long id;
    private String title;
    private String description;
    private int price;
    private String address;
    private DiscountDTO discount;
    private int discountedPrice;
    
    // dto 폼
    public static ProductResponseDTO from(Product product) {
        return ProductResponseDTO.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .price(product.getPrice())
            .address(product.getAddress())
            .discount(DiscountDTO.from(product.getDiscount()))
            .discountedPrice(product.getDiscountedPrice())
            .build();
    }
}
