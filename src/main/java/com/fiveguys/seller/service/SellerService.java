package com.fiveguys.seller.service;


import org.springframework.stereotype.Service;

@Service
public class SellerService {


    public List<SellerDto> getAllSellers() {

         return sellerSqlMapper.selectAllSellers();
    }



    public SellerDto sellerLoginProcess(SellerDto sellerDto) {
        SellerDto sellerInform = sellerSqlMapper.sellerLoginProcess(sellerDto);

        return sellerInform;

    }

}
