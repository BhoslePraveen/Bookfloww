# BookFlow

A smart appointment scheduling system with seamless Google Calendar integration.

## Overview

BookFlow streamlines the booking process for service providers and their clients. It supports two booking modes: registered users with Google OAuth authentication and guest users who can book without creating an account.

## Features

- **Google Calendar Integration** — Automatic sync with Google Calendar for real-time availability and conflict detection
- **Dual Booking Modes** — Support for both registered users and guest bookings
- **Customer ID System** — Unique IDs for registered users to share with organizers
- **Contact Management** — Organize and manage client information
- **Email Notifications** — Automated confirmations, reminders, and updates
- **Dashboard** — Overview of appointments, availability, and client activity

## Tech Stack

| Layer    | Technology          |
|----------|---------------------|
| Backend  | Spring Boot         |
| Frontend | React               |
| Calendar | Google Calendar API |
| Auth     | OAuth 2.0           |

## Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- Google Cloud Console project with Calendar API enabled
- OAuth 2.0 credentials