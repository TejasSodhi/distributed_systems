# Project Readme

[//]: # (## General guidelines)

[//]: # (* Please spend some time to make a proper `ReadME` markdown file, explaining all the steps necessary to execute your source code.)

[//]: # (* Do not hardcode IP address or port numbers, try to collect these configurable information from config file/env variables/cmd input args.)

[//]: # (* Attach screenshots of your testing done on your local environment.)

## Brief overview of the project
* This project comes with 2 separate clients and 2 separate servers, for TCP and
UDP each
* There are 2 separate packages, for `client` and `server`

### Sample configuration

#### Project structure
* The following is our project structure.
```bash
├── README.md
├── client
│   ├── AbstractClient.java
│   ├── ClientAppTCP.java
│   ├── ClientAppUDP.java
│   ├── ClientLogger.java
│   ├── IClient.java
│   ├── TCPClient.java
│   └── UDPClient.java
├── client_log.txt
├── distributed_systems.iml
├── server
│   ├── AbstractServer.java
│   ├── IServer.java
│   ├── KeyValueStore.java
│   ├── ServerAppTCP.java
│   ├── ServerAppUDP.java
│   ├── ServerLogger.java
│   ├── TCPServer.java
│   └── UDPServer.java
└── server_log.txt
```
* Compile the code using `javac server/*.java client/*.java`
* To run the 
  * TCP server `java server.ServerAppTCP <tcp-port-number>`
  * UDP server `java server.ServerAppUDP <udp-port-number>`
* To run the 
  * TCP client `java client/ClientAppTCP <host-name> <port-number>`
  * UDP client `java client/ClientAppUDP <host-name> <port-number>`
* TCP client communicates with TCP server and UDP client communicates with UDP server
* All the client and server logs are generated automatically even if they don't exist in the project
  * Client logs are generated as `client_log.txt`
  * Server logs are generated as`server_log.txt`