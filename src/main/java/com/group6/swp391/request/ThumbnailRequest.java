package com.group6.swp391.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ThumbnailRequest {
    private int thumbnailId;
    private String imageUrl;
}
