package com.maveric.balances.service;

import com.maveric.balances.dto.BalanceDTO;
import com.maveric.balances.model.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BalancesService {

    public ResponseEntity createBalance(BalanceDTO balanceDTO);

    public  ResponseEntity getBalanceDetailsByBalanceId(String accountId,String balanceId);

    public  BalanceDTO updateBalanceByBalanceIdAndAccountId(BalanceDTO balanceDTO);

    public  String deleteBalance(String accountId,String balanceId);

    public Page<List<Balance>> getBalances(String accountId, Pageable pageable);
}
