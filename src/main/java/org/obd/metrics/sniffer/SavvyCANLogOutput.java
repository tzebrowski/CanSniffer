package org.obd.metrics.sniffer;

import java.io.FileWriter;
import java.io.IOException;

import org.obd.metrics.api.model.ReplyObserver;
import org.obd.metrics.api.model.SnifferMetric;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class SavvyCANLogOutput extends ReplyObserver<SnifferMetric>{
	private final FileWriter fw;
	private final String header = "Time Stamp,ID,Extended,Dir,Bus,LEN,D1,D2,D3,D4,D5,D6,D7,D8";
	
	SavvyCANLogOutput(String fileName) throws IOException {
		fw = new FileWriter(fileName, false);
		fw.write(header);
	}
	
	@Override
	public void onNext(SnifferMetric t) {
		final long timestamp = System.currentTimeMillis();
		for (final String line : t.getRaw().getMessage().split("\r")) {
			final String segments[] = line.split(" ");
			if (segments.length == 9) {
				final String out = timestamp + "," 
							+ segments[0] + ","
							+ "1, Rx, 0, 8, "
							+ segments[1] + ", "
							+ segments[2] + ", "
							+ segments[3] + ", "
							+ segments[4] + ", "
							+ segments[5] + ", "
							+ segments[6] + ", "
							+ segments[7] + ", "
							+ segments[8] + "\n";
				try {
					fw.write(out);
					fw.flush();
				} catch (IOException e) {
					log.error("Failed to write", e);
				}
			}
		}
	}
}