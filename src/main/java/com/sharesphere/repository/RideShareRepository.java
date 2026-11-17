package com.sharesphere.repository;

import com.sharesphere.entity.RideShare;
import com.sharesphere.entity.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideShareRepository extends JpaRepository<RideShare, Long> {
    List<RideShare> findByStatusOrderByDepartureTimeAsc(RideStatus status);
    List<RideShare> findByStatusInOrderByDepartureTimeAsc(List<RideStatus> statuses);
    List<RideShare> findByOrganizerIdOrderByCreatedAtDesc(Long organizerId);
}
