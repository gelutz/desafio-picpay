# Desafio PicPay

Resolving the PicPay developer challenge.

## Description

This project is an API service built using TypeScript, allowing users to perform CRUD (Create, Read, Update, Delete) operations with PostgreSQL as the database. It encompasses three different models: Transactions, Users, and Wallets. Each Transaction is associated with a User and a Wallet.

Additionally, the project integrates with external payment processing services to handle real-time transactions.

Whenever a new transaction is created, the API processes it and updates the user's wallet balance. The system ensures transactional integrity and provides detailed transaction history.

## Technologies Used

- TypeScript
- PostgreSQL
- Express
- Node.js
- Docker

## Usage

- Perform CRUD operations on Transactions, Users, and Wallets through the API endpoints.
- Access transaction history and wallet balance.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/gelutz/desafio-picpay.git
    cd desafio-picpay
    ```

2. Install the dependencies:
    ```bash
    npm install
    ```

3. Set up the database:
    - Ensure PostgreSQL is running.
    - Create a `.env` file based on `.env.example` and configure the database connection.

4. Run the migrations:
    ```bash
    npx prisma migrate dev
    ```

5. Start the application:
    ```bash
    npm start
    ```

## API Endpoints

- **Users**
  - `GET /users` - Retrieve all users
  - `POST /users` - Create a new user
  - `GET /users/:id` - Retrieve a user by ID
  - `PUT /users/:id` - Update a user by ID
  - `DELETE /users/:id` - Delete a user by ID

- **Transactions**
  - `GET /transactions` - Retrieve all transactions
  - `POST /transactions` - Create a new transaction
  - `GET /transactions/:id` - Retrieve a transaction by ID
  - `PUT /transactions/:id` - Update a transaction by ID
  - `DELETE /transactions/:id` - Delete a transaction by ID

- **Wallets**
  - `GET /wallets` - Retrieve all wallets
  - `POST /wallets` - Create a new wallet
  - `GET /wallets/:id` - Retrieve a wallet by ID
  - `PUT /wallets/:id` - Update a wallet by ID
  - `DELETE /wallets/:id` - Delete a wallet by ID

## Contribution Guidelines

1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature-name
    ```
3. Make your changes and commit them:
    ```bash
    git commit -m 'Add new feature'
    ```
4. Push to the branch:
    ```bash
    git push origin feature-name
    ```
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
