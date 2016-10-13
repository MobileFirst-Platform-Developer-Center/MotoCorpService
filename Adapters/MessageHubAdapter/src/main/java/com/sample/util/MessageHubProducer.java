/*
 * Copyright 2016 IBM Corp.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sample.util;

import com.sample.config.MessageHubProperties;
import org.apache.kafka.clients.producer.KafkaProducer;

public class MessageHubProducer {

    private static KafkaProducer<byte[], byte[]> producer = null;

    public static KafkaProducer<byte[], byte[]> getInstance(MessageHubProperties properties) {

        if (producer == null) {
            // Create a Kafka producer, providing client configuration.
            producer = new KafkaProducer<byte[], byte[]>(properties.getConfig());
        }


        return producer;
    }
}