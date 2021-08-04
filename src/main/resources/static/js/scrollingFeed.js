function searchPost(){
let filter=document.getElementById("filter").value.toUpperCase();

let PlanPostRecord=document.getElementsByClassName("Record-Plan-post");
for(var i=0;i<PlanPostRecord.length;i++){
 let title=PlanPostRecord[i].getElementsByTagName("p")[0];
 let content=PlanPostRecord[i].getElementsByTagName("p")[1];
 let user=PlanPostRecord[i].getElementsByTagName("p")[2];

   if(title||content||user){
     let titleValue=title.textContent;
     let contentValue=content.textContent;
     let userValue=user.textContent;
     if(titleValue.toUpperCase().indexOf(filter)>-1||contentValue.toUpperCase().indexOf(filter)>-1||userValue.toUpperCase().indexOf(filter)>-1){
        PlanPostRecord[i].style.display="";
     }
     else{
        PlanPostRecord[i].style.display="none";
     }
   }
}

let ReviewPostRecord=document.getElementsByClassName("Record-Review-post");
for(var i=0;i<ReviewPostRecord.length;i++){
  let title=ReviewPostRecord[i].getElementsByTagName("p")[0];
  let content=ReviewPostRecord[i].getElementsByTagName("p")[2];
  let user=ReviewPostRecord[i].getElementsByTagName("p")[3];
    if(title||content||user){
     let titleValue=title.textContent;
     let contentValue=content.textContent;
     let userValue=user.textContent;
     if(titleValue.toUpperCase().indexOf(filter)>-1||contentValue.toUpperCase().indexOf(filter)>-1||userValue.toUpperCase().indexOf(filter)>-1){
        ReviewPostRecord[i].style.display="";
      }
     else{
      ReviewPostRecord[i].style.display="none";
     }
    }
  }

let TextPostRecord=document.getElementsByClassName("Record-Text-post");
for(var i=0;i<TextPostRecord.length;i++){
 let title=TextPostRecord[i].getElementsByTagName("p")[0];
 let content=TextPostRecord[i].getElementsByTagName("p")[1];
 let user=TextPostRecord[i].getElementsByTagName("p")[2];
 if(title||content||user){
   let titleValue=title.textContent;
   let contentValue=content.textContent;
   let userValue=user.textContent;
   if(titleValue.toUpperCase().indexOf(filter)>-1||contentValue.toUpperCase().indexOf(filter)>-1||userValue.toUpperCase().indexOf(filter)>-1){
      TextPostRecord[i].style.display="";
   }
   else{
      TextPostRecord[i].style.display="none";
   }
 }
}
}