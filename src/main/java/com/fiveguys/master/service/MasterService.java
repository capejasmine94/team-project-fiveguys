package com.fiveguys.master.service;

import com.fiveguys.dto.*;
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
        List<SellerReviewImageDto> sellerReviewImageDtoList = masterSqlMapper.selectReviewImage(sellerReviewDto.getSellerReviewNumber());

        Map<String, Object> map = new HashMap<>();

        map.put("sellerDto", sellerDto);
        map.put("sellerOrderDto", sellerOrderDto);
        map.put("sellerReviewDto", sellerReviewDto);
        map.put("masterReplyDto", masterReplyDto);
        map.put("sellerReviewImageDtoList", sellerReviewImageDtoList);

        return map;
    }


    public void insertMasterReply(MasterReplyDto masterReplyDto) {
        masterSqlMapper.insertMasterReply(masterReplyDto);
    }


    public List<Map<String, Object>> selectAllSellerOrder(int sellerNumber) {
        List<Map<String, Object>> sellerOrderInform = new ArrayList<>();
        List<SellerOrderDto> sellerOrderDtoList = masterSqlMapper.selectAllSellerOrder(sellerNumber);

        for (SellerOrderDto sellerOrderDto : sellerOrderDtoList) {

            SellerDto sellerDto = masterSqlMapper.selectSeller(sellerOrderDto.getSellerNumber());

            Map<String, Object> map = new HashMap<>();
            map.put("sellerDto", sellerDto);
            map.put("sellerOrderDto", sellerOrderDto);

            sellerOrderInform.add(map);
        }
        return sellerOrderInform;
    }


    public SellerOrderDto selectSellerOrder(int sellerOrderNumber) {
        SellerOrderDto sellerOrderDto = masterSqlMapper.selectSellerOrder(sellerOrderNumber);

        return sellerOrderDto;
    }


    public List<Map<String, Object>> selectSameSellerOrder(SellerOrderDto sellerOrderDto, int id) {
        List<Map<String, Object>> sellerOrderList = new ArrayList<>();

        SellerOrderDto sellerOrderInform = masterSqlMapper.selectSellerOrder(id);

        sellerOrderDto.setSellerNumber(sellerOrderInform.getSellerNumber());
        sellerOrderDto.setSellerOrderCreatedAt(sellerOrderInform.getSellerOrderCreatedAt());

        List<SellerOrderDto> sellerOrderDtoList = masterSqlMapper.selectSameSellerOrder(sellerOrderDto);

        for (SellerOrderDto sellerOrderDtos : sellerOrderDtoList) {
            SellerDto sellerDto = masterSqlMapper.selectSeller(sellerOrderDtos.getSellerNumber());
            MaterialDto materialDto = masterSqlMapper.selectMaterialInform(sellerOrderDtos.getMaterialNumber());

            Map<String, Object> map = new HashMap<>();
            map.put("sellerDto", sellerDto);
            map.put("sellerOrderDto", sellerOrderDtos);
            map.put("materialDto", materialDto);

            sellerOrderList.add(map);
        }
        return sellerOrderList;
    }



    public void updateOrderStatusProcessingShipment(int id) {
        SellerOrderDto sellerOrderInform = masterSqlMapper.selectSellerOrder(id);

        SellerOrderDto sellerOrderDto = new SellerOrderDto();

        sellerOrderDto.setSellerOrderCreatedAt(sellerOrderInform.getSellerOrderCreatedAt());
        sellerOrderDto.setSellerNumber(sellerOrderInform.getSellerNumber());

        masterSqlMapper.updateOrderStatusProcessingShipment(sellerOrderDto);

    }

    public void updateOrderStatusDelivery(int id) {
        SellerOrderDto sellerOrderInform = masterSqlMapper.selectSellerOrder(id);

        SellerOrderDto sellerOrderDto = new SellerOrderDto();

        sellerOrderDto.setSellerOrderCreatedAt(sellerOrderInform.getSellerOrderCreatedAt());
        sellerOrderDto.setSellerNumber(sellerOrderInform.getSellerNumber());

        masterSqlMapper.updateOrderStatusDelivery(sellerOrderDto);
    }


    public void updateOrderStatusDeliveryCompleted(int id) {
        SellerOrderDto sellerOrderInform = masterSqlMapper.selectSellerOrder(id);

        SellerOrderDto sellerOrderDto = new SellerOrderDto();

        sellerOrderDto.setSellerOrderCreatedAt(sellerOrderInform.getSellerOrderCreatedAt());
        sellerOrderDto.setSellerNumber(sellerOrderInform.getSellerNumber());

        masterSqlMapper.updateOrderStatusDeliveryCompleted(sellerOrderDto);
    }


    public void insertMaterialCategory(String materialCategoryName) {
        masterSqlMapper.insertMaterialCategory(materialCategoryName);
    }



}
