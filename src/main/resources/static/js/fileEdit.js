var imgRes00 = document.getElementsByClassName("imageRes00");
var imgRes01 = document.getElementsByClassName("imageRes01");
var imgRes02 = document.getElementsByClassName("imageRes02");
var imgRes03 = document.getElementsByClassName("imageRes03");
var imgRes04 = document.getElementsByClassName("imageRes04");
var imgRes05 = document.getElementsByClassName("imageRes05");
var imgRes06 = document.getElementsByClassName("imageRes06");
var imgRes07 = document.getElementsByClassName("imageRes07");
var imgRes08 = document.getElementsByClassName("imageRes08");
var imgRes09 = document.getElementsByClassName("imageRes09");
var vidRes00 = document.getElementsByClassName("videoID");

var fileCount = document.getElementsByClassName("numFiles");
var mediaType = document.getElementsByClassName("fileType");

imgRes00 = imgRes00[0];
imgRes01 = imgRes01[0];
imgRes02 = imgRes02[0];
imgRes03 = imgRes03[0];
imgRes04 = imgRes04[0];
imgRes05 = imgRes05[0];
imgRes06 = imgRes06[0];
imgRes07 = imgRes07[0];
imgRes08 = imgRes08[0];
imgRes09 = imgRes09[0];
vidRes00 = vidRes00[0];

fileCount = fileCount[0];
mediaType = mediaType[0];

var imageCounter = fileCount.value;

function loadImages(){
	console.log("called load, i = " + imageCounter);
	for (let i = 0; i < imageCounter; i++){
		var curRes = document.getElementsByClassName("imageRes0" + i);
		curRes = curRes[0];
		document.getElementById("viewImg0" + i).style = 'width: 250px; height: 250px; background-image: url("' + curRes.value + '"';
		document.getElementById("remButton0" + i).innerHTML = '<button type="button" onclick="removeImageNo(' + i + ')" class="removeButton">X</button>';
	}
}

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

function loadVideo(){
	playerOnPage.innerHTML = '<video id="demo-player" controls class="cld-video-player HiddenPlayer"></video>';
	enablePlayer();
	demoplayer.source( vidRes00.value );
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

console.log("called fix function");
if ( mediaType.value == "images" ){
	loadImages();
}
else if ( mediaType.value == "video"){
	loadVideo();
}