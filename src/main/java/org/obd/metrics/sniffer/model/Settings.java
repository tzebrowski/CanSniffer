package org.obd.metrics.sniffer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public final class Settings {
	private String adapterName;
	private String fileName;
	private long duration;
	private boolean stnEnabled;
}
