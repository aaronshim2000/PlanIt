console.log("loaded ur js");

var cld = cloudinary.Cloudinary.new({ cloud_name: "hq73wefct", secure: true});

var demoplayer = cld.videoPlayer('demo-player', {
	bigPlayButton: 'init',
	controls: true,
	showLogo: false,
	loop: true
});