// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js

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
    url: '/customer/:plate',
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
