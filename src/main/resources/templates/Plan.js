function calculateTotal(){
var table=document.getElementById("cost_table");
    var total=0;
    for(var i=0;i<table.rows.length;i++){
      if(parseInt(table.rows[i].cells[1].innerHTML)>=1)
        total=total+parseInt(table.rows[i].cells[1].innerHTML);
    }
    document.getElementById("total").innerHTML=total;
}