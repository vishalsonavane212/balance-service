package com.maveric.balances.service;

import com.maveric.balances.dto.BalanceDTO;
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
import java.util.stream.Collectors;

import static com.maveric.balances.utils.BalanceServiceConstants.balanceId_is_not_present;

@Service
public class BalancesServiceImpl implements BalancesService{

    @Autowired
    private BalanceRepository balanceRepository;
    @Override
    public ResponseEntity createBalance(BalanceDTO balanceDTO) {
        Balance balance =new Balance();
        ResponseEntity response=new ResponseEntity(HttpStatus.OK);
        try {
            balanceDTO.setCreatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            balanceDTO.setUpdatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            balance = mapBalanceDtoToBalance(balanceDTO,balance);
            balance = balanceRepository.save(balance);
            if(balance != null) {
                response = new ResponseEntity(mapBalanceToBalanceDto(balance), HttpStatus.CREATED);
            }else {
                response = new ResponseEntity(BalanceServiceConstants.Balance_not_created,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
       return  response;
    }

    @Override
    public ResponseEntity getBalanceDetailsByBalanceId(String accountId, String balanceId) {
        ResponseEntity response=new ResponseEntity(HttpStatus.OK);
        //handle null
        Optional<Balance> balance = balanceRepository.findByBalanceId(balanceId);
        if(balance.isPresent()) {
            //return mapBalanceToBalanceDto(balance.get());
            response =new ResponseEntity(mapBalanceToBalanceDto(balance.get()),HttpStatus.OK);
        }else {
            Map<String,String> result=new HashMap<String,String>();
            result.put(BalanceServiceConstants.error_code,BalanceServiceConstants.BAD_REQUEST);
            result.put(BalanceServiceConstants.error_message,BalanceServiceConstants.balanceId_is_not_present);
            response = new ResponseEntity(result,HttpStatus.BAD_REQUEST);
        }
        return  response;
    }

    @Override
    public BalanceDTO updateBalanceByBalanceIdAndAccountId(BalanceDTO balanceDTO) {
        BalanceDTO dto=null;
        Balance entity=null;
        try {
            Optional<Balance> balance = balanceRepository.findByBalanceId(balanceDTO.getBalanceId());
            if (balance.isPresent()){
                balanceDTO.setUpdatedAt(Utils.convertDateToString(Utils.getCurrentDate()));
            entity = mapBalanceDtoToBalance(balanceDTO, balance.get());
            Balance updatedData = balanceRepository.save(entity);
            dto = mapBalanceToBalanceDto(updatedData);
        }else {
                return null;
            }
        }catch (Exception e){

        }
        return dto;
    }

    @Override
    public String deleteBalance(String accountId, String balanceId) {
        try {
            Balance entity=null;
            Optional<Balance> balance = balanceRepository.findByBalanceId(balanceId);
            if (balance.isPresent()) {
                entity = balanceRepository.deleteByBalanceIdAndAccountId(accountId, balanceId);
                return BalanceServiceConstants.Balance_deleted_successfully;
            } else {
                return BalanceServiceConstants.Balance_not_deleted;
            }
        }catch (Exception e){

        }
        return "";
    }

    @Override
    public Page getBalances(String accountId, Pageable pageable) {
        Page<List<Balance>> list =null;
        try {
             list = balanceRepository.findByAccountId(accountId, pageable);
            //List<BalanceDTO> results = list.stream().map(balance -> mapBalanceToBalanceDto(balance)).collect(Collectors.toList());
            return list;
        }catch (Exception e){

        }
        return list;
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
