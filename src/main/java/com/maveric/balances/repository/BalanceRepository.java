package com.maveric.balances.repository;

import com.maveric.balances.model.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends MongoRepository<Balance,String> {

    public Optional<Balance> findByBalanceId(String balanceId);

    public  Balance deleteByBalanceIdAndAccountId(String accountId,String balanceId);

    public Page<List<Balance>> findByAccountId(String accountId, Pageable pageable);
}
