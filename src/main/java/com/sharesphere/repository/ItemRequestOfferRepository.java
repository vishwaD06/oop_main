package com.sharesphere.repository;

import com.sharesphere.entity.ItemRequestOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestOfferRepository extends JpaRepository<ItemRequestOffer, Long> {
    List<ItemRequestOffer> findByRequestIdOrderByCreatedAtDesc(Long requestId);
    List<ItemRequestOffer> findByOffererIdOrderByCreatedAtDesc(Long offererId);
}
