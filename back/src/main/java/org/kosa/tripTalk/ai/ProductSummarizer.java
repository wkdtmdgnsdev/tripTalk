package org.kosa.tripTalk.ai;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.review.ReviewResponseDTO;
import org.kosa.tripTalk.review.ReviewService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductSummarizer {
	
	private final ReviewService reviewService;
	private static final int MAX_PRODUCTS = 10;
	
	// 상품(정보), 리뷰 (평점, 키워드) 요약
	public String summarize(Product product) {
		List<ReviewResponseDTO> reviews = reviewService.getReviewsByProduct(product.getId());
		
        return """
        [%s]
        설명: %s
        가격: %,d원
        주소: %s
        특징 키워드: %s
        후기 요약: %s
        """.formatted(
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getAddress(),
                String.join(", ", extractKeywordsFromDescription(product.getDescription())),
                summarizeReview(reviews)
        ).trim();
    }
	
	// 상품마다 상위 10개 키워드 제한
	public String summarizeList(List<Product> products) {
		return products.stream()
				.limit(MAX_PRODUCTS)
				.map(this::summarize)
				.collect(Collectors.joining("\n\n"));
	}
	
	// 리뷰 요약, 평점, 키워드
	String summarizeReview(List<ReviewResponseDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) return "후기가 없습니다.";

        List<ReviewResponseDTO> top10 = reviews.stream()
                .sorted(Comparator.comparing(ReviewResponseDTO::getId).reversed())
                .limit(10)
                .toList();

        List<String> keywords = extractKeywordsFromReviews(top10);
        double avg = reviews.stream().mapToInt(ReviewResponseDTO::getRating).average().orElse(0.0);

        return "평점 %.1f점, 후기 키워드: %s"
                .formatted(avg, String.join(", ", keywords));
    }
	
	public List<String> extractKeywordsFromDescription(String description) {
        if (description == null || description.isBlank()) return List.of("정보 없음");
        return extractTopKeywords(List.of(description), 10);
    }
	
	private List<String> extractKeywordsFromReviews(List<ReviewResponseDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) return List.of("후기 없음");

        List<String> reviewContents = reviews.stream()
                .map(ReviewResponseDTO::getContent)
                .toList();

        return extractTopKeywords(reviewContents, 10);
    }
	
	// 상위 10 리스트에서 추출
	private List<String> extractTopKeywords(List<String> texts, int limit) {
        return texts.stream()
                .flatMap(text -> Arrays.stream(
                        text.replaceAll("[^가-힣a-zA-Z0-9 ]", "").split("\\s+")))
                .map(String::trim)
                .filter(word -> word.length() >= 2)
                .filter(word -> !List.of("이", "그", "그리고", "또한", "더욱", "숙소", "상품", "여행", "했어요").contains(word))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
