package org.bstu.govoronok.provider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class SimpleDataProvider {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateField;

    private Double doubleField;
    private Integer numberField;
    private String textField;
    private String colorField;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTimeField;
}
