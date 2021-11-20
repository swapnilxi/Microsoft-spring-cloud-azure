# Spring Cloud Azure Configuration

Most of Azure Service SDKs could divide into two categories by transport type, HTTP-based and AMQP-based. There are properties that are common to all SDKs such as authentication principals and Azure environment settings. Or common to all HTTP-based clients, such as setting logging level to log http requests and responses. In Spring Cloud Azure 4.0 we added five common categories of configuration properties, which could be specified to each Azure service.

| Prefix                                           | Description                                                  |
| ------------------------------------------------ | ------------------------------------------------------------ |
| spring.cloud.azure.*\<azure-service>*.client     | To configure the transport clients underneath one Azure service SDK. |
| spring.cloud.azure.*\<azure-service>*.credential | To configure how to authenticate with Azure Active Directory for one Azure service SDK.. |
| spring.cloud.azure.*\<azure-service>*.profile    | To configure the Azure cloud environment one Azure service SDK. |
| spring.cloud.azure.*\<azure-service>*.proxy      | To configure the proxy options for one Azure service SDK.    |
| spring.cloud.azure.*\<azure-service>*.retry      | To configure the retry options apply to one Azure service SDK. |

There are some properties that could be shared among different Azure services, for example using the same service principal to access Azure Cosmos DB and Azure Event Hubs. Spring Cloud Azure 4.0 allows developers to define properties that apply to all Azure SDKs in the namespace `spring.cloud.azure`.

| Prefix                        | Description                                                  |
| ----------------------------- | ------------------------------------------------------------ |
| spring.cloud.azure.client     | To configure the transport clients apply to all Azure SDKs by default. |
| spring.cloud.azure.credential | To configure how to authenticate with Azure Active Directory for all Azure SDKs by default. |
| spring.cloud.azure.profile    | To configure the Azure cloud environment for all Azure SDKs by default. |
| spring.cloud.azure.proxy      | To configure the proxy options apply to all Azure SDK clients by default. |
| spring.cloud.azure.retry      | To configure the retry options apply to all Azure SDK clients by default. |

Please note that properties configured under each Azure service will override the global configurations.

The configuration properties' prefixes have been unified to `spring.cloud.azure` namespace since Spring Cloud Azure 4.0, it will make configuration properties more consistent and configuring it in a more intuitive way. Here's a quick review of the prefixes of supported Azure services.

| Azure Service               | Configuration Property Prefix           |
| --------------------------- | --------------------------------------- |
| Azure App Configuration     | spring.cloud.azure.appconfiguration     |
| Azure Cosmos DB             | spring.cloud.azure.cosmos               |
| Azure Event Hubs            | spring.cloud.azure.eventhubs            |
| Azure Key Vault Certificate | spring.cloud.azure.keyvault.certificate |
| Azure Key Vault Secret      | spring.cloud.azure.keyvault.secret      |
| Azure Service Bus           | spring.cloud.azure.servicebus           |
| Azure Storage Blob          | spring.cloud.azure.storage.blob         |
| Azure Storage File Share    | spring.cloud.azure.storage.fileshare    |
| Azure Storage Queue         | spring.cloud.azure.storage.queue        |