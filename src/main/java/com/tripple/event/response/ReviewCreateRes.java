package com.tripple.event.response;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Review;
import com.tripple.event.request.ReviewCreateReq;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ReviewCreateRes {
    private String reviewId;
    private String content;
    private String userId;
    private String placeId;
    private List<String> photos;

    public ReviewCreateRes() {
    }

    public ReviewCreateRes(String reviewId, String content, String userId, String placeId, List<String> photos) {
        this.reviewId = reviewId;
        this.content = content;
        this.userId = userId;
        this.placeId = placeId;
        this.photos = photos;
    }

    public static ReviewCreateRes of(Review review) {
        return ReviewCreateRes.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .userId(review.getUser().getId())
                .placeId(review.getPlace().getId())
                .photos(review.getPhotos().stream().map(Photo::getId)
                        .collect(Collectors.toList()))
                .build();
    }

}
