package org.kosa.tripTalk.seller;

import java.time.LocalDate;
import java.util.List;

public interface SellerRepositoryCustom {
	// 단위(daily/monthly/yearly)에 따른 매출 통계 조회
	List<?> getSalesByUnit(Long sellerId, LocalDate start, LocalDate end, String unit);
}
