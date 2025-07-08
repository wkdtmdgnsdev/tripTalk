package org.kosa.tripTalk.product.repository;

import java.util.List;

import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.common.querydsl.QuerydslUtils;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.QProduct;
import org.kosa.tripTalk.product.discount.QDiscount;
import org.kosa.tripTalk.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	// 페이징, 검색, 정렬 리스트
	@Override
	public Page<ProductResponseDTO> searchAll(Pageable pageable, Search search) {
		QProduct product = QProduct.product;
		BooleanExpression condition = ProductPredicateBuilder.build(product, search);
		
		List<Product> contents = fetchContent(pageable, product, condition);
		Long total = fetchCount(product, condition);
		List<ProductResponseDTO> dtoList = toDtoList(contents);
		
		return new PageImpl<>(dtoList, pageable, total);
	}

	// dto변환
	private List<ProductResponseDTO> toDtoList(List<Product> contents) {
		return contents.stream()
						.map(ProductResponseDTO::from)
						.toList();
	}

	// 카운트
	private Long fetchCount(QProduct product, BooleanExpression condition) {
		return queryFactory
						.select(product.count())
						.from(product)
						.where(condition)
						.fetchOne();
	}

	// 내용 가져오기
	private List<Product> fetchContent(Pageable pageable, QProduct product, BooleanExpression condition) {
		return queryFactory
						.selectFrom(product)
						// 연관 엔티티를 지연 로딩하지 않고 즉시 함께 가져옴
						.leftJoin(product.discount, QDiscount.discount).fetchJoin()
						.where(condition)
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.orderBy(QuerydslUtils.toOrders(pageable.getSort(), product))
						.fetch();
	}
}
