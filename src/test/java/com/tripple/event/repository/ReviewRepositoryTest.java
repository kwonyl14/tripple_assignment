package com.tripple.event.repository;

import com.tripple.event.entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    PointlogRepository pointlogRepository;
    @PersistenceContext
    EntityManager em;

    final static String user1_id = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
    final static String place1_id = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
    final static String temp_review_id = "33333333-dc5f-4878-9381-ebb7b2667772";
    final static String temp_user_id = "3ede0ef2-1111-4817-a5f3-0c575361f745";
    final static String temp_place_id = "2e4baf1c-1111-4efb-a1af-eddada31b00f";

    @BeforeAll
    static void 사용자_장소_리뷰_전처리(@Autowired UserRepository userRepository,
                              @Autowired PlaceRepository placeRepository, @Autowired ReviewRepository reviewRepository) {
        User tempUser1 = new User(temp_user_id, "tempUser1");
        userRepository.save(tempUser1);
        Place tempPlace1 = new Place(temp_place_id, "tempPlace1");
        placeRepository.save(tempPlace1);
        Review tempReview = new Review(temp_review_id, "review1", tempUser1, tempPlace1);
        reviewRepository.save(tempReview);

        User user1 = new User(user1_id, "user_id");
        userRepository.save(user1);
        Place place1 = new Place(place1_id, "place_id");
        placeRepository.save(place1);
    }

    @Test
    void 리뷰_첫_저장_조회_테스트() throws Exception {
        String review_id = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String photo_id1 = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8";
        String photo_id2 = "afb0cef2- 851d-4a50-bb07-9cc15cbdc332";
        //사용자, 장소 Entity 조회
        User user1 = userRepository.findById(user1_id).orElseThrow(Exception::new);
        Place place1 = placeRepository.findById(place1_id).orElseThrow(Exception::new);
        //해당 장소의 리뷰갯수 조회
        int count = reviewRepository.countByPlace(place1);
        //조회된 유저 장소 데이터 검증
        assertThat(user1.getId()).isEqualTo(user1_id);
        assertThat(place1.getId()).isEqualTo(place1_id);

        //리뷰 등록
        Review review = new Review(review_id, "좋아요!", user1, place1);
        reviewRepository.save(review);

        //리뷰의 사진등록
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo(photo_id1, review));
        photos.add(new Photo(photo_id2, review));
        photoRepository.saveAll(photos);

        //포인트 등록
        int value = 1;//내용이 있을때만 들어온다고 가정
        if (count == 0) value++;
        if (photos.size() > 0) value++;
        pointlogRepository.save(new Pointlog(value, review));

        //저장 후 em 플러시,비우기
        em.flush();
        em.clear();

        //저장된 데이터 조회하기
        Review findReview = reviewRepository.findById(review_id).orElseThrow(Exception::new);
        List<Photo> findPhotoList = photoRepository.findByReview(findReview);
        List<Pointlog> pointsByReview = pointlogRepository.findPointsByReview(findReview);
        int sum = pointsByReview.stream().parallel().mapToInt(Pointlog::getValue).sum();

        //리뷰 검증
        assertThat(findReview.getId()).isEqualTo(review_id);
        //리뷰에 저장된 사진 검증
        assertThat(findPhotoList.size()).isEqualTo(2);
        //포인트 검증
        assertThat(sum).isEqualTo(3);
    }

    @Test
    void 리뷰_두번째_저장_조회_테스트() throws Exception {
        String review_id = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String photo_id1 = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8";
        String photo_id2 = "afb0cef2- 851d-4a50-bb07-9cc15cbdc332";
        //사용자, 장소 Entity 조회
        User user1 = userRepository.findById(user1_id).orElseThrow(Exception::new);
        Place place1 = placeRepository.findById(temp_place_id).orElseThrow(Exception::new);
        //해당 장소의 리뷰갯수 조회
        int count = reviewRepository.countByPlace(place1);
        //조회된 유저 장소 데이터 검증
        assertThat(user1.getId()).isEqualTo(user1_id);
        assertThat(place1.getId()).isEqualTo(temp_place_id);

        //리뷰 등록
        Review review = new Review(review_id, "좋아요!", user1, place1);
        reviewRepository.save(review);

        //리뷰의 사진등록
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo(photo_id1, review));
        photos.add(new Photo(photo_id2, review));
        photoRepository.saveAll(photos);

        //포인트 등록
        int value = 1;//내용이 있을때만 들어온다고 가정
        if (count == 0) value++;
        if (photos.size() > 0) value++;
        pointlogRepository.save(new Pointlog(value, review));

        //저장 후 em 플러시,비우기
        em.flush();
        em.clear();

        //저장된 데이터 조회하기
        Review findReview = reviewRepository.findById(review_id).orElseThrow(Exception::new);
        List<Photo> findPhotoList = photoRepository.findByReview(findReview);
        List<Pointlog> pointsByReview = pointlogRepository.findPointsByReview(findReview);
        int sum = pointsByReview.stream().parallel().mapToInt(Pointlog::getValue).sum();

        //리뷰 검증
        assertThat(findReview.getId()).isEqualTo(review_id);
        //리뷰에 저장된 사진 검증
        assertThat(findPhotoList.size()).isEqualTo(2);
        //포인트 검증
        assertThat(sum).isEqualTo(2);
    }
}