package com.fiveguys.login.service;

import com.fiveguys.dto.CustomerDto;
import com.fiveguys.dto.MasterDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.login.mapper.LoginSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginSqlMapper loginSqlMapper;

    public CustomerDto selectCustomerCheck(CustomerDto customerDto) {
        return loginSqlMapper.selectCustomerCheck(customerDto);
    }

    public SellerDto selectSellerCheck(SellerDto sellerDto) {
        return loginSqlMapper.selectSellerCheck(sellerDto);
    }

    public MasterDto selectMasterCheck(MasterDto masterDto) {
        return loginSqlMapper.selectMasterCheck(masterDto);
    }
}
