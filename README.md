# Next Lifts - Smart Elevator System

**Team Name:** Javengers

**Team Members:**

- **Κωνσταντίνος Αλεξόπουλος :** Project Manager, API Developer, Build, CI & Integration Lead, Testing Engineer

- **Ιωάννης Βασταρδής:** Back-end Developer, API Developer, Testing Engineer

- **Ανδριάνα Βόγλη:** Elevator System Developer, Presentation Designer, Testing Engineer

- **Στέφανος Μουράτογλου:** Back-end Developer, UI Developer , Testing Engineer

- **Κυριάκος Σπανάκης:** Project Manager, Evaluation Engineer, Testing Engineer

- **Μενέλαος-Γεώργιος Τσίτας:** UI Developer, Testing Engineer

- **Αιμιλία Φλωρά:** Evaluation Engineer, Build, CI & Integration Lead, Testing Engineer

- **Δήμητρα Ναυπλιώτου:** Elevator System Developer, Presentation Designer, Testing Engineer

## Project Tree


```
Elevator_BackEnd
├─ config
│  └─ checkstyle
│     └─ checkstyle.xml
├─ pom.xml
├─ README.md
└─ src
   ├─ main
   │  └─ java
   │     └─ gr
   │        └─ aueb
   │           └─ dmst
   │              └─ javengers
   │                 ├─ central
   │                 │  ├─ CentralApp.java
   │                 │  ├─ dispatch
   │                 │  └─ handlers
   │                 │     ├─ RequestHandler.java
   │                 │     └─ StatusHandler.java
   │                 ├─ common
   │                 │  ├─ dto
   │                 │  │  ├─ ConfigResponse.java
   │                 │  │  ├─ ElevatorResponse.java
   │                 │  │  ├─ ElevatorStatus.java
   │                 │  │  ├─ MetricsResponse.java
   │                 │  │  ├─ PassengerRequest.java
   │                 │  │  ├─ ResetResponse.java
   │                 │  │  ├─ ScenarioRequest.java
   │                 │  │  ├─ StopRequest.java
   │                 │  │  └─ StopResponse.java
   │                 │  └─ exceptions
   │                 │     ├─ BadRequestException.java
   │                 │     ├─ InvalidMethodException.java
   │                 │     ├─ InvalidStopException.java
   │                 │     └─ MissingSourceFloorException.java
   │                 ├─ elevator
   │                 │  ├─ handlers
   │                 │  │  └─ StatusHandler.java
   │                 │  └─ model
   │                 │     ├─ Direction.java
   │                 │     ├─ DoorState.java
   │                 │     ├─ ElevatorConfig.java
   │                 │     ├─ OperatingState.java
   │                 │     ├─ Stop.java
   │                 │     └─ StopType.java
   │                 ├─ Evaluation
   │                 │  ├─ EvaluationMain.java
   │                 │  ├─ Generator
   │                 │  │  └─ RandomGenerator.java
   │                 │  ├─ MathEvaluator
   │                 │  │  └─ MathEvaluator.java
   │                 │  └─ Storage
   │                 │     └─ DataStorage.java
   │                 └─ ui
   │                    └─ UserInterface.java
   └─ test
      └─ java
         └─ gr
            └─ aueb
               └─ dmst
                  └─ javengers
                     ├─ central
                     │  └─ handlers
                     │     └─ TestRequestHandler.java
                     └─ ui
                        └─ TestUserInterface.java

```