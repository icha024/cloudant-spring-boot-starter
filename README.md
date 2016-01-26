# Cloudant Auto-Configuration for Spring Boot
Provides convenient Cloudant dependency and configuration helper.
## Installing with Maven
```
<dependency>
  <groupId>com.clianz</groupId>
  <artifactId>spring-boot-starter-cloudant</artifactId>
  <version>0.0.1</version>
</dependency>
```
## Usage
### Using Cloudant client
Just inject the client:
```
@Autowired
CloudantClient cloudant
```
Then do some client API function, or just create database from it and start using it:
```
Database db = cloudant.database("mydb", true);
db.save(data);
```
### Using database directly
Alternatively, you may inject a database instance directly:
```
@Bean
public Database mydb(CloudantClient cloudant) {
	return cloudant.database("mydb", true);
}
```
Then start using it:
```
@Autowired
Database mydb
```
```
mydb.save(data);
```
## Configuration
### Spring Boot Configuration
Configurations can be place in the Application.properties (or yml, or json) as usual. The username and password is mandatory.
```
##### Mandatory configs #####
username=myUserName     #Username as assigned by Cloudant.
password=myPasswd       #Password as assigned by Cloudant.

##### Optional configs #####
account=myAccountName   #Defaults to username if left blank.
url=Cloudant URL        #Defaults to official server.
proxyURL=http://...     #URL to proxy server
proxyUser=myUserName    #Proxy username
proxyPassword=myPasswd  #Proxy password.
connectTimeout=300      #Connect timeout in seconds. Default to 300 seconds (5 minutes).
readTimeout=300         #Read timeout in seconds. Default to 300 seconds (5 minutes).
maxConnections=6        #Default to 6.
disableSSLAuthentication=false   #Defaults to false.
```
### Bluemix (CloudFoundry) Configuration
When using Bluemix (CloudFoundry), the client will automatically use the Cloudant service binded to the app instead of the Spring configuration.

Bluemix's VCAP_SERVICES environment variable containing a Cloudant service will always take precedence over any Spring configuration. This is useful - Local development will use the Spring configuration properties, and promoting it to Bluemix will automatically use the environment configured instance. If Spring's configuration is desired, just remove the Cloudant service binding from your Bluemix app.

## Requirements
- Java 1.6+
- Official Cloudant client 2.x
