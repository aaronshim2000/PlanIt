var imgCell00 = document.getElementById("viewImg00");
var imgCell01 = document.getElementById("viewImg01");
var imgCell02 = document.getElementById("viewImg02");
var imgCell03 = document.getElementById("viewImg03");
var imgCell04 = document.getElementById("viewImg04");

var imgRes00 = document.getElementById("imageRes00"); imgRes00.value = "empty";
var imgRes01 = document.getElementById("imageRes01"); imgRes01.value = "empty";
var imgRes02 = document.getElementById("imageRes02"); imgRes02.value = "empty";
var imgRes03 = document.getElementById("imageRes03"); imgRes03.value = "empty";
var imgRes04 = document.getElementById("imageRes04"); imgRes04.value = "empty";

var butContainer00 = document.getElementById("remButton00");
var butContainer01 = document.getElementById("remButton01");
var butContainer02 = document.getElementById("remButton02");
var butContainer03 = document.getElementById("remButton03");
var butContainer04 = document.getElementById("remButton04");

var imageCounter = 0;
var fileCount = document.getElementById("numFiles"); fileCount.value = 0;

function uploadImages(){
	imageCounter = 0;
	imgCell00.style = "";
	imgCell01.style = "";
	imgCell02.style = "";
	imgCell03.style = "";
	imgCell04.style = "";
	butContainer00.innerHTML = '';
	butContainer01.innerHTML = '';
	butContainer02.innerHTML = '';
	butContainer03.innerHTML = '';
	butContainer04.innerHTML = '';
	imgWidget.open();
	imgRes00 = document.getElementById("imageRes00"); imgRes00.value = "empty";
	imgRes01 = document.getElementById("imageRes01"); imgRes01.value = "empty";
	imgRes02 = document.getElementById("imageRes02"); imgRes02.value = "empty";
	imgRes03 = document.getElementById("imageRes03"); imgRes03.value = "empty";
	imgRes04 = document.getElementById("imageRes04"); imgRes04.value = "empty";
	fileCount.value = imageCounter;
	if (imageCounter > 0){  }
}

function removeImageNo( index ){
	imageCounter--;
	if (imageCounter == 0){ mediaType.value = "none"; }
	while (index < imageCounter){
		document.getElementById("viewImg0" + index).style = 'width: 250px; height: 250px; background-image: url(' + document.getElementById("imageRes0" + (index+1)).value + ')';
		document.getElementById("imgRes0" + index).value = document.getElementById("imageRes0" + (index+1)).value;
		index++;
	}
	document.getElementById("viewImg0" + imageCounter).style = "";
	document.getElementById("butContainer0" + imageCounter).innerHTML = '';
	document.getElementById("imgRes0" + imageCounter).value = "empty";
}

var imgWidget = cloudinary.createUploadWidget({
  cloudName: 'hq73wefct', 
  uploadPreset: 't4vy5bss',
  sources: [ 'local', 'url', 'camera', 'image_search', 'google_drive'],
  searchBySites: ["all"],
  searchByRights: true,
  multiple: true,
  maxFiles: 5,
  resourceType: 'image',
  maxFileSize: 2000000,
  maxImageWidth: 5000,
  maxImageHeight: 5000,
  singleUploadAutoClose: false,
  googleApiKey: 'AIzaSyBwjuDWKJxCU5dEYC16AtB7AV26-n1Y-RY' },
  (error, result) => { 
    if (!error && result && result.event === "success") {
	  var newRes = result.info;
	  document.getElementById("viewImg0" + imageCounter).style = 'width: 250px; height: 250px; background-image: url("' + newRes.url + '"';
	  document.getElementById("butContainer0" + imageCounter).innerHTML = '<button type="button" onclick="removeImageNo(' + imageCounter + ')" class="removeButton">X</button>';
	  document.getElementById("imgRes0" + imageCounter).value = newRes.url;
	  imageCounter++;
	  fileCount.value = imageCounter;
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()});