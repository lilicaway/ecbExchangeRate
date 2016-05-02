'use strict';

var angular = require('angular');

var ngModule = angular.module('ecbRates.services', []);
module.exports = ngModule.name;

ngModule.factory('convertAmount', ['$http', function($http) {
    return function(amount, fromCurrency, toCurrency, date) {
        if (!date) {
            // TODO: Use a better default date.
            date = '2016-04-28';
        }
        return $http.get('/api/convertAmount', {
            params: {
                amount: amount,
                fromCurrency: fromCurrency,
                toCurrency: toCurrency,
                date: date
            } 
          })
    };
}]);