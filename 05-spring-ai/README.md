# DIO Spring Boot - Final Project 05: Spring AI (budgeting)

## Introduction

This final module applies Spring AI in a budgeting API while preserving the same layered architecture used across the track.

The goal is to integrate AI capabilities without bypassing domain and use case boundaries.

The project demonstrates how artificial intelligence can be integrated into a traditional Spring Boot application using clean architecture principles, allowing AI features to interact with business rules through application use cases.

## Code Context

The project processes voice commands to create and query financial transactions.

Primary flow:

1. Client uploads an audio file.
2. Audio is transcribed into text using OpenAI Whisper.
3. Spring AI processes the user intention.
4. The model selects an application tool/use case.
5. The use case executes business rules.
6. Transaction data is persisted or queried.
7. The final response is converted into audio using Text-to-Speech.

## Project Structure

The application follows a layered architecture inspired by DDD and Clean Architecture.

```
src/main/java/dio/budgeting

├── domain
│   ├── Entities
│   ├── Value Objects
│   └── Repository contracts
│
├── application
│   ├── Use cases
│   └── Business operations
│
└── infrastructure
    ├── REST controllers
    ├── Persistence adapters
    ├── AI integrations
    └── External configurations
```

### Layers responsibility

- `domain`
  - Contains business rules and core models.
  - Does not depend on external frameworks.

- `application`
  - Contains use cases that coordinate business operations.
  - Used by both REST endpoints and AI tool calling.

- `infrastructure`
  - Handles external communication such as HTTP, database and AI providers.

## Module-Specific Topics

### Speech-to-text

The application uses Spring AI `TranscriptionModel` to convert audio messages into text.

Configuration:

- Provider: OpenAI
- Model: Whisper-1
- Language: Portuguese Brazilian
- Temperature: 0

The transcription prompt was customized to improve recognition of financial statements, identifying:

- Values in Brazilian Real.
- Actions such as "gastei", "paguei" and "comprei".
- Establishments and locations.

Example:

Input audio:

> "Gastei 45 reais no supermercado"

Converted information:

```
Value: R$ 45
Category: Food
Description: Supermarket expense
```

## Tool Calling

The project uses Spring AI `ChatClient` with tool calling.

Application use cases are exposed through `@Tool` methods, allowing the AI model to execute real business operations without directly accessing domain objects.

Example flow:

```
User message
      |
      v
AI Model
      |
      v
Application Tool
      |
      v
Use Case
      |
      v
Domain
```

This keeps AI integration isolated from business rules.

## Text-to-Speech

The final response can be converted into audio using Spring AI `TextToSpeechModel`.

Configuration:

- Provider: OpenAI
- Model: gpt-4o-mini-tts
- Voice: nova
- Output: MP3

The generated audio allows the API to return a complete voice interaction experience.

## Technologies Used

- Java 26
- Spring Boot 4.0.5
- Spring AI 2.0.0-M4
- OpenAI API
- Spring Data JPA
- Gradle
- Docker Compose
- MySQL

## Configuration

The application requires an OpenAI API key.

Environment variable:

```bash
export OPENAI_API_KEY="your_api_key_here"
```

Application configuration:

```properties
spring.ai.openai.chat.options.model=gpt-4o-mini

spring.ai.openai.audio.transcription.options.model=whisper-1

spring.ai.openai.audio.speech.options.model=gpt-4o-mini-tts
```

## Docker Compose

Spring Boot Docker Compose integration is enabled to automatically manage local services during development.

Configured through:

```properties
spring.docker.compose.file=docker-compose.yml
```

Before running the application, ensure Docker Desktop is running.

## How to Run

Start the application:

```bash
./gradlew bootRun
```

Run tests:

```bash
./gradlew test
```

## Challenges and Solutions

### Docker Compose integration

Problem:

The application depends on Docker services during startup.

Solution:

Configured Spring Boot Docker Compose lifecycle management and ensured Docker Desktop was running before application initialization.

### AI Provider Integration

Problem:

Connecting application layers directly with AI providers could compromise architecture.

Solution:

AI interactions were isolated inside infrastructure while using application use cases as the entry point.

### Audio Processing

Problem:

Voice commands require interpretation before reaching business logic.

Solution:

Implemented a pipeline using:

- Speech-to-text
- AI reasoning
- Tool calling
- Text-to-speech

## Spring AI Documentation

- Spring AI Reference: https://docs.spring.io/spring-ai/reference/index.html
- ChatModel API: https://docs.spring.io/spring-ai/reference/api/chatmodel.html
- ChatClient API: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- Tools API: https://docs.spring.io/spring-ai/reference/api/tools.html
- Audio Transcriptions API: https://docs.spring.io/spring-ai/reference/api/audio/transcriptions.html
- Audio Speech API: https://docs.spring.io/spring-ai/reference/api/audio/speech.html

## Shared Architecture References

Common architecture concepts are documented in the root README:

- [DDD layers](../README.md#ddd-layered-architecture)
- [Class vs record](../README.md#java-class-vs-java-record-in-domain-modeling)
- [Strong typed identifiers](../README.md#strong-typed-identifiers)
- [Repository pattern](../README.md#repository-pattern)
- [Use cases and Clean Architecture](../README.md#use-cases-and-clean-architecture)
- [Docker Compose support](../README.md#docker-compose-support-in-development)

## Notes

- Educational final project focused on Spring AI integration and architectural discipline.
- External provider integration tests require valid OpenAI credentials.
- The project demonstrates AI integration while preserving separation between infrastructure, application and domain layers.

## Author

José Antonio