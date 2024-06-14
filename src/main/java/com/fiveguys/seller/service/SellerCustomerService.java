package com.fiveguys.seller.service;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.mapper.SellerCustomerSqlMapper;
import com.fiveguys.seller.mapper.SellerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerCustomerService {

    @Autowired
    private SellerCustomerSqlMapper sellerCustomerSqlMapper;

    public List<SellerDto> selectAllSellers() {

        return sellerCustomerSqlMapper.selectAllSellers();
    }

    public SellerDto selectSellersByNumber(int sellerNumber) {

        return sellerCustomerSqlMapper.selectSellersByNumber(sellerNumber);

    }
}
