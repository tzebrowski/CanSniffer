 /**
 * Copyright 2019-2025, Tomasz Żebrowski
 *
 * <p>Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.obd.metrics.sniffer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.obd.metrics.api.Workflow;
import org.obd.metrics.api.model.ReplyObserver;
import org.obd.metrics.api.model.SniffingPolicy;
import org.obd.metrics.api.model.SniffingPolicy.STNxxExtensions;
import org.obd.metrics.sniffer.model.Settings;
import org.obd.metrics.transport.AdapterConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sniffer  {

	public void run(Settings settings) 
		throws IOException, InterruptedException, ExecutionException {
		
		log.debug("Starting sniffer for following settings: {}", settings);
		
		final AdapterConnection connection = BluetoothConnection.openConnection(settings.getAdapterName());

		final SavvyCANLogOutput output = new SavvyCANLogOutput(String.format(settings.getFileName(), System.currentTimeMillis()));
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Workflow workflow = Workflow.instance().observer((ReplyObserver) output).initialize();

		final SniffingPolicy sniffingPolicy = SniffingPolicy
				.builder()
				.enabled(true)
				.debugEnabled(false)
				.stNxx(STNxxExtensions.builder().enabled(settings.isStnEnabled()).build()).build();

		workflow.start(connection, sniffingPolicy);
		WorkflowFinalizer.finalizeAfter(workflow, settings.getDuration());
		
	}
}
