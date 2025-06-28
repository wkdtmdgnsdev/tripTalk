package org.kosa.tripTalk.ai;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimilarityScoreDTO {
    private ProductSummaryDTO product;
    private double score;
}