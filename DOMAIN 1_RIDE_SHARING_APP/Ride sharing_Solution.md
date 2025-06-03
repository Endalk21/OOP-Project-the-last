**INTIAL ATTRIBUTE AND METHOD TABLE**  

| **Class**       | **Attribute / Method**              | **Type**                | **Visibility** | **Description**                                                                 |
|------------------|--------------------------------------|--------------------------|----------------|------------------------------------------------------------|
| **Rider**        | riderId                              | String                   | private        | Unique ID identifying the rider                                                 |
|                  | name                                 | String                   | private        | Full name of the rider                                                          |
|                  | email                                | String                   | private        | Contact email                                                                   |
|                  | phone                                | String                   | private        | Phone number                                                                    |
|                  | password                             | String                   | private        | Login credential                                                                |
|                  | currentLocation                      | Location                 | private        | Real-time rider location                                                        |
|                  | rideHistory                          | List\<Ride\>             | private        | Past rides taken                                                                |
|                  | +requestRide(pickup, dropoff)        | Ride                     | public         | Initiates a new ride request                                                    |
|                  | +viewRideHistory()                   | List\<Ride\>             | public         | Returns list of completed rides                                                 |
| **Driver**       | driverId                             | String                   | private        | Unique ID for driver                                                            |
|                  | name                                 | String                   | private        | Driver’s full name                                                              |
|                  | phone                                | String                   | private        | Driver’s contact number                                                         |
|                  | licenseNo                            | String                   | private        | Driving license ID                                                              |
|                  | vehicle                              | Vehicle                  | private        | Associated car                                                                  |
|                  | status                               | String                   | private        | Driver availability: Online/Offline                                             |
|                  | rating                               | Float                    | private        | Average user rating                                                             |
|                  | currentLocation                      | Location                 | private        | GPS position                                                                    |
|                  | +acceptRide(rideId)                  | Boolean                  | public         | Accepts an incoming ride request                                                |
|                  | +updateStatus(status)                | void                     | public         | Changes driver’s current availability                                           |
| **Ride**         | rideId                               | String                   | private        | Unique ride identifier                                                          |
|                  | riderId                              | String                   | private        | ID of the rider                                                                 |
|                  | driverId                             | String                   | private        | ID of the assigned driver                                                       |
|                  | pickup                               | Location                 | private        | Start location                                                                  |
|                  | dropoff                              | Location                 | private        | End location                                                                    |
|                  | fare                                 | Float                    | private        | Total cost of ride                                                              |
|                  | status                               | String                   | private        | Ride state: Requested, Accepted, Completed                                      |
|                  | startTime                            | DateTime                 | private        | Ride start time                                                                 |
|                  | endTime                              | DateTime                 | private        | Ride end time                                                                   |
|                  | +updateStatus(newStatus)             | void                     | public         | Updates the status of the ride                                                  |
|                  | -calculateFare()                     | Float                    | private        | Calculates ride fare based on time and distance                                |
| **Vehicle**      | vehicleId                            | String                   | private        | Unique vehicle ID                                                               |
|                  | driverId                             | String                   | private        | Owner driver’s ID                                                               |
|                  | model                                | String                   | private        | Vehicle make/model                                                              |
|                  | licensePlate                         | String                   | private        | Car registration number                                                         |
|                  | color                                | String                   | private        | Color of the vehicle                                                            |
| **Payment**      | paymentId                            | String                   | private        | Unique transaction ID                                                           |
|                  | rideId                               | String                   | private        | Associated ride                                                                 |
|                  | method                               | String                   | private        | Payment method: Card, Wallet, etc.                                              |
|                  | amount                               | Float                    | private        | Amount charged                                                                  |
|                  | timestamp                            | DateTime                 | private        | Payment timestamp                                                               |
|                  | +processPayment()                    | Boolean                  | public         | Executes and confirms payment                                                   |
| **Rating**       | ratingId                             | String                   | private        | Unique feedback ID                                                              |
|                  | rideId                               | String                   | private        | Ride being rated                                                                |
|                  | fromUserId                           | String                   | private        | ID of reviewer                                                                  |
|                  | toUserId                             | String                   | private        | ID of person being rated                                                        |
|                  | score                                | Integer                  | private        | 1 to 5 rating value                                                             |
|                  | comments                             | String                   | private        | Optional review                                                                 |
|                  | +submitRating()                      | void                     | public         | Sends rating for completed ride                                                 |
| **Notification** | notificationId                       | String                   | private        | Unique ID of notification                                                       |
|                  | userId                               | String                   | private        | Recipient ID                                                                    |
|                  | message                              | String                   | private        | Content of the notification                                                     |
|                  | type                                 | String                   | private        | Alert type: ride status, payment, etc.                                          |
|                  | timestamp                            | DateTime                 | private        | Time sent                                                                       |
|                  | +sendNotification()                  | void                     | public         | Sends system or user-triggered alert                                            |
| **Location**     | locationId                           | String                   | private        | Unique location ID                                                              |
|                  | latitude                             | Double                   | private        | GPS latitude                                                                    |
|                  | longitude                            | Double                   | private        | GPS longitude                                                                   |
|                  | address                              | String                   | private        | Human-readable address                                                          |
|                  | city                                 | String                   | private        | City name                                                                       |
|                  | +getDistanceTo(location)             | Float                    | public         | Returns distance between two locations                                          |

