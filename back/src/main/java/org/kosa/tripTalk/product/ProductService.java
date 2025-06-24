package org.kosa.tripTalk.product;

import org.kosa.tripTalk.category.Category;
import org.kosa.tripTalk.category.CategoryRepository;
import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.exception.NotFoundException;
import org.kosa.tripTalk.seller.Seller;
import org.kosa.tripTalk.seller.SellerRepository;
import org.kosa.tripTalk.user.User;
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
		 // 1. 판매자 유저 조회
		User sellerUser = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new NotFoundException("판매자 유저가 존재하지 않습니다."));
		
		// 2. 판매자 프로필 조회
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new NotFoundException("판매자 프로필이 존재하지 않습니다."));
        
     // 3. 카테고리 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리가 존재하지 않습니다."));
        
     // 4. 상품 생성 및 저장
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
	public Product update(Long id, ProductRequestDTO dto) {
		Product product = notFoundProductId(id);
		product.updateFromDTO(dto);

		return product;
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
}



















