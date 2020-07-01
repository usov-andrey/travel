/**
 * Created by admin on 29.04.15.
 */
'use strict';

angular.module('jhipsterApp')
    .controller('InventoryController', function ($scope, $http) {

        $http.get('/inventory').success(function (data) {
            $scope.inventory = data;
        });

        $scope.addItem = function () {
            $scope.inventory.items.push({itemAmount: {item: {id: "", name: ""}, amount: 1}});
        };

        $scope.removeItem = function (item) {
            $scope.inventory.items.splice($scope.inventory.items.indexOf(stop), 1);
        };

        // Any function returning a promise object can be used to load values asynchronously
        $scope.getItems = function (val) {
            return $http.get('/items/ac', {
                params: {
                    value: val
                }
            }).then(function (response) {
                return response.data;
            });
        };

        $scope.save = function () {
            $http.post('/inventory', {items: $scope.inventory.items}).success(function (data) {
                alert('saved');
            });
        };
    });
