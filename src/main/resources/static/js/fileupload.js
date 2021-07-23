var myWidget = cloudinary.createUploadWidget({
  cloudName: 'hq73wefct', 
  uploadPreset: 't4vy5bss'}, (error, result) => { 
    if (!error && result && result.event === "success") { 
      console.log('Done! Here is the image info: ', result.info); 
    }
  }
)

document.getElementById("upload_widget").addEventListener("click", function(){
	myWidget.open();
}, false);

var myCropWidget = cloudinary.createUploadWidget({
  cloudName: 'demo', uploadPreset: 'preset1', folder: 'widgetUpload', cropping: true}, 
  (error, result) => { console.log(error, result) })

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