package org.kosa.tripTalk.product;

import org.kosa.tripTalk.category.Category;
import org.kosa.tripTalk.category.CategoryRepository;
import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.exception.NotFoundException;
import org.kosa.tripTalk.product.discount.Discount;
import org.kosa.tripTalk.product.discount.DiscountDTO;
import org.kosa.tripTalk.product.dto.ProductRequestDTO;
import org.kosa.tripTalk.product.dto.ProductResponseDTO;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.kosa.tripTalk.seller.Seller;
import org.kosa.tripTalk.seller.SellerRepository;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final SellerRepository sellerRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	// 생성
	@Transactional
	public Long create(ProductRequestDTO request) {
		Seller seller = getSeller(request.getSellerId());
        Category category = getCategory(request.getCategoryId());
        
        Product product = request.toEntity(seller, category);
        productRepository.save(product);
        
        return product.getId();
	}

	// 상세
	public ProductResponseDTO getProductById(Long id) {
		Product product = notFoundProductId(id);
		
		return ProductResponseDTO.from(product);
	}

	// 수정
	@Transactional
	public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
		Product product = notFoundProductId(id);
		Discount discount = DiscountDTO.toEntity(dto.getDiscount());
		
		product.update(
			dto.getTitle(),
			dto.getDescription(),
			dto.getAddress(),
			dto.getPrice(),
			dto.getStartDate(),
			dto.getEndDate(),
			discount
		);

		return ProductResponseDTO.from(product);
	}

	// 리스트
	public Page<ProductResponseDTO> getAllProducts(Pageable pageable, Search search) {
		return productRepository.searchAll(pageable, search);
	}
	
	// 없는 상품 예외
	private Product notFoundProductId(Long id) {
		return productRepository.findById(id)
		        .orElseThrow(() -> new NotFoundException("해당 상품을 찾을 수 없습니다"));
	}
	
	// 판매자 조회
	private Seller getSeller(Long sellerId) {
		userRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("판매자 유저가 존재하지 않습니다."));
		
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("판매자 프로필이 존재하지 않습니다."));
	}
	
	// 카테고리 조회
	private Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리가 존재하지 않습니다."));
	}
	
	
}



















