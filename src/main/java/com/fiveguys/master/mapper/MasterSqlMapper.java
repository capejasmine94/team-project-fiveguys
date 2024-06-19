package com.fiveguys.master.mapper;

import com.fiveguys.dto.MasterReplyDto;
import com.fiveguys.dto.SellerDto;
import com.fiveguys.dto.SellerOrderDto;
import com.fiveguys.dto.SellerReviewDto;
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


}
