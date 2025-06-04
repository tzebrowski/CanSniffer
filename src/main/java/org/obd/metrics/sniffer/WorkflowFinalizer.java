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

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.obd.metrics.api.ConditionalSleep;
import org.obd.metrics.api.Workflow;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class WorkflowFinalizer {

	int DEFAULT_FINALIZE_TIME = 200;

	static void finalizeAfter(final Workflow workflow, long sleepTime, Supplier<Boolean> condition)
	        throws InterruptedException {
		final Callable<String> end = () -> {
			final ConditionalSleep conditionalSleep = ConditionalSleep
			        .builder()
			        .condition(condition)
			        .slice(10l)
			        .build();

			conditionalSleep.sleep(sleepTime);
			workflow.stop();
			log.info("Finalizing the process.");
			System.exit(0);
			return "end";
		};

		final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
		newFixedThreadPool.invokeAll(Arrays.asList(end));
		newFixedThreadPool.shutdown();
	}

	static void finalizeAfter(final Workflow workflow, long sleepTime) throws InterruptedException {
		finalizeAfter(workflow, sleepTime, () -> false);
	}
}
