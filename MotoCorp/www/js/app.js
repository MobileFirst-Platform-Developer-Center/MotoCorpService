// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js

function wlCommonInit() {
	angular.bootstrap(document, ['starter']);
}

var app = angular.module('starter', ['ionic', 'starter.controllers']);

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
})

app.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider.state('app', {
    url: '/app',
    abstract: true,
    templateUrl: 'pages/menu.html',
    controller: 'AppCtrl'
  }).state('app.search', {
    url: '/search',
    views: {
      'menuContent': {
        templateUrl: 'pages/search.html',
        controller: 'SearchCtrl'
      }
    }
  }).state('app.customer', {
    url: '/customer/:customerId',
    views: {
      'menuContent': {
        templateUrl: 'pages/customer.html',
        controller: 'CustomerCtrl'
      }
    }
  }).state('app.signin', {
      url: '/signin',
      views: {
        'menuContent': {
          templateUrl: 'pages/signin.html',
          controller: 'SignInCtrl'
        }
      }
    });
    
  // if none of the above states are matched, use this as the fallback
  // $urlRouterProvider.otherwise('/signin');
  // $urlRouterProvider.otherwise('app/search');
});


app.run(function($rootScope, $state){
    $rootScope.$on('login-challenge', function(){
        $state.go('app.signin');
    });
    
    $state.go('app.signin');
});