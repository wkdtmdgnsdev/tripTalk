package org.kosa.tripTalk.seller;

import java.time.LocalDateTime;

//  일별 매출 DTO (x축: LocalDate, y축: amount)
public record DailySaleDTO(LocalDateTime date, long amount) {

}
