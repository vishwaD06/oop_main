package com.sharesphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_request_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    @ToString.Exclude
    private ItemRequest request;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offerer_id", nullable = false)
    private User offerer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private BigDecimal priceOffered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "linked_item_id")
    private Item linkedItem;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
