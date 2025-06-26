package org.kosa.tripTalk.seller;

import java.time.LocalDate;

import org.kosa.tripTalk.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class SellerController {
	private final SellerService sellerService;
	
	// 판매자 매출 통계 조회 (일/월/연 단위)
    @GetMapping("/sales")
    public ResponseEntity<?> getSales(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "daily") String unit,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                sellerService.getSalesByUserId(user.getId(), start, end, unit)
        );
    }
}
