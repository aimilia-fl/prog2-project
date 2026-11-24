# Next Lifts — Τεκμηρίωση

**Ομάδα:** Javengers   
**Τεχνολογίες:** Java 21, Maven, JDK HTTP Server, Jackson 2.15.2, JUnit 4

## 1. Περιγραφή

Το Next Lifts είναι ένα σύστημα διαχείρισης αιτημάτων ασανσέρ. Αφορά μια κεντρική υπηρεσία που δέχεται το όροφο προέλευσης και τον προορισμό ενός επιβάτη, ελέγχει την κατάσταση του στόλου και επιλέγει διαθέσιμο ασανσέρ.

Η τρέχουσα έκδοση υλοποιεί:

- κεντρικό HTTP endpoint για αιτήματα επιβίβασης
- επικύρωση βασικών περιορισμών ορόφων
- επιλογή ασανσέρ βάσει της μικρότερης απόστασης από τον προορισμό
- μοντέλα κατάστασης, διαδρομής και στάσεων ασανσέρ
- γραμμική διεπαφή χρήστη
- HTTP handlers για τον κεντρικό server και την κατάσταση ασανσέρ
- unit και integration tests

## 2. Ομάδα

| Μέλος | Ρόλοι |
|---|---|
| Κωνσταντίνος Αλεξόπουλος | Project Manager, API Developer, Build, CI & Integration Lead, Testing Engineer |
| Ιωάννης Βασταρδής | Back-end Developer, API Developer, Testing Engineer |
| Ανδριάνα Βόγλη | Elevator System Developer, Presentation Designer, Testing Engineer |
| Στέφανος Μουράτογλου | Back-end Developer, UI Developer, Testing Engineer |
| Κυριάκος Σπανάκης | Project Manager, Evaluation Engineer, Testing Engineer |
| Μενέλαος-Γεώργιος Τσίτας | UI Developer, Testing Engineer |
| Αιμιλία Φλωρά | Evaluation Engineer, Build, CI & Integration Lead, Testing Engineer |
| Δήμητρα Ναυπλιώτου | Elevator System Developer, Presentation Designer, Testing Engineer |

## 3. Αρχιτεκτονική

### 3.1 Κεντρική υπηρεσία

Η `CentralApp`:

- ξεκινά HTTP server στη θύρα `8080`
- δημιουργεί χάρτη είκοσι ασανσέρ, με IDs `A` έως `T`
- αντιστοιχεί τα IDs στις τοπικές θύρες `8081` έως `8100`
- ζητά την κατάσταση κάθε ασανσέρ μέσω `StatusHandler`
- επιλέγει το ασανσέρ σε `NORMAL` κατάσταση με τη μικρότερη απόλυτη διαφορά `currentFloor - destinationFloor`
- στέλνει το `PassengerRequest` στο endpoint `/api/v1/elevator/dispatch`

Αν κανένα ασανσέρ δεν είναι διαθέσιμο ή η αποστολή στο επιλεγμένο ασανσέρ αποτύχει, επιστρέφεται `503` από τον κεντρικό handler.

### 3.2 Request handling

Ο `RequestHandler`:

- δέχεται μόνο `POST`
- διαβάζει και αποκωδικοποιεί JSON σε `PassengerRequest`
- απορρίπτει κενό σώμα και μη έγκυρο JSON
- επιτρέπει όρους μόνο στο εύρος `0` έως `200`
- απορρίπτει ίσο αρχικό και τελικό όροφο
- αναθέτει το αίτημα μέσω της `CentralApp`
- επιστρέφει `ElevatorResponse` σε JSON

### 3.3 Διεπαφή χρήστη

Η `UserInterface` δέχεται τον αρχικό όροφο ως command-line argument. Στη συνέχεια ζητά προορισμούς, δημιουργεί `PassengerRequest` και κάνει `POST` στο κεντρικό endpoint.

Παρέχει εξαγωγή για επιτυχή ανάθεση, άκυρο αίτημα, μη επιτρεπτή HTTP μέθοδο και αποτυχία σύνδεσης. Η λέξη `exit` τερματίζει το πρόγραμμα.

### 3.4 Μοντέλο ασανσέρ

Το πακέτο `gr.aueb.dmst.javengers.elevator.model` περιέχει:

| Κλάση/enum | Περιγραφή |
|---|---|
| `ElevatorConfig` | ελάχιστος και μέγιστος όροφος, ταχύτητα, μέγιστο φορτίο |
| `Direction` | `UP`, `DOWN`, `IDLE` |
| `DoorState` | `OPEN`, `CLOSED`, `OPENING`, `CLOSING` |
| `OperatingState` | `NORMAL`, `OVERLOAD`, `OUT_OF_SERVICE`, `EMERGENCY_STOP` |
| `Stop` | όροφος και τύπος στάσης |
| `StopQueue` | ουρά στάσεων χωρίς διπλότυπα |
| `StopType` | `PICK_UP`, `DROP_OFF` |

Το `ElevatorConfig` απορρίπτει αντιστραμμένα όρια ορόφων και μη θετικές τιμές ταχύτητας ή φορτίου.

### 3.5 Κοινά δεδομένα και εξαιρέσεις

Τα κυριότερα DTO βρίσκονται στο `common.dto`:

