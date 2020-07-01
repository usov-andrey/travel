'use strict';

angular.module('jhipsterApp')
    .controller('RouteQueryController', function ($scope, $http, route) {

        $http.get('/query/testRouteQuery.json').success(function (data) {
            $scope.query = data;
        });

        $scope.addStop = function () {
            $scope.query.stops.push({place: ""});
        };

        $scope.removeStop = function (stop) {
            $scope.query.stops.splice($scope.query.stops.indexOf(stop), 1);
        };

        $scope.canAddStop = function (stop) {
            if ($scope.query != undefined) {
                var stops = $scope.query.stops;
                return stops[stops.length - 1].place != "";
            }
            return true;
        };

        // Any function returning a promise object can be used to load values asynchronously
        $scope.getLocation = function (val) {
            return $http.get('/autoComplete', {
                params: {
                    value: val
                }
            }).then(function (response) {
                return response.data;
            });
        };

        $scope.toggleOpenDatePicker = function ($event, datePicker) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope[datePicker] = !$scope[datePicker];
        };

        $scope.searchRoutes = function () {
            route.setQuery($scope.query);
        };
    });
