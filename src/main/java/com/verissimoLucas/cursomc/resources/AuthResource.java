package com.verissimoLucas.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.verissimoLucas.cursomc.security.JWTUtil;
import com.verissimoLucas.cursomc.security.UserSS;
import com.verissimoLucas.cursomc.services.UserService;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {

	@Autowired
	JWTUtil jwtUtil;
	@RequestMapping(value = "/refresh_token",method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Baerer "+token);
		return ResponseEntity.noContent().build();
	}
}