- `PassengerRequest`: αρχικός και τελικός όροφος
- `ElevatorResponse`: ID ασανσέρ και status
- `ElevatorStatus`: κατάσταση, όροφος, κατεύθυνση, πόρτα, βάρος και επόμενες στάσεις
- `StopRequest`/`StopResponse`: δεδομένα αίτησης στάσης
- `ConfigResponse`: αριθμός ασανσέρ και ορόφων
- `ScenarioRequest`: παράμετροι σεναρίου αξιολόγησης
- `MetricsResponse`/`Metrics`: μετρικές απόδοσης
- `ResetResponse`: κατάσταση επαναφοράς.

Οι custom εξαιρέσεις βρίσκονται στο `common.exceptions` και καλύπτουν άκυρο αίτημα, μέθοδο, στάση και απουσία αρχικού ορόφου.

## 4. Ροή αιτήματος

```text
UserInterface
  -> POST /api/v1/central/requests
  -> RequestHandler
  -> επικύρωση JSON και ορόφων
  -> CentralApp.assignElevator(...)
  -> ανάγνωση κατάστασης ασανσέρ
  -> επιλογή NORMAL ασανσέρ πλησιέστερου στον προορισμό
  -> POST /api/v1/elevator/dispatch
  -> ElevatorResponse
```

## 5. HTTP API

### 5.1 Αίτημα επιβίβασης

**Endpoint**

```text
POST http://localhost:8080/api/v1/central/requests
```

**Body**

```json
{
  "sourceFloor": 1,
  "destinationFloor": 6
}
```

**Επιτυχής απάντηση — 200**

```json
{
  "assignedElevator": "A",
  "status": 200
}
```

**Κωδικοί απόκρισης**

| Status | Συνθήκη |
|---|---|
| `200` | το αίτημα επικυρώθηκε και το ασανσέρ ανατέθηκε |
| `400` | κενό ή μη έγκυρο JSON, `null`, όροφος εκτός `0–200` ή ίδιος αρχικός/τελικός όροφος |
| `405` | μέθοδος διαφορετική από `POST` |
| `503` | κανένα διαθέσιμο ασανσέρ ή αποτυχία προώθησης |

### 5.2 Κατάσταση ασανσέρ

Ο elevator `StatusHandler` επιστρέφει `ElevatorStatus` μόνο για `GET`:

```text
GET /api/v1/elevators/A/status
```

Το 405 επιστρέφεται για άλλη μέθοδο. Το 500 επιστρέφεται αν η υπηρεσία κατάστασης δεν παρέχει status.

### 5.3 Dispatch ασανσέρ

Η `CentralApp` χρησιμοποιεί:

```text
POST http://localhost:<θύρα-ασανσέρ>/api/v1/elevator/dispatch
```

Το σώμα είναι το ίδιο `PassengerRequest` που χρησιμοποιήθηκε στο κεντρικό endpoint.

> Ο `ElevatorServer` είναι ακόμη κενός και η `ElevatorApp.main()` δεν εκκινεί server. Επομένως, ο τρέχων κώδικας δεν διαθέτει πλήρως υλοποιημένο στόλο και dispatch endpoint. Η περιγραφόμενη διαδρομή αποτελεί τη σχεδιασμένη ενσωμάτωση.

## 6. Δομή έργου

```text
.
├── config/checkstyle/checkstyle.xml
├── docs/documentation.md
├── output/src.puml
├── pom.xml
├── README.md
└── src
    ├── main/java/gr/aueb/dmst/javengers
    │   ├── central/
    │   ├── common/
    │   ├── elevator/
    │   ├── Evaluation/
    │   └── ui/
    └── test/java/gr/aueb/dmst/javengers
```

## 7. Εκτέλεση και έλεγχοι

### Προϋποθέσεις

- JDK 21 ή νεότερη έκδοση
- Maven
- διαθεσιμότητα θύρας `8080`

### Μεταγλώττιση

```powershell
mvn clean compile
```

### Δημιουργία πακέτου

```powershell
mvn clean package
```

Το Maven output δημιουργείται στο `target/`.

### Εκτέλεση κεντρικής υπηρεσίας

Η build δεν ορίζει ακόμη Maven main-class ή runnable fat JAR. Εκτελέστε τη `gr.aueb.dmst.javengers.central.CentralApp` από Java IDE που φορτώνει τις Maven dependencies.

Μετά την εκκίνηση:

```text
Central App is running on Port: 8080
```

### Εκτέλεση διεπαφής χρήστη

Εκτελέστε τη `gr.aueb.dmst.javengers.ui.UserInterface` από IDE με program argument τον αρχικό όροφο, για παράδειγμα `1`.

### Tests

```powershell
mvn test
```

Τα tests καλύπτουν:

- validation αιτημάτων και HTTP methods
- επιτυχή/αποτυχημένη ανάθεση
- elevator status JSON και status codes
- UI επιτυχία/σφάλματα
- `ElevatorConfig`, `StopQueue` και custom εξαιρέσεις.

### Checkstyle

```powershell
mvn checkstyle:check
```

Το PlantUML διάγραμμα της συνολικής δομής βρίσκεται στο [`output/src.puml`](../output/src.puml).
