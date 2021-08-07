var vidRes00 = document.getElementById("videoPubID");

var cld = cloudinary.Cloudinary.new({ cloud_name: "hq73wefct", secure: true});

function enablePlayer(){
	demoplayer = cld.videoPlayer('demo-player', {
		bigPlayButton: 'init',
		controls: true,
		showLogo: false,
		loop: true,
		preload: 'auto'
	});
}

function displayMedia( postID ){
	console.log("Called display from post " + postID);
	/*currPost = document.getElementById(postID);
	aFiles = currPost.getElementsByClassName("imageCell");
	if (mType == "images"){
		if (imgAmmount == 1){
			currPost.innerHTML = '<img src="' + aFiles[0].innerHTML + '" class="postImage" />'
		}
		else{
			for (let i = 0; i < imgAmmount; i++){
				
			}
		}
	}
	else if (mType == "video"){
		
	}
	else{
		currPost.innerHTML = '';
	}*/
}