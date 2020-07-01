//Angular App Module and Controller
angular.module('mapsApp', [])
    .controller('MapCtrl', function ($scope) {

        var mapOptions = {
            /*
             zoom: 10,
            center: new google.maps.LatLng(10.0000, 100.0000),
             mapTypeId:google.maps.MapTypeId.ROADMAP */
            zoom: 4,
            center: new google.maps.LatLng(10.0000, 100.0000),
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            mapTypeControl: !1,
            streetViewControl: !1,
            scaleControl: !0,
            zoomControl: !0,
            panControl: !1,
            styles: [{
                featureType: "water",
                stylers: [{visibility: "simplified"}, {color: "#3fb3de"}]
            }, {
                featureType: "transit.line",
                elementType: "labels.text.stroke",
                stylers: [{color: "#2ebad3"}]
            }, {
                featureType: "transit.line",
                elementType: "labels.text.fill",
                stylers: [{color: "#ffffff"}]
            }, {
                featureType: "transit.line",
                elementType: "geometry",
                stylers: [{color: "#ffffff"}]
            }, {
                featureType: "transit.line",
                elementType: "geometry.fill",
                stylers: [{color: "#cccccc"}]
            }, {
                featureType: "road.highway",
                elementType: "geometry.fill",
                stylers: [{color: "#bbbbbb"}]
            }, {
                featureType: "road.highway",
                elementType: "geometry.stroke",
                stylers: [{color: "#ffffff"}]
            }, {
                featureType: "road.local",
                elementType: "geometry.fill",
                stylers: [{color: "#ffffff"}]
            }, {
                featureType: "road.local",
                elementType: "geometry.stroke",
                stylers: [{color: "#d1d1d1"}]
            }, {
                featureType: "road.arterial",
                elementType: "geometry.fill",
                stylers: [{color: "#ffffff"}]
            }, {
                featureType: "road.arterial",
                elementType: "geometry.stroke",
                stylers: [{color: "#d1d1d1"}]
            }, {
                featureType: "road",
                elementType: "labels.icon",
                stylers: [{lightness: 50}]
            }, {
                featureType: "administrative",
                elementType: "labels.text.fill",
                stylers: [{color: "#393c3d"}]
            }, {
                featureType: "poi",
                elementType: "geometry.fill",
                stylers: [{lightness: 30}]
            }, {featureType: "landscape", stylers: [{lightness: 30}, {saturation: -50}]}],
            zoomControlOptions: {position: google.maps.ControlPosition.RIGHT_CENTER}
        };

        var map = new google.maps.Map(document.getElementById('map'), mapOptions);
        /*
         var mc = new MarkerClusterer(map);
         map.data.addListener('addfeature', function (e) {
         var geo = e.feature.getGeometry();
         if (geo.getType() === 'Point') {

         mc.addMarker(new google.maps.Marker({
         position: geo.get(),
         title: e.feature.getProperty('code')
         }));
         map.data.remove(e.feature);
         }
         });*/

        //map.data.loadGeoJson("/map/places");
        map.data.loadGeoJson("/map/routes");

        var infowindow = new google.maps.InfoWindow();
        // Set event listener for each feature.
        map.data.addListener('click', function (event) {

            infowindow.setContent(event.feature.getProperty('id') + " - " + event.feature.getProperty('name'));
            infowindow.setPosition(event.latLng);
            infowindow.setOptions({pixelOffset: new google.maps.Size(0, -34)});
            infowindow.open(map);
        });

        var marker = null;

        function createMarker(latlng, name, html) {
            var contentString = html;
            var marker = new google.maps.Marker({
                position: latlng,
                map: map,
                zIndex: Math.round(latlng.lat() * -100000) << 5
            });

            google.maps.event.addListener(marker, 'click', function () {
                infowindow.setContent(contentString);
                infowindow.open(map, marker);
            });
            google.maps.event.trigger(marker, 'click');
            return marker;
        }

        google.maps.event.addListener(map, 'click', function (event) {
            //call function to create marker
            if (marker) {
                marker.setMap(null);
                marker = null;
            }
            marker = createMarker(event.latLng, "name", "<b>Location</b><br>" + event.latLng);
        });

        map.data.setStyle(function (feature) {
            return {icon: feature.getProperty('icon')};
        });
        /*
         map.data.addListener('mouseover', function (event) {

         infowindow.setContent(event.feature.getProperty('code') + " - " + event.feature.getProperty('shortName'));
            infowindow.setPosition(event.latLng);
            infowindow.setOptions({pixelOffset: new google.maps.Size(0, -34)});
            infowindow.open(map);
        });

         map.data.addListener('mouseout', function (event) {

         infowindow.close();
         });



         map.data.addListener('addfeature', function (e) {
            //when it's a Line
            if (e.feature.getGeometry().getType() === 'LineString') {
                //hide the feature
                map.data.overrideStyle(e.feature, {visible: false});
                //add a polyline
                new google.maps.Polyline({
                    path: e.feature.getGeometry().getArray(),
                    map: this.getMap(),
                    geodesic: true
                });
            }
        });
         */

    });