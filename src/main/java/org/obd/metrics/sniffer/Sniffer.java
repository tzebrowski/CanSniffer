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

import org.obd.metrics.api.Workflow;
import org.obd.metrics.api.model.Reply;
import org.obd.metrics.api.model.ReplyObserver;
import org.obd.metrics.api.model.SniffingPolicy;
import org.obd.metrics.api.model.SniffingPolicy.STNxxExtensions;
import org.obd.metrics.sniffer.model.Mode;
import org.obd.metrics.sniffer.model.Settings;
import org.obd.metrics.transport.AdapterConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class Sniffer  {

	void run(Settings settings) 
		throws IOException, InterruptedException {
		
		log.debug("Starting sniffer for following settings: {}", settings);
		
		final AdapterConnection connection = BluetoothConnection.openConnection(settings.getAdapterName());

		final Workflow workflow = Workflow.instance().observer(getObserver(settings)).initialize();

		final SniffingPolicy sniffingPolicy = SniffingPolicy
				.builder()
				.enabled(true)
				.debugEnabled(settings.isDebug())
				.stNxx(STNxxExtensions.builder().enabled(settings.isStnEnabled()).build()).build();

		workflow.start(connection, sniffingPolicy);
		WorkflowFinalizer.finalizeAfter(workflow, settings.getDuration());
		
	}

	@SuppressWarnings("unchecked")
	private ReplyObserver<Reply<?>> getObserver(Settings settings) throws IOException {
		ReplyObserver<?> observer = null;
		
		if (settings.getMode() ==  Mode.LOG_FILE) {
			observer = new SavvyCanCsvLogOutput(settings);
		} else {
			observer = new SavvyCANServer(settings);
		}
		return (ReplyObserver<Reply<?>>) observer;
	}
}
