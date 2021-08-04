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
var uploadBtn = document.getElementById("upload_widgetImage");
var vidRes00 = document.getElementById("videoID");
var fileCount = document.getElementById("numFiles");
var mediaType = document.getElementById("fileType");
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
	imgCell00.innerHTML = '<input type="text" th:field=*{image00} id="imageRes00" class="imageResult" value="empty" disabled>';
	imgCell01.innerHTML = '<input type="text" th:field=*{image01} id="imageRes01" class="imageResult" value="empty" disabled>';
	imgCell02.innerHTML = '<input type="text" th:field=*{image02} id="imageRes02" class="imageResult" value="empty" disabled>';
	imgCell03.innerHTML = '<input type="text" th:field=*{image03} id="imageRes03" class="imageResult" value="empty" disabled>';
	imgCell04.innerHTML = '<input type="text" th:field=*{image04} id="imageRes04" class="imageResult" value="empty" disabled>';
	imgCell05.innerHTML = '<input type="text" th:field=*{image05} id="imageRes05" class="imageResult" value="empty" disabled>';
	imgCell06.innerHTML = '<input type="text" th:field=*{image06} id="imageRes06" class="imageResult" value="empty" disabled>';
	imgCell07.innerHTML = '<input type="text" th:field=*{image07} id="imageRes07" class="imageResult" value="empty" disabled>';
	imgCell08.innerHTML = '<input type="text" th:field=*{image08} id="imageRes08" class="imageResult" value="empty" disabled>';
	imgCell09.innerHTML = '<input type="text" th:field=*{image09} id="imageRes09" class="imageResult" value="empty" disabled>';
	imgRes00 = document.getElementById("imageRes00");
	imgRes01 = document.getElementById("imageRes01");
	imgRes02 = document.getElementById("imageRes02");
	imgRes03 = document.getElementById("imageRes03");
	imgRes04 = document.getElementById("imageRes04");
	imgRes05 = document.getElementById("imageRes05");
	imgRes06 = document.getElementById("imageRes06");
	imgRes07 = document.getElementById("imageRes07");
	imgRes08 = document.getElementById("imageRes08");
	imgRes09 = document.getElementById("imageRes09");
	vidRes00.value = "empty";
	fileCount.value = "0";
	mediaType.value = "none";
	playerOnPage.innerHTML = '';
}

function callUploader(){
	clearFiles();
	if ( uploadBtn.innerHTML = "Upload Video" ){
		videoWidget.open();
		mediaType.value = "video";
		fileCount.value = "1";
	}
	else{
		imgWidget.open();
		imgRes00 = document.getElementById("imageRes00");
		imgRes01 = document.getElementById("imageRes01");
		imgRes02 = document.getElementById("imageRes02");
		imgRes03 = document.getElementById("imageRes03");
		imgRes04 = document.getElementById("imageRes04");
		imgRes05 = document.getElementById("imageRes05");
		imgRes06 = document.getElementById("imageRes06");
		imgRes07 = document.getElementById("imageRes07");
		imgRes08 = document.getElementById("imageRes08");
		imgRes09 = document.getElementById("imageRes09");
		mediaType.value = "images";
		fileCount.value = '"' + imageCounter + '"';
	}
}

function switchTo( newFileType ){
	clearFiles();
	uploadBtn.innerHTML = "Upload " + newFileType;
}

function removeImageNo( index ){
	imageCounter--;
	fileCount.value = '"' + imageCounter + '"';
	if (imageCounter == 0){ mediaType.value = "none"; }
	while (index < imageCounter){
		document.getElementById("viewImg0" + index).style = 'width: 250px; height: 250px; background-image: url(' + document.getElementById("imageRes0" + (index+1)).value + ')';
		document.getElementById("viewImg0" + index).innerHTML = '<button type="button" id="removeImg0' + index + '" onclick="removeImageNo(' + index + ')" class="removeButton">X</button><input type="text" th:field=*{image0' + index + '} id="imageRes0' + index + '" class="imageResult" value="' + document.getElementById("imageRes0" + (index+1)).value + '" disabled>';
		index++;
	}
	document.getElementById("viewImg0" + imageCounter).style = "";
	document.getElementById("viewImg0" + imageCounter).innerHTML = '<input type="text" th:field=*{image0' + imageCounter + '} id="imageRes0' + imageCounter + '" class="imageResult" value="empty" disabled>';
	imgRes00 = document.getElementById("imageRes00");
	imgRes01 = document.getElementById("imageRes01");
	imgRes02 = document.getElementById("imageRes02");
	imgRes03 = document.getElementById("imageRes03");
	imgRes04 = document.getElementById("imageRes04");
	imgRes05 = document.getElementById("imageRes05");
	imgRes06 = document.getElementById("imageRes06");
	imgRes07 = document.getElementById("imageRes07");
	imgRes08 = document.getElementById("imageRes08");
	imgRes09 = document.getElementById("imageRes09");
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
	  currCell.style = 'width: 250px; height: 250px; background-image: url("' + newRes.url + '"';
	  currCell.innerHTML = '<button type="button" id="removeImg0' + imageCounter + '" onclick="removeImageNo(' + imageCounter + ')" class="removeButton">X</button><input type="text" th:field=*{image0' + imageCounter + '} id="imageRes0' + imageCounter + '" class="imageResult" value="' + newRes.url + '" disabled>';
	  imageCounter++;
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
    }
  }
)

uploadBtn.addEventListener("click", function(){callUploader()});

document.getElementById("selectImgs").addEventListener("click", function(){ switchTo( "Images" ) });
document.getElementById("selectVid").addEventListener("click", function(){ switchTo( "Video" ) });