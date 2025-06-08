package finpik.profile.entity.policy;

import java.util.EnumMap;
import java.util.Map;

import finpik.entity.enums.CreditGradeStatus;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;

public class NiceCreditGradePolicy {
    private static final Map<CreditGradeStatus, GradeRange> gradeRanges = new EnumMap<>(CreditGradeStatus.class);

    static {
        gradeRanges.put(CreditGradeStatus.EXCELLENT, new GradeRange(870, 1000, "아주 안정적이에요"));
        gradeRanges.put(CreditGradeStatus.GOOD, new GradeRange(805, 869, "꽤 괜찮아요"));
        gradeRanges.put(CreditGradeStatus.FAIR, new GradeRange(665, 804, "조금 불안정해요"));
        gradeRanges.put(CreditGradeStatus.POOR, new GradeRange(515, 664, "가끔 어려워요"));
        gradeRanges.put(CreditGradeStatus.VERY_POOR, new GradeRange(0, 514, "관리가 필요해요"));
    }

    public static CreditGradeStatus fromScore(Integer score) {
        return gradeRanges.entrySet().stream().filter(entry -> entry.getValue().contains(score)).map(Map.Entry::getKey)
                .findFirst().orElseThrow(() -> new BusinessException(ErrorCode.OUT_OF_RANGE_CREDIT_GRADE_STATUS));
    }

    public static String getDescription(CreditGradeStatus status) {
        return gradeRanges.get(status).description();
    }

    public static GradeRange getRange(CreditGradeStatus status) {
        return gradeRanges.get(status);
    }

    public record GradeRange(Integer min, Integer max, String description) {
        public boolean contains(Integer score) {
            return score >= min && score <= max;
        }
    }
}
