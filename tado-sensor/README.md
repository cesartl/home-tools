# Tado Sensor

## Goal
Tado and other smart thermostat work well, but their main drawback is that they measure temperature close to the radiator.
When it gets hot the temperature measurement is biased. 
The goal of this tool is to periodically poll the Tado API to get the temperature of a given room, compare it against a separate
measurement and update the offset so that the two temperature probes eventually converge.

## Configuration
You need to create the following yaml file:
```yaml
tadoConfig:
  username: "<username>"
  password: "<password>"
  zoneId: "123"
  tadoUrl: "https://my.tado.com" #Optional
  accessTokenUri: "https://auth.tado.com/oauth/token" #Optional
  clientId: "tado-web-app" #Optional
  clientSecret: "wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc" #Optional
offsetThreshold: 0.5 #Optional
temperatureSensorConfig: !<bmp280>
  address: 119 #Optional
  offset: 0.0 #Optional
frequency: 5 #Optional
timeUnit: "MINUTES" #Optional
```

At the moment only Bmp280 temperature sensor is supported. Other can simply be added by implementing
the `TemperatureSensor` interface and a corresponding `TemperatureSensorConfig`.

## How to Run

### Prerequisites
* JDK 8+

### How to run
1. run `./gradlew build`
2. Copy  `tado-sensor-1.*-SNAPSHOT-all.jar` to your measuring device (e.g. Raspberry pi)
3. Create a YAML config file like described above
4. Run `java -jar -Dconfig.path=<path to your config file> tado-sensor-1.0-SNAPSHOT-all.jar`

If you want to customise logging you can add `-Dlogback.configurationFile=<path to logback.xml>`

If you want to run the process in the background you can create a script like:

`tadoSensor.sh`:
```shell script
DIR=$1
nohup java -jar -Dconfig.path=$DIR/tadoSenorConfig.yaml -Dlogback.configurationFile=$DIR/logback-tado-sensor.xml $DIR/tado-sensor-1.0-SNAPSHOT-all.jar &> nohup-tado.out &
``` 

And then `./tadoSensor.sh $(pwd)`

### Example output
```
Measured room temperature is 17.682393587008118 °C
Tado room temperature is 18.99 °C
Existing offset: -4.0. Need to change offset by -1.3076064129918805. Setting offset to -5.3076064129918805
Successfully set offset to -5.31 °C

```