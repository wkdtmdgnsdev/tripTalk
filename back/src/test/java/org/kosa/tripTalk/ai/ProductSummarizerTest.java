package org.kosa.tripTalk.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.review.ReviewResponseDTO;
import org.kosa.tripTalk.review.ReviewService;

class ProductSummarizerTest {

	private ReviewService reviewService;
    private ProductSummarizer summarizer;
    
    @BeforeEach
    void setUp() {
        reviewService = mock(ReviewService.class);
        summarizer = new ProductSummarizer(reviewService);
    }

    @Test
    @DisplayName("상품 설명에서 키워드를 정상적으로 추출한다")
    void extractKeywordsFromDescription_basic() {
        String description = "이 여행은 제주도에서 3박 4일 동안 진행되며, 휴식과 관광이 포함됩니다.";
        
        List<String> result = summarizer.extractKeywordsFromDescription(description);
        
        assertThat(result).isNotEmpty();
        assertThat(result).contains("제주도에서", "휴식과", "관광이");
    }
    
    @Test
    @DisplayName("상품 요약 문자열을 생성한다")
    void summarize_productWithReviews() {
        // given
        Product product = Product.builder()
                .id(1L)
                .title("부산 투어")
                .description("부산 해운대와 광안리를 둘러보는 여행")
                .price(150000)
                .address("부산광역시")
                .build();

        List<ReviewResponseDTO> dummyReviews = List.of(
                ReviewResponseDTO.builder().id(1L).rating(4).content("좋았어요 해운대").build(),
                ReviewResponseDTO.builder().id(2L).rating(5).content("광안리 뷰 최고에요").build()
        );

        when(reviewService.getReviewsByProduct(1L)).thenReturn(dummyReviews);

        // when
        String summary = summarizer.summarize(product);

        // then
        assertThat(summary).contains("부산 투어");
        assertThat(summary).contains("해운대", "광안리");
        assertThat(summary).contains("평점");
    }
    
    @Test
    @DisplayName("리뷰 평균 평점 및 키워드 요약 정상 동작")
    void summarizeReview_basic() {
        // given: 리뷰 3건 생성
        List<ReviewResponseDTO> reviews = List.of(
                ReviewResponseDTO.builder().id(1L).rating(5).content("정말 최고의 여행이었어요! 숙소도 좋고, 식사도 훌륭했습니다.").build(),
                ReviewResponseDTO.builder().id(2L).rating(4).content("편안한 휴식과 멋진 관광이 인상적이었어요.").build(),
                ReviewResponseDTO.builder().id(3L).rating(3).content("괜찮았지만 조금 아쉬웠어요. 음식이 평범했어요.").build()
        );

        // when
        String result = summarizer.summarizeReview(reviews);

        // then
        assertThat(result).contains("평점 4.0점");
        assertThat(result).contains("후기 키워드:"); // 키워드가 존재해야 함
        assertThat(result).doesNotContain("후기가 없습니다");
    }

    @Test
    @DisplayName("리뷰가 없을 때는 '후기가 없습니다' 반환")
    void summarizeReview_empty() {
        // when
        String result = summarizer.summarizeReview(List.of());

        // then
        assertThat(result).isEqualTo("후기가 없습니다.");
    }
}
