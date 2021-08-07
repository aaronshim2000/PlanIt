var imagesCounter = document.getElementById("imagesNum").value;
if (imagesCounter == ""){ imagesCounter = 0; }
var mediaType = document.getElementById("fileType").value;
var videoID = document.getElementById("video00").value;
if (document.getElementById("imagesMax").value == "text-post"){ myMax = 10; }
if (document.getElementById("imagesMax").value == "review-post"){ myMax = 5; }
document.getElementById("imNumContain").innerHTML = '';
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

for (let i = imagesCounter; i < 10; i++){
	document.getElementById("image0" + i).innerHTML = '';
}

if (mediaType == "video"){
	document.getElementById("images").innerHTML = '';
	playerOnPage.innerHTML = '<video id="demo-player" controls class="cld-video-player HiddenPlayer"></video>';
	enablePlayer();
	demoplayer.source( videoID );
}