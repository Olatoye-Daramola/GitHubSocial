package io.olatoye.githubsocial.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class ResponseSchema {

    private Integer statusCode;
    private String message;
    private Object data;
}
