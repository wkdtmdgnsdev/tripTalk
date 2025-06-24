package org.kosa.tripTalk.review;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDTO {
    private Long id;
    private String userName;
    private Integer rating;
    private String content;

    public static ReviewResponseDTO from(org.kosa.tripTalk.review.Review review) {
        return ReviewResponseDTO.builder()
            .id(review.getId())
            .userName(review.getUser().getNickname())
            .rating(review.getRating())
            .content(review.getContent())
            .build();
    }
}