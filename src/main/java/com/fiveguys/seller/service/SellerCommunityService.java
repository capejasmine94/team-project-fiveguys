package com.fiveguys.seller.service;

import com.fiveguys.dto.*;
import com.fiveguys.seller.mapper.SellerCommunitySqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SellerCommunityService {

    @Autowired
    private SellerCommunitySqlMapper sellerCommunitySqlMapper;

    public SellerDto selectSeller(int sellerNumber){
        return sellerCommunitySqlMapper.selectSeller(sellerNumber);
    }

    //게시글등록
    public void insertSellerCommunityWrite(SellerCommunityDto sellerCommunityDto, List<SellerCommunityImageDetailDto> sellerCommunityImageDetailDtoList) {
        sellerCommunitySqlMapper.insertSellerCommunityWrite(sellerCommunityDto);
        for (SellerCommunityImageDetailDto sellerCommunityImageDetailDto : sellerCommunityImageDetailDtoList) {
            sellerCommunityImageDetailDto.setSellerCommunityNumber(sellerCommunityDto.getSellerCommunityNumber());
            sellerCommunitySqlMapper.insertSellerCommunityImageDetail(sellerCommunityImageDetailDto);
        }
    }

    public List<Map<String,Object>> selectSellerCommunityList(SellerCommunityPaginationDto sellerCommunityPaginationDto, int sellerNumber){
        List<Map<String,Object>> result = new ArrayList<>();
        List<SellerCommunityDto> sellerCommunityDtoList = sellerCommunitySqlMapper.selectSellerCommunityList(sellerCommunityPaginationDto);

        System.out.println(sellerCommunitySqlMapper.selectSellerCommunityList(sellerCommunityPaginationDto));
        for (SellerCommunityDto sellerCommunityDto : sellerCommunityDtoList) {
            Map<String,Object> map = new HashMap<>();


            SellerCommunityLikeDto sellerCommunityLikeDto = new SellerCommunityLikeDto();
            sellerCommunityLikeDto.setSellerNumber(sellerNumber);
            sellerCommunityLikeDto.setSellerCommunityNumber(sellerCommunityDto.getSellerCommunityNumber());

            int textLength = sellerCommunityDto.getSellerCommunityContent().length();
            map.put("checkIfSellerCommunityLikeExists",sellerCommunitySqlMapper.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto));
            map.put("textLength", textLength);
            map.put("sellerCommunityDto", sellerCommunityDto);
            map.put("selectTotalCommentReplyCount",sellerCommunitySqlMapper.selectTotalCommentCount(sellerCommunityDto.getSellerCommunityNumber())+sellerCommunitySqlMapper.selectTotalReplyCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityDto.getSellerCommunityNumber()));
            result.add(map);
        }
        return result;
    }

    public List<Map<String,Object>> selectSellerCommunityByPopularity(SellerCommunityPaginationDto sellerCommunityPaginationDto, int sellerNumber){
        List<Map<String,Object>> result = new ArrayList<>();
        List<SellerCommunityDto> sellerCommunityDtoList = sellerCommunitySqlMapper.selectSellerCommunityByPopularity(sellerCommunityPaginationDto);

        for (SellerCommunityDto sellerCommunityDto : sellerCommunityDtoList) {
            Map<String,Object> map = new HashMap<>();


            SellerCommunityLikeDto sellerCommunityLikeDto = new SellerCommunityLikeDto();
            sellerCommunityLikeDto.setSellerNumber(sellerNumber);
            sellerCommunityLikeDto.setSellerCommunityNumber(sellerCommunityDto.getSellerCommunityNumber());

            int textLength = sellerCommunityDto.getSellerCommunityContent().length();
            map.put("checkIfSellerCommunityLikeExists",sellerCommunitySqlMapper.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto));
            map.put("textLength", textLength);
            map.put("sellerCommunityDto", sellerCommunityDto);
            map.put("selectTotalCommentReplyCount",sellerCommunitySqlMapper.selectTotalCommentCount(sellerCommunityDto.getSellerCommunityNumber())+sellerCommunitySqlMapper.selectTotalReplyCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityDto.getSellerCommunityNumber()));
            map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityDto.getSellerCommunityNumber()));
            result.add(map);
        }
        return result;
    }

    public Map<String,Object> selectSellerCommunityById(int sellerCommunityNumber){
        Map<String,Object> map = new HashMap<>();

        SellerCommunityDto sellerCommunityDto =sellerCommunitySqlMapper.selectSellerCommunityById(sellerCommunityNumber);
        String escapedContent = StringUtils.escapeXml(sellerCommunityDto.getSellerCommunityContent());
        escapedContent=escapedContent.replaceAll("\n","<br>");
        sellerCommunityDto.setSellerCommunityContent(escapedContent);

        map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityNumber));
        map.put("selectSellerCommunityById",sellerCommunityDto);
        map.put("selectImageListById",sellerCommunitySqlMapper.selectImageListById(sellerCommunityNumber));
        map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityNumber));
        map.put("selectTotalCommentReplyCount",sellerCommunitySqlMapper.selectTotalCommentCount(sellerCommunityNumber)+sellerCommunitySqlMapper.selectTotalReplyCount(sellerCommunityNumber));

        List<Map<String,Object>> sellerCommunityCommentDtoList = sellerCommunitySqlMapper.selectSellerCommunityComment(sellerCommunityNumber);
        List<Map<String,Object>> result = new ArrayList<>();
        for(Map<String,Object> sellerCommunityCommentDto: sellerCommunityCommentDtoList){
            Map<String,Object> smallMap = new HashMap<>();


            List<Map<String,Object>> result2 = new ArrayList<>();
            List<Map<String,Object>> sellerCommunityReplyLikeStatusList = sellerCommunitySqlMapper.selectSellerCommunityReply((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber"));

            for(Map<String,Object> sellerCommunityReplyLikeStatusMap : sellerCommunityReplyLikeStatusList){

                Map<String,Object> verySmallMap = new HashMap<>();
                SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto1= new SellerCommunityReplyLikeStatusDto();
                sellerCommunityReplyLikeStatusDto1.setSellerCommunityReplyNumber((int)sellerCommunityReplyLikeStatusMap.get("sellerCommunityReplyNumber"));
                sellerCommunityReplyLikeStatusDto1.setSellerNumber((int)sellerCommunityReplyLikeStatusMap.get("sellerNumber"));
                SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto = sellerCommunitySqlMapper.selectSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto1);

                verySmallMap.put("selectSellerCommunityReply",sellerCommunityReplyLikeStatusMap);
                verySmallMap.put("selectSellerReplyLikeCount",sellerCommunitySqlMapper.selectSellerReplyLikeCount(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyNumber()));
                verySmallMap.put("selectSellerReplyDisLikeCount",sellerCommunitySqlMapper.selectSellerReplyDisLikeCount(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyNumber()));
                result2.add(verySmallMap);
            }
            smallMap.put("sellerCommunityReplyContainer",result2);
            smallMap.put("sellerCommunityCommentDto",sellerCommunityCommentDto);
            SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDtoTemp = new SellerCommunityCommentLikeStatusDto();
            sellerCommunityCommentLikeStatusDtoTemp.setSellerNumber((int)sellerCommunityCommentDto.get("sellerNumber"));
            sellerCommunityCommentLikeStatusDtoTemp.setSellerCommunityCommentNumber((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber"));

            SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto = sellerCommunitySqlMapper.selectSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDtoTemp);

            smallMap.put("selectEachSellerCommentReplyCount",sellerCommunitySqlMapper.selectEachSellerCommentReplyCount((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber")));
            smallMap.put("selectSellerCommentLikeCount",sellerCommunitySqlMapper.selectSellerCommentLikeCount(sellerCommunityCommentLikeStatusDto.getSellerCommunityCommentNumber()));
            smallMap.put("selectSellerCommentDisLikeCount",sellerCommunitySqlMapper.selectSellerCommentDisLikeCount(sellerCommunityCommentLikeStatusDto.getSellerCommunityCommentNumber()));
            smallMap.put("selectSellerCommentLikeStatus",sellerCommunityCommentLikeStatusDto);
            result.add(smallMap);
        }
        map.put("selectSellerCommunityComment",result);

        return map;
    }
    public Map<String,Object> selectSellerCommunityByIdWithSession(int sellerCommunityNumber,int sellerNumber){
        Map<String,Object> map = new HashMap<>();

        SellerCommunityDto sellerCommunityDto =sellerCommunitySqlMapper.selectSellerCommunityById(sellerCommunityNumber);
        String escapedContent = StringUtils.escapeXml(sellerCommunityDto.getSellerCommunityContent());
        escapedContent=escapedContent.replaceAll("\n","<br>");
        sellerCommunityDto.setSellerCommunityContent(escapedContent);

        map.put("sellerName",sellerCommunitySqlMapper.selectSeller(sellerCommunityNumber));
        map.put("selectSellerCommunityById",sellerCommunityDto);
        map.put("selectImageListById",sellerCommunitySqlMapper.selectImageListById(sellerCommunityNumber));
        map.put("selectSellerCommunityLikeCount",sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityNumber));
        map.put("selectTotalCommentReplyCount",sellerCommunitySqlMapper.selectTotalCommentCount(sellerCommunityNumber)+sellerCommunitySqlMapper.selectTotalReplyCount(sellerCommunityNumber));

        List<Map<String,Object>> sellerCommunityCommentDtoList = sellerCommunitySqlMapper.selectSellerCommunityComment(sellerCommunityNumber);
        List<Map<String,Object>> result = new ArrayList<>();


        for(Map<String,Object> sellerCommunityCommentDto: sellerCommunityCommentDtoList){
            Map<String,Object> smallMap = new HashMap<>();
            List<Map<String,Object>> result2 = new ArrayList<>();
            List<Map<String,Object>> sellerCommunityReplyLikeStatusList = sellerCommunitySqlMapper.selectSellerCommunityReply((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber"));

            for(Map<String,Object> sellerCommunityReplyLikeStatusMap : sellerCommunityReplyLikeStatusList){

                Map<String,Object> verySmallMap = new HashMap<>();
                SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto1= new SellerCommunityReplyLikeStatusDto();
                sellerCommunityReplyLikeStatusDto1.setSellerCommunityReplyNumber((int)sellerCommunityReplyLikeStatusMap.get("sellerCommunityReplyNumber"));
                sellerCommunityReplyLikeStatusDto1.setSellerNumber(sellerNumber);
                SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto = sellerCommunitySqlMapper.selectSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto1);

                if(sellerCommunityReplyLikeStatusDto ==null){
                    SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto2 = new SellerCommunityReplyLikeStatusDto();
                    sellerCommunityReplyLikeStatusDto2.setSellerCommunityReplyNumber((int)sellerCommunityReplyLikeStatusMap.get("sellerCommunityReplyNumber"));
                    sellerCommunityReplyLikeStatusDto2.setSellerNumber(sellerNumber);
                    sellerCommunityReplyLikeStatusDto2.setSellerCommunityReplyLikeStatus("");
                    sellerCommunitySqlMapper.insertSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto2);
                    sellerCommunityReplyLikeStatusDto=sellerCommunityReplyLikeStatusDto2;
                }

                verySmallMap.put("sellerCommunityReplyLikeStatusDto",sellerCommunityReplyLikeStatusDto);
                verySmallMap.put("selectSellerCommunityReply",sellerCommunityReplyLikeStatusMap);
                verySmallMap.put("selectSellerReplyLikeCount",sellerCommunitySqlMapper.selectSellerReplyLikeCount(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyNumber()));
                verySmallMap.put("selectSellerReplyDisLikeCount",sellerCommunitySqlMapper.selectSellerReplyDisLikeCount(sellerCommunityReplyLikeStatusDto.getSellerCommunityReplyNumber()));
                result2.add(verySmallMap);
            }
            smallMap.put("sellerCommunityReplyContainer",result2);
            smallMap.put("sellerCommunityCommentDto",sellerCommunityCommentDto);

            SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDtoTemp = new SellerCommunityCommentLikeStatusDto();
            sellerCommunityCommentLikeStatusDtoTemp.setSellerNumber(sellerNumber);
            sellerCommunityCommentLikeStatusDtoTemp.setSellerCommunityCommentNumber((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber"));
            SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto = sellerCommunitySqlMapper.selectSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDtoTemp);
            //댓글 좋아요 싫어요 삽입
            if(sellerCommunityCommentLikeStatusDto==null){
                SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto1 = new SellerCommunityCommentLikeStatusDto();
                sellerCommunityCommentLikeStatusDto1.setSellerCommunityCommentNumber((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber"));
                sellerCommunityCommentLikeStatusDto1.setSellerNumber(sellerNumber);
                sellerCommunityCommentLikeStatusDto1.setSellerCommentLikeStatus("");
                sellerCommunitySqlMapper.insertSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto1);
                sellerCommunityCommentLikeStatusDto=sellerCommunityCommentLikeStatusDto1;
            }
            smallMap.put("selectEachSellerCommentReplyCount",sellerCommunitySqlMapper.selectEachSellerCommentReplyCount((int)sellerCommunityCommentDto.get("sellerCommunityCommentNumber")));
            smallMap.put("selectSellerCommentLikeCount",sellerCommunitySqlMapper.selectSellerCommentLikeCount(sellerCommunityCommentLikeStatusDto.getSellerCommunityCommentNumber()));
            smallMap.put("selectSellerCommentDisLikeCount",sellerCommunitySqlMapper.selectSellerCommentDisLikeCount(sellerCommunityCommentLikeStatusDto.getSellerCommunityCommentNumber()));
            smallMap.put("selectSellerCommentLikeStatus",sellerCommunityCommentLikeStatusDto);
            result.add(smallMap);
        }
        map.put("selectSellerCommunityComment",result);
        return map;
    }

    public List<SellerCommunityImageDetailDto> selectImageListById(int sellerCommunityNumber){
        return sellerCommunitySqlMapper.selectImageListById(sellerCommunityNumber);
    }

    public int selectSellerCommunityCount(SellerCommunityPaginationDto sellerCommunityPaginationDto){
        return sellerCommunitySqlMapper.selectSellerCommunityCount(sellerCommunityPaginationDto);
    }

    //댓글삽입
    public void insertSellerCommunityComment(SellerCommunityCommentDto sellerCommunityCommentDto){
        sellerCommunitySqlMapper.insertSellerCommunityComment(sellerCommunityCommentDto);
    }
    //댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityComment(int sellerCommunityNumber){
       return sellerCommunitySqlMapper.selectSellerCommunityComment(sellerCommunityNumber);
    }


    //대댓글 삽입
    public void insertSellerCommunityReply(SellerCommunityReplyDto sellerCommunityReplyDto){
        sellerCommunitySqlMapper.insertSellerCommunityReply(sellerCommunityReplyDto);
    }
    //대댓글 가져오기
    public List<Map<String,Object>> selectSellerCommunityReply(int sellerCommunityCommentNumber){
        return  sellerCommunitySqlMapper.selectSellerCommunityReply(sellerCommunityCommentNumber);
    }

    //게시글에 대한 좋아요
    public void insertSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto){
        sellerCommunitySqlMapper.insertSellerCommunityLike(sellerCommunityLikeDto);
    }
    public int selectSellerCommunityLikeCount(int sellerCommunityNumber){
      return  sellerCommunitySqlMapper.selectSellerCommunityLikeCount(sellerCommunityNumber);
    }
    public void deleteSellerCommunityLike(SellerCommunityLikeDto sellerCommunityLikeDto){
        sellerCommunitySqlMapper.deleteSellerCommunityLike(sellerCommunityLikeDto);
    }
    public int checkIfSellerCommunityLikeExists(SellerCommunityLikeDto sellerCommunityLikeDto){
        return sellerCommunitySqlMapper.checkIfSellerCommunityLikeExists(sellerCommunityLikeDto);
    }

    //게시글 내에서 페이지 이동
    public SellerCommunityDto selectPreviousSellerCommunity(int sellerCommunityNumber){
        return sellerCommunitySqlMapper.selectPreviousSellerCommunity(sellerCommunityNumber);
    }

    public SellerCommunityDto selectNextSellerCommunity(int sellerCommunityNumber){
        return sellerCommunitySqlMapper.selectNextSellerCommunity(sellerCommunityNumber);
    }

    //게시글 조회수 증가
    public void updateSellerCommunityVisitCount(int sellerCommunityNumber){
        sellerCommunitySqlMapper.updateSellerCommunityVisitCount(sellerCommunityNumber);
    }

    //댓글 좋아요 싫어요
    public SellerCommunityCommentLikeStatusDto selectSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto){
        return sellerCommunitySqlMapper.selectSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
    }

    public void insertSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto){
        sellerCommunitySqlMapper.insertSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
    }
    public void updateSellerCommentLikeStatus(SellerCommunityCommentLikeStatusDto sellerCommunityCommentLikeStatusDto){
        sellerCommunitySqlMapper.updateSellerCommentLikeStatus(sellerCommunityCommentLikeStatusDto);
    }

    public int selectSellerCommentLikeCount(int sellerCommunityCommentLikeStatusNumber){
        return sellerCommunitySqlMapper.selectSellerCommentLikeCount(sellerCommunityCommentLikeStatusNumber);
    }
    public int selectSellerCommentDisLikeCount(int sellerCommunityCommentLikeStatusNumber){
        return sellerCommunitySqlMapper.selectSellerCommentDisLikeCount(sellerCommunityCommentLikeStatusNumber);
    }

    //대댓글 좋아요 싫어요 업데이트
    public void updateSellerReplyLikeStatus(SellerCommunityReplyLikeStatusDto sellerCommunityReplyLikeStatusDto){
        sellerCommunitySqlMapper.updateSellerReplyLikeStatus(sellerCommunityReplyLikeStatusDto);
    }

    public List<Map<String,Object>> getChartRegisterCountPerMonth(){
        return sellerCommunitySqlMapper.getChartRegisterCountPerMonth();
    }

    public List<Map<String,Object>> getChartRegisterCount(){
        return sellerCommunitySqlMapper.getChartRegisterCount();
    }

    public List<Map<String,Object>> getPieRegisterCount(){
        return sellerCommunitySqlMapper.getPieRegisterCount();
    }
}
