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