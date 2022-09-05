package com.maveric.balances.controller;

import com.maveric.balances.dto.BalanceDTO;
import com.maveric.balances.model.Balance;
import com.maveric.balances.service.BalancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BalancesController {

    @Autowired
    private BalancesService iBalancesService;

    @GetMapping("/accounts/{accountId}/balances")
    public ResponseEntity getBalances(@PathVariable String accountId, Pageable pageable){
        Page<List<Balance>> response = iBalancesService.getBalances(accountId,pageable);
        return new ResponseEntity(response,HttpStatus.OK);

    }
     @PostMapping(value = "/accounts/{accountId}/balances")
    public  ResponseEntity createBalance(@PathVariable String accountId, @RequestBody @Valid BalanceDTO balanceDTO){
           ResponseEntity response =  iBalancesService.createBalance(balanceDTO);
        return  new ResponseEntity(response.getBody(),response.getStatusCode());
    }

    @GetMapping("/accounts/{accountId}/balances/{balanceId}")
    public ResponseEntity getBalanceByBalanceId(@PathVariable String accountId,@PathVariable String balanceId){
        ResponseEntity response = iBalancesService.getBalanceDetailsByBalanceId(accountId,balanceId);
        return new ResponseEntity(response.getBody(),response.getStatusCode());
    }

    @PutMapping("/accounts/{accountId}/balances/{balanceId}")
    public  ResponseEntity updateBalance(@PathVariable String accountId,
                                         @PathVariable String balanceId,@RequestBody BalanceDTO balanceDTO){
        balanceDTO.setBalanceId(balanceId);
       BalanceDTO response = iBalancesService.updateBalanceByBalanceIdAndAccountId(balanceDTO);
       return  new ResponseEntity(response,HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{accountId}/balances/{balanceId}")
    public  ResponseEntity deleteBalance(@PathVariable String accountId, @PathVariable String balanceId){
        iBalancesService.deleteBalance(accountId,balanceId);
        return  new ResponseEntity("Balance deleted successfully.",HttpStatus.OK);
    }
}
