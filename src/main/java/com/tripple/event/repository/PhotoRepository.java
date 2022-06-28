package com.tripple.event.repository;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, String> {
    List<Photo> findByReview(Review review);
}
