'use strict';

var angular = require('angular');
var ngRoute = require('angular-route');
var services = require('../services/services.js');

var ngModule = angular.module('ecbRates.main', [ ngRoute, services ]);
module.exports = ngModule.name;

ngModule.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/main', {
        templateUrl : '/templates/main/main.html',
        controller : 'MainCtrl',
        controllerAs : 'mainCtrl'
    });
} ]);

ngModule.controller('MainCtrl', [ 'convertAmount', MainCtrl ]);

function MainCtrl(convertAmount) {
    this.convertAmount = convertAmount;
    this.fromCurrency = 'CHF';
    this.toCurrency = 'GBP';
    this.fromAmount = 1.3;
    this.resultAmount;
}

MainCtrl.prototype.doConversion = function() {
    var that = this;
    this.convertAmount(this.fromAmount, this.fromCurrency, this.toCurrency)
    .then(function(response) {
        console.log("Reply from server", response);
        that.resultAmount = response.data.resultAmount;
    });
}
