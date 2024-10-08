package net.datasa.finders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datasa.finders.domain.dto.FreelancerPortfoliosDTO;
import net.datasa.finders.domain.entity.FreelancerPortfoliosEntity;
import net.datasa.finders.domain.entity.MemberEntity;
import net.datasa.finders.repository.FreelancerPortfoliosRepository;
import net.datasa.finders.repository.FreelancerRepository;
import net.datasa.finders.repository.MemberRepository;
import net.datasa.finders.security.AuthenticatedUser;

@Service
@Transactional
@RequiredArgsConstructor
public class FreelancerPortfoliosService {
	
	private final FreelancerPortfoliosRepository freelancerPortfoliosRepository;
	private final FreelancerRepository freelancerRepository;
	private final MemberRepository memberRepository;
	
	public void save(FreelancerPortfoliosDTO fPDTO, AuthenticatedUser user) {
		
		MemberEntity memberEntity = memberRepository.findById(user.getUsername()).orElseThrow(() -> new EntityNotFoundException("회원정보가 없습니다."));
		
		FreelancerPortfoliosEntity freelancerPortfoliosEntity = FreelancerPortfoliosEntity.builder()
				.portfolioTitle(fPDTO.getPortfolioTitle())
				.portfolioDescription(fPDTO.getPortfolioDescription())
				.portfolioLink(fPDTO.getProjectLink())
				.member(memberEntity)
				.build();
		
		freelancerPortfoliosRepository.save(freelancerPortfoliosEntity);
	}

	public List<FreelancerPortfoliosDTO> findPortfolioList(String memberId) {
		
		MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원정보가 없습니다."));
		
		List<FreelancerPortfoliosEntity> freelancerPortfoliosEntityList = freelancerPortfoliosRepository.findByMember(memberEntity);
		ArrayList<FreelancerPortfoliosDTO> freelancerPortfoliosDTOList = new ArrayList<>();
		
		for (FreelancerPortfoliosEntity freelancerPortfoliosEntity : freelancerPortfoliosEntityList) {
			FreelancerPortfoliosDTO freelancerPortfoliosDTO = FreelancerPortfoliosDTO.builder()
					.portfolioId(freelancerPortfoliosEntity.getPortfolioId())
					.portfolioTitle(freelancerPortfoliosEntity.getPortfolioTitle())
					.build();
			freelancerPortfoliosDTOList.add(freelancerPortfoliosDTO);
		}
		
		return freelancerPortfoliosDTOList;
	}

	public FreelancerPortfoliosDTO findPortfolioById(int portfolioId) {
		
		FreelancerPortfoliosEntity freelancerPortfoliosEntity = freelancerPortfoliosRepository.findById(portfolioId)
					.orElseThrow(() -> new EntityNotFoundException("포트폴리오정보가 없습니다."));
		
		FreelancerPortfoliosDTO freelancerPortfoliosDTO = FreelancerPortfoliosDTO.builder()
				.portfolioId(freelancerPortfoliosEntity.getPortfolioId())
				.portfolioTitle(freelancerPortfoliosEntity.getPortfolioTitle())
				.portfolioDescription(freelancerPortfoliosEntity.getPortfolioDescription())
				.build();
		
		return freelancerPortfoliosDTO;
	}

}
