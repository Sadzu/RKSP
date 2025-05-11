package ru.nstu.core.objects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjectDto {
    private String type;
    private long x;
    private long y;
    private Long width;
    private Long height;
    private Long radius;
}
