package com.maveric.balances.service;

import com.maveric.balances.dto.BalanceDTO;
import com.maveric.balances.exception.CreateBalanceException;
import com.maveric.balances.model.Balance;
import com.maveric.balances.repository.BalanceRepository;
import com.maveric.balances.utils.BalanceServiceConstants;
import com.maveric.balances.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BalancesServiceImpl implements BalancesService{

    @Autowired
    private BalanceRepository balanceRepository;
    @Override
    public ResponseEntity createBalance(BalanceDTO balanceDTO) {
        Balance balance =new Balance();
        try {
            balanceDTO.setCreatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            balanceDTO.setUpdatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            balance = mapBalanceDtoToBalance(balanceDTO,balance);
            balance = balanceRepository.save(balance);
            Optional<Balance> result=balanceRepository.findByBalanceId(balance.getBalanceId());
            if(result.isPresent()) {
                return new ResponseEntity(mapBalanceToBalanceDto(result.get()), HttpStatus.CREATED);
            }else {
                return new ResponseEntity(BalanceServiceConstants.BALANCE_NOT_CREATED,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            throw new CreateBalanceException(e.getMessage(),e);
        }
    }

    @Override
    public ResponseEntity getBalanceDetailsByBalanceId(String accountId, String balanceId) {
        try {
            Optional<Balance> balance = balanceRepository.findByBalanceId(balanceId);
            if (balance.isPresent()) {
                return new ResponseEntity(balance.get().getAmount(), HttpStatus.OK);
            } else {
                Map<String, String> result = new HashMap();
                result.put(BalanceServiceConstants.ERROR_CODE, BalanceServiceConstants.BAD_REQUEST);
                result.put(BalanceServiceConstants.ERROR_MESSAGE, BalanceServiceConstants.BALANCE_ID_IS_NOT_PRESENT);
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            throw new CreateBalanceException(e.getMessage(),e);
        }
    }

    @Override
    public ResponseEntity updateBalanceByBalanceIdAndAccountId(BalanceDTO balanceDTO) {
        BalanceDTO dto=null;
        Balance entity=null;
        try {
            Optional<Balance> balance = balanceRepository.findByBalanceId(balanceDTO.getBalanceId());
            if (balance.isPresent()){
                balanceDTO.setUpdatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            entity = mapBalanceDtoToBalance(balanceDTO, balance.get());
            Balance updatedData = balanceRepository.save(entity);
            dto = mapBalanceToBalanceDto(updatedData);
           return new  ResponseEntity(dto,HttpStatus.OK);
        }else {
                Map<String,String> result=new HashMap();
                result.put(BalanceServiceConstants.ERROR_CODE,BalanceServiceConstants.BAD_REQUEST);
                result.put(BalanceServiceConstants.ERROR_MESSAGE,BalanceServiceConstants.BALANCE_ID_NOT_PRESENT);
                return new  ResponseEntity(result,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
        throw new CreateBalanceException(e.getMessage(),e);
        }
    }

    @Override
    public ResponseEntity deleteBalance(String accountId, String balanceId) {
        try {
            Optional<Balance> entity=null;
            Optional<Balance> balance = balanceRepository.findByBalanceId(balanceId);
            if (balance.isPresent()) {
                   balanceRepository.deleteById(balanceId);
               return new ResponseEntity(BalanceServiceConstants.BALANCE_DELETED_SUCCESSFULLY,HttpStatus.OK);
            } else {
                Map<String,String> result=new HashMap();
                result.put(BalanceServiceConstants.ERROR_CODE,BalanceServiceConstants.BAD_REQUEST);
                result.put(BalanceServiceConstants.ERROR_MESSAGE,BalanceServiceConstants.BALANCE_ID_NOT_PRESENT);
                return new ResponseEntity(result,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
      throw new CreateBalanceException(e.getMessage(),e);
        }
    }

    @Override
    public Page getBalances(String accountId, Pageable pageable) {
        Page<List<Balance>> list =null;
        try {
             list = balanceRepository.findByAccountId(accountId, pageable);
            return list;
        }catch (Exception e){
        throw  new CreateBalanceException(e.getMessage(),e);
        }
    }

    private Balance mapBalanceDtoToBalance(BalanceDTO balanceDTO ,Balance balance){
        balance.setAccountId(balanceDTO.getAccountId());
        balance.setAmount(balanceDTO.getAmount());
        balance.setCurrency(balanceDTO.getCurrency());
        if(balanceDTO.getCreatedAt() != null)
        balance.setCreatedAt(balanceDTO.getCreatedAt());
        if(balanceDTO.getUpdatedAt() != null)
        balance.setUpdatedAt(balanceDTO.getUpdatedAt());
        return balance;
    }

    private  BalanceDTO mapBalanceToBalanceDto(Balance balance){
        BalanceDTO balanceDTO=new BalanceDTO();
        balanceDTO.setAccountId(balance.getAccountId());
        balanceDTO.setAmount(balance.getAmount());
        balanceDTO.setCurrency(balance.getCurrency());
        balanceDTO.setBalanceId(balance.getBalanceId());
        balanceDTO.setUpdatedAt(balance.getUpdatedAt());
        balanceDTO.setCreatedAt(balance.getCreatedAt());
      return balanceDTO;
    }
}
