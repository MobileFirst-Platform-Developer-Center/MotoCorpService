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

// This application uses express as its web server
// for more info, see: http://expressjs.com
var express = require('express');
var bodyParser = require('body-parser');
var cfenv = require('cfenv');
var MessageHub = require('message-hub-rest');
var http = require('http');
var request = require('request-json');
var app = express();
var appEnv = cfenv.getAppEnv();
var instance;
var consumerInstance;

// serve the files out of ./public as our main files
app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json());

var pushMessage = function(message) {
    var list = new MessageHub.MessageList();

    list.push(JSON.stringify(message));

    instance.produce('new-customer', list.messages)
      .then(function(response) {
          console.log(response);
      })
      .fail(function(error) {
        throw new Error(error);
      });
  };

// endpoint for the Foundation adapter to call to send a message to messagehub
app.post('/sendMessage', function (req, res) {
  pushMessage(req.body);
  res.json(req.body);
});

// endpoint for the Foundation adapter to call to send a message to messagehub
app.post('{id}/newVisit', function (req, res) {
    // add id to req.body
  pushMessage(req.body);
  res.json(req.body);
});

var start = function(restEndpoint, apiKey, callback) {
  if(!appEnv.services || (appEnv.services && Object.keys(appEnv.services).length === 0)) {
    if(restEndpoint && apiKey) {
      appEnv.services = {
        "messagehub": [
           {
              "label": "messagehub",
              "credentials": {
                 "api_key": apiKey,
                 "kafka_rest_url": restEndpoint,
              }
           }
        ]
      };
    } else {
      console.error('A REST Endpoint and API Key must be provided.');
      process.exit(1);
    }
  } else {
    console.log('Endpoint and API Key provided have been ignored, as there is a valid VCAP_SERVICES.');
  }

  instance = new MessageHub(appEnv.services);

  // Set up an interval which will poll Message Hub for
  // new messages on the 'new-customer' topic.
  produceInterval = setInterval(function() {

    // Attempt to consumer messages from the 'new-customer' topic_data
    // if at least one user is connected to the service.
    if(consumerInstance) {
      consumerInstance.get('new-customer')
        .then(function(data) {
          if(data.length > 0) {
             
            var myData = JSON.parse(data);
            console.log(myData.name);

          }
        });

      consumerInstance.get('new-visit')
        .then(function(data) {
          if(data.length > 0) {
             
            var myData = JSON.parse(data);
            console.log(myData);
          }
        });
    }

  }, 250);

  instance.topics.create('new-customer')
    .then(function(response) {
      console.log('"new-customer" topic created.');
      // Set up a consumer group of the provided name.
      return instance.consume("consumerGroupName", "consumerInstanceName", { 'auto.offset.reset': 'largest' });
    })
    .then(function(response) {
      consumerInstance = response[0];
      console.log('Consumer Instance created.');
      // Set offset for current consumer instance.
      return consumerInstance.get('new-customer');
    })
    .then(function() {
       // start server on the specified port and binding host
      app.listen(appEnv.port, '0.0.0.0', function() {
        // print a message when the server starts listening
        console.log("server starting on " + appEnv.url);
      });
    })
    .fail(function(error) {
      console.log(error);
      
    });
};

start();