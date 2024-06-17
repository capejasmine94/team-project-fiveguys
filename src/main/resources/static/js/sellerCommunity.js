window.addEventListener("DOMContentLoaded",()=>{
    getSellerCommunityList();
});

function getSellerCommunityList(){
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
                const hrefValue = sellerCommentMain.getAttribute("href");
                const newHrefValue = hrefValue+"?sellerCommunityNumber="+e.sellerCommunityDto.sellerCommunityNumber;
                sellerCommentMain.setAttribute("href",newHrefValue);

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
            reloadSellerCommunityLike();
            getSellerCommunityList();
        });

}
function reloadSellerCommunityLike(){
    const sellerCommunityNumbers = document.querySelectorAll(".sellerCommunityNumber");
   for(const sellerCommunityNumber of sellerCommunityNumbers){
       const value = sellerCommunityNumber.value;
       const likeDislikeStatus = sellerCommunityNumber.parentElement.querySelector(".likeDislikeStatus");

       const url="/api/seller/reloadSellerCommunityLike?sellerCommunityNumber="+sellerCommunityNumber.value;
       fetch(url)
           .then(response=>response.json())
           .then(response=>{
               if(response.success===false){
                   window.location.href = "/login/sellerLogin";
               }
               console.log(response.heartFill);

               if(response.heartFill===true){
                   likeDislikeStatus.classList.remove("bi-heart");
                   likeDislikeStatus.classList.add("bi-heart-fill");
               }else{
                   likeDislikeStatus.classList.remove("bi-heart-fill");
                   likeDislikeStatus.classList.add("bi-heart");
               }
           })

        }

}