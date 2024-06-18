window.addEventListener("DOMContentLoaded",()=>{
    getSellerCommunityList();
});

function getSellerCommunityList(){
    console.log("셀러 커뮤니티 리스트 실행됨");
    const url="/api/seller/getSellerCommunityList";
    fetch(url)
        .then(response=>response.json())
        .then(response=>{

            const sellerCommunityBox = document.getElementById("sellerCommunityBox");
            const sellerCommentWrapper = document.querySelector(".sellerCommentWrapper");
            sellerCommunityBox.innerHTML = '';
            for(let e of response.sellerCommunity){

                const newSellerCommentWrapper = sellerCommentWrapper.cloneNode(true);

                const sellerCommentMain =newSellerCommentWrapper.querySelector(".sellerCommentMain");
                // const hrefValue = sellerCommentMain.getAttribute("href");
                // const newHrefValue = hrefValue+"?sellerCommunityNumber="+e.sellerCommunityDto.sellerCommunityNumber;
                // sellerCommentMain.setAttribute("href",newHrefValue);
                sellerCommentMain.setAttribute("onclick",`sellerCommunityDetailPage(${e.sellerCommunityDto.sellerCommunityNumber})`);

                const sellerCommentTitle = newSellerCommentWrapper.querySelector(".sellerCommentTitle");
                sellerCommentTitle.innerText=e.sellerCommunityDto.sellerCommunityTitle;


                const sellerCommentContent = newSellerCommentWrapper.querySelector(".sellerCommentContent");
                sellerCommentContent.innerText=e.sellerCommunityDto.sellerCommunityContent;

                const sellerCommentInfo = newSellerCommentWrapper.querySelector(".sellerCommentInfo");
                if(e.textLength<=100){
                    sellerCommentInfo.classList.remove("mt-4");
                    sellerCommentInfo.classList.add("mt-5");
                }else{
                    sellerCommentInfo.classList.add("mt-4");
                    sellerCommentInfo.classList.remove("mt-5");
                }

                const sellerCommentSellerName = newSellerCommentWrapper.querySelector(".sellerCommentSellerName");
                sellerCommentSellerName.innerText=e.sellerName.sellerName;

                const sellerCommentSellerCreatedAt = newSellerCommentWrapper.querySelector(".sellerCommentSellerCreatedAt");
                sellerCommentSellerCreatedAt.innerText=e.sellerCommunityDto.sellerCommunityCreatedAt;

                const sellerCommentSellerVisitCount = newSellerCommentWrapper.querySelector(".sellerCommentSellerVisitCount");
                sellerCommentSellerVisitCount.innerText=e.sellerCommunityDto.sellerCommunityVisitCount;

                const sellerCommentSellerNameLikeCount = newSellerCommentWrapper.querySelector(".sellerCommentSellerNameLikeCount");
                sellerCommentSellerNameLikeCount.innerText=e.selectSellerCommunityLikeCount;

                const sellerCommentSellerNameReplyCount = newSellerCommentWrapper.querySelector(".sellerCommentSellerNameReplyCount");
                sellerCommentSellerNameReplyCount.innerText=e.selectTotalCommentReplyCount;

                const sellerCommunityImageContainer =newSellerCommentWrapper.querySelector(".sellerCommunityImageContainer");
                const sellerCommunityHrefValue = sellerCommunityImageContainer.getAttribute("href");
                const newSellerCommunityHrefValue = sellerCommunityHrefValue+"?sellerCommunityNumber="+e.sellerCommunityDto.sellerCommunityNumber;
                sellerCommunityImageContainer.setAttribute("href",newSellerCommunityHrefValue);

                const sellerCommunityImage = newSellerCommentWrapper.querySelector(".sellerCommunityImage");
                const setSrc = "/images/"+e.sellerCommunityDto.sellerCommunityImage;
                sellerCommunityImage.setAttribute("src",setSrc);

                //게시판 아이디를 식별할수 잇게 하는 인풋태그
                const sellerCommunityNumber = newSellerCommentWrapper.querySelector(".sellerCommunityNumber");
                sellerCommunityNumber.value=e.sellerCommunityDto.sellerCommunityNumber;

                //세션별 좋아요 싫어요
                const checkIfSellerCommunityLikeExists = newSellerCommentWrapper.querySelector(".likeDislikeStatus");
                if(e.checkIfSellerCommunityLikeExists!==0){
                    checkIfSellerCommunityLikeExists.classList.add("bi-heart-fill");
                    checkIfSellerCommunityLikeExists.classList.remove("bi-heart");
                }else{
                    checkIfSellerCommunityLikeExists.classList.remove("bi-heart-fill");
                    checkIfSellerCommunityLikeExists.classList.add("bi-heart");
                }

                newSellerCommentWrapper.classList.remove("d-none");
                sellerCommunityBox.appendChild(newSellerCommentWrapper);
            }
            //페이징
            const previousPage = document.querySelector("#previousPage");
            const getUrl = previousPage.getAttribute("href");
            const newUrl = getUrl +"?currentPage="+ (response.sellerCommunityPaginationDto.startPage - 1);
            previousPage.setAttribute("href",newUrl);

            if(response.sellerCommunityPaginationDto.startPage !==1){
                previousPage.classList.add("bi-arrow-left");
            }else{
                previousPage.classList.remove("bi-arrow-left");
            }

            const firstPage = document.querySelector("#firstPage");
            const getUrlFirstPage = firstPage.getAttribute("href");
            const newUrlFirstPage = getUrlFirstPage + "?currentPage=1";
            firstPage.setAttribute("href",newUrlFirstPage);

            if(response.sellerCommunityPaginationDto.currentPage !==1 || response.sellerCommunityPaginationDto.startPage !==1){
                firstPage.classList.remove("d-none");
            }else{
                firstPage.classList.add("d-none");
            }

            const pageNumbers = document.querySelector("#numberContainer");
            const pageList = document.querySelector(".pageList");

            pageList.innerHTML="";

            for(let e=1; e<=response.sellerCommunityPaginationDto.endPage; e++){

                const newPageNumbers = pageList.cloneNode(true);

                const pageHref= newPageNumbers.getAttribute("href");
                const newPageHref = pageHref + "?currentPage="+e;
                newPageNumbers.setAttribute("href",newPageHref);

                const numberSequence = newPageNumbers.querySelector(".numberSequence");
                numberSequence.innerText=e;

                if(response.sellerCommunityPaginationDto.currentPage ===e){
                    newPageNumbers.classList.add("border-top", "border-3", "border-primary");
                    newPageNumbers.classList.add("text-primary");
                }else{
                    newPageNumbers.classList.remove("border-top", "border-3", "border-primary");
                    newPageNumbers.classList.remove("text-primary");
                }
                pageNumbers.appendChild(newPageNumbers);

            }

        });
}
function sellerCommunityLike(){
    const target=event.target;
   let findClosestSellerCommentWrapper = target.closest(".sellerCommentWrapper");
   let sellerCommunityNumber = findClosestSellerCommentWrapper.querySelector(".sellerCommunityNumber").value;
    const url="/api/seller/sellerCommunityLike?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            getSellerCommunityList();
        });

}

