package org.kosa.tripTalk.ai;

import java.util.Comparator;
import java.util.List;

import org.kosa.tripTalk.product.Product;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Top10ProductRecommender {
	
	private final ProductSummarizer summarizer;
	private static final int MAX_RECOMMENDED = 10;
	
	public List<ProductSummaryDTO> recommend(String userInput, List<Product> products) {
		
		// 후보 상품들 요약
		List<SimilarityScoreDTO> scored = products.stream()
				.map(product -> {
					String summary = summarizer.summarize(product);
					double score = calculateSimilarity(userInput, summary);
					
					return SimilarityScoreDTO.builder()
							.product(ProductSummaryDTO.builder()
									 .productId(product.getId())
									 .title(product.getTitle())
									 .summaryText(summary)
									 .keywords(summarizer.extractKeywordsFromDescription(product.getDescription()))
									 .build())
								.score(score)
								.build();
				})
				.sorted(Comparator.comparing(SimilarityScoreDTO::getScore, Comparator.reverseOrder()))
				.limit(MAX_RECOMMENDED)
				.toList();
		
		return scored.stream()
                .map(SimilarityScoreDTO::getProduct)
                .toList();
	}
	
	// 유사도 측정 (자카드 유사도 방식)
	private double calculateSimilarity(String input, String summary) {
        List<String> inputWords = tokenize(input);
        List<String> summaryWords = tokenize(summary);

        long intersection = inputWords.stream().filter(summaryWords::contains).count();
        long union = inputWords.stream().distinct().count() + summaryWords.stream().distinct().count() - intersection;

        return union == 0 ? 0.0 : (double) intersection / union;
    }
	
	private List<String> tokenize(String text) {
        return List.of(text.replaceAll("[^가-힣a-zA-Z0-9 ]", "")
                .split("\\s+")).stream()
                .map(String::trim)
                .filter(word -> word.length() >= 2)
                .toList();
    }
}
