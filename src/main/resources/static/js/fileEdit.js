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
var vidRes00 = document.getElementsByClassName("imageRes09");

var fileCount = document.getElementsByClassName("numFiles");
var starReview = document.getElementsByClassName("starRatingField");

imgRes00 = imgRes00[0];
imgRes01 = imgRes01[0];
imgRes02 = imgRes02[0];
imgRes03 = imgRes03[0];
imgRes04 = imgRes04[0];

fileCount = fileCount[0];
starReview = starReview[0];

var imageCounter = fileCount.value;

function setReviewScore(scoreG){
	starReview.value = scoreG;
}

function checkCorrectStar(){
	console.log("called star");
	var ReviewTranslation;
	if ( starReview.value == "0.5" ){ ReviewTranslation = "s0" }
	if ( starReview.value == "1" ){ ReviewTranslation = "s1" }
	if ( starReview.value == "1.5" ){ ReviewTranslation = "s2" }
	if ( starReview.value == "2" ){ ReviewTranslation = "s3" }
	if ( starReview.value == "2.5" ){ ReviewTranslation = "s4" }
	if ( starReview.value == "3" ){ ReviewTranslation = "s5" }
	if ( starReview.value == "3.5" ){ ReviewTranslation = "s6" }
	if ( starReview.value == "4" ){ ReviewTranslation = "s7" }
	if ( starReview.value == "4.5" ){ ReviewTranslation = "s8" }
	if ( starReview.value == "5" ){ ReviewTranslation = "s9" }
	document.getElementById(ReviewTranslation).checked = true;
}

function loadImages(){
	console.log("called load, i = " + imageCounter);
	for (let i = 0; i < imageCounter; i++){
		var curRes = document.getElementsByClassName("imageRes0" + i);
		curRes = curRes[0];
		document.getElementById("viewImg0" + i).style = 'width: 250px; height: 250px; background-image: url("' + curRes.value + '"';
		document.getElementById("remButton0" + imageCounter).innerHTML = '<button type="button" onclick="removeImageNo(' + i + ')" class="removeButton">X</button>';
	}
}

function removeImageNo( index ){
	imageCounter--;
	fileCount.value = imageCounter;
	//if (imageCounter == 0){ mediaType.value = "none"; }
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
checkCorrectStar();
loadImages();