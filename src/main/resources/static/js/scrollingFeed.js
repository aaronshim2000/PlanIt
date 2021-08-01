function searchPost(){
let filter=document.getElementById("filter").value.toUpperCase();

let PlanPostRecord=document.getElementsByClassName("Record-Plan-post");
for(var i=0;i<PlanPostRecord.length;i++){
 let title=PlanPostRecord[i].getElementsByTagName("p")[0];
 if(title){
   let titleValue=title.textContent;
   if(titleValue.toUpperCase().indexOf(filter)>-1){
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
    if(title){
     let titleValue=title.textContent;
      if(titleValue.toUpperCase().indexOf(filter)>-1){
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
 if(title){
   let titleValue=title.textContent;
   if(titleValue.toUpperCase().indexOf(filter)>-1){
      TextPostRecord[i].style.display="";
   }
   else{
      TextPostRecord[i].style.display="none";
   }
 }
}
}