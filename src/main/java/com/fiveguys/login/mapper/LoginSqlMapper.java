package com.fiveguys.login.mapper;

import com.fiveguys.dto.CustomerDto;
import com.fiveguys.dto.MasterDto;
import com.fiveguys.dto.SellerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginSqlMapper {
    public CustomerDto selectCustomerCheck(CustomerDto customerDto);
    public SellerDto selectSellerCheck(SellerDto sellerDto);
    public MasterDto selectMasterCheck(MasterDto masterDto);
}
