package com.sharesphere.service.impl;

import com.sharesphere.dto.request.ItemRequestCreateDto;
import com.sharesphere.dto.request.OfferCreateDto;
import com.sharesphere.dto.response.*;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.exception.UnauthorizedException;
import com.sharesphere.exception.ValidationException;
import com.sharesphere.repository.*;
import com.sharesphere.service.ItemRequestService;
import com.sharesphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestOfferRepository itemRequestOfferRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;

    @Override
    public List<ItemRequestResponse> getAllOpenRequests() {
        return itemRequestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.OPEN)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public ItemRequestResponse getRequestById(Long id) {
        ItemRequest req = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        return toResponseWithOffers(req);
    }

    @Override
    @Transactional
    public ItemRequestResponse createRequest(ItemRequestCreateDto dto, Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        ItemRequest req = ItemRequest.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(category)
                .requester(requester)
                .location(dto.getLocation())
                .landmark(dto.getLandmark())
                .budget(dto.getBudget())
                .neededTimeframe(dto.getNeededTimeframe())
                .urgency(dto.getUrgency() != null ? dto.getUrgency() : RequestUrgency.MEDIUM)
                .status(RequestStatus.OPEN)
                .build();

        return toResponse(itemRequestRepository.save(req));
    }

    @Override
    @Transactional
    public OfferResponseDto addOffer(Long requestId, OfferCreateDto dto, Long offererId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (request.getStatus() != RequestStatus.OPEN) {
            throw new ValidationException("Cannot offer on a non-open request");
        }
        if (request.getRequester().getId().equals(offererId)) {
            throw new ValidationException("Cannot offer on your own request");
        }

        User offerer = userRepository.findById(offererId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item linkedItem = null;
        if (dto.getLinkedItemId() != null) {
            linkedItem = itemRepository.findById(dto.getLinkedItemId()).orElse(null);
        }

        ItemRequestOffer offer = ItemRequestOffer.builder()
                .request(request)
                .offerer(offerer)
                .message(dto.getMessage())
                .priceOffered(dto.getPriceOffered())
                .linkedItem(linkedItem)
                .build();

        offer = itemRequestOfferRepository.save(offer);

        notificationService.sendNotification(
                request.getRequester().getId(),
                "New Offer on Your Wishlist Request",
                offerer.getName() + " can help with: \"" + request.getTitle() + "\"",
                NotificationType.RENTAL_REQUEST,
                request.getId(),
                "REQUEST"
        );

        return toOfferResponse(offer);
    }

    @Override
    @Transactional
    public ItemRequestResponse fulfillRequest(Long requestId, Long userId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new UnauthorizedException("Only requester can mark request as fulfilled");
        }
        request.setStatus(RequestStatus.FULFILLED);
        return toResponse(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestResponse> getMyRequests(Long userId) {
        return itemRequestRepository.findByRequesterIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    private ItemRequestResponse toResponse(ItemRequest req) {
        List<ItemRequestOffer> offers = itemRequestOfferRepository.findByRequestIdOrderByCreatedAtDesc(req.getId());
        return ItemRequestResponse.builder()
                .id(req.getId())
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory() != null ? toCategoryResponse(req.getCategory()) : null)
                .requester(toUserResponse(req.getRequester()))
                .location(req.getLocation())
                .landmark(req.getLandmark())
                .budget(req.getBudget())
                .neededTimeframe(req.getNeededTimeframe())
                .urgency(req.getUrgency())
                .status(req.getStatus())
                .offerCount(offers.size())
                .offers(Collections.emptyList())
                .createdAt(req.getCreatedAt())
                .build();
    }

    private ItemRequestResponse toResponseWithOffers(ItemRequest req) {
        List<ItemRequestOffer> offers = itemRequestOfferRepository.findByRequestIdOrderByCreatedAtDesc(req.getId());
        List<OfferResponseDto> offerDtos = offers.stream().map(this::toOfferResponse).toList();
        return ItemRequestResponse.builder()
                .id(req.getId())
                .title(req.getTitle())
                .description(req.getDescription())
                .category(req.getCategory() != null ? toCategoryResponse(req.getCategory()) : null)
                .requester(toUserResponse(req.getRequester()))
                .location(req.getLocation())
                .landmark(req.getLandmark())
                .budget(req.getBudget())
                .neededTimeframe(req.getNeededTimeframe())
                .urgency(req.getUrgency())
                .status(req.getStatus())
                .offerCount(offers.size())
                .offers(offerDtos)
                .createdAt(req.getCreatedAt())
                .build();
    }

    private OfferResponseDto toOfferResponse(ItemRequestOffer offer) {
        return OfferResponseDto.builder()
                .id(offer.getId())
                .requestId(offer.getRequest().getId())
                .offerer(toUserResponse(offer.getOfferer()))
                .message(offer.getMessage())
                .priceOffered(offer.getPriceOffered())
                .linkedItem(offer.getLinkedItem() != null ? toItemResponse(offer.getLinkedItem()) : null)
                .createdAt(offer.getCreatedAt())
                .build();
    }

    private CategoryResponse toCategoryResponse(Category cat) {
        return CategoryResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .icon(cat.getIcon())
                .description(cat.getDescription())
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .college(user.getCollege())
                .location(user.getLocation())
                .averageRating(user.getAverageRating())
                .totalRatings(user.getTotalRatings())
                .build();
    }

    private ItemResponse toItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .rentalPricePerDay(item.getRentalPricePerDay())
                .condition(item.getCondition())
                .status(item.getStatus())
                .build();
    }
}