**🔗Detailed Relationships and Associations**

|**Relationship**          |**Multiplicity**  | **Association Type** | **Description**                                       |
|------------------------------------------------------------|--------------------|-----------------------|-----------------------------------------------------|
| Rider ->Ride             | 1 to many         | Aggregation           | One rider can request many rides. Rides exist independently                    |
| Driver -> Ride            | 1 to many         | Aggregation           | One driver is assigned to many rides                                         |
| Driver ->Vehicle          | 1 to 1             | Aggregation           | Each driver owns exactly one vehicle                                              |
| Ride  ->Location       | 1 to ..2           | Composition           | Each ride has tightly coupled pickup and dropoff locations                        |
| Ride -> Payment      | 1 to 1             | Aggregation           | Each ride generates one payment record                                            |
| Ride -> Rating         | 1 to ..2         | Aggregation           | Ride receives ratings from both rider and driver                                  |
| User(Rider/Driver)-> Notification     | 1 to many          | Aggregation           | Users can receive many notifications                               |
| Rating ->  User     | 1 to 1             | Association            | Ratings are created by riders or drivers                                          |
| Vehicle -> Driver   | 1 to 1             | Aggregation           | Every vehicle is associated with a single driver                                   |

**OOP Principles Applied**  

**1. Encapsulation**  

Encapsulation is used to hide the internal state of an object and allow access only through well-defined interfaces.  
In the ride-sharing system, classes like Rider, Driver, and Ride have private attributes (e.g., status, fare, location) that are accessed or modified using public methods like requestRide() or updateStatus().  
This prevents unauthorized changes and improves security.  

**2. Abstraction**  

Abstraction simplifies complex processes by exposing only essential features.  
In this domain, methods like requestRide(), calculateFare(), or sendNotification() hide the complexity of driver-matching, route calculation, or message delivery.  
Users and developers work with simplified interfaces without worrying about internal implementations.  

**3. Inheritance**  
Inheritance enables the creation of a base class with common attributes and behaviors that can be reused by subclasses.  
The ride-sharing system can use a base class User containing shared attributes like name, email, and phone. Rider and Driver classes inherit from User and add specific behaviors like requestRide() or acceptRide().  
This promotes code reuse and cleaner class design.  
A User base class was introduced to encapsulate common attributes (userId, name, email, phone, password) shared by both   Rider and Driver. This allows the system to apply inheritance, reducing duplication and improving code reusability. Rider   and Driver now extend User.  
**4. Polymorphism**    
Polymorphism allows objects to respond differently to the same method call.  
For example, the method submitRating() may behave differently depending on whether a Driver is rating a Rider or vice versa. Similarly, sendNotification() might send a push notification to the app, an email, or an SMS, depending on user preferences  all using the same method name.  

