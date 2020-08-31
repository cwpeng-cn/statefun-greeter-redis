/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greeter;

import org.apache.flink.statefun.examples.greeter.generated.GreetRequest;
import org.apache.flink.statefun.examples.greeter.generated.GreetResponse;
import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.EgressSpec;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import org.apache.flink.statefun.sdk.kafka.KafkaEgressBuilder;
import org.apache.flink.statefun.sdk.kafka.KafkaEgressSerializer;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressBuilder;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * A collection of all the components necessary to consume from and write to an external system, in
 * this case Apache Kafka.
 *
 * <p>The ingress and egress identifiers provide named references without exposing the underlying
 * system. This way, in a multi-module deployment, functions can interact with IO modules through
 * identifiers without depending on specific implementations.
 */
final class GreetingIO {

    static final IngressIdentifier<GreetRequest> GREETING_INGRESS_ID = new IngressIdentifier<>(GreetRequest.class, "redis", "users");

    static final EgressIdentifier<GreetResponse> GREETING_EGRESS_ID = new EgressIdentifier<>("example", "user", GreetResponse.class);
}