function sellerCommunityDetailPage(sellerCommunityNumber){

    const sellerCommunityDetailPage =bootstrap.Modal.getOrCreateInstance(document.getElementById("sellerCommunityDetail"));
    const url="/api/seller/sellerCommunityDetailPage?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            const sellerName = document.querySelector("#sellerName");
            sellerName.innerText=response.sellerCommunityDetail.sellerName.sellerName;
            console.log(response.sellerCommunityDetail);

            const sellerCommunityCreatedAt = document.querySelector("#sellerCommunityCreatedAt");
            sellerCommunityCreatedAt.innerText=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityCreatedAt;
            const sellerCommunityVisitCount = document.querySelector("#sellerCommunityVisitCount");
            sellerCommunityVisitCount.innerText=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityVisitCount;

            const sellerCommunityTitle = document.querySelector("#sellerCommunityTitle");
            sellerCommunityTitle.innerText=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityTitle;
            const sellerCommunityContent = document.querySelector("#sellerCommunityContent");
            sellerCommunityContent.innerText=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityContent;

            const imageContainer = document.querySelector("#imageContainer");
            const imageWrapper = document.querySelector(".imageWrapper");
            imageContainer.innerHTML="";
            for(let e of response.sellerCommunityDetail.selectImageListById){


                const newImageContainer = imageWrapper.cloneNode(true);

                const images = newImageContainer.querySelector(".images");
                const setImageUrl = "/images/"+e.sellerCommunityImageList;
                images.setAttribute("src",setImageUrl);

                imageContainer.appendChild(newImageContainer);
            }

            const sellerCommunityDetailLike = document.querySelector("#sellerCommunityDetailLike");
            const getSellerCommunityDetailLikeUrl = sellerCommunityDetailLike.getAttribute("href");
            const newUDetailLikeUrl = getSellerCommunityDetailLikeUrl+"?sellerCommunityNumber="+response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityNumber
                                        +"&sellerCommunityNumber="+response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityNumber;
            sellerCommunityDetailLike.setAttribute("href",newUDetailLikeUrl);

            const heart = document.querySelector(".heart");


            if(response.checkIfSellerCommunityLikeExists!==0) {
                heart.classList.add("bi-heart-fill");
                heart.classList.remove("bi-heart");
            }else{
                heart.classList.add("bi-heart");
                heart.classList.remove("bi-heart-fill");
            }

            const detailLikeCount = document.querySelector("#detailLikeCount");
            detailLikeCount.innerText=response.sellerCommunityDetail.selectSellerCommunityLikeCount;

            const detailDislikeCount = document.querySelector("#detailDislikeCount");
            detailDislikeCount.innerText=response.sellerCommunityDetail.selectTotalCommentReplyCount;

            const commentContainer = document.querySelector("#commentContainer");
            const chatContainer = document.querySelector(".chatContainer");
            commentContainer.innerHTML="";
            for(let e of response.sellerCommunityDetail.selectSellerCommunityComment){
                const newChatContainer = chatContainer.cloneNode(true);

                const commentSellerName = newChatContainer.querySelector(".commentSellerName");
                commentSellerName.innerText=e.sellerCommunityCommentDto.sellerName;

                const commentCreatedAt = newChatContainer.querySelector(".commentCreatedAt");
                commentCreatedAt.innerText=e.sellerCommunityCommentDto.sellerCommunityCommentCreatedAt;

                const commentContent = newChatContainer.querySelector(".commentContent");
                commentContent.innerText=e.sellerCommunityCommentDto.sellerCommunityCommentContent;

                const commentReplyCount = newChatContainer.querySelector(".commentReplyCount");
                commentReplyCount.innerText=e.selectEachSellerCommentReplyCount;

                const commentLikeStatus = newChatContainer.querySelector(".commentLikeStatus");
                const getCommentLikeStatusHref = commentLikeStatus.getAttribute("href");
                const newCommentLikeStatusHref = getCommentLikeStatusHref+"?sellerCommunityNumber="+e.sellerCommunityCommentDto.sellerCommunityNumber
                                                        +"&sellerCommunityCommentNumber="+e.selectSellerCommentLikeStatus.sellerCommunityCommentNumber
                                                        +"&sellerCommentLikeStatus="+e.selectSellerCommentLikeStatus.sellerCommentLikeStatus;
                commentLikeStatus.setAttribute("href",newCommentLikeStatusHref);

                const thumb = newChatContainer.querySelector(".thumb");

                if(e.selectSellerCommentLikeStatus.sellerCommentLikeStatus==='like'){
                    thumb.classList.add("bi-hand-thumbs-up-fill");
                    thumb.classList.remove("bi-hand-thumbs-up");
                }else{
                    thumb.classList.remove("bi-hand-thumbs-up-fill");
                    thumb.classList.add("bi-hand-thumbs-up");
                }

                const commentLikeCount = newChatContainer.querySelector(".commentLikeCount");
                commentLikeCount.innerText=e.selectSellerCommentLikeCount;
                //아아앙아
                const commentDislikeStatus = newChatContainer.querySelector(".commentDislikeStatus");
                const getCommentDislikeStatusHref = commentDislikeStatus.getAttribute("href");
                const newCommentDislikeStatusHref = getCommentDislikeStatusHref+"?sellerCommunityNumber="+e.sellerCommunityCommentDto.sellerCommunityNumber
                    +"&sellerCommunityCommentNumber="+e.selectSellerCommentLikeStatus.sellerCommunityCommentNumber
                    +"&sellerCommentLikeStatus="+e.selectSellerCommentLikeStatus.sellerCommentLikeStatus;

                commentDislikeStatus.setAttribute("href",newCommentDislikeStatusHref);

                const thumbDown = newChatContainer.querySelector(".thumbDown");

                if(e.selectSellerCommentLikeStatus.sellerCommentLikeStatus==='dislike'){
                    thumbDown.classList.add("bi-hand-thumbs-down-fill");
                    thumbDown.classList.remove("bi-hand-thumbs-down");
                }else{
                    thumbDown.classList.remove("bi-hand-thumbs-down-fill");
                    thumbDown.classList.add("bi-hand-thumbs-down");
                }

                const commentDisLikeCount = newChatContainer.querySelector(".commentDisLikeCount");
                commentDisLikeCount.innerText=e.selectSellerCommentDisLikeCount;

                const sellerCommunityNumberInput = newChatContainer.querySelector(".sellerCommunityNumberInput");
                sellerCommunityNumberInput.value=e.sellerCommunityCommentDto.sellerCommunityNumber;

                const sellerCommunityCommentNumberInput = newChatContainer.querySelector(".sellerCommunityCommentNumberInput");
                sellerCommunityCommentNumberInput.value=e.sellerCommunityCommentDto.sellerCommunityCommentNumber;

                const sellerNumberInput = newChatContainer.querySelector(".sellerNumberInput");
                sellerNumberInput.value=response.sellerDto.sellerNumber;

                const replyContainer = document.querySelector("#replyContainer");
                const chatReplyContainer = document.querySelector(".chatReplyContainer");
                replyContainer.innerHTML="";

                for (let i of e.sellerCommunityReplyContainer){
                    const replyChatContainer = chatReplyContainer.cloneNode(true);

                    const replySellerName = replyChatContainer.querySelector(".replySellerName");
                    replySellerName.innerText=i.selectSellerCommunityReply.sellerName;

                    const replyCreatedAt = replyChatContainer.querySelector(".replyCreatedAt");
                    replyCreatedAt.innerText=i.selectSellerCommunityReply.sellerCommunityReplyCreatedAt;

                    const replyContent = replyChatContainer.querySelector(".replyContent");
                    replyContent.innerText=i.selectSellerCommunityReply.sellerCommunityReplyContent;

                    const replyLikeStatus = replyChatContainer.querySelector(".replyLikeStatus");
                    const getReplyLikeStatusHref = replyLikeStatus.getAttribute("href");
                    const newReplyLikeStatusHref = getReplyLikeStatusHref+"?sellerCommunityNumber="+e.sellerCommunityCommentDto.sellerCommunityNumber
                        +"&sellerCommunityReplyNumber="+i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyNumber
                        +"&sellerCommunityReplyLikeStatus="+i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyLikeStatus;
                    replyLikeStatus.setAttribute("href",newReplyLikeStatusHref);

                    const replyThumb = replyChatContainer.querySelector(".replyThumb");

                    if(i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyLikeStatus==='like'){
                        replyThumb.classList.add("bi-hand-thumbs-up-fill");
                       replyThumb.classList.remove("bi-hand-thumbs-up");
                    }else{
                        replyThumb.classList.remove("bi-hand-thumbs-up-fill");
                       replyThumb.classList.add("bi-hand-thumbs-up");
                    }

                    const replyThumbCount = replyChatContainer.querySelector(".replyThumbCount");
                    replyThumbCount.innerText=i.selectSellerReplyLikeCount;


                    const replyDislikeStatus = replyChatContainer.querySelector(".replyDislikeStatus");
                    const getReplyDislikeStatusHref = replyDislikeStatus.getAttribute("href");
                    const newReplyDisLikeStatusHref = getReplyDislikeStatusHref+"?sellerCommunityNumber="+e.sellerCommunityCommentDto.sellerCommunityNumber
                        +"&sellerCommunityReplyNumber="+i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyNumber
                        +"&sellerCommunityReplyLikeStatus="+i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyLikeStatus;
                    replyDislikeStatus.setAttribute("href",newReplyDisLikeStatusHref);

                    const replyThumbDown = replyChatContainer.querySelector(".replyThumbDown");

                    if(i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyLikeStatus==='dislike'){
                        replyThumbDown.classList.add("bi-hand-thumbs-down-fill");
                        replyThumbDown.classList.remove("bi-hand-thumbs-down");
                    }else{
                        replyThumbDown.classList.remove("bi-hand-thumbs-down-fill");
                        replyThumbDown.classList.add("bi-hand-thumbs-down");
                    }

                    const replyThumbDownCount = replyChatContainer.querySelector(".replyThumbDownCount");
                    replyThumbDownCount.innerText=i.selectSellerReplyDisLikeCount;

                    replyChatContainer.classList.remove("d-none");

                    replyContainer.appendChild(replyChatContainer);
                }

                const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber");
                console.log(sellerCommunityNumber);
                sellerCommunityNumber.value = response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityNumber;

                const sellerNumber = document.querySelector("#sellerNumber");
                sellerNumber.value = response.sellerDto.sellerNumber;

                newChatContainer.classList.remove("d-none");

                commentContainer.appendChild(newChatContainer);

            }

            sellerCommunityDetailPage.show();
        });
}

