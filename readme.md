# Customer Service

The objective of this service is to validate if a client exists and is active, to determine if it has contracted phishing detection to indicate that it is available.
The solution is based in request reply using rabbitMQ:

![image](https://github.com/user-attachments/assets/8b83cf7b-0d9b-4efd-aaf5-04286e479e8d)






## Getting Started

### Dependencies

- Java 17
- Maven 3.9.+
- Docker & Docker Componse

### Run

- Execute the docker compose command for start up the components
    - The path base is: /deployment/docker-compose.yml
        ```
        > docker-compose up
        ```

- The docker-compose should create two containers:
    - A postgres container with a testing DB with customers table
    - A rabbitmq container with a testing exchange with two queues.

- Start manually or via command the Application
    ```
    > mvn clean package
    > java -jar target/customer-0.0.1-SNAPSHOT.jar
    ```

### Test

For test the service, go to http://localhost:15672/#/queues, select the *testing.customer.query.is-customer-available* queue, and publish the follow message:
```
{
    "customer": "CUSTOMER1"
}
```

This message should be processed for the service, and produce a new event message to *testing.customer.response.is-customer-available* queue:
```
{
    "customer": "CUSTOMER1",
    "isAvailable": true
}
```
for consult the customer data, connect to postgres DB using the credentials in *customer/src/main/resources/application.properties* and execute:

```
    select * from customers;
```
result:

```
    +--+---------+------+------------------+
    |id|name     |status|phishing_detection|
    +--+---------+------+------------------+
    | 1|CUSTOMER1|true  |true              |
    | 2|CUSTOMER2|true  |false             |
    | 3|CUSTOMER3|false |true              |
    | 4|CUSTOMER4|false |false             |
    +--+---------+------+------------------+
```

### Automation Test

For run the automation suite on your local, follow these steps:
```
Clone the repository.
Create a global environment variable, with name: RABBIT_PWD and value: the password of the RabbitMQ user.
Go to the root of the project and run the testng.xml file.
```
This will run all scenarios automated on Messages.feature, and a html report will be generated on target/Customer-Availability.html
