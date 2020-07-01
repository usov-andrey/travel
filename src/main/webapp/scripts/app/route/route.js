'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('route', {
                parent: 'site',
                url: '/route',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/route/route.html',
                        controller: 'RouteController'
                    },
                    'query@route': {
                        controller: 'RouteQueryController',
                        templateUrl: '/scripts/components/route/query/query.html'
                    },
                    'result@route': {
                        controller: 'RouteResultController',
                        templateUrl: '/scripts/components/route/result/result.html'
                    }
                }
            });
    });

