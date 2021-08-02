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
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()});