/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var app = angular.module('motocorp', ['ionic']);

app.run(function ($ionicPlatform) {
  $ionicPlatform.ready(function () {

    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
});

app.config(function ($stateProvider) {
  $stateProvider.state('app', {
    url: '/app',
    abstract: true,
    templateUrl: 'app/views/menu.html',
    controller: 'HomeCtrl'
  }).state('app.search', {
    url: '/search',
    views: {
      'menuContent': {
        templateUrl: 'app/views/search.html',
        controller: 'SearchCtrl'
      }
    }
  }).state('app.customer', {
    url: '/customer/:id',
    views: {
      'menuContent': {
        templateUrl: 'app/views/view-customer.html',
        controller: 'ViewCustomerCtrl'
      }
    }
  }).state('app.new-visit', {
    url: '/new-visit/:id',
    views: {
      'menuContent': {
        templateUrl: 'app/views/new-visit.html',
        controller: 'NewVisitCtrl'
      }
    }
  }).state('app.new-customer', {
    url: '/new-customer',
    views: {
      'menuContent': {
        templateUrl: 'app/views/new-customer.html',
        controller: 'NewCustomerCtrl'
      }
    }
  }).state('login', {
    url: '/login',
    templateUrl: 'app/views/login.html',
    controller: 'LoginCtrl'
  }).state('app.test', {
    url: '/test',
    views: {
      'menuContent': {
        templateUrl: 'app/views/test.html',
        controller: 'TestCtrl'
      }
    }
  });

});


app.run(function ($rootScope, $state) {
  // when the login challenge event is fired redirect the user to the login page for authentication
  $rootScope.$on('login-challenge', function () {
    $state.go('login');
  });

  // redirect the user to the login page when the application opens
  $state.go('login');
});
