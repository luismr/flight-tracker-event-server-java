# ADR 001 - WebSocket Notification Scalability Strategy

## Context
The `flight-tracker-event-server` currently sends flight updates directly to clients over WebSocket. Sessions are kept in memory, which limits horizontal scalability, requires sticky sessions in the load balancer, and makes managing thousands of connections inefficient.

## Decision
Refactor the application so that the `PingEventPublisher` publishes flight events to a Kafka topic. A new dedicated component will consume these events and manage active WebSocket sessions for message delivery.

This decision enables separation of responsibilities and prepares the system for a future migration to a STOMP-based architecture (e.g., RabbitMQ) if scale demands increase.

## Justification
- **Time-to-market**: quick delivery without frontend changes
- **Decoupling**: separates event generation from delivery logic
- **Reuses existing infrastructure**: Kafka is already in place
- **Low impact**: avoids protocol changes or client modifications for now

## Alternatives Considered
- **STOMP Broker with RabbitMQ**: powerful but requires client refactor and more setup
- **Redis Streams/PubSub**: simple, fast, but with delivery and clustering limitations
- **Optimizing the current implementation**: fast but not future-proof

## Consequences
- The system becomes modular and more scalable
- Kafka enables better backpressure and failover handling
- Prepares for a transition to STOMP when scale justifies it
- A new WebSocket delivery component must be monitored closely

## Links
- [Technical Analysis – WebSocket Scalability](../analysis/technical-analysis-websocket-flight-tracker.md)
