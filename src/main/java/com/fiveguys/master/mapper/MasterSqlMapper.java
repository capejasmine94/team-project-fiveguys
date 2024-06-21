package com.fiveguys.master.mapper;

import com.fiveguys.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MasterSqlMapper {

    public List<SellerReviewDto> selectAllSellerReview();

    public SellerReviewDto selectSellerReview(int sellerOrderNumber);

    public SellerOrderDto selectSellerOrder(int sellerOrderNumber);

    public SellerDto selectSeller(int sellerNumber);

    public void insertMasterReply(MasterReplyDto masterReplyDto);

    public MasterReplyDto selectMasterReply(int sellerReviewNumber);

    public List<SellerOrderDto> selectAllSellerOrder(int sellerNumber);

    public List<SellerOrderDto> selectSameSellerOrder(SellerOrderDto sellerOrderDto);

    public MaterialDto selectMaterialInform(int materialNumber);

    public void updateOrderStatusProcessingShipment(SellerOrderDto sellerOrderDto);

    public void updateOrderStatusDelivery(SellerOrderDto sellerOrderDto);

    public void updateOrderStatusDeliveryCompleted(SellerOrderDto sellerOrderDto);

    public List<SellerReviewImageDto> selectReviewImage(int sellerReviewNumber);

    public void insertMaterialCategory(String materialCategoryName);




}
