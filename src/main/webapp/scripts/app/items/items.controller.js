/**
 * Created by admin on 29.04.15.
 */
'use strict';

angular.module('jhipsterApp')
    .controller('ItemsController', function ($scope, $http) {
        $scope.jobs = ["All", "Alchemist", "Armorer", "Blacksmith", "Carpenter", "Culinarian", "Goldsmith", "Leatherworker",
            "Weaver"];
        $scope.search = {
            job: $scope.jobs[0],
            startLevel: 1,
            endLevel: 10
        };

        $scope.itemsPerPage = 10;

        $scope.doSearch = function () {
            $http.get('/items/job/' + $scope.search.job + "/" + $scope.search.startLevel + "/" + $scope.search.endLevel).success(function (data) {
                $scope.totalItems = data;
                $scope.totalItemsCount = $scope.totalItems.length;
                $scope.currentPage = 1;
                $scope.pageChanged();
            });
        };

        $scope.pageChanged = function () {
            var begin = (($scope.currentPage - 1) * $scope.itemsPerPage), end = begin + $scope.itemsPerPage;
            $scope.items = $scope.totalItems.slice(begin, end);
        };

        $scope.addPrice = function (item) {
            if (item.priceHistory == undefined) {
                item.priceHistory = [];
            }
            item.priceHistory.push({hq: false, amount: 1, saleDate: new Date(), saveDate: new Date()});
        };

        $scope.removePrice = function (item, price) {
            item.priceHistory.splice(item.priceHistory.indexOf(price), 1);
        };

        $scope.save = function () {
            $http.post('/items/salehistory', $scope.totalItems).success(function (data) {
                alert('saved');
            });
        };

        $scope.datePicker = (function () {
            var method = {};
            method.instances = [];

            method.open = function ($event, instance) {
                $event.preventDefault();
                $event.stopPropagation();

                method.instances[instance] = true;
            };

            method.options = {
                'show-weeks': false,
                startingDay: 0
            };

            var formats = ['MM/dd/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            method.format = formats[0];

            return method;
        }());
    });
