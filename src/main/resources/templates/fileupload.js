var cl = new cloudinary.Cloudinary({cloud_name: "planit276", secure: true});
var i00s = document.getElementById("image00s");

i00s.addEventListener("change", function(){uploadI( 0 )});

function uploadI( i ){
	var uploadedI = document.getElementById("image0" + i + "s");
	var uIValue = uploadedI.value;
	cloudinary.v2.uploader.upload(uIValue,
  { public_id: "planit_image" }, 
  function(error, result) {console.log(result); });
}