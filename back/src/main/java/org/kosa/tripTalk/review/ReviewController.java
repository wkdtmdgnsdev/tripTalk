package org.kosa.tripTalk.review;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

	private final ReviewService reviewService;
	
	// 리뷰 등록
	@PostMapping
    public ResponseEntity<Long> createReview(@RequestBody ReviewRequestDTO dto) {
        Long reviewId = reviewService.createReview(dto);
        return ResponseEntity.ok(reviewId);
    }

	// 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDTO dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    // 상품별 리뷰 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewResponseDTO> list = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(list);
    }
}
