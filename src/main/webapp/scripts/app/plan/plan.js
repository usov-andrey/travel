/**
 * Created by admin on 29.04.15.
 */
'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('plan', {
                parent: 'site',
                url: '/plan',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/plan/plan.html',
                        controller: 'PlanController'
                    }
                }
            });
    });
