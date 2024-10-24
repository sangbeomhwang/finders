package net.datasa.finders.domain.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    String memberId;
    String memberPw;
    String memberName;
    String email;
	
	Integer clientId;
	String clientPhone;
	String industry;
	LocalDate foundedDate;
	Integer employeeCount;
	String website;
	String postalCode;
	String address;
	String detailAddress;
	String extraAddress;

	
	
	List<String> fields;
	List<String> categorys;
}
