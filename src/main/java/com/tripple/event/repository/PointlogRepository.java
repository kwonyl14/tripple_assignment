package com.tripple.event.repository;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Pointlog;
import com.tripple.event.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.*;
import java.util.List;

public interface PointlogRepository extends JpaRepository<Pointlog, Long> {
    List<Pointlog> findPointsByReview(Review review);

    @Query("select coalesce(sum(p.value), 0) from Pointlog p join p.review r join p.review.user u where u.id = :userId")
    int countByUserId(@Param("userId") String userId);

    void deleteByReview(Review review);
}
