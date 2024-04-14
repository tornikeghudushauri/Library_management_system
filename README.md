# Library Management System (LMS)

Welcome to the Library Management System (LMS) project! This system aims to streamline the management of library resources, including books, users, borrowing, and returning.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)

## Features

- **Book Management**: Add, update, delete, and search for books in the library.
- **User Management**: Manage library users, including registration and authentication.
- **Borrowing and Returning**: Allow users to borrow and return books.
- **Rate Limit Protection**: Implement rate limiting to prevent abuse and ensure fair access to resources.
- **Messaging**: Utilize messaging systems for events like book borrowing and returning.

## Technologies Used

- **Spring Boot**: Backend framework for building robust Java applications.
- **Spring Security**: Authentication and authorization framework for securing the application.
- **Spring Data JPA**: Simplifies database access and manipulation.
- **Swagger**: API documentation tool for easy exploration and usage of endpoints.
- **Java Messaging Service (JMS)**: Spring jms and ActiveMQ for implementing asynchronous communication.
- **Rate Limit Protection**: Implemented using spring components (AOP). rate limiting prevents abuse and ensures fair access.
- **Lombok**: Library for reducing boilerplate code in Java classes.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven
- ActiveMQ

### Installation

1. Clone the repository.

2. Navigate to the project directory.

3. Build the project.

## Usage

1. Check application.properties and change what ever is necessary.

2. Start ActiveMQ on default port (see application.properties)

3. Start the application.  

4. Access the API documentation using the provided Swagger UI URL (e.g., http://localhost:8000/swagger-ui.html) and explore the available endpoints.

5. Use the API endpoints to manage books, users, and borrowing/returning operations.
