package com.maveric.balances.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@ToString
@Document(collection = "Balance")
public class Balance {
    @Id
    private  String balanceId;
    private String amount ;
    private String currency;
    private  String accountId;
    private String createdAt;
    private String updatedAt;
}
