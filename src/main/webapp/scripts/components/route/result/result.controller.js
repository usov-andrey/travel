'use strict';

angular.module('jhipsterApp')
    .controller('RouteResultController', function ($scope, route) {
        //$scope.routes = route.searchResult;
        var scope = $scope;
        scope.routeService = route;

        scope.getPlace = function (routes, placeId) {
            return routes.places[placeId];
        }

    });
