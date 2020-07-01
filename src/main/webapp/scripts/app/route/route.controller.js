'use strict';

angular.module('jhipsterApp')
    .controller('RouteController', function ($scope, Principal) {
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
