package com.sharesphere.dto.request;

import lombok.Data;

@Data
public class ModerationCheckRequest {
    private String name;
    private String description;
    private String location;
}
