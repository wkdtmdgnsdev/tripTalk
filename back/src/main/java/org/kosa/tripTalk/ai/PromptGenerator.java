package org.kosa.tripTalk.ai;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PromptGenerator {
	
	public String generatePrompt(String userInput, List<ProductSummaryDTO> candidates) {
		StringBuilder sb = new StringBuilder();
		sb.append("다음은 고객의 관심사 : ").append(userInput).append("\n\n")
		  .append("다음은 자사 여행 상품 요약입니다.\n");
		
		for(int i=0; i<candidates.size(); i++) {
			ProductSummaryDTO dto = candidates.get(i);
			sb.append("[%d. %s]\n".formatted(i +1, dto.getTitle()));
			sb.append(dto.getSummaryText()).append("\n\n");
		}
		
		sb.append("너가 영업 사원이 되어 꼭팔아야하니 고객의 관심사에 가장 적합한 3개를 골라 추천해줘");
		
		return sb.toString();
	}
}
