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

var imgRes00 = document.getElementById("imageRes00"); imgRes00.value = "empty";
var imgRes01 = document.getElementById("imageRes01"); imgRes01.value = "empty";
var imgRes02 = document.getElementById("imageRes02"); imgRes02.value = "empty";
var imgRes03 = document.getElementById("imageRes03"); imgRes03.value = "empty";
var imgRes04 = document.getElementById("imageRes04"); imgRes04.value = "empty";
var imgRes05 = document.getElementById("imageRes05"); imgRes05.value = "empty";
var imgRes06 = document.getElementById("imageRes06"); imgRes06.value = "empty";
var imgRes07 = document.getElementById("imageRes07"); imgRes07.value = "empty";
var imgRes08 = document.getElementById("imageRes08"); imgRes08.value = "empty";
var imgRes09 = document.getElementById("imageRes09"); imgRes09.value = "empty";

var butContainer00 = document.getElementById("remButton00");
var butContainer01 = document.getElementById("remButton01");
var butContainer02 = document.getElementById("remButton02");
var butContainer03 = document.getElementById("remButton03");
var butContainer04 = document.getElementById("remButton04");
var butContainer05 = document.getElementById("remButton05");
var butContainer06 = document.getElementById("remButton06");
var butContainer07 = document.getElementById("remButton07");
var butContainer08 = document.getElementById("remButton08");
var butContainer09 = document.getElementById("remButton09");

var imageCounter = 0;
var uploadBtn = document.getElementById("upload_widgetImage");
var vidRes00 = document.getElementById("videoID"); vidRes00.value = "empty";
var fileCount = document.getElementById("numFiles");  fileCount.value = 0;
var mediaType = document.getElementById("fileType"); mediaType.value = "none";
var playerOnPage = document.getElementById("containerForVid");

var cld = cloudinary.Cloudinary.new({ cloud_name: "hq73wefct", secure: true});

var demoplayer;

function enablePlayer(){
	demoplayer = cld.videoPlayer('demo-player', {
		bigPlayButton: 'init',
		controls: true,
		showLogo: false,
		loop: true,
		preload: 'auto'
	});
}

function clearFiles(){
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
	butContainer00.innerHTML = '';
	butContainer01.innerHTML = '';
	butContainer02.innerHTML = '';
	butContainer03.innerHTML = '';
	butContainer04.innerHTML = '';
	butContainer05.innerHTML = '';
	butContainer06.innerHTML = '';
	butContainer07.innerHTML = '';
	butContainer08.innerHTML = '';
	butContainer09.innerHTML = '';
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
	vidRes00.value = "empty";
	fileCount.value = 0;
	mediaType.value = "none";
	playerOnPage.innerHTML = '';
}

function callUploader(){
	clearFiles();
	if ( uploadBtn.innerHTML == "Upload Video" ){
		mediaType.value = "video";
		videoWidget.open();
		fileCount.value = 0;
	}
	else{
		mediaType.value = "images";
		imgWidget.open();
		fileCount.value = imageCounter;
	}
	if (imageCounter == 0){
		mediaType.value = "none";
	}
}

function switchTo( newFileType ){
	clearFiles();
	uploadBtn.innerHTML = "Upload " + newFileType;
}

function removeImageNo( index ){
	imageCounter--;
	fileCount.value = imageCounter;
	if (imageCounter == 0){ mediaType.value = "none"; }
	while (index < imageCounter){
		document.getElementById("viewImg0" + index).style = 'width: 250px; height: 250px; background-image: url(' + document.getElementById("imageRes0" + (index+1)).value + ')';
		document.getElementById("imageRes0" + index).value = document.getElementById("imageRes0" + (index+1)).value;
		index++;
	}
	document.getElementById("viewImg0" + imageCounter).style = "";
	document.getElementById("remButton0" + imageCounter).innerHTML = '';
	document.getElementById("imageRes0" + imageCounter).value = "empty";
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
	  document.getElementById("viewImg0" + imageCounter).style = 'width: 250px; height: 250px; background-image: url("' + newRes.url + '"';
	  document.getElementById("remButton0" + imageCounter).innerHTML = '<button type="button" onclick="removeImageNo(' + imageCounter + ')" class="removeButton">X</button>';
	  document.getElementById("imageRes0" + imageCounter).value = newRes.url;
	  imageCounter++;
	  fileCount.value = imageCounter;
	  mediaType.value = "images";
    }
  }
)

var videoWidget = cloudinary.createUploadWidget({
  cloudName: 'hq73wefct', 
  uploadPreset: 't4vy5bss',
  sources: [ 'local', 'url', 'camera', 'google_drive'],
  searchBySites: ["all"],
  searchByRights: true,
  multiple: false,
  resourceType: 'video',
  maxFileSize: 50000000,
  maxImageWidth: 5000,
  maxImageHeight: 5000,
  singleUploadAutoClose: false},
  (error, result) => { 
    if (!error && result && result.event === "success") {
	  var newRes = result.info;
	  playerOnPage.innerHTML = '<video id="demo-player" controls class="cld-video-player HiddenPlayer"></video>';
	  enablePlayer();
	  demoplayer.source(newRes.public_id);
	  vidRes00.value = newRes.public_id;
	  mediaType.value = "video";
	  imageCounter++;
    }
  }
)

uploadBtn.addEventListener("click", function(){callUploader()});

document.getElementById("selectImgs").addEventListener("click", function(){ switchTo( "Images" ) });
document.getElementById("selectVid").addEventListener("click", function(){ switchTo( "Video" ) });