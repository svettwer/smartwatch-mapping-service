# smartwatch-mapping-service
A sample Microservice with BDD and Citrus integration tests using Apache Kafka.

# System architecture overview
![system-architecture](system-architecture.png)

# Scope of this project
As the system architecture overview shows, we've a lot of system involved. Nevertheless the testing scope of this
project is limited to the integrations of the Device-Pairing Service. Therefore, we'll just simulate the surrounding
Systems of our system under test using Citrus. The other services of the architecture are not involved here. 

# Test execution
`mvn clean verify`

# Disclaimer
Even if testing should improve the code quality, the *"productive code"* of this microservice is not representative
by any means. It does not follow clean code or software architecture standards. Its only purpose is to show the
possibilities of Citrus even in complex test cases.   