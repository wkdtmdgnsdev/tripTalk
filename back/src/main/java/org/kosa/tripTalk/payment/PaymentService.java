package org.kosa.tripTalk.payment;

import lombok.RequiredArgsConstructor;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.kosa.tripTalk.reservation.ReservationRequest;
import org.kosa.tripTalk.reservation.ReservationService;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

	 // 결제 정보 저장소 (DB 접근)
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReservationService reservationService;

    // 결제 생성 메서드
    public Payment createPayment(PaymentRequest request) {
    	// 사용자 조회 (userId로)
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 상품 조회 (productId로)
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        // 결제 정보 생성
        Payment payment = Payment.builder()
                .user(user)
                .product(product)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status("APPROVED")
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .build();

        // 결제 정보를 DB에 저장
        return paymentRepository.save(payment);
    }

    // 결제 ID로 결제 조회 (Optional 반환)
    public Optional<Payment> getPayment(Long id) {
        return paymentRepository.findById(id);
    }
    
    // 결제 승인 처리 후 예약 생성
    public void approvePaymentAndCreateReservation(Long id, LocalDateTime approvedAt) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("결제 없음"));
        System.out.println(id); // 디버깅용 출력

        // 결제 상태와 승인일시 업데이트
        payment.setStatus("APPROVED");
        payment.setPaymentDate(approvedAt);
        paymentRepository.save(payment);

        // 예약 요청 객체 생성
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setUserId(payment.getUser().getId());
        reservationRequest.setProductId(payment.getProduct().getId());
        reservationRequest.setReservationDate(System.currentTimeMillis());
        reservationRequest.setTotalPrice(payment.getAmount());
        reservationRequest.setPaymentMethod(payment.getPaymentMethod());
        reservationRequest.setTransactionId(payment.getTransactionId());
        reservationRequest.setPaymentApprovedAt(approvedAt);

        // 예약 생성
        reservationService.createReservation(reservationRequest);
    }

}