# SwitchHub

**This is a work in progress.**

SwitchHub is a tool for remote management and control of home appliances (or really anything connected to an outlet). It's a DIY project based on the "internet of things" turned into an app.

Developed as a project for CSM117 at UCLA by Anthony Ortega, Gabriel Lopez, 
Omar Leyva, and Ryan Baker.

### android

"android" contains the front-end Android application.

### device

"device" contains the code that runs on an Arduino and communicates with a central hub via bluetooth. The Arduino microcontroller, combined with the appliance itself and the relay controlling current to the device, can constitute a device.

### hub

"hub" contains the code that controls all devices in the network via bluetooth. It is meant to run on a Raspberry Pi and connects to the internet via WiFi.

### server

~~"server" contains the backend code, processing requests from the Android application and communicating with the hub, authenticating users, etc.~~

Parse is used as the backend server for SwitchHub.
