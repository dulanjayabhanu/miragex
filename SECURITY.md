# Security Policy

## Supported Versions

| Version | Supported |
|---------|---|
| 1.0.0   | Yes |

<br>

## Reporting a Vulnerability

If you discover a security vulnerability in MirageX, please do not open a public
GitHub issue. Public disclosure of security vulnerabilities can put users at risk
before a fix is available.

Instead, please report it privately by contacting:

**Email:** dulanjayawebs@gmail.com

<br>

## What to Include in Your Report

- A clear description of the vulnerability
- Steps to reproduce the issue
- Potential impact of the vulnerability
- Any suggested fixes if available

<br>

## Response Process

- You will receive an acknowledgement within 48 hours
- We will investigate and keep you informed of the progress
- Once the vulnerability is confirmed and fixed, a new release will be published
- You will be credited for the discovery unless you prefer to remain anonymous

<br>

## Scope

The following are considered in scope for security reports:

- Weaknesses in the encryption or key derivation implementation
- File header parsing vulnerabilities
- Any behavior that could lead to unintended data exposure

The following are out of scope:

- Vulnerabilities in third-party dependencies (report those to the respective projects)
- Issues related to user error such as weak passwords or lost credentials

<br>

## Security Design

MirageX is built on the following industry-standard cryptographic primitives:

- AES-256-GCM for authenticated encryption
- Argon2id for password-based key derivation
- Bouncy Castle as the cryptographic provider

All cryptographic decisions are documented in the [README](README.md) and the source code
is fully open for review.