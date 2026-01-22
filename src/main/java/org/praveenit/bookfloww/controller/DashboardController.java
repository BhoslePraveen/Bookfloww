package org.praveenit.bookfloww.controller;

import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.service.google.CalenderSyncFacade;
import org.praveenit.bookfloww.service.google.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	private final CalenderSyncFacade calenderSyncFacade;
	private final SecurityUtil securityUtil;

    @GetMapping("/calender")
    public ResponseEntity<?> getCalender() {
    	User user=securityUtil.getCurrentUser();
    	calenderSyncFacade.syncCalenderForUser(user);
    	return ResponseEntity.ok("calender synced successfully");
    }
}

