package com.fiveguys.seller.mapper;

import com.fiveguys.dto.MaterialDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerSqlMapper {

    public void insertSellerOrder(SellerOrderDto sellerOrderDto);

    public List<SellerOrderDto> selectOrderList(int sellerNumber);

    public MaterialDto selectMaterialInform(int materialNumber);

    public SellerDto selectSellerInform(int sellerNumber);

    public void updateMaterialQuantity(SellerOrderDto sellerOrderDto);

    public List<SellerOrderDto> selectRecentSellerOrder(int sellerNumber);

    public List<SellerOrderDto> selectAllSellerOrder(int sellerNumber);

    public List<SellerOrderDto> selectSameSellerOrder(SellerOrderDto sellerOrderDto);

    public SellerOrderDto selectSellerOrderInform(int sellerOrderNumber);







}
