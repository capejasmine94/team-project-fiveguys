package com.fiveguys.seller.mapper;

import com.fiveguys.dto.MaterialDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.dto.SellerReviewDto;
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

    public void deleteSellerRecentOrder();

    public SellerOrderDto selectSellerOrderInform(int sellerOrderNumber);

    public void insertSellerReview(SellerReviewDto sellerReviewDto);

    public List<SellerReviewDto> selectAllSellerReview();

    public SellerReviewDto selectSellerReview(int id);

    public int selectSellerReviewCheck(int sellerOrderNumber);







}
