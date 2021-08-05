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

var imageCounter = 0;
var fileCount = document.getElementById("numFiles"); fileCount.value = 0;

function uploadImages(){
	imageCounter = 0;
	imgCell00.style = "";
	imgCell01.style = "";
	imgCell02.style = "";
	imgCell03.style = "";
	imgCell04.style = "";
	imgCell00.innerHTML = '<input type="text" th:field=*{image00} id="imageRes00" class="imageResult">';
	imgCell01.innerHTML = '<input type="text" th:field=*{image01} id="imageRes01" class="imageResult">';
	imgCell02.innerHTML = '<input type="text" th:field=*{image02} id="imageRes02" class="imageResult">';
	imgCell03.innerHTML = '<input type="text" th:field=*{image03} id="imageRes03" class="imageResult">';
	imgCell04.innerHTML = '<input type="text" th:field=*{image04} id="imageRes04" class="imageResult">';
	imgWidget.open();
	imgRes00 = document.getElementById("imageRes00"); imgRes00.value = "empty";
	imgRes01 = document.getElementById("imageRes01"); imgRes01.value = "empty";
	imgRes02 = document.getElementById("imageRes02"); imgRes02.value = "empty";
	imgRes03 = document.getElementById("imageRes03"); imgRes03.value = "empty";
	imgRes04 = document.getElementById("imageRes04"); imgRes04.value = "empty";
	fileCount.value = imageCounter;
}

function removeImageNo( index ){
	imageCounter--;
	while (index < imageCounter){
		document.getElementById("viewImg0" + index).style = 'width: 250px; height: 250px; background-image: url(' + document.getElementById("imageRes0" + (index+1)).value + ')';
		document.getElementById("viewImg0" + index).innerHTML = '<button type="button" id="removeImg0' + index + '" onclick="removeImageNo(' + index + ')" class="removeButton">X</button><input type="text" th:field=*{image0' + index + '} id="imageRes0' + index + '" class="imageResult" value="' + document.getElementById("imageRes0" + (index+1)).value + '">';
		index++;
	}
	document.getElementById("viewImg0" + imageCounter).style = "";
	document.getElementById("viewImg0" + imageCounter).innerHTML = '<input type="text" th:field=*{image0' + imageCounter + '} id="imageRes0' + imageCounter + '" class="imageResult">';
	imgRes00 = document.getElementById("imageRes00"); imgRes00.value = "empty";
	imgRes01 = document.getElementById("imageRes01"); imgRes01.value = "empty";
	imgRes02 = document.getElementById("imageRes02"); imgRes02.value = "empty";
	imgRes03 = document.getElementById("imageRes03"); imgRes03.value = "empty";
	imgRes04 = document.getElementById("imageRes04"); imgRes04.value = "empty";
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
	  var currCell = document.getElementById("viewImg0" + imageCounter);
	  currCell.style = 'width: 250px; height: 250px; background-image: url("' + newRes.url + '"';
	  currCell.innerHTML = '<button type="button" id="removeImg0' + imageCounter + '" onclick="removeImageNo(' + imageCounter + ')" class="removeButton">X</button><input type="text" th:field=*{image0' + imageCounter + '} id="imageRes0' + imageCounter + '" class="imageResult" value="' + newRes.url + '">';
	  imageCounter++;
	  fileCount.value = imageCounter;
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()});