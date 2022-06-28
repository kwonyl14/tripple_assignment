package com.tripple.event.repository;

import com.tripple.event.entity.Photo;
import com.tripple.event.entity.Pointlog;
import com.tripple.event.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;

public interface PointlogRepository extends JpaRepository<Pointlog, Long> {
    List<Pointlog> findPointsByReview(Review review);
}
