# 📦 Payment Discount Optimizer


## 📜 Description

Payment Discount Optimizer is a simple and efficient Java application that calculates the most cost-effective way to pay for orders using loyalty points (PUNKTY) and various traditional payment methods (e.g., mZysk, BosBankrut). It ensures every order is fully paid while maximizing the total discount a customer can receive.

This project was created as a solution for a recruitment task (Zadanie2025v2) and reflects best practices in object-oriented Java programming, clean code, and simple optimization strategies.

## ✨ Features

- Applies the best discount per order, choosing:
  - Full payment with one bank method
  - Full payment with loyalty points
  - Partial payment with ≥10% loyalty points + a remaining method
- Ensures only valid promotions are applied (e.g., exclusivity between point discounts and bank discounts)
- Respects payment method limits
- Outputs total amount spent per method

## 🛠 Technologies Used

- Java 21 – modern Java language features
- Maven – build and dependency management
- Jackson – for JSON file parsing
- JUnit 5 – unit testing framework

## 📂 Project Structure

```
app/
├── pom.xml
├── src/
│   └── main/java/org/app/
│       ├── Main.java
│       ├── Order.java
│       ├── PaymentMethod.java
│       └── PaymentOptimizer.java
├── target/
│   └── app.jar
orders.json
paymentmethods.json
```



## 🚀 How to Run

1. Make sure Java 21 and Maven are installed.
2. Build the project:

```sh
mvn clean package
```
3. Run the JAR file with paths to your input files:
```sh
java -jar target/app.jar ./orders.json ./paymentmethods.json
```

📄 Input Format

orders.json
```json
[
  {
    "id": "ORDER1",
    "value": "150.00",
    "promotions": ["mZysk", "BosBankrut"]
  }
]
```

paymentmethods.json
```json
[
  {
    "id": "PUNKTY",
    "discount": "15",
    "limit": "120.00"
  },
  {
    "id": "mZysk",
    "discount": "10",
    "limit": "180.00"
  }
]
```


## 📤 Output Example
```
PUNKTY 100.00
mZysk 120.00
BosBankrut 190.00
```

## ✅ Code Quality

- Fully object-oriented with clean separation of concerns
- Proper usage of getters, setters, equals(), and hashCode() in model classes
- Small, reusable helper methods instead of complex logic blocks
- No unnecessary data structures or overengineering


## 💡 Author Notes
This solution is structured to be simple, clear, and scalable. Even though some methods like equals() and hashCode() are not directly used, they are included as part of solid OOP practices, ensuring the model classes behave correctly in future scenarios.

Feel free to review, reuse, or improve! 😎
