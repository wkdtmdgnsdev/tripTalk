package org.kosa.tripTalk.reservation;

import lombok.RequiredArgsConstructor;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Reservation createReservation(ReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

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

        return reservationRepository.save(reservation);
    }
}