package org.kosa.tripTalk.product;

import org.kosa.tripTalk.common.dto.PageRequestDTO;
import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.product.dto.ProductRequestDTO;
import org.kosa.tripTalk.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;
	
	// 등록
	@PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody @Valid ProductRequestDTO request) {
        Long productId = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

	// 상세
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}
	
	// 수정
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(
			@PathVariable Long id,
			@RequestBody ProductRequestDTO requestDTO) {
		return ResponseEntity.ok(productService.update(id, requestDTO));
	}
	
	// 리스트
	@GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
    		PageRequestDTO pageRequestDTO,
    		Search search
    		) {
		Pageable pageable = pageRequestDTO.toPageable();
		Page<ProductResponseDTO> result = productService.getAllProducts(pageable, search);
        return ResponseEntity.ok(result);
    }
}
