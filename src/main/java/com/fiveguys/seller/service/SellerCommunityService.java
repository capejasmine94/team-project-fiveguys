package com.fiveguys.seller.service;

import com.fiveguys.seller.mapper.SellerCommunitySqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerCommunityService {

    @Autowired
    private SellerCommunitySqlMapper sellerCommunitySqlMapper;
}
