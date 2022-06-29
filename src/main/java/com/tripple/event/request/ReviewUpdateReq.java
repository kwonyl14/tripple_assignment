package com.tripple.event.request;

import lombok.Getter;

import java.util.List;

/**
 * @FileName : ReviewUpdateReq
 * @Class 설명 : 리뷰 수정 요청정보를 담는 클래스
 */
@Getter
public class ReviewUpdateReq {
    String reviewId;
    String content;
    List<String> deletePhotoIdList;
    List<String> insertPhotoIdList;
}
