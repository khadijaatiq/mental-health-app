package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceRequest {

    private String title;
    private String description;
    private String category;
    private String link;
    private String fileUrl;
    public ResourceRequest() {
    }
}

