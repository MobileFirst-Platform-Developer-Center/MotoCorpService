// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js

var app = angular.module('motocorp', ['ionic']);

app.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {

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

app.config(function($stateProvider, $urlRouterProvider) {
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
    url: '/customer/:customerId',
    views: {
      'menuContent': {
        templateUrl: 'app/views/customer.html',
        controller: 'CustomerCtrl'
      }
    }
  }).state('login', {
      url: '/login',
      templateUrl: 'app/views/login.html',
      controller: 'LoginCtrl'
    })
    .state('app.newcustomer', {
      url: '/newcustomer',
      views: {
      'menuContent': {
        templateUrl: 'app/views/newcustomer.html',
        controller: 'CustomerCtrl'
      }
    }
    })
    .state('app.newvisit', {
      url: '/newvisit',
      views: {
      'menuContent': {
        templateUrl: 'app/views/newvisit.html',
        controller: 'VisitCtrl'
      }
    }
    });


  // if none of the above states are matched, use this as the fallback
  // $urlRouterProvider.otherwise('/signin');
  // $urlRouterProvider.otherwise('app/search');
});



app.run(function($rootScope, $state){
    $rootScope.$on('login-challenge', function(){
        $state.go('login');
    });

    $state.go('login');
});
