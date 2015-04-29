function loadMap() {
    var latlng = new google.maps.LatLng(55.9278956, 37.5238322);
    var myOptions = {
        zoom: 5,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
}

function loadTrack(POINTS) {
    var latlng = new google.maps.LatLng(55.9278956, 37.5238322);
    var myOptions = {
        zoom: 17,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP};
    var map = new google.maps.Map(document.getElementById("map_container"), myOptions);
    var flightPath = new google.maps.Polyline({
            path: POINTS,
            geodesic: true,
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 2
    })
    flightPath.setMap(map);

}