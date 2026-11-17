package com.sharesphere.repository;

import com.sharesphere.entity.ItemRequest;
import com.sharesphere.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<ItemRequest> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);
}
