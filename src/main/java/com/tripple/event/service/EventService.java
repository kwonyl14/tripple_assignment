package com.tripple.event.service;

import com.tripple.event.entity.*;
import com.tripple.event.repository.*;
import com.tripple.event.request.ReviewCreateReq;
import com.tripple.event.request.ReviewUpdateReq;
import com.tripple.event.response.ReviewCreateRes;
import com.tripple.event.response.ReviewUpdateRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    @Transactional
    public ReviewCreateRes addReview(ReviewCreateReq reviewCreateReq) {
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
        int photoCount = reviewCreateReq.getAttachedPhotoIds().size();
        if (photoCount > 0) {
            List<Photo> photos = new ArrayList<>();
            for (String attachedPhotoId : reviewCreateReq.getAttachedPhotoIds()) {
                photos.add(new Photo(attachedPhotoId, review));
            }
            photoRepository.saveAll(photos);
            review.setPhotos(photos);
        }

        //포인트 등록
        int value = 1;
        if (count == 0) value++;
        if (photoCount > 0) value++;
        pointlogRepository.save(new Pointlog(value, review));
        return ReviewCreateRes.of(review);
    }

    public int sumPointByUserId(String userId) {
        return pointlogRepository.countByUserId(userId);
    }

    @Transactional
    public ReviewUpdateRes updateReview(ReviewUpdateReq reviewUpdateReq) {
        /**
         * @Method Name : updateReview
         * @Method 설명 : 수정사항이 있는 리뷰 정보를 수정하고 사진 삭제,추가 유무
         * 를 판단해 포인트 증감을 수행하는 메소드
         */
        //리뷰 조회
        Review review = reviewRepository.findById(reviewUpdateReq.getReviewId())
                .orElseThrow(NoSuchElementException::new);
        //리뷰 업데이트
        review.updateReview(reviewUpdateReq);
        //사진 추가 or 삭제, 포인트 변경 유무 계산
        int beforeUpdateCount = photoRepository.countByReview(review);
        int deletePhotoCount = reviewUpdateReq.getDeletePhotoIdList().size();
        int updatePhotoCount = reviewUpdateReq.getInsertPhotoIdList().size();
        if (deletePhotoCount > 0) {
            photoRepository.deleteAllById(reviewUpdateReq.getDeletePhotoIdList());
        }
        if (updatePhotoCount > 0) {
            List<Photo> insertPhotoList = reviewUpdateReq.getInsertPhotoIdList()
                    .stream().map(id -> new Photo(id, review)).collect(Collectors.toList());
            photoRepository.saveAll(insertPhotoList);
            review.setPhotos(insertPhotoList);
        }
        int value = calcPoint(beforeUpdateCount, deletePhotoCount, updatePhotoCount);
        //계산될 포인트가 있다면 로그를 남김
        if (value != 0)
            pointlogRepository.save(new Pointlog(value, review));
        return ReviewUpdateRes.of(review);
    }

    private int calcPoint(int beforeUpdateCount, int deletePhotoCount, int updatePhotoCount) {
        /**
         * @Method Name : calcPoint
         * @Method 설명 : 이전 포인트와 증가,삭제된 사진갯수로 증감 계산
         */
        if (beforeUpdateCount == 0 && updatePhotoCount > 0)
            return 1;
        if (beforeUpdateCount > 0 && beforeUpdateCount + updatePhotoCount - deletePhotoCount == 0)
            return -1;
        return 0;
    }

    public void deleteReview(String reviewId) {
        //리뷰 삭제(해당 리뷰의 사진과 포인트 로그도 같이 삭제)
        reviewRepository.deleteById(reviewId);
    }
}
