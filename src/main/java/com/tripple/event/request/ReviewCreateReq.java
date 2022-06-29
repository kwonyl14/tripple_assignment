package com.tripple.event.request;

import com.tripple.event.entity.Place;
import com.tripple.event.entity.Review;
import com.tripple.event.entity.User;
import lombok.Getter;

import java.util.List;

/**
 * @FileName : ReviewCreateReq
 * @Class 설명 : 리뷰 등록 요청을 담는 Dto
 */

@Getter
public class ReviewCreateReq {
    String type;
    String action;
    String reviewId;
    String content;
    List<String> attachedPhotoIds;
    String userId;
    String placeId;

    public Review toEntity(User user, Place place) {
        return new Review(reviewId, content, user, place);
    }
}
