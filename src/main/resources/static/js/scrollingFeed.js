function defaultSet(){
let users=document.getElementsByClassName("userList");
for(var i=0;i<users.length;i++){
  users[i].style.display="none";
}
let tabs=document.getElementsByClassName("tabs");
let userTab=tabs[0].getElementsByTagName("li")[3];
userTab.style.display="none";
}

function searchPost(){
let filter=document.getElementById("filter").value.toUpperCase();

let PlanPostRecord=document.getElementsByClassName("Record-Plan-post");
for(var i=0;i<PlanPostRecord.length;i++){
 let title=PlanPostRecord[i].getElementsByTagName("p")[0];
 let content=PlanPostRecord[i].getElementsByTagName("p")[1];
 let user=PlanPostRecord[i].getElementsByTagName("p")[2];

   if(title||content||user){
     let titleValue=title.textContent||titleValue.innerHTML;
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
 let user=TextPostRecord[i].getElementsByTagName("p")[3];
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

function searchUser(){
 let filter=document.getElementById("filter").value.toUpperCase();
 let tabs=document.getElementsByClassName("tabs");
 let userTab=tabs[0].getElementsByTagName("li")[3];
 if(filter!=""){
 userTab.style.display="";
 }
 else{
 userTab.style.display="none";
 }

 let UserRecord=document.getElementsByClassName("userList");
 console.log(UserRecord);
 for(var i=0;i<UserRecord.length;i++){
  let username=UserRecord[i].getElementsByTagName("p")[0];
  console.log(username);
  if(filter==""){
  UserRecord[i].style.display="none";
  }
  else if(username){
     let usernameValue=username.textContent||username.innerHTML;
     if(usernameValue.toUpperCase().indexOf(filter)>-1){
      UserRecord[i].style.display="";
     }
     else{
     UserRecord[i].style.display="none";
     }
     }
  }
}

function toggle(likes)
{
  var blur = document.getElementById('blur');
  blur.classList.toggle('active');

  var popup = document.getElementById('popup');
  popup.classList.toggle('active');

  var tableBody = document.getElementById('tableBody');

  var row = "";

  for(like in likes)
  {
    row += `<tr>
              <td>${like}</td>
            </tr>`;
  }

  tableBody.innerHTML = row;
}