package org.praveenit.bookfloww.controller;

import lombok.RequiredArgsConstructor;
import org.praveenit.bookfloww.dto.auth.AuthTokenData;
import org.praveenit.bookfloww.service.googlecallback.CallBackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/google")
@RequiredArgsConstructor
public class GoogleCallbackController {
	private final CallBackService callBackService;

	@GetMapping("/callback")
	public ResponseEntity<AuthTokenData> callback(@RequestParam("code") String code) {
		return ResponseEntity.ok(callBackService.processCallBack(code));
	}
}
