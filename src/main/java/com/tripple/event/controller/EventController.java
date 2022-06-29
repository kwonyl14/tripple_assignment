package com.tripple.event.controller;

import com.tripple.event.request.ReviewCreateReq;
import com.tripple.event.request.ReviewUpdateReq;
import com.tripple.event.response.ReviewCreateRes;
import com.tripple.event.response.ReviewUpdateRes;
import com.tripple.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @FileName : EventController
 * @Class 설명 : 이벤트발생시 요청을 받는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReview(@RequestBody ReviewCreateReq reviewCreateReq) {
        ReviewCreateRes reviewCreateRes = eventService.addReview(reviewCreateReq);
        return new ResponseEntity<>(reviewCreateRes, HttpStatus.OK);
    }

    @PatchMapping("/review")
    public ResponseEntity<?> updateReview(@RequestBody ReviewUpdateReq reviewUpdateReq) {
        ReviewUpdateRes reviewUpdateRes = eventService.updateReview(reviewUpdateReq);
        return new ResponseEntity<>(reviewUpdateRes, HttpStatus.OK);
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReivew(@PathVariable("reviewId") String reviewId) {
        eventService.deleteReview(reviewId);
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    @GetMapping("/point/{userId}")
    public ResponseEntity<?> getPoint(@PathVariable("userId") String userId) {
        int sum = eventService.sumPointByUserId(userId);
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
}
