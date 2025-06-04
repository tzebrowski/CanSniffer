package org.obd.metrics.sniffer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.obd.metrics.sniffer.model.Settings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		final Settings settings = SettingsLoader.settings();
		log.info("Starting service with the settings: {}", settings);
		final Sniffer sniffer = new Sniffer();
		sniffer.run(settings);
	}
}
