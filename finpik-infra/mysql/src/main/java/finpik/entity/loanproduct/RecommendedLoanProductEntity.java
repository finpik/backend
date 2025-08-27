package finpik.entity.loanproduct;

import finpik.entity.enums.LoanProductBadge;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RecommendedLoanProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long profileId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "loan_product_id")
    private LoanProductEntity loanProductEntity;
    private Float similarity;
    @ElementCollection(fetch = EAGER)
    private List<LoanProductBadge> loanProductBadgeList;

    public static RecommendedLoanProductEntity of(
        Long profileId, LoanProductEntity loanProductEntity, Float similarity,
        List<LoanProductBadge> loanProductBadgeList
    ) {
        return new RecommendedLoanProductEntity(
            null, profileId, loanProductEntity, similarity, loanProductBadgeList
        );
    }
}
