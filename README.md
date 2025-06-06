# Gaming Trading System

A gamified trading platform that allows users to trade virtual assets, earn gems, and compete on leaderboards.

## Features

### 1. User Management
- Create user accounts
- Track user statistics
- View user portfolios
- Monitor gem earnings

### 2. Portfolio Management
- Create multiple portfolios per user
- Track portfolio performance
- View portfolio analytics
- Monitor asset holdings

### 3. Trading System
- Buy and sell assets
- Real-time price updates
- Dynamic asset pricing
- Trade history tracking

### 4. Gamification
- Earn gems for trading activity
- Trading streaks with bonus rewards
- Milestone achievements
- Leaderboard rankings

### 5. Analytics
- Most traded assets
- Highest portfolio values
- Portfolio performance tracking
- Trading volume analysis
- Average trade size statistics

## API Endpoints

### User Management
```
POST /api/users?username={username}     # Create new user
GET  /api/users/{userId}                # Get user stats
```

### Portfolio Management
```
POST /api/portfolios                    # Create new portfolio
GET  /api/portfolios/{portfolioId}      # Get portfolio details
GET  /api/users/{userId}/portfolios     # Get user's portfolios
```

### Trading
```
POST /api/trade                         # Execute a trade
```

### Asset Management
```
POST /api/assets                        # Create new asset
GET  /api/assets                        # Get all assets
GET  /api/assets/{assetId}              # Get asset by ID
GET  /api/assets/symbol/{symbol}        # Get asset by symbol
```

### Leaderboard
```
GET /api/leaderboard                    # Get full leaderboard
GET /api/leaderboard/top?limit={n}      # Get top N users
```

### Analytics
```
GET /api/analytics/most-traded          # Get most traded assets
GET /api/analytics/highest-portfolios   # Get highest portfolio values
GET /api/analytics/portfolio-performance/{userId}  # Get portfolio performance
GET /api/analytics/trading-volume       # Get trading volume by asset
GET /api/analytics/average-trade-size   # Get average trade size by user
```

## Request/Response Examples

### Create Asset
```json
POST /api/assets
{
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "initialPrice": 150.00
}
```

### Execute Trade
```json
POST /api/trade
{
    "portfolioId": 1,
    "assetId": 1,
    "quantity": 10,
    "price": 150.00,
    "tradeType": "BUY"
}
```

### Create Portfolio
```json
POST /api/portfolios
{
    "userId": 1,
    "name": "My First Portfolio"
}
```

## Gem System

### Base Rewards
- 1 gem per trade (buy or sell)

### Milestone Bonuses
- 5 bonus gems at 5 trades
- 10 bonus gems at 10 trades

### Streak Bonuses
- Additional gems equal to streak length when streak ≥ 3
- Streaks maintained when trades are within 30 minutes

## Technical Details

### Prerequisites
- Java 17 or higher
- Maven
- Spring Boot 3.x

### Dependencies
- Spring Boot Web
- Spring Boot Validation
- Spring Boot Scheduling
- Jakarta Validation API

### Running the Application
```bash
# Clone the repository
git clone https://github.com/richynick/game_trading

# Navigate to project directory
cd gaming-trading-system

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

### Configuration
The application uses default Spring Boot configuration. Key properties can be modified in `application.properties`:
```properties
# Server port
server.port=8080

# Asset price update interval (in milliseconds)
asset.price.update.interval=60000
```

## Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=PortfolioServiceTest
```

## API Documentation

The API documentation is automatically generated using SpringDoc OpenAPI. You can access the documentation in two ways:

1. **Swagger UI**: Visit `http://localhost:8080/swagger-ui.html` in your browser to see an interactive API documentation interface.

2. **OpenAPI Specification**: The raw OpenAPI specification is available at `http://localhost:8080/v3/api-docs`.

The documentation includes:
- Detailed descriptions of all endpoints
- Request/response schemas
- Example requests and responses
- Authentication requirements
- Error responses

### Example API Documentation

Here's an example of how the API documentation looks for the trade execution endpoint:

```yaml
/trade:
  post:
    summary: Execute a trade
    description: Executes a buy or sell trade for an asset
    requestBody:
      required: true
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/TradeRequest'
    responses:
      '200':
        description: Trade executed successfully
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Trade'
      '400':
        description: Invalid input
      '404':
        description: Portfolio or asset not found
      '409':
        description: Insufficient assets for sell trade
```
