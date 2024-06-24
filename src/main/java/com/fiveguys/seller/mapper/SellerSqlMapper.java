package com.fiveguys.seller.mapper;

import com.fiveguys.dto.*;
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

    public List<SellerReviewDto> selectMyReview(int sellerNumber);

    public MasterReplyDto selectMasterReply(int sellerReviewNumber);

    public void insertReviewImage(SellerReviewImageDto sellerReviewImageDto);

    public List<SellerReviewImageDto> selectReviewImage(int sellerReviewNumber);

    public List<MaterialDto> selectMaterial();

    public List<MaterialCategoryDto> selectMaterialCategory();

    public List<MaterialDto> selectMaterialByCategoryNumber(int materialCategoryNumber);

    public MaterialImageDto selectMaterialImage(int materialNumber);

    public int selectRecentReviewNumber();

    public void deleteReview(int sellerReviewNumber);

    public void updateReview(SellerReviewDto sellerReviewDto);





}
