package org.kosa.tripTalk.ai;

import java.util.List;

import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {
	
	private final ProductRepository productRepository;
	private final Top10ProductRecommender recommender;
	private final PromptGenerator promptGenerator;

	public List<ProductSummaryDTO> recommend(String input) {
		List<Product> products = productRepository.findAll();
		List<ProductSummaryDTO> top10 = recommender.recommend(input, products);
		
		promptGenerator.generatePrompt(input, top10);
		
		return top10;
	}

}
