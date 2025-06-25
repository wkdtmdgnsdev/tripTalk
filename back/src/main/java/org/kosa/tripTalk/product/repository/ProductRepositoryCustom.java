package org.kosa.tripTalk.product.repository;

import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
	Page<ProductResponseDTO> searchAll(Pageable pageable, Search search);
}
