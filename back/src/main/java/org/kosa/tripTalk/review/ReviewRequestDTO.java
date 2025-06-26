package org.kosa.tripTalk.review;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewRequestDTO {
    private Long productId;
    private Long userId;
    private Integer rating;
    private String content;
}