package net.datasa.finders.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerReviewDTO {
    private int reviewId;
    private int projectNum;
    private String clientId;
    private String freelancerId;
    private float rating;
    private String comment;
    private List<ReviewItemDTO> reviewItems;
    private List<FreelancerDataDTO> freelancerData; // 프리랜서 리스트 추가
}
