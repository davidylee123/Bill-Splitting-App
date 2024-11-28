# Bill Splitting Application

This project is a Bill Splitting application that allows users to manage shared expenses, create bills, and track who owes what. The application is built with a **React** frontend, a **Java Spring Boot** backend, and uses **MongoDB** as its database.

## Table of Contents

- [Technologies](#technologies)
- [Features](#features)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Backend Setup](#backend-setup)
- [Frontend Setup](#frontend-setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)

## Technologies

- **Backend**: Java, Spring Boot, MongoDB
- **Frontend**: React, HTML/CSS
- **Database**: MongoDB

## Features

- User management: Create and manage users.
- Bill management: Create bills that include multiple expenses and users.
- Expense tracking: Add expenses to bills, and split the cost among users.
- API for interacting with users, bills, and expenses.

## Getting Started

To get a copy of the project up and running on your local machine, follow the instructions below.

## Prerequisites

- **Java JDK** (version 17 or higher)
- **Node.js** (version 18 or higher)
- **MongoDB** (local instance or cloud-based MongoDB Atlas)
- **Maven** for building the backend

## Backend Setup

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/bill-splitting-app.git
   cd bill-splitting-app/backend
   ```

2. **Configure MongoDB**:

   ````bash
   Ensure MongoDB is running locally or provide your MongoDB URI. Update the application.properties file in the src/main/resources/ folder with the MongoDB connection details:
   spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.mongodb.net/billsplitter?retryWrites=true&w=majority
   ````

3. **Build and run the backend**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Frontend Setup

1. **Navigate to the frontend directory**:

   ```bash
   cd ../frontend
   ```

2. **Install dependencies**:

   ```bash
   npm install
   ```

3. **Run the frontend server**:
   ```bash
   npm start
   ```

**The frontend will be accessible at http://localhost:3000**

## Running the Application

1. **Ensure the backend server is running** on port 8080 as described in the backend setup.
2. **Ensure the frontend server is running** on port 3000 as described in the frontend setup.
3. **Access the application**:
   - Open your web browser and go to `http://localhost:3000` to start using the Bill Splitting app.

## API Endpoints

The backend exposes the following REST API endpoints:

### Users

- `GET /api/users` - Retrieve all users
- `POST /api/users` - Create a new user
- `GET /api/users/{id}` - Retrieve a user by ID
- `PUT /api/users/{id}` - Update user information by ID
- `DELETE /api/users/{id}` - Delete a user by ID
- `POST /api/users/{userId}/friends/{friendId}` - Add a friend by friendId to a user by userId
- `DELETE /api/users/{userId}/friends/{friendId}` - Remove a friend by friendId from a user by userId
- `POST /api/users/authenticate` - Authenticate a user by email and password

### Bills

- `GET /api/bills` - Retrieve all bills
- `POST /api/bills` - Create a new bill
- `GET /api/bills/{id}` - Retrieve a bill by ID
- `PUT /api/bills/{id}` - Update a bill by ID
- `DELETE /api/bills/{id}` - Delete a bill by ID

### Expenses

- `POST /api/expenses` - Create a new expense
- `GET /api/expenses` - Retrieve all expenses
- `GET /api/expenses/{id}` - Retrieve an expense by ID
- `PUT /api/expenses/{id}` - Update an expense by ID
- `DELETE /api/expenses/{id}` - Delete an expense by ID
- `GET /api/expenses/customId/{customId}` - Retrieve an expense by custom ID

## Example Usage

### Adding a Friend

**Request:**

- **URL**: `POST /api/users/{userId}/friends/{friendId}`
- **Body**: None
- **Example**: Add user with `id` `644000000000000000000001` as a friend of user with `id` `644000000000000000000002`.

**Response:**

- **200 OK**: Successfully added the friend.