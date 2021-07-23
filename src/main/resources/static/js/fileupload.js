var imgResults = document.getElementById("imgRes");
var imageCounter = 0;

function uploadImages(){
	imageCounter = 0;
	imgResults.outerHTML = '<input type="text" style="visibility: visible" id="imgRes" value="" disabled>';
	imgWidget.open();
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
      imgResults.outerHTML = imgResults.outerHTML + '<input type="text" style="visibility: visible" id="imgRes' + imageCounter + '" value="' + newRes.url + '" disabled>';
	  i++;
    }
  }
)

document.getElementById("upload_widgetImage").addEventListener("click", function(){uploadImages()}, false);

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