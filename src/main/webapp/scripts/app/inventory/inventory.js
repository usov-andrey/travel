/**
 * Created by admin on 29.04.15.
 */
'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('inventory', {
                parent: 'site',
                url: '/inventory',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/inventory/inventory.html',
                        controller: 'InventoryController'
                    }
                }
            });
    });
