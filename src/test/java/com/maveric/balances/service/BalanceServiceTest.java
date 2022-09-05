package com.maveric.balances.service;

public class BalanceServiceTest {
      @Mock
     private BalanceRepository balanceRepository;
    @InjectMocks
    private BalancesServiceImpl balancesService;

    private Balance balance;

    private BalanceDTO balanceDTO;
    @BeforeEach
    public  void setUp(){
        balance=new Balance();
        balance.setBalanceId("a1");
        balance.setAmount("22");
        balance.setCurrency(Currency.INR);
        balance.setAccountId("1");
        balanceDTO=new BalanceDTO();
        balanceDTO.setBalanceId("a1");
        balanceDTO.setAmount("22");
        balanceDTO.setCurrency(Currency.INR);
        balanceDTO.setAccountId("1");
    }

    public  BalanceDTO prepairDto(){
       BalanceDTO balanceDTO1=new BalanceDTO();
        balanceDTO1.setBalanceId("a1");
        balanceDTO1.setAmount("22");
        balanceDTO1.setCurrency(Currency.INR);
        balanceDTO1.setAccountId("1");
        return  balanceDTO1;
    }

      @Test
    public  void giveBalanceObjectWhenCreateBalanceThenReturnObject(){
              Balance balance1=new Balance();
          when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(balance);
          given(balanceRepository.findByBalanceId(balance.getBalanceId())).willReturn(Optional.of(balance));
        ResponseEntity response = balancesService.createBalance(prepairDto());

          verify(balanceRepository, times(1)).save(Mockito.any(Balance.class));
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    @Test
    public  void givenBalanceObjectWhenCreateBalanceThenReturnBalanceNotCreateMsg(){
       when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(balance);
       given(balanceRepository.findByBalanceId(balance.getBalanceId())).willReturn(Optional.empty());
       ResponseEntity response = balancesService.createBalance(prepairDto());
        verify(balanceRepository, times(1)).save(Mockito.any(Balance.class));
        Assertions.assertEquals(BalanceServiceConstants.BALANCE_NOT_CREATED,response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void givenObjectWhenCreateBalanceThenThrowException(){

        given(balanceRepository.save(balance)).willReturn(new Balance());
        given(balanceRepository.findByBalanceId(balance.getBalanceId())).willThrow(new CreateBalanceException());
    Assertions.assertThrows(CreateBalanceException.class,()->balancesService.createBalance(balanceDTO));
    }

    @Test
    public  void  giveAccountIdAndBalanceIdWhenGetBalanceDetailsByBalanceIdThenReturnBalance(){
       given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.of(balance));
       ResponseEntity response =  balancesService.getBalanceDetailsByBalanceId(balanceDTO.getAccountId(),balanceDTO.getBalanceId());
       Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void giveAccountIdAndBalanceIdWhenGetBalanceDetailsByBalanceIdThenReturnBalanceIdNotPresentMsg(){
     given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.empty());
     ResponseEntity response = balancesService.getBalanceDetailsByBalanceId(balanceDTO.getAccountId(),balanceDTO.getBalanceId());
        Map<String, String> result = new HashMap();
        result.put(BalanceServiceConstants.ERROR_CODE, BalanceServiceConstants.BAD_REQUEST);
        result.put(BalanceServiceConstants.ERROR_MESSAGE, BalanceServiceConstants.BALANCE_ID_IS_NOT_PRESENT);
     Assertions.assertEquals(result,response.getBody());
     Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void giveAccountIdAndBalanceIdWhenGetBalanceDetailsByBalanceIdThenThrowException(){
        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willThrow(new CreateBalanceException());
        Assertions.assertThrows(CreateBalanceException.class,()->balancesService.getBalanceDetailsByBalanceId(balanceDTO.getAccountId(),balanceDTO.getBalanceId()));
    }
    @Test
    public void givenBalanceObjectWhenUpdateBalanceByBalanceIdAndAccountIdThenReturnObject(){

        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.of(balance));
        when(balanceRepository.save(balance)).thenReturn(balance);
        ResponseEntity response = balancesService.updateBalanceByBalanceIdAndAccountId(prepairDto());

        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void givenBalanceObjectWhenUpdateBalanceByBalanceIdAndAccountIdThenReturnErrorMsg(){

        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.empty());
        ResponseEntity response = balancesService.updateBalanceByBalanceIdAndAccountId(prepairDto());
        Map<String,String> result=new HashMap();
        result.put(BalanceServiceConstants.ERROR_CODE,BalanceServiceConstants.BAD_REQUEST);
        result.put(BalanceServiceConstants.ERROR_MESSAGE,BalanceServiceConstants.BALANCE_ID_NOT_PRESENT);
        Assertions.assertEquals(result,response.getBody());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void givenBalanceObjectWhenUpdateBalanceByBalanceIdAndAccountIdThenThrowException(){

        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.of(balance));
        given(balanceRepository.save(Mockito.any(Balance.class))).willThrow(new CreateBalanceException());
        Assertions.assertThrows(CreateBalanceException.class,()->balancesService.updateBalanceByBalanceIdAndAccountId(prepairDto()));
    }
    @Test
   public void giveBalanceIdWhenDeleteBalanceThenReturnSuccessfullyMsg(){
        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.of(balance));
        ResponseEntity response = balancesService.deleteBalance(balanceDTO.getAccountId(),balanceDTO.getBalanceId());
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertEquals(BalanceServiceConstants.BALANCE_DELETED_SUCCESSFULLY,response.getBody());
    }

    @Test
    public void giveBalanceIdWhenDeleteBalanceThenReturnErrorMsg(){
        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willReturn(Optional.empty());
        ResponseEntity response = balancesService.deleteBalance(balanceDTO.getAccountId(),balanceDTO.getBalanceId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Map<String,String> result=new HashMap();
        result.put(BalanceServiceConstants.ERROR_CODE,BalanceServiceConstants.BAD_REQUEST);
        result.put(BalanceServiceConstants.ERROR_MESSAGE,BalanceServiceConstants.BALANCE_ID_NOT_PRESENT);
        Assertions.assertEquals(result,response.getBody());
    }

    @Test
    public void giveBalanceIdWhenDeleteBalanceThenThrowException(){
        given(balanceRepository.findByBalanceId(balanceDTO.getBalanceId())).willThrow(new CreateBalanceException());
        Assertions.assertThrows(CreateBalanceException.class,()->balancesService.deleteBalance(balanceDTO.getAccountId(),balanceDTO.getBalanceId()));
    }

    @Test
    public void getBalancesTest(){
        Pageable paging = PageRequest.of(0, 5, Sort.by("inventory"));
        Page<List<Balance>> balancePage= Mockito.mock(Page.class);

        given(balanceRepository.findByAccountId(balanceDTO.getAccountId(),paging)).willReturn(balancePage);
      Page response  = balancesService.getBalances(balanceDTO.getAccountId(),paging);

      Assertions.assertTrue(response.getTotalElements() >0);
    }

}
