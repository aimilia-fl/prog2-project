# Next Lifts - Smart Elevator System

**Team Name:** Javengers

**Team Members:**

- **Κωνσταντίνος Αλεξόπουλος :** Project Manager, API Developer, Build, CI & Integration Lead, Testing Engineer

- **Ιωάννης Βασταρδής:** Back-end Developer, API Developer, Testing Engineer

- **Ανδριάνα Βόγλη:** Elevator System Developer, Presentation Designer, Testing Engineer

- **Στέφανος Μουράτογλου:** Back-end Developer, Testing Engineer

- **Κυριάκος Σπανάκης:** Project Manager, Evaluation Engineer, Testing Engineer

- **Μενέλαος-Γεώργιος Τσίτας:** UI Developer, Testing Engineer

- **Αιμιλία Φλωρά:** Evaluation Engineer, Build, CI & Integration Lead, Testing Engineer

- **Δήμητρα Ναυπλιώτου:** Elevator System Developer, Presentation Designer, Testing Engineer

## Project Tree

```
prog2-project
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
   │                 │  │  ├─ ElevatorResponse.java
   │                 │  │  ├─ ElevatorStatus.java
   │                 │  │  ├─ PassengerRequest.java
   │                 │  │  ├─ StopRequest.java
   │                 │  │  └─ StopResponse.java
   │                 │  └─ exceptions
   │                 │     ├─ BadRequestException.java
   │                 │     ├─ InvalidMethodException.java
   │                 │     └─ MissingSourceFloorException.java
   │                 ├─ elevator
   │                 │  └─ model
   │                 │     ├─ Direction.java
   │                 │     ├─ DoorState.java
   │                 │     └─ OperatingState.java
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
