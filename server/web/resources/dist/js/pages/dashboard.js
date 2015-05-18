var script = document.createElement('script');
script.src = "resources/dist/js/jQuery-2.1.3.min.js";
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

function loadMap() {
    var latlng = new google.maps.LatLng(55.9278956, 37.5238322);
    var myOptions = {
        zoom: 5,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_container"), myOptions);
}

function loadPoints(POINTS) {
    console.log(POINTS);
    var latlng = new google.maps.LatLng(55.9278956, 37.5238322);
    var myOptions = {
        zoom: 17,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
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
function signinCallback(authResult) {
    if (authResult['access_token']) {
        // Успешная авторизация
        // Скрыть кнопку входа после авторизации пользователя, например:

        document.getElementById('signinButton').setAttribute('style', 'display: none');
        getEmail();
    } else if (authResult['error']) {
        // Произошла ошибка.
        // Возможные коды ошибок:
        //   "access_denied" – пользователь отказался предоставить приложению доступ к данным
        //   "immediate_failed" – не удалось выполнить автоматический вход пользователя
        // console.log('There was an error: ' + authResult['error']);
    }
}
function getEmail() {
    // Загружает библиотеки oauth2 для активации методов userinfo.
    gapi.client.load('oauth2', 'v2', function () {
        var request = gapi.client.oauth2.userinfo.get();
        request.execute(getEmailCallback);
    });
}
function getEmailCallback(obj) {
    var emailHidden = document.getElementById('email-hidden');
    email = '';

    if (obj['email']) {
        email = obj['email'];
    }

    //console.log(obj);   // Отменяет преобразование для проверки всего объекта.


    emailHidden.value = email;

    var emailSpan = document.getElementById('user-name-label');
    emailSpan.textContent = email;
    jQuery("#tracks-sidebar").load('tracks.xhtml?email='+email);
}

