# Task 1: Refactoring Code to Comply with the Single Responsibility Principle (SRP)

> [!IMPORTANT]
> USE JDK 17 TO COMPILE AND RUN

## Problem Statement
The provided code violates the **Single Responsibility Principle (SRP)**. The `HttpRestRequest` class currently has multiple responsibilities, making it difficult to maintain, test, and extend. Your goal is to refactor the code so that it adheres to SRP while ensuring that its functionality remains unchanged.

## Requirements
1. Refactor the code to ensure compliance with the **Single Responsibility Principle**.
2. The **interface of the `HttpRestRequest` class** must remain unchanged. Specifically, the public method signatures cannot be modified.
3. The provided **test case** must pass after your refactoring.

## Constraints
- You may create additional classes or interfaces as needed.
- Do not change the method names or parameters in the `HttpRestRequest` class.
- Follow clean code principles while focusing on SRP.
