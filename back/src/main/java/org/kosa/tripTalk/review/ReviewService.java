package org.kosa.tripTalk.review;

import java.util.List;

import org.kosa.tripTalk.exception.NotFoundException;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.ProductRepository;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    public Long createReview(ReviewRequestDTO dto) {
        Product product = productNotFound(dto.getUserId());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("유저 없음"));

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        reviewRepository.save(review);
        
        return review.getId();
    }

    public void updateReview(Long reviewId, ReviewRequestDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰 없음"));
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByProduct(Long productId) {
    	Product product = productNotFound(productId);
    	
        return reviewRepository.findByProduct(product)
                .stream().map(ReviewResponseDTO::from).toList();
    }
    
    private Product productNotFound(Long userId) {
		Product product = productRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("상품 없음"));
		
		return product;
	}
}
