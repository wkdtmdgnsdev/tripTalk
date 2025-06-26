package org.kosa.tripTalk.seller;

// 월별/연도별 매출 DTO (x축: key = "2025-06", "2025", y축: amount)
public record GroupedSaleDTO(String key, long amount) {

}
