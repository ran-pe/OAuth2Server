package com.example.oauth2apiserver.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpStatus;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class JsonResponse<T> implements Serializable {

    private static final long serialVersionUID = 2136423626626329169L;

    public static final String BODY_TYPE_OBJECT = "OBJECT";

    public static final String BODY_TYPE_ARRAY = "ARRAY";

    /**
     * 수행시간
     */
    @NotNull
    @Builder.Default
    private long elapsedTime = 0;

    /**
     * HTTP 상태
     */
    @NotNull
    @Builder.Default
    private HttpStatus status = HttpStatus.OK;

    /**
     * 에러메세지 리스트
     */
    @Builder.Default
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> errors = new ArrayList<>();

    /**
     * 일반메세지
     */
    @Builder.Default
    private String message = "";

    /**
     * 응답시간
     */
    @Builder.Default
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 응답데이터타입
     */
    @Builder.Default
    private String bodyType = JsonResponse.BODY_TYPE_OBJECT;

    /**
     * 응답데이터
     */
    @Builder.Default
    private T body = null;


}
