package com.fiveguys.seller.service;

import com.fiveguys.dto.SellerDto;
import com.fiveguys.seller.mapper.SellerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerSqlMapper sellerSqlMapper;


    public List<SellerDto> getAllSellers() {

         return sellerSqlMapper.selectAllSellers();
    }



    public SellerDto sellerLoginProcess(SellerDto sellerDto) {
        SellerDto sellerInform = sellerSqlMapper.sellerLoginProcess(sellerDto);

        return sellerInform;

    }

}
