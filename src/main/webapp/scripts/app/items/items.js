/**
 * Created by admin on 29.04.15.
 */
'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('items', {
                parent: 'site',
                url: '/items',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/items/items.html',
                        controller: 'ItemsController'
                    }
                }
            });
    });
