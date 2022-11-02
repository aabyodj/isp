package by.aab.isp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.ToString;

@RequestScope
@Component
@ToString
public class Now {

    private LocalDateTime localDateTime = null;

    public LocalDateTime getLocalDateTime() {
        if (null == localDateTime) {
            localDateTime = LocalDateTime.now();
        }
        return localDateTime;
    }

    public LocalDate getLocalDate() {
        return getLocalDateTime().toLocalDate();
    }

}
