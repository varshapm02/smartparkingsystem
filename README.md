
A low-level backend design of a Smart Parking Lot System that manages vehicle entry, parking spot allocation, and billing.  
This project demonstrates object-oriented design,concurrency handling, and modular architecture, implemented entirely in Java.

Objective
Design a backend system that:
- Automatically allocates parking spots based on vehicle type and size.
- Tracks vehicle entry and exit times.
- Calculates parking fees based on time and vehicle type.
- Handles concurrent vehicle operations safely.

Key Features
- Automatic parking spot allocation by vehicle type.
- Real-time availability updates.
- Time-based billing with configurable strategy.
- Thread-safe design using `ReentrantLock`.
- Modular and scalable structure for multi-floor parking.

Setup and Execution
Requirements
- Java: JDK 17 or later  
- IDE:IntelliJ IDEA / Eclipse  
- Build Tool: Not required (pure Java project)

Running the Project
**Option 1: Using IntelliJ IDEA
1. Clone the repository:
   ```bash
   git clone https://github.com/varshapm02/smartparkingsystem.git
2. Open the project in IntelliJ.
3. Run the file SmartParkingApp.java.

System Design Overview
Architecture Flow

1. SmartParkingApp initializes the parking lot and runs the simulation.
2. SpotAllocationService assigns parking spots using ParkingLotManager.
3. TicketFactory creates parking tickets.
4. BillingService computes parking fees using the configured strategy.
5. Parking spot availability is updated in real time.
