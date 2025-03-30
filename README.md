# Smart Motel Management with AI-Powered Chatbot
![Screenshot 2025-03-30 112449](https://github.com/user-attachments/assets/ab2ccd12-faac-45ef-a6ce-4ac35794168a)
![Screenshot 2025-03-30 112848](https://github.com/user-attachments/assets/7632c116-12b9-4e88-9d2a-84757e17fcca)
![Screenshot 2025-03-30 112912](https://github.com/user-attachments/assets/b44b0e85-7f2f-46b9-a454-7ff511dd218e)

## Overview
**Smart Motel Management with AI-Powered Chatbot** is an intelligent rental management system that leverages AI to assist tenants in finding and booking rooms efficiently. The system integrates a chatbot powered by **Llama 3** to provide real-time recommendations and automate the booking process. It also supports online payments, revenue tracking, and customer management.

## Features
- **AI-Powered Chatbot** – Provides smart recommendations for available rooms based on user preferences.
- **Real-time Room Search** – Users can search for available rooms based on location, price, and amenities.
- **Automated Room Booking** – The chatbot assists in reserving rooms automatically.
- **Email Notifications** – Sends invoices to tenants via email.
- **Online Payment Support** – Integrates **VNPAY API** for deposits and monthly rent payments.
- **JWT Authentication & Authorization** – Secure login system for admins and customers.
- **Role-Based Access Control** – Admins manage rooms, bookings, and finances, while users can book rooms and make payments.
- **Customer Portal** – Allows tenants to book rooms, view invoices, and make payments.
- **Revenue & Reporting** – Tracks investments, profits, and tenant details.

## Technologies Used
- **AI & NLP:** Ollama Llama 3
- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **Authentication:** JWT
- **Payment Integration:** VNPAY API
- **Frontend:** Vue.js ([Frontend Repository](#))

## Installation
### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/Smart-Motel-Management.git
   cd Smart-Motel-Management
   ```
2. Configure **MySQL Database** in `application.properties`.
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup
1. Clone the frontend repository:
   ```bash
   git clone https://github.com/your-repo/frontend.git
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Vue.js app:
   ```bash
   npm run dev
   ```

## API Endpoints
### Authentication
- **POST** `/api/auth/login` – User login
- **POST** `/api/auth/register` – User registration

### Chat Bot
- **GET** `/api/chatbot/ask` – Ask AI chatbot

### Rent Room
- **GET** `/api/rent-rooms/all` – Get all rooms
- **POST** `/api/rent-rooms/{id}/rentRoom` – Rent a room
- **POST** `/api/rent-rooms/create-VNPayUrl/{id}` – Payment

### Send Mail
- **POST** `/api/sendMail/orderRoom/{id}` – Send mail with order room

### Statistical
- **GET** `/api/statistical` – Statistical motel

### Manage Room
- **GET** `/api/rooms` – Get all rooms
- **POST** `/api/rooms` – Add room
- **PUT** `/api/rooms` – Update room
- **DELETE** `/api/rooms` – Delete room


## Contribution
Feel free to contribute by submitting issues and pull requests.

## License
This project is licensed under the MIT License.


