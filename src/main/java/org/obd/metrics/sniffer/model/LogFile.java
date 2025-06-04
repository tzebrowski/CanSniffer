package org.obd.metrics.sniffer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public final class LogFile {
	private int port;
	private String fileName;
}
