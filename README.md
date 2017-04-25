# Cloudant/CouchDB Auto-Configuration for Spring Boot
[![Build Status](https://travis-ci.org/icha024/cloudant-spring-boot-starter.svg?branch=master)](https://travis-ci.org/icha024/cloudant-spring-boot-starter) [![Coverage Status](https://coveralls.io/repos/github/icha024/cloudant-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/icha024/cloudant-spring-boot-starter?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.clianz/cloudant-spring-boot-starter.svg)](http://search.maven.org/#search%7Cga%7C1%7Ccloudant-spring-boot-starter)

A convienient way to use the [official Cloudant client](https://github.com/cloudant/java-cloudant) with Spring Boot.

## Installing
**_Maven_**
```xml
<dependency>
  <groupId>com.clianz</groupId>
  <artifactId>cloudant-spring-boot-starter</artifactId>
  <version>0.9.5</version>
</dependency>
```
**_Gradle_**
```
repositories {
	mavenCentral()
}
```
```
compile('com.clianz:cloudant-spring-boot-starter:0.9.5')
```

## Usage
Inject a database:
```java
@Bean
public Database mydb(CloudantClient cloudant) {
	return cloudant.database("mydb", true);
}
```
Then start using it:
```java
@Autowired
Database mydb;
```
```java
mydb.save(data);
```

#### Using the Cloudant client directly
Alternatively, you may inject the client for fine-grain controls:
```java
@Autowired
CloudantClient cloudant
```
```java
Database db = cloudant.database("mydb", true);
```

## Configuration
### Spring Boot Configuration
Configurations can be placed in the application.properties (or yml) as usual.
Either username/password or a URL must be specified.
```properties
##### Mandatory: Provide URL or username/password  #####
cloudant.username=myUserName              #Username as assigned by Cloudant.
cloudant.password=myPasswd                #Password as assigned by Cloudant.
cloudant.url=http...                      #Url to CouchDB or a Cloudant instance. Defaults to official Cloudant server.

##### Optional configs #####
cloudant.account=myAccountName            #Defaults to username if left blank.
cloudant.proxyURL=http...                 #URL to proxy server.
cloudant.proxyUser=myUserName             #Proxy username.
cloudant.proxyPassword=myPasswd           #Proxy password.
cloudant.connectTimeout=300               #Connect timeout in seconds. Default to 300 sec (5 minutes).
cloudant.readTimeout=300                  #Read timeout in seconds. Default to 300 sec (5 minutes).
cloudant.maxConnections=6                 #Default to 6.
cloudant.disableSSLAuthentication=false   #Defaults to false.
```
### Bluemix (CloudFoundry) Configuration
When using Bluemix (CloudFoundry), the client will **automatically use the Cloudant service binded to the app** instead of the Spring configuration.

Bluemix's VCAP_SERVICES environment variable containing a Cloudant service will *always* take precedence over any Spring configuration. This is useful - Local development will use the Spring configuration properties, and promoting it to Bluemix will automatically use the environment configured instance. If Spring's configuration is desired, just remove the Cloudant service binding from your Bluemix app.

### CouchDB Compatibility
Since [Cloudant API is compatible](https://cloudant.com/product/cloudant-features/restful-api/) with Apache CouchDB's API, this client can be used with a regular CouchDB instance. This is useful for things like local development or if you want to host your own service.

To connect to CouchDB on localhost for example, just set the url:
```properties
cloudant.url=http://localhost:5984
```

### Example
Example app is available at https://github.com/icha024/cloudant-spring-boot-starter-example

## Requirements
- Java 1.6+
- [Official Cloudant client 2.x](https://github.com/cloudant/java-cloudant)    (v2.7.0 is included as transitive dependency) 

## License

Version 2.0 of the Apache License.
