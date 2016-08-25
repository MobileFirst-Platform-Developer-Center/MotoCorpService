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

var express = require('express');
var http = require('http');
var request = require('request');

var app = express();
var server = http.createServer(app);
var mfpServer = "http://mobiledev-bo-server.mybluemix.net:80";
var port = 9081;

server.listen(port);
app.use('/sampleapp', express.static(__dirname + '/'));
console.log('::: server.js ::: Listening on port ' + port);

// Web server - serves the web application
app.get('/home', function(req, res) {
    // Website you wish to allow to connect
    res.sendFile(__dirname + '/index.html');
});

// Reverse proxy, pipes the requests to/from MobileFirst Server
app.use('/mfp/*', function(req, res) {
    var url = mfpServer + req.originalUrl;
    console.log('::: server.js ::: Passing request to URL: ' + url);
    req.pipe(request[req.method.toLowerCase()](url)).pipe(res);
});
