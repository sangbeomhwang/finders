package net.datasa.finders.domain.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindFreelancerDTO {

	private String memberId;
	private String profileImg;
	private Double totalRating;
	private Integer totalPortfolios;
	private Integer totalReviews;
	private Integer totalProjects;
	private String[] skills;
}
