# ClearSkiesWeather

Weather forecast + weather station.
Android App written in Kotlin with MVVM architeture.

[![Contributors](https://img.shields.io/github/contributors//mjcarterdev/ClearSkiesWeather.svg)](https://github.com/mjcarterdev/ClearSkiesWeather/graphs/contributors)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/mjcarterdev/ClearSkiesWeather/master.svg)](https://github.com/mjcarterdev/ClearSkiesWeather)
[![license](https://img.shields.io/github/license//mjcarterdev/ClearSkiesWeather.svg)](https://github.com//mjcarterdev/ClearSkiesWeather/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues//mjcarterdev/ClearSkiesWeather.svg)](https://github.com//mjcarterdev/ClearSkiesWeather/issues)
[![GitHub closed issues](https://img.shields.io/github/issues-closed//mjcarterdev/ClearSkiesWeather.svg)](https://github.com//mjcarterdev/ClearSkiesWeather/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull requests](https://img.shields.io/github/issues-pr//mjcarterdev/ClearSkiesWeather.svg)](https://github.com//mjcarterdev/ClearSkiesWeather/pulls)
[![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed//mjcarterdev/ClearSkiesWeather.svg)](https://github.com//mjcarterdev/ClearSkiesWeather/pulls?q=is%3Apr+is%3Aclosed)

## Features

- Display the weather based on values colected from sensors
- Display the weather forecast based on user location
- Show 8 days forecast
- Get the current location
- Search weather by city name
- Show weather statistics

## Tech Stack

- minSdk 26
- targetSdk 30
- Kotlin
- MVVM architecture
- Retrofit
- Material Design
- Room database
- DataBinding
- LiveData
- Singleton Pattern
- Coroutines
- Sensors: temperature, light, humidity, pressure
- Location + geocoding
- Broadcast receiver
- Accessibility checked
- Fragments
- Service
- AnyGraph

## API

- OpenWeather API for weather forecast
- https://openweathermap.org/api

## Run the app

1. Clone the repo https://github.com/mjcarterdev/ClearSkiesWeather.git
2. Create an an account on OpenWeatherMap and generate One Call API key

```
// setup your own API KEY
object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "YOUR API KEY"
}
```

3. Open project with Android Studio
4. Add file ... to folder iii with the following content:

5. Build project
6. Run project with Your mobile or Android Emulator SDK 26 or higher
7. On phone functionality of weather station is limited to the sensor that the phone has.
   In emulator, sensor activity can be simulated. Graph data will appear after the phone registers a sensor change.

## Demo video

## Screenshots

## Presentation

![Presentation](/ClearSkies.pptx)

## Project Stucture

<img src="https://github.com/mjcarterdev/ClearSkiesWeather/master/screennshots/structure.png"/>
