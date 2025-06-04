# Can Sniffer

![CI](https://github.com/tzebrowski/CanSniffer/workflows/Build/badge.svg?branch=main)

## About

`Can Sniffer` is a lightweight application built on top of [ObdMetrics](https://github.com/tzebrowski/ObdMetrics "ObdMetrics") that captures CAN bus traffic using Elm327 BT compatible adapters and either saves it to a csv file or stream data through network using `canlogserver` schema. This data can later be read and visualized using `SavvyCAN` either by loading the csv or having live update from device.


Application outputs csv file with following schema

`Time Stamp,ID,Extended,Dir,Bus,LEN,D1,D2,D3,D4,D5,D6,D7,D8`

### Features

* Capturing can bus traffic and dumping into file. 

### Building and running

Build command

```
	mvn clean install
```


Run command

```
	java -jar .\target\can-sniffer-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```


### Configuration

Application during startup is looking for configuration within execution dir. File should be named `config.yaml` and contains configuration like bellow. If file does not exists in the specified location, default one is loaded.


#### Configuration file scheme

* adapterName: BT OBD Adapter name, e.g: "AABBCC112233" 
* logFile.fileName: csv file name in which data will be saved
* duration: duration of the process, in milliseconds
* stnEnabled: enable if you have stn based adapter e.g: stn2120
* mode: CAN_SERVER | LOG_FILE - indicates whether data should be dumped into file or application should behave as network server.
* canServer.port - port on which application listen to in server mode

#### Example configuration

```
adapterName: "AABBCC112233"
duration: 30000
stnEnabled: false
mode: CAN_SERVER 

logFile:
   fileName: "./vw_out.%d.csv"

canServer:
   port: 28700   

      
```