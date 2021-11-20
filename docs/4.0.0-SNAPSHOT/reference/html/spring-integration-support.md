# Spring Integration Support

Spring Integration Extension for Azure provides Spring Integration adapters for the various services provided by the [Azure SDK for Java](https://github.com/Azure/azure-sdk-for-java/). Below is a list of supported adapters:

- spring-integration-azure-eventhbus
- spring-integration-azure-servicebus
- spring-integration-azure-storage-queue

Provide Spring Integration support for these Azure serices: eh, sb, storage q

## Spring Integration with Azure Event Hubs

### Dependency Setup

```xml
<dependency>
	<groupId>com.azure.spring</groupId>
	<artifactId>azure-spring-integration-eventhubs</artifactId>
</dependency>
```

### Configuration


for full configurations, check appendix

### Basic Usage

### Samples
Please refer to this [sample project](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/tag_azure-spring-boot_3.6.0/eventhubs/azure-spring-integration-sample-eventhubs) to learn how to use Event Hubs integration. (todo: @chenrujun, update this link)

## Spring Integration with Azure Service Bus

### Dependency Setup

```xml
<dependency>
	<groupId>com.azure.spring</groupId>
	<artifactId>azure-spring-integration-servicebus</artifactId>
</dependency>
```

### Configuration

for full configurations, check appendix

### Basic Usage

### Samples

**Example: Manually set the partition key for the message**

This example demonstrates how to manually set the partition key for the message in the application.

**Way 1:**
This example requires that `spring.cloud.stream.default.producer.partitionKeyExpression` be set `"'partitionKey-' + headers[<message-header-key>]"`.
```yaml
spring:
  cloud:
    azure:
      servicebus:
        connection-string: [servicebus-namespace-connection-string]
    stream:
      default:
        producer:
          partitionKeyExpression:  "'partitionKey-' + headers[<message-header-key>]"
```
```java
public class SampleController {
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader("<message-header-key>", "Customize partition key")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }
}
```

> **NOTE:** When using `application.yml` to configure the partition key, its priority will be the lowest.
> It will take effect only when the `ServiceBusMessageHeaders.SESSION_ID`, `ServiceBusMessageHeaders.PARTITION_KEY`, `AzureHeaders.PARTITION_KEY` are not configured.
**Way 2:**
Manually add the partition Key in the message header by code.

*Recommended:* Use `ServiceBusMessageHeaders.PARTITION_KEY` as the key of the header.
```java
public class SampleController {
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.PARTITION_KEY, "Customize partition key")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }
}
```

*Not recommended but currently supported:* `AzureHeaders.PARTITION_KEY` as the key of the header.
```java
public class SampleController {
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(AzureHeaders.PARTITION_KEY, "Customize partition key")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }
}
```
> **NOTE:** When both `ServiceBusMessageHeaders.PARTITION_KEY` and `AzureHeaders.PARTITION_KEY` are set in the message headers,
> `ServiceBusMessageHeaders.PARTITION_KEY` is preferred.
**Example: Set the session id for the message**

This example demonstrates how to manually set the session id of a message in the application.

```java
public class SampleController {
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        LOGGER.info("Going to add message {} to Sinks.Many.", message);
        many.emitNext(MessageBuilder.withPayload(message)
                                    .setHeader(ServiceBusMessageHeaders.SESSION_ID, "Customize session id")
                                    .build(), Sinks.EmitFailureHandler.FAIL_FAST);
        return ResponseEntity.ok("Sent!");
    }
}
```

> **NOTE:** When the `ServiceBusMessageHeaders.SESSION_ID` is set in the message headers, and a different `ServiceBusMessageHeaders.PARTITION_KEY` (or `AzureHeaders.PARTITION_KEY`) header is also set,
> the value of the session id will eventually be used to overwrite the value of the partition key.


## Spring Integration with Azure Storage Queue

### Dependency Setup

```xml
<dependency>
	<groupId>com.azure.spring</groupId>
	<artifactId>azure-spring-integration-storage-queue</artifactId>
</dependency>
```

### Configuration

for full configurations, check appendix

### Basic Usage

### Samples

Please refer to this [sample project](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/tag_azure-spring-boot_3.6.0/storage/azure-spring-integration-sample-storage-queue) illustrating how to use Storage Queue integration.