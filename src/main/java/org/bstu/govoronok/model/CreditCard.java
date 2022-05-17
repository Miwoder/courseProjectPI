package org.bstu.govoronok.model;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class CreditCard {

    @CreditCardNumber(message="{creditCardNumber.error}")
    private String ccNumber;
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
            message="{expiration.error2}")
    private String ccExpiration;
    @Digits(integer=3, fraction=0, message="{cvv.error}")
    private String ccCVV;

//    @Min(value = 1L, message = "Amount should not be less than 1")
    @Positive(message = "{amount.error}")
    private BigDecimal amount = BigDecimal.valueOf(0);
}