function writeSellerCommunity(){
    const writeSellerCommunity  = bootstrap.Modal.getOrCreateInstance("#writeSellerCommunity");
    writeSellerCommunity.show();

}

function submitSellerCommunity(){
    const sellerCommunityForm = document.querySelector("#sellerCommunityForm");

    const sellerCommunityTitleInput = document.querySelector("#sellerCommunityTitleInput").value;
    const oneSellerCommunityImage = document.querySelector("#oneSellerCommunityImage").files[0];
    const multipleSellerCommunityImageList = document.querySelector("#multipleSellerCommunityImageList").files;
    const sellerCommunityContentInput = document.querySelector("#sellerCommunityContentInput").value;

    const formData = new FormData();
    formData.append('sellerCommunityTitle', sellerCommunityTitleInput);
    formData.append('sellerCommunityContent', sellerCommunityContentInput);
    formData.append('oneSellerCommunityImage', oneSellerCommunityImage);

    // 여러 파일을 FormData에 추가
    for (let i = 0; i < multipleSellerCommunityImageList.length; i++) {
        formData.append('multipleSellerCommunityImageList', multipleSellerCommunityImageList[i]);
    }
    console.log(formData);


    const url="/api/seller/sellerCommunityWriteProcess";

    fetch(url,{
        method:"post",
        body: formData,
    })
        .then(response=>response.json())
        .then(response=>{
            console.log(response);
            const inputSuccessMessage = document.querySelector("#inputSuccessMessage");
            if(response.inputSuccess===true){
                inputSuccessMessage.innerText="게시글이 성공적으로 등록되었습니다";
                inputSuccessMessage.classList.add("text-success");
            }else{
                inputSuccessMessage.innerText="게시글 등록 실패입니다";
                inputSuccessMessage.classList.add("text-danger");
            }
        });
}

function addReply(target) {
    const clickedElement = target.closest(".chatContainer");
    const showEditBox = clickedElement.querySelector(".addReply");
    const hideEditBox = document.querySelectorAll(".addReply");
    for (const e of hideEditBox) {
        if(e !==showEditBox) {
            e.classList.add("d-none");
        }
    }
    if (showEditBox.classList.contains("d-none")) {
        showEditBox.classList.remove("d-none");
    }else {
        showEditBox.classList.add("d-none");
    }
}
