# Sample showing FIWARE Context Broker integration with Azure services

Using [Spring Boot](https://spring.io/projects/spring-boot) for a REST microservice that connects a [FIWARE](https://www.fiware.org/) Context broker with an Azure Event Hubs. Changes made in a FIWARE Context broker (like the [Orion Context Broker](https://fiware-orion.readthedocs.io/)) will be published to the configured [Azure Event Hub](https://azure.microsoft.com/en-in/services/event-hubs/) by sending events for the notifications of the FIWARE Context broker.

## Architecture

![Architecture](assets/fiware-publisher%20architecture.png)

Basic operation:

1. Start fiware-publisher (Java process or [Docker container](https://hub.docker.com/r/jmayrbaeurl/fiware-publisher))
2. Use fiware-publisher Mgmt API to subscribe to changes of the Context broker
3. Changes to entities of the Context broker are sent to the Notification endpoint of the fiware-publisher that forwards them to Azure Event Hub
4. Process changes in an Azure Service (e.g. [Time Series insights](https://azure.microsoft.com/en-us/services/time-series-insights/) for visualization)

## Prerequisites

* FIWARE Context broker, e.g. [Get started Tutorial from FIWARE](https://fiware-tutorials.readthedocs.io/en/latest/getting-started/index.html)
* Azure Event Hub. See tutorial [creating an Azure Event Hub](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-quickstart-cli)

## Configuration

1. Basic configuration of the fiware-publisher component
2. Creating subscriptions

### Basic configuration

* Azure Event Hub connectivity: Fiware-publisher expects the [Azure Event Hub connection string](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-get-connection-string) in the environment variable `FIWARE_EHNS_CONNSTRING`. Default Event hub name is `fiware-notifications`, but can be changed with the Spring Boot property `fiware.eh.hubname`
* FIWARE Context broker URL: Fiware publisher uses the [Spring Boot application property](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) `fiware.broker.url` to connect to the FIWARE Context broker
* Notification endpoint: Spring Boot application property `fiware.publisher.notificationurl`used to define the Notification endpoint for the Context broker to send change notifications. Defaults to `http://host.docker.internal:8080/notification/` (No need to change when fiware-publisher is running as Docker container on the same docker host)

### Creating subscriptions

The following examples, taken from the file [ctxbrokersetup.http](scripts/fiware/ctxbrokersetup.http) assume fiware-publisher is running on localhost and port 8080 (using syntax according to the [Visual Studio Code plugin REST client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client))

Sample - Creating a subscription for changes to all entities of type 'Product'

```bash
# REST Client syntax
POST http://localhost:8080/subscriptions/Product
# Curl syntax
curl --request POST --url http://localhost:8080/subscriptions/Product
```

Sample - Create a subscription for changes of any entity with `@All`

```bash
# REST Client syntax
POST http://localhost:8080/subscriptions/@All
# Curl syntax
curl --request POST --url http://localhost:8080/subscriptions/@All
```

Sample - Create a subscription with [full format](http://fiware.github.io/specifications/ngsiv2/stable/) from FIWARE Context broker

```bash
# REST Client syntax
POST http://localhost:8080/subscriptions
Content-Type: application/json

{
  "description": "Notify me of all product price changes",
  "subject": {
    "entities": [{"idPattern": ".*", "type": "Product"}],
    "condition": {
      "attrs": [ "price" ]
    }
  },
  "notification": {
    "http": {
      "url": "http://host.docker.internal:8080/notification/product"
    }
  }
}
# Curl syntax
curl --request POST --url http://localhost:8080/subscriptions --header 'content-type: application/json' --data '{"description": "Notify me of all product price changes","subject": {"entities": [{"idPattern": ".*", "type": "Product"}],"condition": {"attrs": [ "price" ]}},"notification": {"http": {"url": "http://host.docker.internal:8080/notification/product"}}}'
```

Sample - Create multiple subscriptions with full format from FIWARE Context broker

```bash
# REST Client syntax
POST http://localhost:8080/subscriptions
Content-Type: application/json

[
  {
    "description": "Notify me of all product price changes",
    "subject": {
      "entities": [{"idPattern": ".*", "type": "Product"}],
      "condition": {
        "attrs": [ "price" ]
      }
    },
    "notification": {
      "http": {
        "url": "http://host.docker.internal:8080/notification/product"
      }
    }
  },
  {
    "description": "Notify me of all product name changes",
    "subject": {
      "entities": [{"idPattern": ".*", "type": "Product"}],
      "condition": {
        "attrs": [ "name" ]
      }
    },
    "notification": {
      "http": {
        "url": "http://host.docker.internal:8080/notification/product"
      }
    }
  }
]
```

### Checking subscription setup

List all subscriptions from FIWARE Context broker

```bash
# REST Client syntax
GET http://localhost:8080/subscriptions
# Curl syntax
curl --request GET --url http://localhost:8080/subscriptions
```

List all subscriptions that were created by fiware-publisher

```bash
# REST Client syntax
GET http://localhost:8080/subscriptions?onlyOwn=true
# Curl syntax
curl --request GET --url 'http://localhost:8080/subscriptions?onlyOwn=true'
```

## Running fiware-publisher

E.g. as Docker container (Insert Event Hub connection string, assumes Context broker running on http://fiware-orion:1026/v2/

```bash
docker run -d --name fiware-publisher -h publisher --network=fiware_default -p 8080:8080 --env fiware.broker.url=http://fiware-orion:1026/v2/ --env FIWARE_EHNS_CONNSTRING=[Event Hub connection string]  jmayrbaeurl/fiware-publisher
```

## Configuration reference

* `FIWARE_EHNS_CONNSTRING`: Azure Event Hub connection string
* `fiware.eh.hubname`: Azure Event Hub name, Default value is `fiware-notifications`
* `fiware.broker.url`: FIWARE Context Broker URL
* `fiware.publisher.notificationurl`: Notification endpoint URL, defaults to `http://host.docker.internal:8080/notification/`
* `fiware.publisher.singleMessages`: boolean, defaults to `true`. If true will split multiple notifications into single Azure Event Hub messages sent in a batch
* `fiware.publisher.subscriptions.all`: boolean, defaults to `false`. If true fiware-publisher will create a subscription for any change on the FIWARE Context broker (if it doesn't exist yet)
* `server.port`: Port used by the fiware-publisher, defaults to `8080`

## Building

Building fiware-publisher from source (Powershell):

```ps
.\mvnw clean install
```

Building the Docker image

```ps
.\mvnw docker-file:build
 ```

Pushing the Docker image to DockerHub

```ps
.\mvnw docker-file:push
 ```

## Swagger support

Assuming fiware-publisher is running on localhost and port 8080, the following will get the Swagger API

```http
http://localhost:8080/v2/api-docs
```

or using the Swagger UI

```http
http://localhost:8080/swagger-ui.html
```
