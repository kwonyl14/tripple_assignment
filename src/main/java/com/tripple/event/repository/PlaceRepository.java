package com.tripple.event.repository;

import com.tripple.event.entity.Place;
import com.tripple.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, String> {

}
