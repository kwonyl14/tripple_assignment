package com.tripple.event.service;

import com.tripple.event.entity.*;
import com.tripple.event.repository.*;
import com.tripple.event.request.ReviewCreateReq;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @FileName : EventService
 * @Class 설명 : 이벤트 요청을 처리하는 서비스 클래스
 */
@Service
public class EventService {

    private final ReviewRepository reviewRepository;
    private final PointlogRepository pointlogRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;

    public EventService(ReviewRepository reviewRepository, PointlogRepository pointlogRepository,
                        UserRepository userRepository, PlaceRepository placeRepository,
                        PhotoRepository photoRepository) {
        this.reviewRepository = reviewRepository;
        this.pointlogRepository = pointlogRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.photoRepository = photoRepository;
    }

    public void addReview(ReviewCreateReq reviewCreateReq) {
        /**
         * @Method Name : addReview
         * @Method 설명 : 사용자 정보와 장소 정보를 이용해 리뷰를 등록하고
         * 그에따른 사진 저장과 포인트 증가를 수행하는 메소드
         */
        //사용자, 장소 조회
        User user = userRepository.findById(reviewCreateReq.getUserId())
                .orElseThrow(NoSuchElementException::new);
        Place place = placeRepository.findById(reviewCreateReq.getPlaceId())
                .orElseThrow(NoSuchElementException::new);
        //현재 장소에 리뷰 갯수 조회
        int count = reviewRepository.countByPlace(place);

        //리뷰 등록
        Review review = reviewCreateReq.toEntity(user, place);
        reviewRepository.save(review);

        //사진이 있으면 등록
        int photoCount = reviewCreateReq.getAttachedPhotoIds().length;
        if (photoCount > 0) {
            List<Photo> photos = new ArrayList<>();
            for (String attachedPhotoId : reviewCreateReq.getAttachedPhotoIds()) {
                photos.add(new Photo(attachedPhotoId, review));
            }
            photoRepository.saveAll(photos);
        }

        //포인트 등록
        int value = 1;
        if (count == 0) value++;
        if (photoCount > 0) value++;
        pointlogRepository.save(new Pointlog(value, review));
    }
}
