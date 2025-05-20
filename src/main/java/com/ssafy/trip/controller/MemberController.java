package com.DB_PASSWORD_REDACTED.trip.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;
import com.DB_PASSWORD_REDACTED.trip.service.MemberService;
import com.DB_PASSWORD_REDACTED.trip.service.MemberServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//@WebServlet("/member")
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController extends HttpServlet {

	private final MemberService mService;
	private final PasswordEncoder pe;

	// TODO 페이지 만들고 리턴해줘야 됨
	@GetMapping("/secured/admin/selectmember")
	public String selectMemeber(@RequestParam String id, Model model) throws Exception {
		MemberDto member = mService.selectMember(id);
		model.addAttribute("member", member);
		
		return "member/memberdetail";
	}
	
	@GetMapping("/secured/admin/selectallmember")
	public String selectAllMember(Model model) throws Exception {
		List<MemberDto> memberList = mService.selectAllMember();
		
		model.addAttribute("memberlist", memberList);
		
		return "member/list";
	}
	
	@GetMapping("/registerform")
	public String registerForm() {
		return "member/register";
	}
	
	@PostMapping("/insertmember")
	public String insertMember(@ModelAttribute MemberDto member) throws Exception {
		int cnt = mService.insertMember(member);
		
		return "redirect:/";
	}
	
	@PostMapping("/secured/updateinfoform")
	public String updateInfoForm() {
		return "member/updateinfo";
	}
	
	@PostMapping("/secured/updateinfo")
	public String updateInfo(@RequestParam String id,
			@RequestParam String name,
			@RequestParam String email,
			HttpSession session) throws Exception {
		
		int cnt = mService.updateInfo(id, name, email);
		MemberDto member = mService.selectMember(id);
		session.setAttribute("loginUser", member);
		
		CustomUserDetails updatedDetails = new CustomUserDetails(member, pe);
	    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
	    Authentication newAuth = new UsernamePasswordAuthenticationToken(
	            updatedDetails,
	            currentAuth.getCredentials(),
	            updatedDetails.getAuthorities()
	    );
	    SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		return "redirect:/member/secured/mypage";
	}
	
	
	@PostMapping("/secured/updatepwform")
	public String updatePwForm() {
		return "member/updatepw";
	}
	
	@PostMapping("/secured/updatepw")
	public String updatePw(@RequestParam String id,
			@RequestParam String pw) throws Exception {
		
		int cnt = mService.updatePw(id, pw);
		
		return "redirect:/member/secured/mypage";
	}
	
	@PostMapping("/secured/deletemember")
	public String deleteMember(@RequestParam String id, HttpSession session) throws Exception {
		int cnt = mService.deleteMember(id);
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	@GetMapping("/loginform")
	public String loginForm() {
		return "member/login";
	}
	
//	@PostMapping("/login")
	public String login(@RequestParam String id, 
			@RequestParam String pw,
			@RequestParam(required = false) String remember,
			HttpSession session,
			HttpServletResponse resp,
			RedirectAttributes redir) throws Exception {
		
		boolean login = mService.login(id, pw);
		
		if (login) {
			MemberDto member = mService.selectMember(id);
			
			Cookie cookie = new Cookie("idsave", id);
			if (remember != null) {
				cookie.setMaxAge(60*60*10);
			}
			else {
				cookie.setMaxAge(0);
			}
			
			resp.addCookie(cookie);
			session.setAttribute("loginUser", member);
			
			return "redirect:/";
		}
		
		redir.addFlashAttribute("alertMsg", "로그인 실패");
		return "redirect:/member/loginform";
	}
	
	@PostMapping("/findpw")
	public String findPw(@RequestParam("modalid") String id,
			@RequestParam("modalname") String name, 
			@RequestParam("modalemail") String email,
			Model model,
			RedirectAttributes redir) throws Exception {
		
		MemberDto member = mService.selectMember(id);
		
		if (member == null || !member.getName().equals(name) || !member.getEmail().equals(email)) {
			redir.addFlashAttribute("alertMsg", "해당되는 id가 존재하지 않습니다.");
			return "member/login";
		}
		
		model.addAttribute("id", id);
		
		return "/member/changepwform";
	}
	
	@GetMapping("/changepwform")
	public String changePwForm() {
		return "member/changepw";
	}

	@PostMapping("/changepw")
	public String changePw(@RequestParam String id, 
			@RequestParam String pw) throws Exception {
		
		mService.updatePw(id, pw);
		
		return "redirect:member/loginform";
	}
	
//	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	@GetMapping("/secured/mypage")
	public String mypage(HttpSession session) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		
		MemberDto member = user.getMember();
		session.setAttribute("loginUser", member);
		
		return "member/mypage";
	}

}
