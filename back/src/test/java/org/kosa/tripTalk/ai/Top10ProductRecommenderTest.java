package org.kosa.tripTalk.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.review.ReviewResponseDTO;
import org.kosa.tripTalk.review.ReviewService;

class Top10ProductRecommenderTest {

	private Top10ProductRecommender recommender;
    private ProductSummarizer summarizer;
    
    @BeforeEach
    void setup() {
        // Mock 객체 생성: 실제 ReviewService 대신 가짜로 대체
        ReviewService reviewService = mock(ReviewService.class);

        // summarize에서 사용하는 리뷰 응답을 미리 정의해둠
        when(reviewService.getReviewsByProduct(any()))
            .thenReturn(List.of(
                ReviewResponseDTO.builder()
                    .id(1L)
                    .rating(5)
                    .content("정말 편하고 좋았어요!") // 후기 키워드 추출용
                    .build()
            ));

        // 의존성 주입된 ProductSummarizer 생성
        summarizer = new ProductSummarizer(reviewService);
        recommender = new Top10ProductRecommender(summarizer);
    }
    
    @Test
    void recommend_유사도_기준으로_상품_리스트_정렬() {
        // given: 테스트용 상품 2개 생성
        Product product1 = Product.builder()
            .id(1L)
            .title("힐링 제주 여행")
            .description("제주 자연에서 휴식") // 키워드와 요약에서 사용
            .price(200000)
            .address("제주")
            .build();

        Product product2 = Product.builder()
            .id(2L)
            .title("서울 시티투어")
            .description("서울 명소를 둘러보는 여행")
            .price(100000)
            .address("서울")
            .build();

        String userInput = "자연 속에서 힐링하고 싶은 여행";

        // when: 추천 로직 실행
        List<ProductSummaryDTO> result = recommender.recommend(userInput, List.of(product1, product2));

        // then: 결과 검증
        assertEquals(2, result.size()); // 결과 개수 검증
        assertTrue(result.get(0).getTitle().contains("제주")); // 유사도 높은 제주 상품이 첫 번째여야 함
    }
}
