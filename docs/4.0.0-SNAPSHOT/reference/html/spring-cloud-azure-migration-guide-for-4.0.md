This guide assists in the migration to **Spring Cloud Azure 4.0** from legacy Azure Spring libraries. 

We will call libraries whose group id and artifact id following pattern `com.azure.spring:spring-cloud-azure-*` the **modern libraries**,
and those with pattern `com.azure.spring:azure-spring-boot-*`, `com.azure.spring:azure-spring-cloud-*` ,
or `com.azure.spring:azure-spring-integration-*` the **legacy** ones. 

This guide will focus the side-by-side comparisons for similar configurations between the modern and legacy libraries. 

Familiarity with `com.azure.spring:azure-spring-boot-*`, `com.azure.spring:azure-spring-cloud-*`
or `com.azure.spring:azure-spring-integration-*` package is assumed. 

For those new to the Spring Cloud Azure 4.0 libraries, please refer to the [Spring Cloud Azure 4.0 Reference Doc](https://github.com/Azure/azure-sdk-for-java/wiki/Spring-Cloud-Azure-4.0-Reference) rather than this guide.

[TOC]

## Migration benefits

A natural question to ask when considering whether to adopt a new version or library is its benefits. As Azure has
matured and been embraced by a more diverse group of developers, we have been focused on learning the patterns and
practices to best support developer productivity and to understand the gaps that the Spring Cloud Azure libraries have.

There were several areas of consistent feedback expressed across the Spring Cloud Azure libraries. The most important is
that the libraries for different Azure services have not enabled the complete set of configurations. Additionally, the
inconsistency of project naming, artifact ids, versions, configurations made the learning curve steep.

To improve the development experience across Spring Cloud Azure libraries, a set of design guidelines was introduced to
ensure that Spring Cloud Azure libraries have a natural and idiomatic feel with respect to the Spring ecosystem. Further
details are available in the [design doc](https://github.com/Azure/azure-sdk-for-java/wiki/Spring-Cloud-Azure-4.0-design) for those interested.

The **Spring Cloud Azure 4.0** provides the shared experience across libraries integrating with different Spring
projects, for example Spring Boot, Spring Integration, Spring Cloud Stream, etc. The shared experience includes:

- **[placeholder]** An official name for the project?

- A unified BOM to include all Spring Cloud Azure 4.0 libraries.
- A consistent naming convention for artifacts.
- A unified way to configure credential, proxy, retry, cloud environment, and transport layer settings.

- Supporting all the authenticating methods an Azure Service or Azure Service SDK supports.

## Overview

This migration guide will be consisted of following sections:

- Naming changes for Spring Cloud Azure 4.0
- Artifact changes: renamed / added / deleted

- Configuration properties

## Naming changes

There has never been a consistent or official name to call all the Spring Cloud Azure libraries, some of them were
called `Azure Spring Boot` and some of them ` Spring on Azure` , and all these names will make developer confused. Since
4.0, we began to use the project name `Spring Cloud Azure` to represent all the Azure Spring libraries.

## BOM

We used to ship two BOMs for our libraries, the `azure-spring-boot-bom` and `azure-spring-cloud-dependencies`, but we
combined these two BOMs into one BOM since 4.0, the `spring-cloud-azure-dependencies`. Please add an entry in the
dependencyManagement of your project to benefit from the dependency management.

```xml

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>com.azure.spring</groupId>
        <artifactId>spring-cloud-azure-dependencies</artifactId>
        <version>4.0.0-beta.1</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>
```

## Artifact changes: renamed / added / deleted

Group ids are the same for modern and legacy Spring Cloud Azure libraries, they are all `com.azure.spring`. Artifact ids
for the modern Spring Cloud Azure libraries have changed. And according to which Spring project it belongs, Spring Boot,
Spring Integration or Spring Cloud Stream, the artifact ids pattern could be `spring-cloud-azure-starter-[service]`
, `spring-integration-azure-[service]` and `spring-cloud-azure-stream-binder-[service]`. The legacy starters each has an
artifact id following the pattern `azure-spring-*`. This provides a quick and accessible means to help understand, at a
glance, whether you are using modern or legacy starters.

In the process of developing Spring Cloud Azure 4.0, we renamed some artifacts to make them follow the new naming
conventions, deleted some artifacts for its functionality could be put in a more appropriate artifact, and added some
new artifacts to better serve some scenarios.

| Legacy Artifact ID                                | Modern Artifact ID                                           | Description                                                  |
| :------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| azure-spring-boot-starter                         | spring-cloud-azure-starter                                   | This artifact has been deleted with all functionality be merged into the new `spring-cloud-azure-starter` artifact. |
| azure-spring-boot-starter-active-directory        | spring-cloud-azure-starter-active-directory                  |                                                              |
| azure-spring-boot-starter-active-directory-b2c    | spring-cloud-azure-starter-active-directory-b2c              |                                                              |
| azure-spring-boot-starter-cosmos                  | spring-cloud-azure-starter-data-cosmos                           |                                                              |
| azure-spring-boot-starter-keyvault-certificates   | // TODO                                                      |                                                              |
| azure-spring-boot-starter-keyvault-secrets        | spring-cloud-azure-starter-keyvault-secrets                  |                                                              |
| azure-spring-boot-starter-servicebus-jms          | spring-cloud-azure-starter-servicebus-jms                    |                                                              |
| azure-spring-boot-starter-storage                 | spring-cloud-azure-starter-storage-blob <br/>spring-cloud-azure-starter-storage-file-share | The legacy artifact contains the functionality of both Storage Blob and File Share, it's been splited into two separate artifacts in 4.0, spring-cloud-azure-starter-storage-blob and spring-cloud-azure-starter-storage-file-share. |
| azure-spring-boot                                 | N/A                                                          | This artifact has been deleted with all functionality be merged into the new `spring-cloud-azure-autoconfigure` artifact. |
| azure-spring-cloud-autoconfigure                  | N/A                                                          | This artifact has been deleted with all functionality be merged into the new `spring-cloud-azure-autoconfigure` artifact. |
| azure-spring-cloud-context                        | N/A                                                          | This artifact has been deleted with all functionality be merged into the new `spring-cloud-azure-autoconfigure`  and `spring-cloud-azure-resourcemanager` artifacts. |
| azure-spring-cloud-messaging                      | // TODO                                                      |                                                              |
| azure-spring-cloud-starter-cache                  | N/A                                                      | This artifact has been deleted, for using redis, just add spring-boot-starter-data-redis, spring-boot-starter-cache and spring-cloud-azure-starter.                                                             |
| azure-spring-cloud-starter-eventhubs-kafka        | N/A                                                       | This artifact has been deleted, for using kafka, just add spring kafka and spring-cloud-azure-starter.                                                            |
| azure-spring-cloud-starter-eventhubs              | spring-cloud-azure-starter-integration-eventhubs             | Starter for using Azure Event Hubs Spring Integration client library                                                             |
| azure-spring-cloud-starter-servicebus             | spring-cloud-azure-starter-integration-servicebus            | Starter for using Azure Service Bus Spring Integration client library                                                             |
| azure-spring-cloud-starter-storage-queue          | spring-cloud-azure-starter-integration-storage-queue         | Starter for using Azure Storage Queue Spring Integration client library                                                             |
| azure-spring-cloud-storage                        | N/A                                                          | This artifact has been deleted with all functionalities merged into the new `spring-cloud-azure-autoconfigure` artifact. |
| azure-spring-cloud-stream-binder-eventhubs        | spring-cloud-azure-stream-binder-eventhubs                   | This artifact has been refactored using new redesign, mainly `spring-cloud-azure-stream-binder-eventhubs` and `spring-cloud-azure-stream-binder-eventhubs-core`.
| N/A                                               | spring-cloud-azure-stream-binder-eventhubs-core                   |                                                              |
| azure-spring-cloud-stream-binder-service-core  | spring-cloud-azure-stream-binder-servicebus-core             |                                                              |
| azure-spring-cloud-stream-binder-servicebus-queue | spring-cloud-azure-stream-binder-servicebus                  |  Starter for using Azure Service Bus Spring Cloud Stream Binder of both Queue and Topic                                                             |
| azure-spring-cloud-stream-binder-servicebus-topic | spring-cloud-azure-stream-binder-servicebus                  |  Starter for using Azure Service Bus Spring Cloud Stream Binder of both Queue and Topic                                                             |
| azure-spring-integration-core                     | spring-integration-azure-core                                |                                                              |
| azure-spring-integration-eventhubs                | spring-integration-azure-eventhubs                           |                                                              |
| azure-spring-integration-servicebus               | spring-integration-azure-servicebus                          |                                                              |
| azure-spring-integration-storage-queue            | spring-integration-azure-storage-queue                       |                                                              |
| N/A                                               | spring-cloud-azure-actuator                                  | Spring Cloud Azure Actuator.                                 |
| N/A                                               | spring-cloud-azure-actuator-autoconfigure                    | Spring Cloud Azure Actuator AutoConfigure.                   |
| N/A                                               | spring-cloud-azure-autoconfigure                             | Spring Cloud Azure AutoConfigure.                            |
| N/A                                               | spring-cloud-azure-core                                      |                                                              |
| N/A                                               | spring-cloud-azure-resourcemanager                           | The Core library using Azure Resource Manager to read metadata and create resources. |
| N/A                                               | spring-cloud-azure-service                                   |                                                              |
| N/A                                               | spring-cloud-azure-starter                                   | The Core Spring Cloud Azure starter, including auto-configuration support. |
| N/A                                               | spring-cloud-azure-starter-appconfiguration                  | Starter for using Azure App Configuration Client library for Java.                   |
| N/A                                               | spring-cloud-azure-starter-eventhubs                         | Starter for using Azure Event Hubs Client library for Java.                          |
| N/A                                               | spring-cloud-azure-starter-servicebus                        | Starter for using Azure Service Bus Client library for Java.                         |
| N/A                                               | spring-cloud-azure-starter-storage-blob                      | Starter for using Azure Storage Blob Client library for Java.                        |
| N/A                                               | spring-cloud-azure-starter-storage-file-share                | Starter for using Azure Storage File Share Client library for Java.                  |
| N/A                                               | spring-cloud-azure-starter-storage-queue                     | Starter for using Azure Storage Queue Client library for Java.                       |
| N/A                                               | spring-cloud-azure-starter-stream-eventhubs                  | Starter for using Azure Event Hubs Spring Cloud Stream Binder                                                             |
| N/A                                               | spring-cloud-azure-starter-stream-servicebus                 | Starter for using Azure Service Bus Spring Cloud Stream Binder                                                             |
| N/A                                               | spring-cloud-azure-starter-cosmos                | Starter for using Azure Cosmos Client library for Java                                                           |


## Dependencies changes

Some unnecessary dependencies were included in the legacy artifacts, which we have removed in the modern Spring Cloud
Azure 4.0 libraries. Please make sure add the removed dependencies manually to your project to prevent unintentionally
crash.

### spring-cloud-azure-starter

| Removed dependencies                                    | Description                                                  |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| org.springframework.boot:spring-boot-starter-validation | Please include the validation starter if you want to use the Hibernate Validator. |

### spring-cloud-azure-starter-active-directory

| Removed dependencies                                    | Description                                                  |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| org.springframework.boot:spring-boot-starter-validation | Please include the validation starter if you want to use the Hibernate Validator. |

### spring-cloud-azure-starter-active-directory-b2c

| Removed dependencies                                    | Description                                                  |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| org.springframework.boot:spring-boot-starter-validation | Please include the validation starter if you want to use the Hibernate Validator. |

## Authentication

Spring Cloud Azure 4.0 supports all the authentication methods each Azure Service SDK supports. It allows configuring a
global token credential as well as providing the token credential at each service level. But credential is not required
to configure in Spring Cloud Azure 4.0, it can leverage the credential stored in a local developing environment, or
managed identity in Azure Services, just make sure the principal has been granted sufficient permission to access the
target Azure resources.

A chained credential, the [DefaultAzureCredential](https://docs.microsoft.com/en-us/java/api/overview/azure/identity-readme?view=azure-java-stable#defaultazurecredential) bean is auto-configured by default and will be used by all components if no more authentication information is specified.
> Warning: There could be some `ERROR` logs be printed out while the `DefaultAzureCredential` running the chain and trying to find the first available credential. It doesn't mean the `DefaultAzureCredential` is broken or unavailable. Meanwhile, we'll keep improving this logging experience.


## Configuration properties

### Global configurations

The modern `spring-cloud-azure-starter` allows developers to define properties that apply to all Azure SDKs in the
namespace `spring.cloud.azure`. It was not supported in the legacy `azure-spring-boot-starter`. The global
configurations can be divided into five categories:

| Prefix                        | Description                                                  |
| ----------------------------- | ------------------------------------------------------------ |
| spring.cloud.azure.client     | To configure the transport clients underneath each Azure SDK. |
| spring.cloud.azure.credential | To configure how to authenticate with Azure Active Directory. |
| spring.cloud.azure.profile    | To configure the Azure cloud environment.                    |
| spring.cloud.azure.proxy      | To configure the proxy options apply to all Azure SDK clients. |
| spring.cloud.azure.retry      | To configure the retry options apply to all Azure SDK clients. |

Check [here](https://github.com/Azure/azure-sdk-for-java/wiki/Spring-Cloud-Azure-4.0-Reference#a-configuration-properties) for a full list of configurations.

### Each SDK configurations

#### spring-cloud-azure-starter-integration-eventhubs  
> (legacy: azure-spring-cloud-starter-eventhubs)
- As per prefix of `spring.cloud.azure.`  

prefix changed from
`spring.cloud.azure.eventhub.`
to
`spring.cloud.azure.eventhubs.`

Changes for the child entries for this prefix, please refer the following tables: 

|  Legacy | Modern Spring Cloud Azure 4.0
 |:---|:---
|`checkpoint-storage-account`|`processor.checkpoint-store.account-name`
|`checkpoint-access-key`|`processor.checkpoint-store.account-key`
|`checkpoint-container`|`processor.checkpoint-store.container-name`

For example, change from:
```yaml
spring:
  cloud:
    azure:
      eventhub:
        connection-string: [eventhub-namespace-connection-string]
        checkpoint-storage-account: [checkpoint-storage-account]
        checkpoint-access-key: [checkpoint-access-key]
        checkpoint-container: [checkpoint-container]
```
to:
```yaml
spring:
  cloud:
    azure:
      eventhubs:
        connection-string: [eventhub-namespace-connection-string]
        processor:
          checkpoint-store:
            container-name: [checkpoint-container]
            account-name: [checkpoint-storage-account]
            account-key: [checkpoint-access-key]
```

#### spring-cloud-azure-stream-binder-eventhubs
> (legacy: azure-spring-cloud-stream-binder-eventhubs)
- As per prefix of `spring.cloud.azure.`  
  [please refer the above section](#azure-spring-cloud-starter-eventhubs)

- As per prefix of `spring.cloud.stream.binders.<eventhub-name>.environment.spring.cloud.azure`:  
prefix change from:  
`spring.cloud.stream.binders.<eventhub-name>.environment.spring.cloud.azure.eventhub`   
to:  
`spring.cloud.stream.binders.<eventhub-name>.environment.spring.cloud.azure.eventhubs`

- As per prefix of `spring.cloud.stream.eventhub`:  
    prefix changed from  
    `spring.cloud.stream.eventhub.bindings.<binding-name>.`  
    to  
    `spring.cloud.stream.eventhubs.bindings.<binding-name>.`

Changes for the child entries for following prefix, please refer the following table:    

|  Legacy | Modern Spring Cloud Azure 4.0
|:---|:---
|`consumer.max-batch-size` | `consumer.batch.max-size`
|`consumer.max-wait-time`|`consumer.batch.max-wait-time`
|`consumer.checkpoint-mode`|`consumer.checkpoint.mode`
|`consumer.checkpoint-count`|`consumer.checkpoint.count`
|`consumer.checkpoint-interval`|`consumer.checkpoint.interval`

For example, you should change from:
```yaml
spring:
  cloud:
    stream:
      eventhub:
        bindings:
            <binding-name>:
                consumer:
                  max-batch-size: [max-batch-size]
                  max-wait-time: [max-wait-time]
                  checkpoint-mode: [check-point-mode]
                  checkpoint-count: [checkpoint-count]
                  checkpoint-interval: [checkpoint-interval]

```
to:
```yaml
spring:
  cloud:
    stream:
      eventhubs:
        bindings:
            <binding-name>:
                consumer:
                  batch:
                    max-size: [max-batch-size]
                    max-wait-time: [max-wait-time]
                  checkpoint:
                    mode: [check-point-mode]
                    count: [checkpoint-count]
                    interval: [checkpoint-interval]
```
 
#### spring-cloud-azure-starter-integration-servicebus
> (legacy: azure-spring-cloud-starter-servicebus)
For all configuration options supported in spring-cloud-azure-starter-integration-servicebus & spring-integration-azure-servicebus libraries,
the prefix remains to be as `spring.cloud.azure.servicebus.`.

- Renamed configuration options

| Legacy configuration suffix value| Current configuration suffix value| Current type
|:---|:---|:---
|`transport-type`|`client.transport-type`|AmqpTransportType
|`retry-options.max-retries`|`retry.max-attempts`|Integer
|`retry-options.delay`|`retry.delay`|Duration
|`retry-options.max-delay`|`retry.max-delay`|Duration
|`retry-options.try-timeout`|`retry.timeout`|Duration
|`retry-options.retry-mode`|Dropped, will be configured according to `retry.backoff.multiplier`|NA

#### spring-cloud-azure-starter-active-directory
1. All configuration property names changed the prefix from `azure.activedirectory.` to `spring.cloud.azure.active-directory.`.
2. New property `spring.cloud.azure.active-directory.enabled=true` is necessary to enable related features.

#### spring-cloud-azure-starter-active-directory.b2c
1. All configuration property names changed the prefix from `azure.activedirectory.b2c.` to `spring.cloud.azure.active-directory.b2c.`.
2. New property `spring.cloud.azure.active-directory.b2c.enabled=true` is necessary to enable related features.

#### From `azure-spring-boot-starter-cosmos` to `spring-cloud-azure-starter-data-cosmos`
1. All configuration property names changed the prefix from `azure.cosmos` to `spring.cloud.azure.cosmos`.

| Legacy properties                 | Morden properties                              | 
|-----------------------------------|------------------------------------------------|
| azure.cosmos.uri                  | spring.cloud.azure.cosmos.endpoint             |
| azure.cosmos.key                  | spring.cloud.azure.cosmos.key                  |
| azure.cosmos.database             | spring.cloud.azure.cosmos.database             |
| azure.cosmos.populateQueryMetrics | spring.cloud.azure.cosmos.populateQueryMetrics |


#### From **azure-spring-boot-starter-storage** to **spring-cloud-azure-starter-storage-blob**
1. All configuration property names changed the prefix from `azure.storage` to `spring.cloud.azure.storage.blob`.

| Legacy properties           | Morden properties                            | 
|-----------------------------|----------------------------------------------|
| azure.storage.account-name  | spring.cloud.azure.storage.blob.account-name |
| azure.storage.account-key   | spring.cloud.azure.storage.blob.account-key  |
| azure.storage.blob-endpoint | spring.cloud.azure.storage.blob.endpoint     |

#### From **azure-spring-boot-starter-storage** to **spring-cloud-azure-starter-storage-file-share**
 All configuration property names changed the prefix from `azure.storage` to `spring.cloud.azure.storage.fileshare`.

| Legacy properties           | Morden properties                                 | 
|-----------------------------|---------------------------------------------------|
| azure.storage.account-name  | spring.cloud.azure.storage.fileshare.account-name |
| azure.storage.account-key   | spring.cloud.azure.storage.fileshare.account-key  |
| azure.storage.file-endpoint | spring.cloud.azure.storage.fileshare.endpoint     |
   

#### From **azure-spring-cloud-starter-storage-queue** to **spring-cloud-azure-starter-integration-storage-queue**
All configuration property names changed the prefix from `spring.cloud.azure.storage` to `spring.cloud.azure.storage.queue`.

| Legacy properties                         | Morden properties                                        |
|-------------------------------------------|----------------------------------------------------------|
| spring.cloud.azure.storage.account        | spring.cloud.azure.storage.queue.account-name            |
| spring.cloud.azure.storage.access-key     | spring.cloud.azure.storage.queue.account-key             |
| spring.cloud.azure.storage.resource-group | spring.cloud.azure.storage.queue.resource.resource-group |


#### spring-cloud-azure-stream-binder-servicebus
> (legacy: azure-spring-cloud-stream-binder-servicebus-queue,azure-spring-cloud-stream-binder-servicebus-topic)   

We have merged these two libraries into one, which supports both topic and queue.And the binder type is combined as `servicebus`.

- New configuration properties

|  Modern Spring Cloud Azure 4.0 | description
|:---|:---
| `spring.cloud.stream.servicebus.bindings.{channel}.producer.entity-type`|`If you use the sending function, you need to set the entity-type, which can be set to topic or queue. ` |

- Properties Configuration Changed

|  Legacy | Modern Spring Cloud Azure 4.0
|:---|:---
|`spring.cloud.stream.servicebus.queue.bindings.*` | `spring.cloud.stream.servicebus.bindings.*`
|`spring.cloud.stream.servicebus.topic.bindings.*` | `spring.cloud.stream.servicebus.bindings.*`

please note: the binder type is renamed from: servicebus-queue/servicebus-topic to `servicebus`.

- If you use the Spring Cloud Stream binder for Azure Service Bus queue/topic，now your properties configuration should be changed to:

```yaml
spring:
  cloud:
    azure:
      servicebus:
        connection-string: [servicebus-namespace-connection-string]
    stream:
      function:
        definition: consume;supply
      bindings:
        consume-in-0:
          destination: [servicebus-queue-name]
        supply-out-0:
          destination: [servicebus-queue-name-same-as-above]
      servicebus:
        bindings:
          consume-in-0:
            consumer:
              checkpoint-mode: MANUAL
          supply-out-0:
            producer:
              entity-type: queue#topic

```
 
- If you use the Spring Cloud Stream Binder for multiple Azure Service Bus namespaces,now your properties configuration should be changed to:

```yaml
spring:
  cloud:
    stream:
      function:
        definition: queueConsume;queueSupply;topicConsume;topicSupply;
      bindings:
        topicConsume-in-0:
          destination: [ servicebus-topic-1-name ]
          group: [ topic-subscription-name ]
        topicSupply-out-0:
          destination: [ servicebus-topic-1-name ]
        queueConsume-in-0:
          binder: servicebus-2
          destination: [ servicebus-queue-1-name ]
        queueSupply-out-0:
          binder: servicebus-2
          destination: [ servicebus-queue-1-name ]
      binders:
        servicebus-1:
          type: servicebus
          default-candidate: true
          environment:
            spring:
              cloud:
                azure:
                  servicebus:
                    connection-string: [ servicebus-namespace-1-connection-string ]
        servicebus-2:
          type: servicebus
          default-candidate: false
          environment:
            spring:
              cloud:
                azure:
                  servicebus:
                    connection-string: [ servicebus-namespace-2-connection-string ]
      servicebus:
        bindings:
          topicSupply-out-0:
            producer:
              entity-type: topic
          queueSupply-out-0:
            producer:
              entity-type: queue
      poller:
        initial-delay: 0
        fixed-delay: 1000
```


## API breaking changes

### `spring-cloud-azure-starter-integration-eventhubs(legacy: azure-spring-cloud-starter-eventhubs)` & `spring-cloud-azure-stream-binder-eventhubs(legacy: azure-spring-cloud-stream-binder-eventhubs)`

- Drop `EventHubOperation`, with its functionalities divided into `EventHubsTemplate` and `EventHubsProcessorContainer`.
- Drop `EventHubsInboundChannelAdapter` and use new named `EventHubsInboundChannelAdapter`, also changed its 
  constructor signature.
- Change `CheckpointConfig` instantiation style to simple constructor instead of build style.
> Notes:  
> In accordance with the `'Azure Event Hubs'` service name, class `EventHubInboundChannelAdapter` is renamed
> to `EventHubsInboundChannelAdapter` and `'eventhub'` configuration entry is renamed to `'eventhubs'`, both 
> appending one **'s'** character after 'eventhub', you can see this difference in the other parts in this document.

EventHubsInboundChannelAdapter usage for eventhubs integration:  
Legacy code:
```java
@Bean
    public EventHubInboundChannelAdapter messageChannelAdapter(
        @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel, EventHubOperation eventhubOperation) {
        eventhubOperation.setCheckpointConfig(CheckpointConfig.builder().checkpointMode(CheckpointMode.MANUAL).build());
        EventHubInboundChannelAdapter adapter = new EventHubInboundChannelAdapter(EVENTHUB_NAME,
            eventhubOperation, CONSUMER_GROUP);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }
```

Modern code:
```java
    @Bean
    public EventHubsInboundChannelAdapter messageChannelAdapter(
        @Qualifier(INPUT_CHANNEL) MessageChannel inputChannel,
        EventHubsProcessorContainer processorContainer) {
        CheckpointConfig config = new CheckpointConfig(CheckpointMode.MANUAL);

        EventHubsInboundChannelAdapter adapter =
                new EventHubsInboundChannelAdapter(processorContainer, EVENTHUB_NAME,
              CONSUMER_GROUP, config);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }
```

And also EventHubOperation usage:

Legacy code:

```java
 @Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL)
    public MessageHandler messageSender(EventHubOperation queueOperation) {
        DefaultMessageHandler handler = new DefaultMessageHandler(EVENTHUB_NAME, queueOperation);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully.");
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("There was an error sending the message.", ex);
            }
        });

        return handler;
    }
```

Modern code:

```java
@Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL)
    public MessageHandler messageSender(EventHubsTemplate queueOperation) {
        DefaultMessageHandler handler = new DefaultMessageHandler(EVENTHUB_NAME, queueOperation);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully.");
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("There was an error sending the message.", ex);
            }
        });

        return handler;
    }
```
- Package path changes:

| Legacy class                                  | Modern class                                                  |
| ------------------------------------------------------- | ------------------------------------------------------------ |
| com.azure.spring.integration.core.EventHubsHeaders | com.azure.spring.eventhubs.support.EventHubsHeaders |
| com.azure.spring.integration.core.AzureHeaders.CHECKPOINTER | com.azure.spring.messaging.AzureHeaders.CHECKPOINTER |
| com.azure.spring.integration.core.api.reactor.Checkpointer | com.azure.spring.messaging.checkpoint.Checkpointer |
| com.azure.spring.integration.core.api.CheckpointConfig | com.azure.spring.messaging.checkpoint.CheckpointConfig |
| com.azure.spring.integration.core.api.CheckpointMode | com.azure.spring.messaging.checkpoint.CheckpointMode
| com.azure.spring.integration.core.api.reactor.DefaultMessageHandler | com.azure.spring.integration.handler.DefaultMessageHandler |
| com.azure.spring.integration.eventhub.inbound.EventHubInboundChannelAdapter | com.azure.spring.integration.eventhubs.inbound.EventHubsInboundChannelAdapter |
| com.azure.spring.integration.eventhub.api.EventHubOperation | com.azure.spring.eventhubs.core.EventHubsTemplate |
| NONE                                                        | com.azure.spring.eventhubs.core.EventHubsProcessorContainer |

 

### spring-cloud-azure-stream-binder-servicebus
| Legacy class                                                | Modern class                                   |
| ------------------------------------------------------------| -----------------------------------------------------|
| com.azure.spring.integration.core.api.Checkpointer          | com.azure.spring.messaging.checkpoint.Checkpointer   |
| com.azure.spring.integration.core.AzureHeaders | com.azure.spring.messaging.AzureHeaders |
| com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders|com.azure.spring.servicebus.support.ServiceBusMessageHeaders|


### spring-cloud-azure-starter-integration-servicebus
> (legacy: azure-spring-cloud-starter-servicebus)
 - Combine the original `ServiceBusQueueTemplate#sendAsync` and `ServiceBusTopicTemplate#sendAsync` as `ServiceBusTemplate#sendAsync`
 and drop class of `ServiceBusQueueTemplate` and `ServiceBusTopicTemplate`.
 - Drop RxJava and CompletableFuture support of `ServiceBusTemplate` and support Reactor instead.
 - Drop interface of `ServiceBusQueueOperation` and `ServiceBusTopicOperation`.
 - Drop API of `ServiceBusQueueOperation#abandon` and `ServiceBusQueueOperation#deadletter`.
 - Combine the original `ServiceBusQueueTemplate#subscribe` and `ServiceBusTopicTemplate#subscribe` as `ServiceBusProcessorClient#subscribe`.
 - Deprecate the interface of `SubscribeOperation`.
 - Add new API of `setDefaultEntityType` for `ServiceBusTemplate`, the default entity type of a ServiceBusTemplate is required when no bean of `PropertiesSupplier<String, ProducerProperties>` is provided
for the `ProducerProperties#entityType`.
 - Drop class of `ServiceBusQueueInboundChannelAdapter` and `ServiceBusTopicInboundChannelAdapter` and combine them as `ServiceBusInboundChannelAdapter`.
 - Package path changes:

| Legacy class                                                                        | Modern class                                                                                                 |
| ----------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| com.azure.spring.integration.core.DefaultMessageHandler                             | com.azure.spring.integration.handler.DefaultMessageHandler                |
| com.azure.spring.integration.servicebus.ServiceBusTemplate                          | com.azure.spring.servicebus.core.ServiceBusTemplate|
| com.azure.spring.integration.servicebus.inbound.ServiceBusQueueInboundChannelAdapter                          | com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter|
| com.azure.spring.integration.servicebus.inbound.ServiceBusTopicInboundChannelAdapter                          | com.azure.spring.integration.servicebus.inbound.ServiceBusInboundChannelAdapter|

 Legacy code:

```java
    @Bean
    public ServiceBusQueueInboundChannelAdapter queueMessageChannelAdapter(
        @Qualifier("INPUT_CHANNEL_NAME") MessageChannel inputChannel, ServiceBusQueueOperation queueOperation) {
        queueOperation.setCheckpointConfig(CheckpointConfig.builder().checkpointMode(CheckpointMode.MANUAL).build());
        ServiceBusQueueInboundChannelAdapter adapter = new ServiceBusQueueInboundChannelAdapter("QUEUE_NAME",
            queueOperation);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean
    public ServiceBusTopicInboundChannelAdapter topicMessageChannelAdapter(
        @Qualifier("INPUT_CHANNEL_NAME") MessageChannel inputChannel, ServiceBusTopicOperation topicOperation) {
        topicOperation.setCheckpointConfig(CheckpointConfig.builder().checkpointMode(CheckpointMode.MANUAL).build());
        ServiceBusTopicInboundChannelAdapter adapter = new ServiceBusTopicInboundChannelAdapter("TOPIC_NAME",
            topicOperation, "SUBSCRIPTION_NAME");
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "OUTPUT_CHANNEL_NAME")
    public MessageHandler queueMessageSender(ServiceBusQueueOperation queueOperation) {
        DefaultMessageHandler handler = new DefaultMessageHandler("QUEUE_NAME", queueOperation);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully.");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("There was an error sending the message.");
            }
        });
        return handler;
    }
```

Modern code:

```java
    public ServiceBusInboundChannelAdapter queueMessageChannelAdapter(
        @Qualifier("INPUT_CHANNEL_NAME") MessageChannel inputChannel, ServiceBusProcessorContainer processorContainer) {
        ServiceBusInboundChannelAdapter adapter = new ServiceBusInboundChannelAdapter(processorContainer, "QUEUE_NAME",
            new CheckpointConfig(CheckpointMode.MANUAL));
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "OUTPUT_CHANNEL_NAME")
    public MessageHandler queueMessageSender(ServiceBusTemplate serviceBusTemplate) {
        serviceBusTemplate.setDefaultEntityType(ServiceBusEntityType.QUEUE);
        DefaultMessageHandler handler = new DefaultMessageHandler("QUEUE_NAME", serviceBusTemplate);
        handler.setSendCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("Message was sent successfully for {}.", "QUEUE_NAME);
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("There was an error sending the message.");
            }
        });

        return handler;
    }

```

### spring-cloud-azure-starter-active-directory
| Legacy class                                                                        | Modern class                                                                                                 |
| ----------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter           | com.azure.spring.cloud.autoconfigure.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter                |
| com.azure.spring.aad.webapp.AADWebSecurityConfigurerAdapter                         | com.azure.spring.cloud.autoconfigure.aad.webapp.AADWebSecurityConfigurerAdapter                              |
| com.azure.spring.autoconfigure.aad.AADAppRoleStatelessAuthenticationFilter          | com.azure.spring.cloud.autoconfigure.aad.filter.AADAppRoleStatelessAuthenticationFilter                      |
| com.azure.spring.autoconfigure.aad.AADAuthenticationFilter                          | com.azure.spring.cloud.autoconfigure.aad.filter.AADAuthenticationFilter                                      |
| com.azure.spring.autoconfigure.aad.AADAuthenticationProperties                      | com.azure.spring.cloud.autoconfigure.aad.properties.AADAuthenticationProperties                              |
| com.azure.spring.autoconfigure.aad.Membership                                       | com.azure.spring.cloud.autoconfigure.aad.graph.Membership                                                    |
| com.azure.spring.autoconfigure.aad.UserPrincipal                                    | com.azure.spring.cloud.autoconfigure.aad.filter.UserPrincipal                                                |

### spring-cloud-azure-starter-active-directory-b2c
| Legacy class                                                                        | Modern class                                                                                                 |
| ----------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| com.azure.spring.autoconfigure.b2c.AADB2CJwtBearerTokenAuthenticationConverter      | com.azure.spring.cloud.autoconfigure.aad.b2c.AADB2CJwtBearerTokenAuthenticationConverter                     |
| com.azure.spring.autoconfigure.b2c.AADB2COidcLoginConfigurer                        | com.azure.spring.cloud.autoconfigure.aad.b2c.AADB2COidcLoginConfigurer                                       |


### spring-cloud-azure-starter-integration-storage-queue  
| Legacy class                                                        | Modern class                                               |
|---------------------------------------------------------------------|------------------------------------------------------------|
| com.azure.spring.integration.core.api.CheckpointMode                | com.azure.spring.messaging.checkpoint.CheckpointMode       |
| com.azure.spring.integration.core.api.reactor.Checkpointer          | com.azure.spring.messaging.checkpoint.Checkpointer         |
| com.azure.spring.integration.core.api.reactor.DefaultMessageHandler | com.azure.spring.integration.handler.DefaultMessageHandler |
| com.azure.spring.integration.core.AzureHeaders                      | com.azure.spring.messaging.AzureHeaders                    |
| com.azure.spring.integration.storage.queue.StorageQueueOperation    | com.azure.spring.storage.queue.core.StorageQueueOperation  |





