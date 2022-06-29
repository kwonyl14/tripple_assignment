package com.tripple.event.request;

import com.tripple.event.entity.Place;
import com.tripple.event.entity.Review;
import com.tripple.event.entity.User;

/**
 * @FileName : ReviewCreateReq
 * @Class 설명 : 리뷰 등록 요청을 담는 Dto
 */

public class ReviewCreateReq {
    String type;
    String action;
    String reviewId;
    String content;
    String[] attachedPhotoIds;
    String userId;
    String placeId;

    public ReviewCreateReq(String type, String action, String reviewId, String content, String[] attachedPhotoIds, String userId, String placeId) {
        this.type = type;
        this.action = action;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getContent() {
        return content;
    }

    public String[] getAttachedPhotoIds() {
        return attachedPhotoIds;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public Review toEntity(User user, Place place) {
        return new Review(reviewId, content, user, place);
    }
}
