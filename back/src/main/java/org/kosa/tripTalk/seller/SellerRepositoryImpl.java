package org.kosa.tripTalk.seller;

import java.time.LocalDate;
import java.util.List;

import org.kosa.tripTalk.exception.NotFoundException;
import org.kosa.tripTalk.payment.QPayment;
import org.kosa.tripTalk.product.QProduct;
import org.springframework.stereotype.Repository;
import static org.kosa.tripTalk.common.predicate.PredicateBuilder.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	@Override
	public List<?> getSalesByUnit(Long sellerId, LocalDate start, LocalDate end, String unit) {
		// 단위별로 매출 집계
        return switch (unit.toLowerCase()) {
            case "daily" -> getDailySales(sellerId, start, end);
            case "monthly" -> getGroupedSales(sellerId, start, end, "month");
            case "yearly" -> getGroupedSales(sellerId, start, end, "year");
            default -> throw new NotFoundException("지원하지 않는 단위입니다: " + unit);
        };
    }

	// 날짜별 결제 금액 합계 반환
    private List<DailySaleDTO> getDailySales(Long sellerId, LocalDate start, LocalDate end) {
        QPayment payment = QPayment.payment;
        QProduct product = QProduct.product;

        // 기간 조건
        BooleanExpression dateCond = between(
            payment.paymentDate,
            start.atStartOfDay(),
            end.plusDays(1).atStartOfDay()
        );

        // 결제일 단위로 각 날짜별 매출합 집계
        return queryFactory
            .select(Projections.constructor(
                DailySaleDTO.class,
                payment.paymentDate,
                Expressions.numberTemplate(Long.class, "SUM({0})", payment.amount)
            ))
            .from(payment)
            .join(payment.product, product)
            .where(
                payment.status.eq("SUCCESS"),
                product.seller.id.eq(sellerId),
                dateCond
            )
            .groupBy(payment.paymentDate)
            .orderBy(payment.paymentDate.asc())
            .fetch();
    }

    // 월별, 연도별 집계, unit 따라 월 또는 년 단위로 그룹핑
    private List<GroupedSaleDTO> getGroupedSales(Long sellerId, LocalDate start, LocalDate end, String unit) {
        QPayment payment = QPayment.payment;
        QProduct product = QProduct.product;

        BooleanExpression dateCond = between(
            payment.paymentDate,
            start.atStartOfDay(),
            end.plusDays(1).atStartOfDay()
        );

        StringTemplate groupKey = switch (unit) {
            case "month" -> Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM')", payment.paymentDate);
            case "year" -> Expressions.stringTemplate("TO_CHAR({0}, 'YYYY')", payment.paymentDate);
            default -> throw new NotFoundException("지원하지 않는 단위입니다: " + unit);
        };

        return queryFactory
            .select(Projections.constructor(
                GroupedSaleDTO.class,
                groupKey,
                Expressions.numberTemplate(Long.class, "SUM({0})", payment.amount)
            ))
            .from(payment)
            .join(payment.product, product)
            .where(
                payment.status.eq("SUCCESS"),
                product.seller.id.eq(sellerId),
                dateCond
            )
            .groupBy(groupKey)
            .orderBy(groupKey.asc())
            .fetch();
    }
}
