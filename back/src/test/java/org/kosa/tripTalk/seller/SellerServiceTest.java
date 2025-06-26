package org.kosa.tripTalk.seller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kosa.tripTalk.category.Category;
import org.kosa.tripTalk.category.CategoryRepository;
import org.kosa.tripTalk.payment.Payment;
import org.kosa.tripTalk.payment.PaymentRepository;
import org.kosa.tripTalk.product.Product;
import org.kosa.tripTalk.product.repository.ProductRepository;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("매출 조회 테스트")
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SellerServiceTest {
	
	@Autowired SellerService sellerService;
    @Autowired SellerRepository sellerRepository;
    @Autowired UserRepository userRepository;
    @Autowired ProductRepository productRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired CategoryRepository categoryRepository;

	 private Long sellerId;

	    @BeforeEach
	    void setup() {
	        // 판매자 회원
	    	User user = userRepository.save(User.builder()
	    		    .userId("seller1")
	    		    .name("홍길동")
	    		    .email("seller@example.com")
	    		    .password("encoded-password")
	    		    .nickname("판매자1")
	    		    .phone("010-1234-5678")
	    		    .role(User.Role.SELLER)
	    		    .loginType("LOCAL")
	    		    .emailVerified(true)
	    		    .build());

	        // 판매자
	        Seller seller = sellerRepository.save(Seller.builder()
	                .user(user)
	                .userid(user.getUserId())
	                .businessName("판매자1")
	                .contact("010-1111-1111")
	                .build());
	        sellerId = seller.getId();
	        
	        Category category = categoryRepository.save(Category.builder()
	        	    .kind("여행")
	        	    .description("여행 관련 카테고리")
	        	    .iconUrl("https://example.com/icon.png")
	        	    .build());

	        // 상품
	        Product product = productRepository.save(Product.builder()
	        	    .title("부산 여행")
	        	    .description("부산 여행 상품 설명")
	        	    .address("부산 해운대구")
	        	    .price(100_000)
	        	    .startDate(LocalDateTime.now().plusDays(1))
	        	    .endDate(LocalDateTime.now().plusDays(5))
	        	    .seller(seller)
	        	    .category(category)
	        	    .build());

	        // 결제 성공
	        paymentRepository.save(Payment.builder()
	        	    .product(product)
	        	    .user(user)
	        	    .transactionId("TXN_SUCCESS_001")
	        	    .paymentMethod("CARD")
	        	    .amount(100_000)
	        	    .status("SUCCESS")
	        	    .paymentDate(LocalDateTime.now().minusDays(1))
	        	    .build());

	        // 결제 실패 (무시되어야 함)
	        paymentRepository.save(Payment.builder()
	        	    .product(product)
	        	    .user(user)
	        	    .transactionId("TXN_FAIL_001")
	        	    .paymentMethod("CARD")
	        	    .amount(100_000)
	        	    .status("FAILED")
	        	    .paymentDate(LocalDateTime.now())
	        	    .build());
	    }

	    @Test
	    @DisplayName("판매자 매출 일별 조회 성공")
	    void getSalesByDay_success() {
	        LocalDate start = LocalDate.now().minusDays(7);
	        LocalDate end = LocalDate.now();

	        List<?> result = sellerService.getSalesByUserId(sellerId, start, end, "daily");

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0)).isInstanceOf(DailySaleDTO.class);

	        DailySaleDTO dto = (DailySaleDTO) result.get(0);
	        assertThat(dto.amount()).isEqualTo(100_000L);
	    }

	    @Test
	    @DisplayName("판매자 매출 월별 조회 성공")
	    void getSalesByMonth_success() {
	        LocalDate start = LocalDate.now().withDayOfMonth(1);
	        LocalDate end = LocalDate.now();

	        List<?> result = sellerService.getSalesByUserId(sellerId, start, end, "monthly");

	        assertThat(result).hasSize(1);
	        assertThat(result.get(0)).isInstanceOf(GroupedSaleDTO.class);

	        GroupedSaleDTO dto = (GroupedSaleDTO) result.get(0);
	        assertThat(dto.amount()).isEqualTo(100_000L);
	    }

}
