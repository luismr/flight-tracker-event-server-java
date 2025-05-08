# WebSocket Notification Scalability

## Context
The **Flight Tracker** project provides users with real-time flight information via **WebSocket** notifications. Currently, the application maintains active WebSocket connections and sends updates directly. As the number of users and simultaneous events increases, the current solution is reaching **scalability limits**, primarily due to in-memory session storage and sticky session dependencies on the load balancer.

## Objective
This document aims to analyze architectural alternatives to scale WebSocket notification delivery while maintaining low latency, high reliability, and minimal disruption to the existing system. The goal is to find an evolutionary solution with fast time-to-market and a path toward future scalability.

## Requirements

### Non-functional Requirements
- Scalability to thousands of simultaneous WebSocket connections
- Low delivery latency (< 100ms)
- High availability and fault tolerance
- Reliable message delivery
- Minimal frontend impact (initially)

### Technical Requirements
- Integration with existing Kafka infrastructure
- Support for component decoupling
- Compatibility with WebSocket and STOMP
- Easy monitoring and operations

## Considered Alternatives

### 1. Kafka + ThreadExecutors
Decouple event dispatching via Kafka and parallelize delivery using thread pools. No frontend changes required.

**Pros:** scalable, no changes to the client, uses existing Kafka  
**Cons:** requires concurrency implementation and session control

### 2. STOMP Broker (RabbitMQ/ActiveMQ)
Use a STOMP-compatible message broker as an external relay. Frontends subscribe to STOMP topics via WebSocket.

**Pros:** complete decoupling, mature pub/sub model  
**Cons:** requires frontend refactoring and broker setup

### 3. Redis Streams/PubSub
Use Redis for message publishing/subscribing or streams. Messages are distributed across WebSocket server instances.

**Pros:** simple, fast, great for low latency  
**Cons:** pub/sub doesn't guarantee delivery; streams require additional handling

### 4. Optimizing the Current Architecture
Local improvements with thread pools, async I/O, or sticky session-based load balancing.

**Pros:** low cost, fast implementation  
**Cons:** doesn't solve horizontal scalability limitations

## Comparative Analysis

| Solution                  | Scalability | Latency | Complexity | Reliability | Frontend Impact |
|---------------------------|-------------|---------|------------|-------------|------------------|
| Kafka + Threads           | High        | Medium  | Medium     | High        | None             |
| STOMP Broker              | High        | Low     | High       | High        | High             |
| Redis Streams/PubSub      | Medium      | Very Low| Medium     | Medium/High | None             |
| Local Optimization        | Low         | Low     | Low        | Low         | None             |

## Recommended Conclusion

We recommend an **evolutionary approach in two phases**:

### Phase 1: Refactoring with Kafka + Dedicated WebSocket Component

Refactor the `PingEventPublisher` to publish events to a Kafka topic. A new (or existing) component will consume the topic and handle active WebSocket session management and message delivery.

**Benefits:**
- Fast time-to-market
- Immediate scalability using current infrastructure
- No frontend changes
- Improves modularity and observability

### Phase 2: Evolve to STOMP Broker

If scalability needs increase significantly, migrate to a **STOMP-based broker architecture** (RabbitMQ or ActiveMQ), enabling:
- Topic-based subscriptions
- Automatic message distribution by the broker
- Event-driven backend/frontend

This phase requires more effort and frontend changes, so it's reserved for future growth that justifies the investment.

### Why this phased approach?

- **Time-to-market**: quick delivery with low risk
- **Low disruption**: avoids major changes to frontend/backend for now
- **Preparation**: creates a foundation for future pub/sub migration

This shows how **architecture can evolve with minimal impact** while aligning with team capacity and business context.

## References

- [ADR 001 – WebSocket Scalability Strategy](../adrs/adr-001-websocket-scalability.md)
- [ByteWise010, *“Scaling WebSockets with STOMP and RabbitMQ”*](https://medium.com/@bytewise010/scaling-websocket-messaging-with-spring-boot-e9877c80f027)
- Internal Kafka and Redis benchmark experiences
