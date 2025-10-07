package finpik.entity.loanproduct;

import finpik.entity.enums.LoanProductBadge;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RecommendedLoanProductEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private Long profileId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "loan_product_id")
    private LoanProductEntity loanProductEntity;
    private Float similarity;
    @ElementCollection(fetch = LAZY)
    private List<LoanProductBadge> loanProductBadgeList;

    public static RecommendedLoanProductEntity of(
        Long profileId, LoanProductEntity loanProductEntity, Float similarity,
        List<LoanProductBadge> loanProductBadgeList
    ) {
        return new RecommendedLoanProductEntity(
            UUID.randomUUID(), profileId, loanProductEntity, similarity, loanProductBadgeList
        );
    }
}
