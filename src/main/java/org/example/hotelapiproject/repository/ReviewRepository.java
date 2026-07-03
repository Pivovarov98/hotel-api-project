package org.example.hotelapiproject.repository;

import org.example.hotelapiproject.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
