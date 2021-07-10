var P1 = document.getElementById("p1");
var P2 = document.getElementById("p2");
var P3 = document.getElementById("p3");
var curPath = window.location.pathname;

if ( curPath = "/post/text" ){
	P2.addEventListener("click", function(){ window.location.pathname = ('/post/review'); };
	P3.addEventListener("click", function(){ window.location.pathname = ('/post/plan'); };
}

if ( curPath = "/post/review" ){
	P1.addEventListener("click", function(){ window.location.pathname = ('/post/text'); };
	P3.addEventListener("click", function(){ window.location.pathname = ('/post/plan'); };
}

if ( curPath = "/post/plan" ){
	P1.addEventListener("click", function(){ window.location.pathname = ('/post/text'); };
	P2.addEventListener("click", function(){ window.location.pathname = ('/post/review'); };
}