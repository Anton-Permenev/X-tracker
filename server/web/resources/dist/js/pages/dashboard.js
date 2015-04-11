/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/
function loadMap() {
    var latlng = new google.maps.LatLng(4.3695030, 101.1224120);
    var myOptions = {
        zoom: 4,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_container"), myOptions);

    var marker = new google.maps.Marker({
        position: latlng,
        map: map,
        title: "my hometown, Malim Nawar!"
    });
}