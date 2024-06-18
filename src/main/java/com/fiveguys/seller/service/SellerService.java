package com.fiveguys.seller.service;


import com.fiveguys.dto.MaterialDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.seller.mapper.SellerSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerService {

    @Autowired
    private SellerSqlMapper sellerSqlMapper;

    public void insertSellerOrder(SellerOrderDto sellerOrderDto, List<Integer> materialNumber) {

        for (Integer materialNumberList : materialNumber) {
            sellerOrderDto.setMaterialNumber(materialNumberList);

            sellerSqlMapper.insertSellerOrder(sellerOrderDto);
        }
    }


    public List<Map<String, Object>> selectSellerOrder(int sellerNumber) {

        List<Map<String, Object>> sellerOrderList = new ArrayList<>();

        List<SellerOrderDto> sellerOrderDtos = sellerSqlMapper.selectOrderList(sellerNumber);

        for (SellerOrderDto sellerOrderDto : sellerOrderDtos) {
            Map<String, Object> map = new HashMap<>();

            int materialNumber = sellerOrderDto.getMaterialNumber();
            MaterialDto materialDto = sellerSqlMapper.selectMaterialInform(materialNumber);

            map.put("materialDto", materialDto);
            map.put("sellerOrderDto", sellerOrderDto);

            sellerOrderList.add(map);
        }

        return sellerOrderList;
    }


    public void updateMaterialQuantity(int[] sellerOrderQuantity, int[] sellerOrderNumber) {

        for (int i = 0; i < sellerOrderQuantity.length; i++) {
            SellerOrderDto sellerOrderDto = new SellerOrderDto();

            sellerOrderDto.setSellerOrderQuantity(sellerOrderQuantity[i]);
            sellerOrderDto.setSellerOrderNumber(sellerOrderNumber[i]);

            sellerSqlMapper.updateMaterialQuantity(sellerOrderDto);

        }
    }


    public List<Map<String, Object>> selectRecentSellerOrder(int sellerNumber) {
        List<Map<String, Object>> sellerOrderList = new ArrayList<>();

        List<SellerOrderDto> sellerOrderDtoList = sellerSqlMapper.selectRecentSellerOrder(sellerNumber);

        for (SellerOrderDto sellerOrderDto : sellerOrderDtoList) {
         int number = sellerOrderDto.getSellerNumber();
         SellerDto sellerDto =  sellerSqlMapper.selectSellerInform(number);

         Map<String, Object> map = new HashMap<>();

         map.put("sellerDto", sellerDto);
         map.put("sellerOrderDto", sellerOrderDto);

         sellerOrderList.add(map);

        }

        return sellerOrderList;
    }



    public List<Map<String, Object>> selectAllSellerOrder(int sellerNumber) {
        List<Map<String, Object>> sellerOrderList = new ArrayList<>();
        List<SellerOrderDto> sellerOrderDtoList = sellerSqlMapper.selectAllSellerOrder(sellerNumber);

        for (SellerOrderDto sellerOrderDto : sellerOrderDtoList) {
            int number = sellerOrderDto.getSellerNumber();
            SellerDto sellerDto =  sellerSqlMapper.selectSellerInform(number);

            Map<String, Object> map = new HashMap<>();

            map.put("sellerDto", sellerDto);
            map.put("sellerOrderDto", sellerOrderDto);

            sellerOrderList.add(map);

        }

        return sellerOrderList;

    }


    public List<Map<String, Object>> selectSameSellerOrder(SellerOrderDto sellerOrderDtos, int id) {

        List<Map<String, Object>> sellerOrderList = new ArrayList<>();

        SellerOrderDto sellerOrderInform = sellerSqlMapper.selectSellerOrderInform(id);

        sellerOrderDtos.setSellerNumber(sellerOrderInform.getSellerNumber());
        sellerOrderDtos.setSellerOrderCreatedAt(sellerOrderInform.getSellerOrderCreatedAt());

        List<SellerOrderDto> sellerOrderDtoList = sellerSqlMapper.selectSameSellerOrder(sellerOrderDtos);

        for (SellerOrderDto sellerOrderDto : sellerOrderDtoList) {
            Map<String, Object> map = new HashMap<>();

            int materialNumber = sellerOrderDto.getMaterialNumber();
            MaterialDto materialDto = sellerSqlMapper.selectMaterialInform(materialNumber);

            map.put("materialDto", materialDto);
            map.put("sellerOrderDto", sellerOrderDto);

            sellerOrderList.add(map);
        }
        return sellerOrderList;

    }



}