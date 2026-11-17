package com.sharesphere.service;

import com.sharesphere.dto.request.ItemRequestCreateDto;
import com.sharesphere.dto.request.OfferCreateDto;
import com.sharesphere.dto.response.ItemRequestResponse;
import com.sharesphere.dto.response.OfferResponseDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestResponse> getAllOpenRequests();
    ItemRequestResponse getRequestById(Long id);
    ItemRequestResponse createRequest(ItemRequestCreateDto dto, Long userId);
    OfferResponseDto addOffer(Long requestId, OfferCreateDto dto, Long offererId);
    ItemRequestResponse fulfillRequest(Long requestId, Long userId);
    List<ItemRequestResponse> getMyRequests(Long userId);
}
