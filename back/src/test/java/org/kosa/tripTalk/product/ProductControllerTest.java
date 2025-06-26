package org.kosa.tripTalk.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kosa.tripTalk.JwtTestConfig;
import org.kosa.tripTalk.exception.GlobalExceptionHandler;
import org.kosa.tripTalk.product.dto.ProductRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
@Import({JwtTestConfig.class, GlobalExceptionHandler.class})
class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
    @DisplayName("상품 등록 실패 - 제목 없음 (@NotBlank)")
    void createProduct_fail_whenTitleIsBlank() throws Exception {
		ProductRequestDTO request = ProductRequestDTO.builder()
		        .title("") // @NotBlank 위반
		        .description("설명입니다")
		        .address("서울시")
		        .price(10000)
		        .startDate(LocalDateTime.of(2025, 7, 1, 10, 0))
		        .endDate(LocalDateTime.of(2025, 7, 10, 10, 0))
		        .sellerId(1L)
		        .categoryId(1L)
		        .build();

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.message").exists());
    }
	
	@Test
    @DisplayName("상품 등록 실패 - 가격이 0원 (@Min)")
    void createProduct_fail_whenPriceIsZero() throws Exception {
		ProductRequestDTO request = ProductRequestDTO.builder()
		        .title("제목입니다")
		        .description("설명입니다")
		        .price(0) // 실패 테스트용
		        .address("서울")
		        .sellerId(1L)
		        .categoryId(1L)
		        .startDate(LocalDateTime.of(2025, 7, 1, 0, 0))
		        .endDate(LocalDateTime.of(2025, 7, 10, 0, 0))
		        .build();

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.message").exists());
    }
}
