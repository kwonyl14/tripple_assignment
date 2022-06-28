package com.tripple.event.repository;

import com.tripple.event.entity.Review;
import com.tripple.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
