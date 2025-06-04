package org.obd.metrics.sniffer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.obd.metrics.api.model.ReplyObserver;
import org.obd.metrics.api.model.SnifferMetric;
import org.obd.metrics.sniffer.model.Settings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class SavvyCANServer extends ReplyObserver<SnifferMetric>{
	private final PrintWriter printWriter;
	
	SavvyCANServer(Settings settings) throws IOException {
		
		try (final ServerSocket serverSocket = new ServerSocket(settings.getCanServer().getPort())) {
			log.info("Server started. Waiting for a client on port ", settings.getCanServer().getPort());

			Socket clientSocket = serverSocket.accept();
			log.info("Client connected: {}", clientSocket.getInetAddress());
			printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
		}
	}

	@Override
	public void onNext(SnifferMetric t) {
		final long timestamp = System.currentTimeMillis();
		for (final String line : t.getRaw().getMessage().split("\r")) {
			final String segments[] = line.split(" ");
			if (segments.length == 9) {
				try {
					final String canServerLine = "(" +timestamp + ") 0 " +  
								segments[0] + "#" + 
								segments[1] + 
								segments[2] + 
								segments[3] + 
								segments[4] + 
								segments[5] + 
								segments[6] + 
								segments[7] + 
								segments[8] + "\n"; 
								
					printWriter.write(canServerLine);
					printWriter.flush();
					
				} catch (Throwable e) {
					log.error("Failed to write log line", e);
				}
			}
		}
	}
}