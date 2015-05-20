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

function loadPoints(POINTS, color) {
    var latlng = POINTS[0];
    var myOptions = {
        zoom: 17,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,

    };

    var map = new google.maps.Map(document.getElementById("map_container"), myOptions);
    var flightPath = new google.maps.Polyline({
        path: POINTS,
        geodesic: false,
        strokeColor: color,
        strokeOpacity: 1.0,
        strokeWeight: 2
    })
    flightPath.setMap(map);
    console.log("map updated");

}

function manageSignin() {}

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
    jQuery("#tracks-sidebar").load('tracks.xhtml?email=' + email);

}
function loadTrack(trackId) {
    var points;
    jQuery("#pointsHiddenDiv").load('points.xhtml?trackId=' + trackId);
    points = JSON.parse(document.getElementById('points_hidden').value);
    points.sort(function (a, b) {
        return a.ordinal - b.ordinal
    });
    console.log(points);
    var i;
    var maxSpeed = 0;
    var minSpeed = points[i].speed();
    for (i = 0; i < points.length; i++) {
        if (points[i].speed() < minSpeed) {
            minSpeed = points[i].speed();
        }
        if (points[i].speed() > maxSpeed) {
            maxSpeed = points[i].speed();
        }
    }
    var POINTS = [];
    var color;
    for (var i = 0; i < points.length - 1; i++) {
        POINTS = [];
        POINTS.push(new google.maps.LatLng(points[i].lat, points[i].lon));
        POINTS.push(new google.maps.LatLng(points[i + 1].lat, points[i + 1].lon));
        color = getColor(maxSpeed, minSpeed, points[i].speed());
        loadPoints(POINTS, color);
    }

    console.log(POINTS);

}

function getColor(maxSpeed, minSpeed, speed) {
    var percentage = (speed - minSpeed) / (maxSpeed - minSpeed),
        r = Math.floor(255 * Math.min(2 * percentage, 1)),
        g = Math.floor(255 * Math.min(2 - 2 * percentage, 1)),
        bl = 0;

    return "rgb(" + r + "," + g + "," + bl + ")";

}

