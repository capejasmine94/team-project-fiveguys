package com.fiveguys.master.service;

import com.fiveguys.dto.MasterReplyDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.dto.SellerReviewDto;
import com.fiveguys.master.mapper.MasterSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MasterService {

    @Autowired
    private MasterSqlMapper masterSqlMapper;



    public List<Map<String, Object>> selectAllSellerReview() {

        List<Map<String, Object>> allSellerReview = new ArrayList<>();
        List<SellerReviewDto> sellerReviewDtoList = masterSqlMapper.selectAllSellerReview();

        for (SellerReviewDto sellerReviewDto : sellerReviewDtoList) {
            Map<String, Object> map = new HashMap<>();

            SellerOrderDto sellerOrderDto = masterSqlMapper.selectSellerOrder(sellerReviewDto.getSellerOrderNumber());
            SellerDto sellerDto = masterSqlMapper.selectSeller(sellerOrderDto.getSellerNumber());

            map.put("sellerDto", sellerDto);
            map.put("sellerOrderDto", sellerOrderDto);
            map.put("sellerReviewDto", sellerReviewDto);

            allSellerReview.add(map);
        }
        return allSellerReview;
    }


    public Map<String, Object> selectSellerReview(int sellerOrderNumber) {
        Map<String, Object> sellerReview = new HashMap<>();
        SellerReviewDto sellerReviewDto = masterSqlMapper.selectSellerReview(sellerOrderNumber);

        SellerOrderDto sellerOrderDto = masterSqlMapper.selectSellerOrder(sellerReviewDto.getSellerOrderNumber());
        SellerDto sellerDto = masterSqlMapper.selectSeller(sellerOrderDto.getSellerNumber());
        MasterReplyDto masterReplyDto = masterSqlMapper.selectMasterReply(sellerReviewDto.getSellerReviewNumber());

        Map<String, Object> map = new HashMap<>();

        map.put("sellerDto", sellerDto);
        map.put("sellerOrderDto", sellerOrderDto);
        map.put("sellerReviewDto", sellerReviewDto);
        map.put("masterReplyDto", masterReplyDto);

        return map;
    }


    public void insertMasterReply(MasterReplyDto masterReplyDto) {
        masterSqlMapper.insertMasterReply(masterReplyDto);
    }

}
