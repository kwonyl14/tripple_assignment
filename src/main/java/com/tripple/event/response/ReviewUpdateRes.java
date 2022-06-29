package com.tripple.event.response;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ReviewUpdateRes {
    private String reviewId;
    private String content;
    private String userId;
    private String placeId;
    private List<String> photos;

    public ReviewUpdateRes() {
    }

    public ReviewUpdateRes(String reviewId, String content, String userId, String placeId, List<String> photos) {
        this.reviewId = reviewId;
        this.content = content;
        this.userId = userId;
        this.placeId = placeId;
        this.photos = photos;
    }

    public static ReviewUpdateRes of(Review review) {
        return ReviewUpdateRes.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .userId(review.getUser().getId())
                .placeId(review.getPlace().getId())
                .photos(review.getPhotos().stream().map(Photo::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
