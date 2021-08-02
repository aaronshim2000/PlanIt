console.log("loaded ur js");

var cld = cloudinary.Cloudinary.new({ cloud_name: "hq73wefct", secure: true});

var demoplayer = cld.videoPlayer('demo-player', {
	bigPlayButton: 'init',
	controls: true,
	showLogo: false,
	loop: true
	preload: 'auto'
});

function uploadImages(){
	imgWidget.open();
}

var imgWidget = cloudinary.createUploadWidget({
  cloudName: 'hq73wefct', 
  uploadPreset: 't4vy5bss',
  sources: [ 'local', 'url', 'camera', 'google_drive'],
  searchBySites: ["all"],
  searchByRights: true,
  multiple: false,
  resourceType: 'video',
  maxFileSize: 20000000,
  maxImageWidth: 5000,
  maxImageHeight: 5000,
  singleUploadAutoClose: false},
  (error, result) => { 
    if (!error && result && result.event === "success") {
	  console.log(result.info);
	  var newRes = result.info;
	  console.log(newRes.url);
	  console.log(newRes.public_id);
	  demoplayer.source(newRes.public_id);
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()});