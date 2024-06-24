package com.fiveguys.dto;

import lombok.Data;

@Data
public class CommunityCommentReplyDto {

    private int communityCommunityReplyNumber;
    private int customerNumber;
    private int commentNumber;
    private String communityCommentReplyText;
    private String communityCommentReplyCreatedAt;
}
