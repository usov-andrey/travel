'use strict';

angular.module('jhipsterApp')
    .service('route', function ($http, $q) {

        var routeService = {};
        routeService.routes = [];

        routeService.setQuery = function (routeQuery) {

            $http.post('/routes', {query: routeQuery}).success(function (data) {
                routeService.routes = data;
            });
        };

        return routeService;

    });
