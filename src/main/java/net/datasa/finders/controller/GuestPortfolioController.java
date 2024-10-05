package net.datasa.finders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.finders.domain.dto.FreelancerPortfoliosDTO;
import net.datasa.finders.security.AuthenticatedUser;
import net.datasa.finders.service.FreelancerPortfoliosService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("guestportfolio")
public class GuestPortfolioController {
	
	private final FreelancerPortfoliosService freelancerPortfoliosService;

	@PostMapping("update")
	public String updatePortfolio(@ModelAttribute FreelancerPortfoliosDTO updatedPortfolio,
	                              @AuthenticationPrincipal AuthenticatedUser user) throws Exception {
	    freelancerPortfoliosService.updatePortfolio(updatedPortfolio, user.getId());
	    return "redirect:/guestportfolio/content?portfolioId=" + updatedPortfolio.getPortfolioId();
	}

	@GetMapping("content")
    public String content(@RequestParam("portfolioId") int portfolioId
    		,@AuthenticationPrincipal AuthenticatedUser user
    		,Model model) throws Exception {
		FreelancerPortfoliosDTO freelancerPortfoliosDTO = freelancerPortfoliosService.getPortfolioById(portfolioId, user.getId());
		List<FreelancerPortfoliosDTO> freelancerPortfoliosDTOList = freelancerPortfoliosService.findPortfolioList(user.getId());

		model.addAttribute("portfoliosList", freelancerPortfoliosDTOList);
		log.debug("여긴옴?");
		model.addAttribute("freelancerPortfolios", freelancerPortfoliosDTO);
        return "guestportfolio/content";
    }
}
