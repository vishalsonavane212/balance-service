package com.maveric.balances.dto;

import com.maveric.balances.utils.Currency;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Setter
@Getter
@ToString
public class BalanceDTO {
    private  String balanceId;
    @NotNull(message = "Please enter amount")
    @Pattern(regexp = "[0-9]+")
    private String amount ;
    private Currency currency;
    @NotNull(message = "Please enter amountId")
    private  String accountId;
    private String createdAt;
    private String updatedAt;
}
