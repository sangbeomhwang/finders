package net.datasa.finders.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.finders.domain.entity.MemberEntity;
import net.datasa.finders.domain.entity.RoleName;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FreelancerDTO {
	
	String freelancerId;
	String freelancerPhone;
	String address;
	String postalCode;
	String country;
	LocalDateTime lastLogin;
	MemberEntity member;
	
}