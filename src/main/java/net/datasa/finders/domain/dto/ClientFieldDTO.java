package net.datasa.finders.domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientFieldDTO {
	
	Integer fieldNum;
	String clientId;
	String fieldText;
}
