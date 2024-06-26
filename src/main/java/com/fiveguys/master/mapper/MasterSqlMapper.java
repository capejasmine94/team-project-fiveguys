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

    public List<MaterialCategoryDto> selectMaterialCategory();

    public void insertMaterial(MaterialDto materialDto);

    public void insertMaterialImage(MaterialImageDto materialImageDto);

    public MaterialDto selectRecentMaterial();

    public MaterialImageDto selectRecentMaterialImage();

    public SellerReviewDto selectSellerReviewInformByReviewNumber(int sellerReviewNumber);

    public List<SellerReviewDto> selectRecentSellerReview();

    public List<SellerOrderDto> selectRecentSellerOrder();

    public void deleteReply(int masterReplyNumber);

    public void updateReply(MasterReplyDto masterReplyDto);

    public MasterReplyDto selectMasterReplyByReviewNumber(int sellerReviewNumber);

    public int selectOrderTotalPrice(SellerOrderDto sellerOrderDto);




}
