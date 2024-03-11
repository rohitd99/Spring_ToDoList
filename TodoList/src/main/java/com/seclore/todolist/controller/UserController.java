package com.seclore.todolist.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.seclore.todolist.domain.UserDetails;

import com.seclore.todolist.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	private UserDetailsServiceInterface userDetailsService = new UserDetailsService();

	@RequestMapping("/")
	public String showDefaultPage() {
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView showLoginPage() {
		ModelAndView modelAndView = new ModelAndView();
		UserDetails user = new UserDetails();
		modelAndView.setViewName("login");
		modelAndView.addObject(user);
		return modelAndView;

	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView showSignupPage() {
		ModelAndView modelAndView = new ModelAndView();
		UserDetails user = new UserDetails();
		modelAndView.setViewName("signup");
		modelAndView.addObject(user);
		
		return modelAndView;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute UserDetails user, HttpSession session) {

		if (userDetailsService.signup(user))
			session.setAttribute("message", "Successfully added user");
		else
			session.setAttribute("message", "failed to add new User");
		return "redirect:/login";

	}

	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	public String userLogin(@ModelAttribute UserDetails user, HttpSession session) {
		UserDetails loggedUser = userDetailsService.login(user.getEmail(), user.getPassword());
		String message, nextPage;
		if (loggedUser == null) {
			message = " INVALID USER_ID OR PASSWORD ";
			session.setAttribute("message", message);
			nextPage = "login";
		} else {
			session.setAttribute("loggedInUser", loggedUser);
			nextPage = "redirect:/tasks";
		}
		return nextPage;
	}

	@RequestMapping("logout")
	public void logOut(HttpSession session, HttpServletResponse response) {
		try {
			session.invalidate();
			response.sendRedirect("login");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
