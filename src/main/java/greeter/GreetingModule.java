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

import com.google.auto.service.AutoService;
import org.apache.flink.statefun.examples.greeter.generated.GreetRequest;
import org.apache.flink.statefun.examples.greeter.generated.GreetResponse;
import org.apache.flink.statefun.flink.io.datastream.SinkFunctionSpec;
import org.apache.flink.statefun.flink.io.datastream.SourceFunctionSpec;
import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.EgressSpec;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import org.apache.flink.statefun.sdk.spi.StatefulFunctionModule;

import java.util.List;
import java.util.Map;

/**
 * The top level entry point for this application.
 *
 * <p>On deployment, the address of the Kafka brokers can be configured by passing the flag
 * `--kafka-address &lt;address&gt;`. If no flag is passed, then the default address will be used.
 */
@AutoService(StatefulFunctionModule.class)
public final class GreetingModule implements StatefulFunctionModule {

    @Override
    public void configure(Map<String, String> globalConfiguration, Binder binder) {

        System.out.println("StatefulFunctionModule");
        IngressSpec<GreetRequest> spec = new SourceFunctionSpec<>(GreetingIO.GREETING_INGRESS_ID, new RedisSource());
        binder.bindIngress(spec);
        binder.bindIngressRouter(GreetingIO.GREETING_INGRESS_ID, new GreetRouter());

        // bind an egress to the system
        EgressSpec<GreetResponse> e_spec = new SinkFunctionSpec<>(GreetingIO.GREETING_EGRESS_ID, new RedisSink());
        binder.bindEgress(e_spec);

        // bind a function provider to a function type
        binder.bindFunctionProvider(GreetStatefulFunction.TYPE, unused -> new GreetStatefulFunction());
    }
}
