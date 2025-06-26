package org.kosa.tripTalk.reservation;

import lombok.RequiredArgsConstructor;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 예약 생성 메서드
    public Reservation createReservation(ReservationRequest request) {
    	// 요청에서 전달된 userId로 사용자 조회 (없으면 예외 발생)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // 요청에서 전달된 productId로 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 조회한 사용자와 상품 정보를 기반으로 예약 엔티티 생성
        Reservation reservation = Reservation.builder()
        	    .user(user)
        	    .product(product)
        	    .reservationDate(request.getReservationDate())
        	    .totalPrice(request.getTotalPrice())
        	    .status("예약완료")
        	    .paymentMethod(request.getPaymentMethod())
        	    .transactionId(request.getTransactionId())
        	    .paymentApprovedAt(request.getPaymentApprovedAt())
        	    .build();

        // 예약 정보를 DB에 저장 후 반환
        return reservationRepository.save(reservation);
    }
}