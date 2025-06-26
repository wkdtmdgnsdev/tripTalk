package org.kosa.tripTalk.seller;

import java.time.LocalDate;
import java.util.List;

import org.kosa.tripTalk.exception.NotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService {
	private final SellerRepository sellerRepository;
	private final SellerRepositoryImpl sellerRepositoryImpl;

     // 로그인한 사용자의 ID로 판매자를 조회하고,
     // 해당 판매자의 기간별 매출을 단위에 따라 조회한다.
    public List<?> getSalesByUserId(Long userId, LocalDate start, LocalDate end, String unit) {
        Seller seller = sellerRepository.findById(userId)
        		.orElseThrow(() -> new NotFoundException("판매자 정보를 찾을 수 없습니다."));

        return sellerRepositoryImpl.getSalesByUnit(seller.getId(), start, end, unit);
    }
}
