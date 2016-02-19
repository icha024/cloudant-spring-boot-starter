# Cloudant Auto-Configuration for Spring Boot
[![Build Status](https://travis-ci.org/icha024/cloudant-spring-boot-starter.svg?branch=master)](https://travis-ci.org/icha024/cloudant-spring-boot-starter)
Provides convenient Cloudant dependency and configuration helper.
## Installing with Maven
```xml
<dependency>
  <groupId>com.clianz</groupId>
  <artifactId>cloudant-spring-boot-starter</artifactId>
  <version>0.9.0</version>
</dependency>
```
## Usage
### Using Cloudant client
Just inject the client:
```java
@Autowired
CloudantClient cloudant
```
Then do some client API function, or just create database from it and start using it:
```java
Database db = cloudant.database("mydb", true);
db.save(data);
```
### Using database directly
Alternatively, you may inject a database instance directly:
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
## Configuration
### Spring Boot Configuration
Configurations can be placed in the application.properties (or yml, or json) as usual.
Either username/password or a url must be specified.
```properties
##### Mandatory configs - username/password or url #####
cloudant.username=myUserName         #Username as assigned by Cloudant.
cloudant.password=myPasswd           #Password as assigned by Cloudant.
cloudant.url=http://localhost:5984   #Defaults to official server.

##### Optional configs #####
cloudant.account=myAccountName       #Defaults to username if left blank.
cloudant.proxyURL=http...            #URL to proxy server
cloudant.proxyUser=myUserName        #Proxy username
cloudant.proxyPassword=myPasswd      #Proxy password.
cloudant.connectTimeout=300          #Connect timeout in seconds. Default to 300 seconds (5 minutes).
cloudant.readTimeout=300             #Read timeout in seconds. Default to 300 seconds (5 minutes).
cloudant.maxConnections=6            #Default to 6.
cloudant.disableSSLAuthentication=false   #Defaults to false.
```
### Bluemix (CloudFoundry) Configuration
When using Bluemix (CloudFoundry), the client will **automatically use the Cloudant service binded to the app** instead of the Spring configuration.

Bluemix's VCAP_SERVICES environment variable containing a Cloudant service will *always* take precedence over any Spring configuration. This is useful - Local development will use the Spring configuration properties, and promoting it to Bluemix will automatically use the environment configured instance. If Spring's configuration is desired, just remove the Cloudant service binding from your Bluemix app.

## Requirements
- Java 1.6+
- Official Cloudant client 2.x (v2.2.0 is included as transitive dependency) 
