let map;

function initMap() {
    const myLatlng = { lat: 49.2827, lng: -123.1207 }; 
    const geocoder = new google.maps.Geocoder();
    map = new google.maps.Map(document.getElementById("map"), {
      center: myLatlng,
      zoom: 8,
    });
  let infoWindow = new google.maps.InfoWindow({
    content: "Click the map to name City!",
    position: myLatlng,
  });
  infoWindow.open(map);
  // Configure the click listener.
  map.addListener("click", (mapsMouseEvent) => {
    // Close the current InfoWindow.
    infoWindow.close();
    // Create a new InfoWindow.
    const latlng = {
      lat: parseFloat(mapsMouseEvent.latLng.lat()),
      lng: parseFloat(mapsMouseEvent.latLng.lng()),
    };
    geocoder
    .geocode({ location: latlng })
    .then((response) => {
      if (response.results[0]) {
        let city;
        for(let i = 0; i < response.results[0].address_components.length ; i++){
          if(response.results[0].address_components[i].types[0] ==="locality"){
            console.log(response.results[0].address_components[i]);
            city = response.results[0].address_components[i].long_name;
            console.log(city);
          }
          
        }
        map.setZoom(11);
        const marker = new google.maps.Marker({
          position: latlng,
          map: map,
        });
        infoWindow.setContent(city);
        infoWindow.open(map, marker);
        /* getWeather(city, infoWindow, map); */
        getWeather(mapsMouseEvent.latLng.lat(), mapsMouseEvent.latLng.lng(), infoWindow, map);
        
      } else {
        window.alert("No results found");
      }
    })
    .catch((e) => window.alert("Geocoder failed due to: " + e));
});

  


  
}
function getWeather(cityID, infoWindow, map){
  var key = 'd69b8af454517be6933e1fe9b1fbd3b0';
  fetch('https://api.openweathermap.org/data/2.5/weather?q=' + cityID+ '&appid=' + key + '&units=metric')  
  .then(function(resp) { return resp.json() }) // Convert data to json
  .then(function(data) {
    console.log(data);
    infoWindow.close();
    infoWindow.setContent("<p" +"<br />" + data['name'] +"<br />" + 
    "Temp: " + data['main']['temp'] + "<br />" + "Desc: " +  data['weather'][0]['description'] + 
    "</p>");
    infoWindow.open(map);

  })
  .catch((e) => window.alert("Weather failed due to: " + e));
}
function getWeather(lat, lng, infoWindow, map){
  var key = 'd69b8af454517be6933e1fe9b1fbd3b0';
  fetch('https://api.openweathermap.org/data/2.5/weather?lat=' + lat+ '&lon=' + lng + '&appid=' + key + '&units=metric')  
  .then(function(resp) { return resp.json() }) // Convert data to json
  .then(function(data) {
    console.log(data);
    infoWindow.close();
    infoWindow.setContent("<p" +"<br />" + data['name'] +"<br />" + 
    "Temp: " + data['main']['temp'] + "<br />" + "Desc: " +  data['weather'][0]['description'] + "<br />" + '<img src=http://openweathermap.org/img/wn/'+data['weather'][0]['icon'] + '@2x.png>'  +
    "</p>");
    infoWindow.open(map);

  })
  .catch((e) => window.alert("Weather failed due to: " + e));

}
  