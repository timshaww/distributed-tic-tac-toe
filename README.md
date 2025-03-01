# 🦩 Distributed Tic Tac Toe Microservices

This project is a **fully automated Tic Tac Toe game**, implemented using a **microservices architecture**. The game runs two players that automatically play moves against each other.

## **🛠 Tech Stack**
- **Backend**: Java, Spring Boot, Maven
- **Frontend**: React, TailwindCSS
- **Database**: In-memory storage (HashMap)
- **API Communication**: REST API (Spring Boot `RestTemplate`)

---

Hey there 🥸

To run this code, follow these steps!
### 1️⃣ Clone the Repo
```
git clone https://github.com/timshaww/distributed-tic-tac-toe.git
cd distrubuted-tic-tac-toe
```

### 2️⃣ Start the Game Engine Service
```
cd game-engine-service/game-engine-service
mvn clean package
mvn spring-boot:run
```
The Game Engine microservice runs on http://localhost:8080

### 3️⃣ Start the Game Session Service
```
cd game-session-service/game-session-service
mvn clean package
mvn spring-boot:run
```
The Game Session microservice runs on http://localhost:8081

### 4️⃣ Start the frontend
```
cd tic-tac-toe-ui
npm install
npm run dev
```
The frontend runs on http://localhost:3000

---

##  How to play

1. Open http://localhost:3000 in your browser.
2. Click "Start Simulation" to create a new game session.
3. The game will automatically simulate moves until a winner is found or the game ends in a draw.
4. The board updates in near-real-time! It goes quick, you might miss it!

❌⭕️❌

⭕️⭕️❌

⭕️❌❌
