package org.kosa.tripTalk.product;

import java.time.LocalDateTime;
import org.kosa.tripTalk.category.Category;
import org.kosa.tripTalk.product.discount.Discount;
import org.kosa.tripTalk.product.discount.DiscountDTO;
import org.kosa.tripTalk.seller.Seller;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Seller seller;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Discount discount;
	
	@Column(nullable = false)
	private Integer price;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column(nullable = false)
	private LocalDateTime endDate;

	// 수정DTO
	public void updateFromDTO(ProductRequestDTO dto) {
		this.title = dto.getTitle();
		this.description = dto.getDescription();
		this.address = dto.getAddress();
		this.price = dto.getPrice();
		this.startDate = dto.getStartDate();
		this.endDate = dto.getEndDate();
		
		Discount discount = DiscountDTO.toEntity(dto.getDiscount());
		// 할인 적용
		this.applyDiscount(discount);
	}
	
	// 할인 가격 계산 메소드
	public int getDiscountedPrice() {
		if(discount != null && discount.isActive())
			return discount.toPolicy().applyDiscount(price);
		
		return price;
	}
	
	// 할인 적용 및 양방향 연관 관계 설정
	// 객체지향적으로 관계를 일관되게 유지하기 위한 설계 원칙
	public void applyDiscount(Discount discount) {
		this.discount = discount;
		if(discount != null)
			discount.setProduct(this);
	}
}