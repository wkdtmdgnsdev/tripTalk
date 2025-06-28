package org.kosa.tripTalk.ai;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSummaryDTO {
    private Long productId;
    private String title;
    private String summaryText;
    private List<String> keywords;
}