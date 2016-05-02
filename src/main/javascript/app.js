'use strict';

var angular = require('angular');
var ngRoute = require('angular-route');
var mainModule = require('./main/main.js');

angular.module('ecbRates', [ngRoute, mainModule])

.config(['$routeProvider' , function($routeProvider) {
    $routeProvider.otherwise({redirectTo: '/main'});
}]);
