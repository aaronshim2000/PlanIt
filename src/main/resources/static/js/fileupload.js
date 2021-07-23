var imgCell00 = document.getElementById("viewImg00");
var imgCell01 = document.getElementById("viewImg01");
var imgCell02 = document.getElementById("viewImg02");
var imgCell03 = document.getElementById("viewImg03");
var imgCell04 = document.getElementById("viewImg04");
var imgCell05 = document.getElementById("viewImg05");
var imgCell06 = document.getElementById("viewImg06");
var imgCell07 = document.getElementById("viewImg07");
var imgCell08 = document.getElementById("viewImg08");
var imgCell09 = document.getElementById("viewImg09");

var imgRes00 = document.getElementById("imageRes00");
var imgRes01 = document.getElementById("imageRes01");
var imgRes02 = document.getElementById("imageRes02");
var imgRes03 = document.getElementById("imageRes03");
var imgRes04 = document.getElementById("imageRes04");
var imgRes05 = document.getElementById("imageRes05");
var imgRes06 = document.getElementById("imageRes06");
var imgRes07 = document.getElementById("imageRes07");
var imgRes08 = document.getElementById("imageRes08");
var imgRes09 = document.getElementById("imageRes09");

var imageCounter = 0;

function uploadImages(){
	imageCounter = 0;
	imgCell00.style = "";
	imgCell01.style = "";
	imgCell02.style = "";
	imgCell03.style = "";
	imgCell04.style = "";
	imgCell05.style = "";
	imgCell06.style = "";
	imgCell07.style = "";
	imgCell08.style = "";
	imgCell09.style = "";
	imgRes00.value = "empty";
	imgRes01.value = "empty";
	imgRes02.value = "empty";
	imgRes03.value = "empty";
	imgRes04.value = "empty";
	imgRes05.value = "empty";
	imgRes06.value = "empty";
	imgRes07.value = "empty";
	imgRes08.value = "empty";
	imgRes09.value = "empty";
	imgWidget.open();
}

function removeImageNo( index ){
	imageCounter--;
	while (index < imageCounter){
		document.getElementById("viewImg0" + index).style = document.getElementById("viewImg0" + (index+1)).style;
		document.getElementById("imageRes0" + index).value = document.getElementById("imageRes0" + (index+1)).value;
		index++;
	}
	document.getElementById("viewImg0" + imageCounter).style = "";
	document.getElementById("viewImg0" + imageCounter).innerHTML = '<input type="text" th:field=*{image0' + imageCounter + '} id="imageRes0' + imageCounter + '" class="imageResult" value="empty" disabled>';
}

var imgWidget = cloudinary.createUploadWidget({
  cloudName: 'hq73wefct', 
  uploadPreset: 't4vy5bss',
  sources: [ 'local', 'url', 'camera', 'image_search', 'google_drive'],
  searchBySites: ["all"],
  searchByRights: true,
  multiple: true,
  maxFiles: 10,
  resourceType: 'image',
  maxFileSize: 2000000,
  maxImageWidth: 5000,
  maxImageHeight: 5000,
  singleUploadAutoClose: false,
  googleApiKey: 'AIzaSyBwjuDWKJxCU5dEYC16AtB7AV26-n1Y-RY' },
  (error, result) => { 
    if (!error && result && result.event === "success") {
	  var newRes = result.info;
	  var currCell = document.getElementById("viewImg0" + imageCounter);
	  var currRes = document.getElementById("imageRes0" + imageCounter);
	  currCell.style = "width: 250px; height: 250px; background-image: url('" + newRes.url + ");";
	  currRes.value = newRes.url;
	  currCell.innerHTML = '<button type="button" id="removeImg0' + imageCounter + '" onclick="removeImageNo(' + imageCounter + ')" class="removeButton">X</button>' + currCell.innerHTML;
	  imageCounter++;
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()});

/*var cl = new cloudinary.Cloudinary({cloud_name: "planit276", secure: true});
var i00s = document.getElementById("image00s");

i00s.addEventListener("change", function(){uploadI( 0 )});

function uploadI( i ){
	var uploadedI = document.getElementById("image0" + i + "s");
	var uIValue = uploadedI.value;
	cloudinary.v2.uploader.upload(uIValue,
  { public_id: "planit_image" }, 
  function(error, result) {console.log(result); });
}*/