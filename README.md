# Model Driven CQRS Proof of Concept Application with Pekko Finite State Machine, Kafka, Cassandra, Elasticsearch
[![LICENSE]](https://github.com/mehmetsalgar/Akka-Finite-State-Machine/edit/master/LICENSE)
## Features
- Event Sourcing / Command Query Responsibility Segregation Pattern Implementation
- Command part with Apache Kafka, Pekko Finite State Machine, Apache Cassandra
- Query part with Pekko Projection, Elasticsearch
- Code Generation with Eclipse XText, Xtend from Eclipse Papyrus UML Diagrams
- Spring Boot Application
- Docker / Helm packaging for deployment to Kubernetes

## Business Case
- Bank Credit Application Workflow implementation with Four Eyes Proofing principle

## Target Use Cases
- Credit Applications from Credit Tenants
- Approval from Relationship Manager
- Approval from Sales Manager
- Inquiries for Credit Tenants credit scores
- Inquiries for Fraud Prevention results
- Inquiries for Address Check
- Approval from Credit Analyst

## Detail Explanations
You can find the full story of the Proof of Concept in the following [Blog](https://mehmetsalgar.wordpress.com/2022/04/18/a-model-driven-event-sourced-cloud-ready-application-with-akka-finite-state-machine-using-kafka-cassandra-and-elasticsearch/) post.
