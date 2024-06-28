window.addEventListener("DOMContentLoaded",()=>{
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];


    const sellerSorts = document.querySelectorAll(".sellerSort");

    for(let e of sellerSorts){
        e.addEventListener('change',sortSeller);
    }


    getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);


    lineChart();
    dayChart();
    pieChart();
});


function sortSeller(){
    const urlParams = new URLSearchParams(window.location.search);
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";

    const sellerSorts = document.querySelectorAll(".sellerSort");
    const selectedValues = Array.from(sellerSorts).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);


    const url= "/api/seller/getSellerCommunityList?sellerSort="+selectedValues;

    fetch(url)
        .then(response=>response.json())
        .then(response=> {
            if (response.login === false) {
                window.location.href = "/login/sellerLogin";
            }

            getSellerCommunityList(1,searchWord,sortedOption,selectedValues);
        });
}

function searchWord(){
    const urlParams = new URLSearchParams(window.location.search);
    const searchWord = document.querySelector("#searchWord").value;
    const sortedOption = document.querySelector("#sortedOption").value;
    const sellerSort = urlParams.get("sellerSort")||[];


    const url= "/api/seller/getSellerCommunityList";

    fetch(url)
        .then(response=>response.json())
        .then(response=> {
            if (response.login === false) {
                window.location.href = "/login/sellerLogin";
            }

            getSellerCommunityList(1,searchWord,sortedOption,sellerSort);
        });
}

function dropDown(){
    const sortBySeller = document.querySelector("#sortBySeller");
    const triangle = document.querySelector("#triangle");
    if(sortBySeller.classList.contains("d-none")){
        triangle.classList.add("bi-caret-up-fill");
        triangle.classList.remove("bi-caret-down-fill");
        sortBySeller.classList.remove("d-none");
    }else{
        triangle.classList.add("bi-caret-down-fill");
        triangle.classList.remove("bi-caret-up-fill");
        sortBySeller.classList.add("d-none");
    }
}

function sortedOption(){
    const urlParams = new URLSearchParams(window.location.search);
    const sortedOption = document.querySelector("#sortedOption").value;
    const searchWord = document.querySelector("#searchWord").value;
    const sellerSort = urlParams.get("sellerSort")||[];

    const url= "/api/seller/getSellerCommunityList";
    fetch(url)
        .then(response=>response.json())
        .then(response=> {
            if (response.login === false) {
                window.location.href = "/login/sellerLogin";
            }


            getSellerCommunityList(1,searchWord,sortedOption,sellerSort);
        });

}

function getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort){

    const url= `/api/seller/getSellerCommunityList?currentPage=${currentPage}&searchWord=${searchWord}&sortedOption=${sortedOption}&sellerSort=${sellerSort}`;

    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.login===false){
                window.location.href="/login/sellerLogin";
            }

            const sellerInfo = document.querySelector("#sellerInfo");
            sellerInfo.innerText=response.sellerDto.sellerName;

            const sellerCommunityBox = document.getElementById("sellerCommunityBox");
            sellerCommunityBox.innerHTML = '';
            const sellerCommentWrapper = document.querySelector(".sellerCommentWrapper");

            for(let e of response.sellerCommunity){



                const newSellerCommentWrapper = sellerCommentWrapper.cloneNode(true);


                const sellerCommentMain =newSellerCommentWrapper.querySelector(".sellerCommentMain");

                const badge = newSellerCommentWrapper.querySelector(".badgeShow");
                badge.classList.remove("badge","rounded-pill","text-bg-danger","align-top");
                badge.innerText="";

                sellerCommentMain.setAttribute("onclick",`sellerCommunityDetailPage(${e.sellerCommunityDto.sellerCommunityNumber})`);

                const sellerCommentTitle = newSellerCommentWrapper.querySelector(".sellerCommentTitle");
                sellerCommentTitle.innerText=e.sellerCommunityDto.sellerCommunityTitle;


                const sellerCommentContent = newSellerCommentWrapper.querySelector(".sellerCommentContent");
                let mainContent = e.sellerCommunityDto.sellerCommunityContent;
                mainContent.replace("\n","");
                sellerCommentContent.innerHTML=mainContent;

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
                sellerCommunityImageContainer.setAttribute("onclick",`sellerCommunityDetailPage(${e.sellerCommunityDto.sellerCommunityNumber})`);

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

            const sellerCommentWrapperColor = document.querySelectorAll(".sellerCommentWrapper");

            const badgeShow = document.querySelectorAll(".badgeShow");
            console.log(response.sellerCommunityPaginationDto.searchWord);
            console.log(response.sellerCommunityPaginationDto.sellerSort.length);



            for(let i=0; i<2; i++) {
                if(response.sellerCommunityPaginationDto.currentPage===1 && response.sellerCommunityPaginationDto.sellerSort.length===0 && response.sellerCommunityPaginationDto.searchWord===""){
                    sellerCommentWrapperColor[i].classList.add("bg-body-secondary");
                    badgeShow[i].innerText="인기";
                    badgeShow[i].classList.add("badge","rounded-pill","text-bg-danger","align-top");
                }else{
                    sellerCommentWrapperColor[i].classList.remove("bg-body-secondary");
                    badgeShow[i].classList.remove("badge","rounded-pill","text-bg-danger","align-top");
                    badgeShow[i].innerText="";
                }

            }



            //페이징 위쪽
            const topCurrentPage = document.querySelector("#topCurrentPage");
            topCurrentPage.innerText=response.sellerCommunityPaginationDto.currentPage;

            const topTotalPage = document.querySelector("#topTotalPage");
            if(response.totalPage===0){
                topTotalPage.innerText=1;
            }else{
                topTotalPage.innerText=response.sellerCommunityPaginationDto.paginationPage;
            }

            const topPreviousPage = document.querySelector("#topPreviousPage");
            topPreviousPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.currentPage-1})`)

            if(response.sellerCommunityPaginationDto.currentPage ===1 || response.totalPage===0){
                topPreviousPage.classList.add("text-black-50");
                topPreviousPage.onclick=null;
            }else{
                topPreviousPage.classList.remove("text-black-50");
            }

            const topNextPage = document.querySelector("#topNextPage");
            topNextPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.currentPage+1})`)

            if(response.sellerCommunityPaginationDto.currentPage ===response.sellerCommunityPaginationDto.paginationPage || response.totalPage===0){
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
            }else{
                previousPageContainer.classList.remove("text-black-50");
            }

            const firstPageContainer = document.querySelector("#firstPageContainer");
            const firstPage = document.querySelector("#firstPage");
            firstPageContainer.setAttribute("onclick",`movePage(1)`);
            firstPage.innerText=1;

            if(response.sellerCommunityPaginationDto.currentPage !==1 || response.sellerCommunityPaginationDto.startPage !==1){
                firstPageContainer.classList.remove("d-none");
            }else{
                firstPageContainer.classList.add("d-none");
            }

            const pageNumbers = document.querySelector("#numberSequenceContainer");
            const pageList = document.querySelector(".pageList");

            pageNumbers.innerHTML="";

            for(let e=response.sellerCommunityPaginationDto.startPage; e<=response.sellerCommunityPaginationDto.endPage; e++){

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

            if(response.totalPage!==0){
                endPage.innerText=response.sellerCommunityPaginationDto.paginationPage;
            }else{
                endPage.innerText="";
            }


            endPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.paginationPage})`);

            if(response.sellerCommunityPaginationDto.currentPage !== response.sellerCommunityPaginationDto.paginationPage){
                endPageContainer.classList.remove("d-none");
            }else{
                endPageContainer.classList.add("d-none");
            }

            const nextPageContainer = document.querySelector("#nextPageContainer");
            const nextPage = document.querySelector("#nextPage");


            nextPage.setAttribute("onclick",`movePage(${response.sellerCommunityPaginationDto.paginationPage})`)



            if(response.sellerCommunityPaginationDto.endPage < response.sellerCommunityPaginationDto.paginationPage ){
                nextPageContainer.classList.remove("text-black-50");
            }else{
                nextPage.onclick=null;
                nextPageContainer.classList.add("text-black-50");
            }

            const noResultMessage = document.querySelector("#noResultMessage");
            const paginationContainer = document.querySelector("#paginationContainer");
            if(response.totalPage===0){
                noResultMessage.innerText="게시글이 존재하지 않습니다";
                paginationContainer.classList.add("d-none");
            }else{
                noResultMessage.innerText="";
                paginationContainer.classList.remove("d-none");
            }

            const newUrl = `${window.location.pathname}?currentPage=${currentPage}&searchWord=${searchWord}&sortedOption=${sortedOption}&sellerSort=${sellerSort}`;
            window.history.pushState({ path: newUrl }, '', newUrl);

        });
}

function movePage(currentPage){
    const urlParams = new URLSearchParams(window.location.search);
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];


    const url="/api/seller/getSellerCommunityList?currentPage="+currentPage;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
        });
}

function sellerCommunityLike(){
    const target=event.target;
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];

   let findClosestSellerCommentWrapper = target.closest(".sellerCommentWrapper");
   let sellerCommunityNumber = findClosestSellerCommentWrapper.querySelector(".sellerCommunityNumber").value;
    const url="/api/seller/sellerCommunityLike?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===false){
                window.location.href = "/login/sellerLogin";
            }
            getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
        });

}



//완료
function deleteArticle(sellerCommunityNumber){
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];


    const sellerCommunityDetailPage =bootstrap.Modal.getOrCreateInstance(document.getElementById("sellerCommunityDetail"));

    const url="/api/seller/deleteSellerCommunityDetail?sellerCommunityNumber="+sellerCommunityNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true){
                lineChart();
                dayChart();
                pieChart();
                sellerCommunityDetailPage.hide();
                getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
                setTimeout(() => {
                    alert("게시글이 삭제되었습니다.");
                }, 500);
            }

        });
}

//해결 완료
function deleteComment(sellerCommunityCommentNumber,element){
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];
    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;

    const updatedElementParent = element.closest(".chatContainer");
    const updatedElement = updatedElementParent.querySelector(".addReply");
    const sellerCommunityCommentNumberInput = updatedElement.querySelector(".sellerCommunityCommentNumberInput").value;

    console.log(sellerCommunityNumber);

    const url="/api/seller/deleteSellerCommunityComment?sellerCommunityCommentNumber="+sellerCommunityCommentNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true) {
                lineChart();
                dayChart();
                pieChart();
                getSellerCommunityList(currentPage, searchWord, sortedOption, sellerSort);
                sellerCommunityDetailPage(sellerCommunityNumber, sellerCommunityCommentNumberInput);
            }
        });
}



//해결완료
function deleteReply(sellerCommunityReplyNumber,element){
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];
    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;

    const addReply1 = element.closest(".addReply");
    const sellerCommunityCommentNumberInput = addReply1.querySelector(".sellerCommunityCommentNumberInput").value;

    const url="/api/seller/deleteSellerCommunityReply?sellerCommunityReplyNumber="+sellerCommunityReplyNumber;
    fetch(url)
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true) {
                lineChart();
                dayChart();
                pieChart();
                getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
                sellerCommunityDetailPage(sellerCommunityNumber,sellerCommunityCommentNumberInput);
            }

        });
}

function commentUpdateModal(){
    const sellerCommunityUpdateModal =bootstrap.Modal.getOrCreateInstance(document.getElementById("sellerCommunityUpdateModal"));

    const updateSellerCommunityTitle = document.querySelector("#updateSellerCommunityTitle");
    updateSellerCommunityTitle.value= document.querySelector("#sellerCommunityTitle").innerText;

    const updateSellerCommunityContent = document.querySelector("#updateSellerCommunityContent");
    updateSellerCommunityContent.value= document.querySelector("#sellerCommunityContent").innerText;

    sellerCommunityUpdateModal.show();
}

function updateSellerCommunity(event){
    event.preventDefault();

    const sellerCommunityUpdateModal =bootstrap.Modal.getOrCreateInstance(document.getElementById("sellerCommunityUpdateModal"));

    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];

    const sellerCommunityUpdateForm = document.querySelector("#sellerCommunityUpdateForm");

    const updateSellerCommunityTitle = document.querySelector("#updateSellerCommunityTitle").value;
    //파일이 한개인 경우 file[0]으로 첫번째 배열만 받는다
    const updateOneSellerCommunityImage = document.querySelector("#updateOneSellerCommunityImage").files[0];
    //여러개의 파일을 제출할 경우 files로 받는다
    const updateMultipleSellerCommunityImageList = document.querySelector("#updateMultipleSellerCommunityImageList").files;
    const updateSellerCommunityContent = document.querySelector("#updateSellerCommunityContent").value;

    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;


    const formData = new FormData();
    formData.append('sellerCommunityTitle', updateSellerCommunityTitle);
    formData.append('sellerCommunityContent', updateSellerCommunityContent);
    formData.append('oneSellerCommunityImage', updateOneSellerCommunityImage);
    formData.append('sellerCommunityNumber', sellerCommunityNumber);

    // 여러개의 파일은 반복문을 돌려서 FormData에 추가
    for (let i = 0; i < updateMultipleSellerCommunityImageList.length; i++) {
        formData.append('multipleSellerCommunityImageList', updateMultipleSellerCommunityImageList[i]);
    }


    const url="/api/seller/updateSellerCommunityDetail";

    fetch(url,{
        method:"post",
        body: formData //form 데이터를 body 에 추가시켜서 넘긴다 스프링에서 파라미터로 받을 곳
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.inputSuccess===true){

                sellerCommunityUpdateModal.hide();
                getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
                sellerCommunityDetailPage(sellerCommunityNumber,"");
                setTimeout(() => {
                    alert("게시글이 수정되었습니다.");
                }, 500);
            }
        });
}













//해결완료
function updateComment(element){

    const commentContentParent = element.closest(".chatContainer");
    const commentContent = commentContentParent.querySelector(".commentContent");

    if(commentContent.tagName==="TEXTAREA"){
        const spanElement = document.createElement('span');
        spanElement.innerText = commentContent.value;
        spanElement.classList.add("commentContent","text-break");
        commentContent.parentNode.replaceChild(spanElement, commentContent);
    }else{
        const commentContentValue = commentContent.innerText.trim();

        const inputElement = document.createElement('textarea');
        inputElement.type = 'text';
        inputElement.classList.add("commentContent","text-break","form-control");
        inputElement.style.resize="none";
        inputElement.style.overflowY="hidden";
        inputElement.style.height="auto";
        inputElement.value = commentContentValue;

        commentContent.parentNode.replaceChild(inputElement, commentContent);
        inputElement.focus();

        const save = commentContentParent.querySelector(".submitComment");
        save.classList.remove("d-none");

    }


}
//해결완료
function submitComment(element){


    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];

    const updatedElementParent = element.closest(".chatContainer");

    const commentContent = updatedElementParent.querySelector(".commentContent");

    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;

    const sellerCommunityCommentNumberInput = updatedElementParent.querySelector(".sellerCommunityCommentNumberInput").value;

    let commentContentValue=null;
    if(commentContent.tagName==="TEXTAREA"){
        console.log(commentContentValue=commentContent.value);
        commentContentValue=commentContent.value;
    }else{
        commentContentValue=commentContent.innerText;
        console.log( commentContentValue=commentContent.innerText);
    }

    const data={
        sellerCommunityCommentContent:commentContentValue,
        sellerCommunityCommentNumber:sellerCommunityCommentNumberInput
    };

    const url="/api/seller/updateSellerCommunityComment"
    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response=>response.json())
        .then(response=>{
                getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
                sellerCommunityDetailPage(sellerCommunityNumber,sellerCommunityCommentNumberInput);
        });

}














//해결완료
function updateReply(element){
    const replyContentParent = element.closest(".chatReplyContainer");
    let replyContent = replyContentParent.querySelector(".replyContent");

    if(replyContent.tagName==="TEXTAREA"){
        const spanElement = document.createElement('span');
        spanElement.innerText = replyContent.value;
        spanElement.classList.add("replyContent","text-break");
        replyContent.parentNode.replaceChild(spanElement, replyContent);
    }else{
        const replyContentValue = replyContent.innerText.trim();

        const inputReplyElement = document.createElement('textarea');
        inputReplyElement.type = 'text';
        inputReplyElement.classList.add("replyContent","text-break","form-control");
        inputReplyElement.style.resize="none";
        inputReplyElement.style.overflowY="hidden";
        inputReplyElement.style.height="auto";
        inputReplyElement.value = replyContentValue;

        replyContent.parentNode.replaceChild(inputReplyElement, replyContent);
        inputReplyElement.focus();

        const save = replyContentParent.querySelector(".submitReply");
        save.classList.remove("d-none");
    }

}
//해결완료
function submitReply(element){
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];

    const chatReplyContainer = document.querySelector(".chatReplyContainer");
    const replyContent = chatReplyContainer.querySelector(".replyContent");

    const sellerCommunityReplyNumber = chatReplyContainer.querySelector(".sellerCommunityReplyNumber").value;

    let replyContentValue=null;
    if(replyContent.tagName==="TEXTAREA"){
        replyContentValue=replyContent.value;
    }else{
        replyContentValue=replyContent.innerText;
    }

    const sellerCommunityNumber = document.querySelector("#sellerCommunityNumber").value;
    const addReply1 = element.closest(".addReply");
    const sellerCommunityCommentNumberInput = addReply1.querySelector(".sellerCommunityCommentNumberInput").value;

    const data={
        sellerCommunityReplyContent:replyContentValue,
        sellerCommunityReplyNumber:sellerCommunityReplyNumber
    }

    const url="/api/seller/updateSellerCommunityReply"
    fetch(url,{
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response=>response.json())
        .then(response=>{
            if(response.success===true) {
                updateReply(element);
                getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
                sellerCommunityDetailPage(sellerCommunityNumber,sellerCommunityCommentNumberInput);
            }
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

            const updateDeleteArticleContainer = document.querySelector("#updateDeleteArticleContainer");
            const deleteArticle = document.querySelector("#deleteArticle");

            deleteArticle.setAttribute("onclick",`deleteArticle(${response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityNumber})`);

            if(response.sellerCommunityDetail.selectSellerCommunityById.sellerNumber===response.sellerDto.sellerNumber){
                updateDeleteArticleContainer.classList.remove("d-none");
            }else{
                updateDeleteArticleContainer.classList.add("d-none");
            }

            const sellerCommunityTitle = document.querySelector("#sellerCommunityTitle");
            sellerCommunityTitle.innerText=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityTitle;


            const sellerCommunityContent = document.querySelector("#sellerCommunityContent");
            let textFormat=response.sellerCommunityDetail.selectSellerCommunityById.sellerCommunityContent;
            textFormat.replace("\n", "");
            sellerCommunityContent.innerHTML=textFormat;

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
            const likeStatusContainer = document.querySelector(".likeStatusContainer");

            if(response.checkIfSellerCommunityLikeExists!==0) {
                likeStatusContainer.classList.remove("border-dark-subtle");
                likeStatusContainer.classList.add("border-danger-subtle");
                heart.classList.add("bi-heart-fill","text-danger");
                heart.classList.remove("bi-heart","text-dark");
            }else{
                likeStatusContainer.classList.add("border-dark-subtle");
                likeStatusContainer.classList.remove("border-danger-subtle");
                heart.classList.add("bi-heart","text-dark");
                heart.classList.remove("bi-heart-fill","text-danger");
            }

            const detailLikeCount = document.querySelector("#detailLikeCount");
            detailLikeCount.innerText=response.sellerCommunityDetail.selectSellerCommunityLikeCount;

            const detailDislikeCount = document.querySelector("#detailDislikeCount");
            detailDislikeCount.innerText=response.sellerCommunityDetail.selectTotalCommentReplyCount;

            const commentContainer = document.querySelector("#commentContainer");
            commentContainer.innerHTML="";
            const chatContainer = document.querySelector(".chatContainer");
            for(let e of response.sellerCommunityDetail.selectSellerCommunityComment){
                const newChatContainer = chatContainer.cloneNode(true);

                const commentSellerName = newChatContainer.querySelector(".commentSellerName");
                commentSellerName.innerText=e.sellerCommunityCommentDto.sellerName;

                const commentCreatedAt = newChatContainer.querySelector(".commentCreatedAt");
                commentCreatedAt.innerText=e.sellerCommunityCommentDto.sellerCommunityCommentCreatedAt;

                const updateDeleteCommentContainer = newChatContainer.querySelector(".updateDeleteCommentContainer");
                if(e.sellerCommunityCommentDto.sellerNumber===response.sellerDto.sellerNumber){
                    updateDeleteCommentContainer.classList.remove("d-none");
                }else{
                    updateDeleteCommentContainer.classList.add("d-none");
                }

                const deleteComment = newChatContainer.querySelector(".deleteComment");
                deleteComment.setAttribute("onclick",`deleteComment(${e.sellerCommunityCommentDto.sellerCommunityCommentNumber},this)`);

                const commentContent = newChatContainer.querySelector(".commentContent");
                let commentFormat = e.sellerCommunityCommentDto.sellerCommunityCommentContent;
                commentFormat.replace("\n", "");
                commentContent.innerHTML=commentFormat;

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


                replyContainer.innerHTML="";
                const chatReplyContainer = document.querySelector(".chatReplyContainer");

                const showBox = newChatContainer.querySelector(".addReply");

                if(sellerCommunityCommentNumberInputInput!==undefined ){
                    if(showBox.querySelector(".sellerCommunityCommentNumberInput").value===sellerCommunityCommentNumberInputInput){
                        showBox.classList.remove("d-none");
                    } else{
                        showBox.classList.add("d-none");
                    }
                }

                for (let i of e.sellerCommunityReplyContainer){
                    const replyChatContainer = chatReplyContainer.cloneNode(true);

                    const replySellerName = replyChatContainer.querySelector(".replySellerName");
                    replySellerName.innerText=i.selectSellerCommunityReply.sellerName;

                    const replyCreatedAt = replyChatContainer.querySelector(".replyCreatedAt");
                    replyCreatedAt.innerText=i.selectSellerCommunityReply.sellerCommunityReplyCreatedAt;

                    const updateDeleteReplyContainer = replyChatContainer.querySelector(".updateDeleteReplyContainer");
                    if(i.selectSellerCommunityReply.sellerNumber===response.sellerDto.sellerNumber){
                        updateDeleteReplyContainer.classList.remove("d-none");
                    }else{
                        updateDeleteReplyContainer.classList.add("d-none");
                    }

                    const deleteReply = replyChatContainer.querySelector(".deleteReply");
                    deleteReply.setAttribute("onclick",`deleteReply(${i.selectSellerCommunityReply.sellerCommunityReplyNumber},this)`);

                    const replyContent = replyChatContainer.querySelector(".replyContent");
                    let replyFormat = i.selectSellerCommunityReply.sellerCommunityReplyContent;
                    replyFormat.replace("\n", "");
                    replyContent.innerHTML=replyFormat;

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

            const previousArticle = document.querySelector("#previousArticle");
            const nextArticle = document.querySelector("#nextArticle");

            if(response.selectPreviousSellerCommunity!==null){
                previousArticle.classList.remove("text-black-50");
                previousArticle.setAttribute("onclick",`sellerCommunityDetailPage(${response.selectPreviousSellerCommunity.sellerCommunityNumber},"")`);
            }else{
                previousArticle.classList.add("text-black-50")
                previousArticle.onclick=null;
            }
            if(response.selectNextSellerCommunity !==null){
                nextArticle.classList.remove("text-black-50");
                nextArticle.setAttribute("onclick",`sellerCommunityDetailPage(${response.selectNextSellerCommunity.sellerCommunityNumber},"")`);
            }else{
                nextArticle.classList.add("text-black-50")
                nextArticle.onclick=null;
            }


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

    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = urlParams.get('currentPage') || 1;
    const searchWord = urlParams.get("searchWord")|| "";
    const sortedOption = urlParams.get("sortedOption")||"recent";
    const sellerSort = urlParams.get("sellerSort")||[];

    fetch(url,{
        method:"post",
        body: formData //form데이터를 body에 추가시켜서 넘긴다 스프링에서 파라미터로 받을 곳
    })
        .then(response=>response.json())
        .then(response=>{
            sellerCommunityForm.reset();
            getSellerCommunityList(currentPage,searchWord,sortedOption,sellerSort);
            lineChart();
            dayChart();
            pieChart();
            setTimeout(() => {
                alert("게시글이 등록되었습니다.");
            }, 500);
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
            sellerCommunityDetailPage(sellerCommunityNumber,"");
        });

}

//댓글달기
function sellerCommunityCommentInsertProcess(event){
    event.preventDefault();
    const sellerCommunityCommentNumberInput = document.querySelector(".sellerCommunityCommentNumberInput").value;


    const sellerCommunityCommentForm = document.querySelector("#sellerCommunityCommentForm");
    console.log(sellerCommunityCommentNumberInput);

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
            lineChart();
            dayChart();
            pieChart();
            sellerCommunityDetailPage(sellerCommunityNumber,"");

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
            lineChart();
            dayChart();
            pieChart();
            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

        });


}

//댓글 좋아요
function commentLikeStatus(target){
    const clickedElementParent = target.closest(".chatContainer");
    const clickedElement = clickedElementParent.querySelector(".likeStatusContainer");


    const sellerCommunityNumberInput = clickedElementParent.querySelector(".sellerCommunityNumberInput").value;

    let sellerCommunityCommentNumberInput = clickedElementParent.querySelector(".sellerCommunityCommentNumberInput").value;
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

            const isOpen = clickedElementParent.querySelector(".addReply");
            if(isOpen.classList.contains("d-none")){
                sellerCommunityCommentNumberInput="";
            }

            sellerCommunityDetailPage(sellerCommunityNumberInput,sellerCommunityCommentNumberInput);

        });

}
//댓글 싫어요
function commentDislikeStatus(target){
    const clickedElementParent = target.closest(".chatContainer");
    const clickedElement = clickedElementParent.querySelector(".likeStatusContainer");


    const sellerCommunityNumberInput = clickedElementParent.querySelector(".sellerCommunityNumberInput").value;
    let sellerCommunityCommentNumberInput = clickedElementParent.querySelector(".sellerCommunityCommentNumberInput").value;
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
            const isOpen = clickedElementParent.querySelector(".addReply");
            if(isOpen.classList.contains("d-none")){
                sellerCommunityCommentNumberInput="";
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

let myLineChart = null;

function lineChart(){
    if (myLineChart !== null) {
        myLineChart.destroy();
        myLineChart = null; // 변수를 null로 설정하여 참조를 제거
    }

    const myLineCharts= document.getElementById("myLineChart");

    const url="/api/seller/getChartRegisterCountPerMonth";

    fetch(url)
        .then(response=>response.json())
        .then(response=>{

            const labels = ['1','2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];
            const tooltipLabels = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
            const data = {
                labels: labels,
                datasets: [{
                    label: '월별 게시글 등록 수',
                    data: [0, 0, 0, 0, 0, 0, 0,0,0,0,0,0],
                    fill: false,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
            };

            for(let e of response.getChartRegisterCountPerMonth){


                if(e.month_name_korean==="1월"){
                    data.datasets[0].data[0]=e.count_per_month;
                }
                if(e.month_name_korean==="2월"){
                    data.datasets[0].data[1]=e.count_per_month;
                }
                if(e.month_name_korean==="3월"){
                    data.datasets[0].data[2]=e.count_per_month;
                }
                if(e.month_name_korean==="4월"){
                    data.datasets[0].data[3]=e.count_per_month;
                }
                if(e.month_name_korean==="5월"){
                    data.datasets[0].data[4]=e.count_per_month;
                }
                if(e.month_name_korean==="6월"){
                    data.datasets[0].data[5]=e.count_per_month;
                }
                if(e.month_name_korean==="7월"){
                    data.datasets[0].data[6]=e.count_per_month;
                }
                if(e.month_name_korean==="8월"){
                    data.datasets[0].data[7]=e.count_per_month;
                }
                if(e.month_name_korean==="9월"){
                    data.datasets[0].data[8]=e.count_per_month;
                }
                if(e.month_name_korean==="10월"){
                    data.datasets[0].data[9]=e.count_per_month;
                }
                if(e.month_name_korean==="11월"){
                    data.datasets[0].data[10]=e.count_per_month;
                }
                if(e.month_name_korean==="12월"){
                    data.datasets[0].data[11]=e.count_per_month;
                }
            }

            const config = {
                type: 'line',
                data: data,
                options: {
                    scales: {
                        x: {
                            ticks: {
                                autoSkip: false,
                                maxRotation: 0,
                                minRotation: 0,
                                font: {
                                    size: 13
                                }
                            }
                        },
                        y: {
                            beginAtZero: true,
                            ticks:{
                                stepSize:5
                            },
                            max: 100 // y축 최대값 설정
                        },
                    },
                    plugins: {
                        legend: {
                            labels: {
                                font: {
                                    weight: 'bold'
                                }
                            }
                        },
                        tooltip: {
                            enabled: true,
                            callbacks: {
                                label: function(context) {
                                    const monthIndex = context.dataIndex;
                                    const month = tooltipLabels[monthIndex];
                                    const value = context.raw;
                                    return `${month}: ${value}개`;
                                }
                            }
                        }
                    },

                }
            };


            myLineChart = new Chart(myLineCharts,config);



            function updateChart() {
                myLineChart.data.datasets.forEach((dataset, i) => {
                    dataset.data = data.datasets[i].data;
                });
                myLineChart.update();
            }

            // 초기 차트 업데이트
            updateChart();
        });


}

let myChart = null;

function dayChart(){
    const myCharts= document.getElementById('myChart').getContext('2d');

    if (myChart !== null) {
        myChart.destroy();
        myChart = null; // 변수를 null로 설정하여 참조를 제거
    }

    const url="/api/seller/getChartRegisterCount";

    fetch(url)
        .then(response=>response.json())
        .then(response=>{



            const labels = ['일', '월', '화', '수', '목', '금', '토'];
            const data = {
                labels: labels,
                datasets: [{
                    label: '일별 게시글 등록 수',
                    data: [0, 0, 0, 0, 0, 0, 0],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(255, 159, 64, 0.2)',
                        'rgba(255, 205, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(201, 203, 207, 0.2)'
                    ],
                    borderColor: [
                        'rgb(255, 99, 132)',
                        'rgb(255, 159, 64)',
                        'rgb(255, 205, 86)',
                        'rgb(75, 192, 192)',
                        'rgb(54, 162, 235)',
                        'rgb(153, 102, 255)',
                        'rgb(201, 203, 207)'
                    ],
                    borderWidth: 1
                }]
            };

            for(let e of response.getChartRegisterCount){


                if(e.day_name_korean==="일"){
                    data.datasets[0].data[0]=e.count_per_day;
                }
                if(e.day_name_korean==="월"){
                    data.datasets[0].data[1]=e.count_per_day;
                }
                if(e.day_name_korean==="화"){
                    data.datasets[0].data[2]=e.count_per_day;
                }
                if(e.day_name_korean==="수"){
                    data.datasets[0].data[3]=e.count_per_day;
                }
                if(e.day_name_korean==="목"){
                    data.datasets[0].data[4]=e.count_per_day;
                }
                if(e.day_name_korean==="금"){
                    data.datasets[0].data[5]=e.count_per_day;
                }
                if(e.day_name_korean==="토"){
                    data.datasets[0].data[6]=e.count_per_day;
                }
            }


            const config = {
                type: 'bar',
                data: data,
                options: {
                    plugins: {
                        legend: {
                            labels: {
                                font: {
                                    weight: 'bold',
                                    color:'red'
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks:{
                                stepSize:5
                            },
                            max: 100 // y축 최대값 설정
                        }
                    },
                    responsive: true,  // 반응형 디자인 활성화
                    maintainAspectRatio: false // 비율 유지 비활성화
                }
            };

            myChart = new Chart(myCharts,config);



            function updateChart() {
                myChart.data.datasets.forEach((dataset, i) => {
                    dataset.data = data.datasets[i].data;
                });
                myChart.update();
            }

            // 초기 차트 업데이트
            updateChart();
        });
}

let myPieChart = null;

function pieChart(){
    if (myPieChart !== null) {
        myPieChart.destroy();
        myPieChart = null; // 변수를 null로 설정하여 참조를 제거
    }

    const myPieCharts= document.getElementById("myPieChart");

    const url="/api/seller/getPieRegisterCount";

    fetch(url)
        .then(response=>response.json())
        .then(response=>{


            const data = {
                labels: [
                    '',
                    '',
                    ''
                ],
                datasets: [{
                    label: '가장 많이 활동한 점주 Top 3',
                    data: [0, 0, 0],
                    backgroundColor: [
                        'rgb(255, 99, 132)',
                        'rgb(54, 162, 235)',
                        'rgb(255, 205, 86)',
                    ],
                    hoverOffset: 4
                }]
            };
            for (let i = 0; i < response.getPieRegisterCount.length; i++) {
                if(response.getPieRegisterCount[i].counts===0){
                    data.labels.filter(label => label !== '')
                }else{
                    data.datasets[0].data[i] = response.getPieRegisterCount[i].counts;
                    data.labels[i] = response.getPieRegisterCount[i].sellerName;
                }
            }

            const config = {
                type: 'pie',
                data: data,
                options: {
                    plugins: {
                        title: {
                            display: true,
                            text: '가장 많이 활동한 점주 Top 3', // 제목 설정
                            font: {
                                size: 12 // 폰트 크기 설정
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    label += context.raw.toLocaleString() + '개 (';
                                    label += ((context.raw / context.dataset.data.reduce((acc, val) => acc + val, 0)) * 100).toFixed(2) + '%)';
                                    return label;
                                }
                            }
                        },
                        legend: {
                            position: 'bottom',
                            labels: {
                                generateLabels: function(chart) {
                                    const data = chart.data;
                                    if (data.labels.length && data.datasets.length) {
                                        return data.labels.map((label, i) => {
                                            const dataset = data.datasets[0];
                                            const value = dataset.data[i];
                                            const percent = ((value / dataset.data.reduce((acc, val) => acc + val, 0)) * 100).toFixed(2) + '%';
                                            return {
                                                text: `${label}: 글 ${value.toLocaleString()}개 (${percent})`,
                                                fillStyle: dataset.backgroundColor[i]
                                            };
                                        });
                                    }
                                    return [];
                                }
                            }
                        }
                    }
                }
            };

            myPieChart = new Chart(myPieCharts,config);



            function updateChart() {
                myPieChart.data.datasets.forEach((dataset, i) => {
                    dataset.data = data.datasets[i].data;
                });
                myPieChart.update();
            }

            // 초기 차트 업데이트
            updateChart();
        });


}
