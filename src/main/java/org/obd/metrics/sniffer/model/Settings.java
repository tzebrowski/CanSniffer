package org.obd.metrics.sniffer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public final class Settings {	
	private Mode mode;	
	private Server canServer = new Server();
	private LogFile logFile = new LogFile();
	private String adapterName;
	
	private long duration;
	private boolean stnEnabled;
}
