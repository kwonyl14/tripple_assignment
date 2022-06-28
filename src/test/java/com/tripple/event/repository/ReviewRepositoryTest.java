package com.tripple.event.repository;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Place;
import com.tripple.event.entity.Review;
import com.tripple.event.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlaceRepository placeRepository;
    @PersistenceContext
    EntityManager em;

    final static String user1_id = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
    final static String place1_id = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";

    @BeforeAll
    static void 사용자_장소_전처리(@Autowired UserRepository userRepository, @Autowired PlaceRepository placeRepository) {
        String user_id = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        String place_id = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        User user1 = new User(user_id, "user1");
        userRepository.save(user1);
        Place place1 = new Place(place_id, "place1");
        placeRepository.save(place1);
    }

    @Test
    void 리뷰_저장_조회_테스트() throws Exception {
        String review_id = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String photo_id1 = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8";
        String photo_id2 = "afb0cef2- 851d-4a50-bb07-9cc15cbdc332";
        //사용자, 장소 Entity 조회
        User user1 = userRepository.findById(user1_id).orElseThrow(Exception::new);
        Place place1 = placeRepository.findById(place1_id).orElseThrow(Exception::new);
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

        //저장 후 em 플러시,비우기
        em.flush();
        em.clear();

        //저장된 데이터 조회하기
        Review findReview = reviewRepository.findById(review_id).orElseThrow(Exception::new);
        List<Photo> findPhotoList = photoRepository.findByReview(findReview);
        //저장된 리뷰, 포토 데이터 검증
        assertThat(findReview.getId()).isEqualTo(review_id);
        assertThat(findPhotoList.size()).isEqualTo(2);
        assertThat(findPhotoList.get(0).getId()).isEqualTo(photo_id1);
        assertThat(findPhotoList.get(0).getReview().getId()).isEqualTo(review_id);
        assertThat(findPhotoList.get(1).getId()).isEqualTo(photo_id2);
        assertThat(findPhotoList.get(1).getReview().getId()).isEqualTo(review_id);
    }
}