package org.kosa.tripTalk.ai;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiRecommendationConroller {
	
	private final AiRecommendationService recommendationService;
	
	@PostMapping("/recommend")
	public ResponseEntity<List<ProductSummaryDTO>> recommend(@RequestBody UserInputDTO input) {
		List<ProductSummaryDTO> result = recommendationService.recommend(input.getInput());
		return ResponseEntity.ok(result);
	}
}
