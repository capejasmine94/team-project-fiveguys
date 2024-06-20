window.addEventListener("DOMContentLoaded",()=>{
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";

    getSellerCommunityList(currentPage,searchWord,sortedOption);
});

function searchWord(){

    const searchWord = document.querySelector("#searchWord").value;
    const sortedOption = document.querySelector("#sortedOption").value;


    const url= "/api/seller/getSellerCommunityList";

    fetch(url)
        .then(response=>response.json())
        .then(response=> {
            if (response.login === false) {
                window.location.href = "/login/sellerLogin";
            }

            getSellerCommunityList(1,searchWord,sortedOption);
        });
}

function sortedOption(){
    const sortedOption = document.querySelector("#sortedOption").value;
    const searchWord = document.querySelector("#searchWord").value;

    const url= "/api/seller/getSellerCommunityList";
    fetch(url)
        .then(response=>response.json())
        .then(response=> {
            if (response.login === false) {
                window.location.href = "/login/sellerLogin";
            }


            getSellerCommunityList(1,searchWord,sortedOption);
        });

}

function getSellerCommunityList(currentPage,searchWord,sortedOption){

    const url= `/api/seller/getSellerCommunityList?currentPage=${currentPage}&searchWord=${searchWord}&sortedOption=${sortedOption}`;

    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.login===false){
                window.location.href="/login/sellerLogin";
            }

            const sellerInfo = document.querySelector("#sellerInfo");
            sellerInfo.innerText=response.sellerDto.sellerName;

            const sellerCommunityBox = document.getElementById("sellerCommunityBox");
            const sellerCommentWrapper = document.querySelector(".sellerCommentWrapper");
            sellerCommunityBox.innerHTML = '';
            for(let e of response.sellerCommunity){

                const newSellerCommentWrapper = sellerCommentWrapper.cloneNode(true);

                const sellerCommentMain =newSellerCommentWrapper.querySelector(".sellerCommentMain");

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

            //페이징 위쪽
            const topCurrentPage = document.querySelector("#topCurrentPage");
            topCurrentPage.innerText=response.sellerCommunityPaginationDto.currentPage;

            const topTotalPage = document.querySelector("#topTotalPage");
            topTotalPage.innerText=response.sellerCommunityPaginationDto.paginationPage;

            const topPreviousPage = document.querySelector("#topPreviousPage");
            topPreviousPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.currentPage-1})`)

            if(response.sellerCommunityPaginationDto.currentPage ===1){
                topPreviousPage.classList.add("text-black-50");
                topPreviousPage.onclick=null;
            }else{
                topPreviousPage.classList.remove("text-black-50");
            }

            const topNextPage = document.querySelector("#topNextPage");
            topNextPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.currentPage+1})`)

            if(response.sellerCommunityPaginationDto.currentPage ===response.sellerCommunityPaginationDto.paginationPage){
                topNextPage.classList.add("text-black-50");
                topNextPage.onclick=null;
            }else{
                topNextPage.classList.remove("text-black-50");
            }

            //페이징 아래쪽
            const previousPage = document.querySelector("#previousPage");
            const previousPageContainer = document.querySelector("#previousPageContainer");

            previousPage.setAttribute("onclick","movePage(1)");


            if(response.sellerCommunityPaginationDto.startPage ===1){
                previousPageContainer.classList.add("text-black-50");
                previousPage.onclick=null;
            }





            const firstPageContainer = document.querySelector("#firstPageContainer");
            const firstPage = document.querySelector("#firstPage");
            firstPageContainer.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.startPage})`);
            firstPage.innerText=response.sellerCommunityPaginationDto.startPage;

            if(response.sellerCommunityPaginationDto.currentPage !==1 || response.sellerCommunityPaginationDto.startPage !==1){
                firstPageContainer.classList.remove("d-none");
            }else{
                firstPageContainer.classList.add("d-none");
            }

            const pageNumbers = document.querySelector("#numberSequenceContainer");
            const pageList = document.querySelector(".pageList");

            pageNumbers.innerHTML="";


            for(let e=1; e<=response.sellerCommunityPaginationDto.endPage; e++){

                const newPageNumbers = pageList.cloneNode(true);


                newPageNumbers.setAttribute("onclick",`movePage(${e})`)
                newPageNumbers.innerText=e;

                if(response.sellerCommunityPaginationDto.currentPage ===e){
                    newPageNumbers.classList.add("border-top", "border-3", "border-primary");
                    newPageNumbers.classList.add("text-primary");
                }else{
                    newPageNumbers.classList.remove("border-top", "border-3", "border-primary");
                    newPageNumbers.classList.remove("text-primary");
                }

                newPageNumbers.classList.remove("d-none");


                pageNumbers.appendChild(newPageNumbers);

            }

            const endPageContainer = document.querySelector("#endPageContainer");
            const endPage = document.querySelector("#endPage");
            endPage.innerText=response.sellerCommunityPaginationDto.paginationPage;



            endPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.endPage})`);

            if(response.sellerCommunityPaginationDto.currentPage !== response.sellerCommunityPaginationDto.paginationPage){
                endPageContainer.classList.remove("d-none");
            }else{
                endPageContainer.classList.add("d-none");
            }

            const nextPageContainer = document.querySelector("#nextPageContainer");
            const nextPage = document.querySelector("#nextPage");


            nextPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.paginationPage})`)

            if(response.sellerCommunityPaginationDto.endPage < response.sellerCommunityPaginationDto.paginationPage){
                nextPageContainer.classList.remove("text-black-50");
            }else{
                nextPage.onclick=null;
                nextPageContainer.classList.add("text-black-50");
            }

            const newUrl = `${window.location.pathname}?currentPage=${currentPage}&searchWord=${searchWord}&sortedOption=${sortedOption}`;
            window.history.pushState({ path: newUrl }, '', newUrl);

        });
}

function movePage(currentPage){
    const urlParams = new URLSearchParams(window.location.search);
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";

    const url="/api/seller/getSellerCommunityList?currentPage="+currentPage;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            getSellerCommunityList(currentPage,searchWord,sortedOption);
        });
}

function sellerCommunityLike(){
    const target=event.target;
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
   let findClosestSellerCommentWrapper = target.closest(".sellerCommentWrapper");
   let sellerCommunityNumber = findClosestSellerCommentWrapper.querySelector(".sellerCommunityNumber").value;
    const url="/api/seller/sellerCommunityLike?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            getSellerCommunityList(currentPage,searchWord,sortedOption);
        });

}




function sellerCommunityDetailPage(sellerCommunityNumber,sellerCommunityCommentNumberInputInput){

    const sellerCommunityDetailPage =bootstrap.Modal.getOrCreateInstance(document.getElementById("sellerCommunityDetail"));
    const url="/api/seller/sellerCommunityDetailPage?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            const sellerName = document.querySelector("#sellerName");
            sellerName.innerText=response.sellerCommunityDetail.sellerName.sellerName;

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

                newImageContainer.classList.remove("d-none");

                imageContainer.appendChild(newImageContainer);
            }


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

                const sellerCommentLikeStatus = newChatContainer.querySelector(".sellerCommentLikeStatus");
                sellerCommentLikeStatus.value=e.selectSellerCommentLikeStatus.sellerCommentLikeStatus;

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

                const replyContainer = newChatContainer.querySelector(".replyContainer");
                const chatReplyContainer = document.querySelector(".chatReplyContainer");
                replyContainer.innerHTML="";

                const showBox = newChatContainer.querySelector(".addReply");

                if(sellerCommunityCommentNumberInputInput!==undefined){
                    if(showBox.querySelector(".sellerCommunityCommentNumberInput").value===sellerCommunityCommentNumberInputInput){
                        showBox.classList.remove("d-none");
                    }else{
                        showBox.classList.add("d-none");
                    }
                }

                for (let i of e.sellerCommunityReplyContainer){
                    const replyChatContainer = chatReplyContainer.cloneNode(true);

                    const replySellerName = replyChatContainer.querySelector(".replySellerName");
                    replySellerName.innerText=i.selectSellerCommunityReply.sellerName;

                    const replyCreatedAt = replyChatContainer.querySelector(".replyCreatedAt");
                    replyCreatedAt.innerText=i.selectSellerCommunityReply.sellerCommunityReplyCreatedAt;

                    const replyContent = replyChatContainer.querySelector(".replyContent");
                    replyContent.innerText=i.selectSellerCommunityReply.sellerCommunityReplyContent;

                    const sellerCommunityReplyNumber = replyChatContainer.querySelector(".sellerCommunityReplyNumber");
                    sellerCommunityReplyNumber.value=i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyNumber;

                    const sellerCommunityReplyLikeStatus = replyChatContainer.querySelector(".sellerCommunityReplyLikeStatus");
                    sellerCommunityReplyLikeStatus.value=i.sellerCommunityReplyLikeStatusDto.sellerCommunityReplyLikeStatus;


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

                newChatContainer.classList.remove("d-none");
                commentContainer.appendChild(newChatContainer);

            }
            const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber");

            sellerCommunityNumber.value = response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityNumber;

            const sellerNumber = document.querySelector("#sellerNumber");
            sellerNumber.value = response.sellerDto.sellerNumber;

            sellerCommunityDetailPage.show();
        });
}


function writeSellerCommunity(){
    const writeSellerCommunity  = bootstrap.Modal.getOrCreateInstance("#writeSellerCommunity");
    writeSellerCommunity.show();

}



function submitSellerCommunity(event){
    event.preventDefault()

    const sellerCommunityForm = document.querySelector("#sellerCommunityForm");

    const sellerCommunityTitleInput = document.querySelector("#sellerCommunityTitleInput").value;
    //파일이 한개인 경우 file[0]으로 첫번째 배열만 받는다
    const oneSellerCommunityImage = document.querySelector("#oneSellerCommunityImage").files[0];
    //여러개의 파일을 제출할 경우 files로 받는다
    const multipleSellerCommunityImageList = document.querySelector("#multipleSellerCommunityImageList").files;
    const sellerCommunityContentInput = document.querySelector("#sellerCommunityContentInput").value;

    const formData = new FormData();
    formData.append('sellerCommunityTitle', sellerCommunityTitleInput);
    formData.append('sellerCommunityContent', sellerCommunityContentInput);
    formData.append('oneSellerCommunityImage', oneSellerCommunityImage);

    // 여러개의 파일은 반복문을 돌려서 FormData에 추가
    for (let i = 0; i < multipleSellerCommunityImageList.length; i++) {
        formData.append('multipleSellerCommunityImageList', multipleSellerCommunityImageList[i]);
    }



    const url="/api/seller/sellerCommunityWriteProcess";

    fetch(url,{
        method:"post",
        body: formData //form데이터를 body에 추가시켜서 넘긴다 스프링에서 파라미터로 받을 곳
    })
        .then(response=>response.json())
        .then(response=>{
            const inputSuccessMessage = document.querySelector("#inputSuccessMessage");
            if(response.inputSuccess===true){
                inputSuccessMessage.innerText="게시글이 성공적으로 등록되었습니다";
                inputSuccessMessage.classList.add("text-success");
                sellerCommunityForm.reset();
            }else{
                inputSuccessMessage.innerText="게시글 등록 실패입니다";
                inputSuccessMessage.classList.add("text-danger");
                sellerCommunityForm.reset();
            }
            getSellerCommunityList();
        });
}

function sellerCommunityLikeProcess(){


    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;
    const sellerNumber = document.querySelector("#sellerNumber").value;

    const likeData ={
        sellerNumber:sellerNumber,
        sellerCommunityNumber:sellerCommunityNumber
    };

    const url="/api/seller/sellerCommunityLikeProcess";
    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(likeData)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            sellerCommunityDetailPage(sellerCommunityNumber);
        });

}
//댓글달기
function sellerCommunityCommentInsertProcess(event){
    event.preventDefault();

    const sellerCommunityCommentForm = document.querySelector("#sellerCommunityCommentForm");

    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;
    const sellerNumber = document.querySelector("#sellerNumber").value;
    const sellerCommunityCommentContent = document.querySelector("#sellerCommunityCommentContent").value;

    const data ={
        sellerCommunityNumber:sellerCommunityNumber,
        sellerNumber:sellerNumber,
        sellerCommunityCommentContent:sellerCommunityCommentContent
    };

    const url="/api/seller/sellerCommunityCommentInsertProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true){

                sellerCommunityCommentForm.reset();
            }else{
                window.location.href="/login/sellerLogin";
                sellerCommunityCommentForm.reset();
            }
            sellerCommunityDetailPage(sellerCommunityNumber);

        });

}

//대댓글달기
function sellerCommunityReplyInsertProcess(event){
    event.preventDefault();
    const sellerCommunityReplyInsertForm = event.target;



    const sellerCommunityNumberInput = sellerCommunityReplyInsertForm.querySelector(".sellerCommunityNumberInput").value;
    const sellerCommunityCommentNumberInput = sellerCommunityReplyInsertForm.querySelector(".sellerCommunityCommentNumberInput").value;
    const sellerNumberInput = sellerCommunityReplyInsertForm.querySelector(".sellerNumberInput").value;
    const sellerCommunityReplyContent = sellerCommunityReplyInsertForm.querySelector(".sellerCommunityReplyContent").value;

    const replyData ={
        sellerCommunityCommentNumber:sellerCommunityCommentNumberInput,
        sellerNumber:sellerNumberInput,
        sellerCommunityReplyContent:sellerCommunityReplyContent
    };

    const url="/api/seller/sellerCommunityReplyInsertProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(replyData)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true){

                sellerCommunityReplyInsertForm.reset();
            }else{
                window.location.href="/login/sellerLogin";
                sellerCommunityReplyInsertForm.reset();
            }
            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

        });



}

//댓글 좋아요
function commentLikeStatus(target){
    const clickedElementParent = target.closest(".chatContainer");
    const clickedElement = clickedElementParent.querySelector(".likeStatusContainer");


    const sellerCommunityNumberInput = clickedElementParent.querySelector(".sellerCommunityNumberInput").value;
    const sellerCommunityCommentNumberInput = clickedElementParent.querySelector(".sellerCommunityCommentNumberInput").value;
    const sellerNumberInput = clickedElementParent.querySelector(".sellerNumberInput").value;
    const sellerCommentLikeStatus = clickedElement.querySelector(".sellerCommentLikeStatus").value;

    const thumb = clickedElement.querySelector(".thumb");

    const likeStatus ={
        sellerCommunityCommentNumber:sellerCommunityCommentNumberInput,
        sellerNumber:sellerNumberInput,
        sellerCommentLikeStatus:sellerCommentLikeStatus
    };

    const url="/api/seller/sellerCommentLikeProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(likeStatus)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href="/login/sellerLogin";
            }
            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

        });

}
//댓글 싫어요
function commentDislikeStatus(target){
    const clickedElementParent = target.closest(".chatContainer");
    const clickedElement = clickedElementParent.querySelector(".likeStatusContainer");


    const sellerCommunityNumberInput = clickedElementParent.querySelector(".sellerCommunityNumberInput").value;
    const sellerCommunityCommentNumberInput = clickedElementParent.querySelector(".sellerCommunityCommentNumberInput").value;
    const sellerNumberInput = clickedElementParent.querySelector(".sellerNumberInput").value;
    const sellerCommentLikeStatus = clickedElement.querySelector(".sellerCommentLikeStatus").value;

    const thumbDown = clickedElement.querySelector(".thumbDown");

    const disLikeStatus ={
        sellerCommunityCommentNumber:sellerCommunityCommentNumberInput,
        sellerNumber:sellerNumberInput,
        sellerCommentLikeStatus:sellerCommentLikeStatus
    };

    const url="/api/seller/sellerCommentDisLikeProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(disLikeStatus)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href="/login/sellerLogin";
            }
            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

        });

}

//대댓글 좋아요
function replyLikeStatus(target){
    const clickedElement = target.closest(".chatReplyContainer");
    const addReply1 = target.closest(".addReply");

    const sellerCommunityCommentNumberInput = addReply1.querySelector(".sellerCommunityCommentNumberInput").value;
    const sellerCommunityNumberInput = addReply1.querySelector(".sellerCommunityNumberInput").value;

    const sellerCommunityReplyNumber = clickedElement.querySelector(".sellerCommunityReplyNumber").value;
    const sellerCommunityReplyLikeStatus = clickedElement.querySelector(".sellerCommunityReplyLikeStatus").value;
    const sellerNumberInput = addReply1.querySelector(".sellerNumberInput").value;

    const replyThumb = clickedElement.querySelector(".replyThumb");

    const replyLikeStatus ={
        sellerCommunityReplyNumber:sellerCommunityReplyNumber,
        sellerNumber:sellerNumberInput,
        sellerCommunityReplyLikeStatus:sellerCommunityReplyLikeStatus
    };

    const url="/api/seller/sellerReplyLikeProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(replyLikeStatus)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href="/login/sellerLogin";
            }
            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);
        });

}

//대댓글 싫어요
function replyDislikeStatus(target){
    const clickedElement = target.closest(".chatReplyContainer");
    const addReply1 = target.closest(".addReply");

    const sellerCommunityCommentNumberInput = addReply1.querySelector(".sellerCommunityCommentNumberInput").value;
    const sellerCommunityNumberInput = addReply1.querySelector(".sellerCommunityNumberInput").value;

    const sellerCommunityReplyNumber = clickedElement.querySelector(".sellerCommunityReplyNumber").value;
    const sellerCommunityReplyLikeStatus = clickedElement.querySelector(".sellerCommunityReplyLikeStatus").value;
    const sellerNumberInput = addReply1.querySelector(".sellerNumberInput").value;

    const replyThumb = clickedElement.querySelector(".replyThumb");

    const replyDislikeStatus ={
        sellerCommunityReplyNumber:sellerCommunityReplyNumber,
        sellerNumber:sellerNumberInput,
        sellerCommunityReplyLikeStatus:sellerCommunityReplyLikeStatus
    };

    const url="/api/seller/sellerReplyDisLikeProcess";

    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(replyDislikeStatus)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href="/login/sellerLogin";
            }

            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

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
