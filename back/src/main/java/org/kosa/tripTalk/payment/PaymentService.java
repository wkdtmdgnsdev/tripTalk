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

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReservationService reservationService;

    public Payment createPayment(PaymentRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        Payment payment = Payment.builder()
                .user(user)
                .product(product)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status("READY")
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPayment(Long id) {
        return paymentRepository.findById(id);
    }
    public void approvePaymentAndCreateReservation(Long id, LocalDateTime approvedAt) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("결제 없음"));
        System.out.println(id);

        payment.setStatus("SUCCESS");
        payment.setPaymentDate(approvedAt);
        paymentRepository.save(payment);

        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setUserId(payment.getUser().getId());
        reservationRequest.setProductId(payment.getProduct().getId());
        reservationRequest.setReservationDate(System.currentTimeMillis());
        reservationRequest.setTotalPrice(payment.getAmount());
        reservationRequest.setPaymentMethod(payment.getPaymentMethod());
        reservationRequest.setTransactionId(payment.getTransactionId());
        reservationRequest.setPaymentApprovedAt(approvedAt);

        reservationService.createReservation(reservationRequest);
    }

}