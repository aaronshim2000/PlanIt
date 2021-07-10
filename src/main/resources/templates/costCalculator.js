function coladd() {
        var table = document.getElementById("table");
        var row = table.insertRow(-1);
        var cell1 = row.insertCell(-1);
        var cell2 = row.insertCell(-1);
        var cell3 = row.insertCell(-1);
        cell1.innerHTML = '<input type="text" name="fee" placeholder="Other fee">';
        cell2.innerHTML = '<input type="text" name="price" placeholder="amount">';
        cell3.innerHTML = '<input type="button" value="delete" id="coladd" onclick="coldel(this)">';
    }
    function coldel(obj) {
        tr = obj.parentNode.parentNode;
        tr.parentNode.deleteRow(tr.sectionRowIndex);
    }

function calculateTotal(){
 var priceArr=document.getElementsByName("price");
 var total=0;
 for(var i=0;i<priceArr.length;i++){
  if(parseFloat(priceArr[i].value))
     total+=parseFloat(priceArr[i].value);
 }
 document.getElementById("total").innerHTML=total;
}