package com.tripple.event.repository;

import com.tripple.event.entity.Place;
import com.tripple.event.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
    int countByPlace(Place place);
}
