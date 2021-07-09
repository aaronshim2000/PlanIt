function calculateTotal(){
 var priceArr=document.getElementsByName("price");
 var total=0;
 for(var i=0;i<priceArr.length;i++){
  if(parseFloat(priceArr[i].value))
     total+=parseFloat(priceArr[i].value);
 }
 document.getElementById("total").innerHTML=total;
}