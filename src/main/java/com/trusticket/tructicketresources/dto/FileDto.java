package com.trusticket.tructicketresources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDto {
    private byte[] bytes;
    private String mimeType;
}