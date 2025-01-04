package com.simbest.cloud.cores.utils.http;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@UtilityClass
public class WebUtils {
    public  Optional<ServletRequestAttributes> getRequestAttributes() {
        return Optional.of ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()));
    }
}
