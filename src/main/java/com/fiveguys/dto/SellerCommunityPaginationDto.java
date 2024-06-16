package com.fiveguys.dto;

import lombok.Data;

@Data
public class SellerCommunityPaginationDto {

    private int currentPage;//현재 페이지
    private int startPage; //시작 페이지
    private int endPage; //마지막 페이지
    private int paginationPage; //페이징 처리후 총 페이지
    private String selectOption; //선택한 페이지
    private String searchWord; //검색한 검색어
    private int itemsPerPage;

    public SellerCommunityPaginationDto() {
        if(this.itemsPerPage == 0 && this.currentPage == 0) {
            this.itemsPerPage = 5;
            this.currentPage = 1;
        }
    }

}
