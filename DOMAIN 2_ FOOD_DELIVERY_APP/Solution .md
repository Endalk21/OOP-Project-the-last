 ## updated: Attribute and Method Table

| Class             | Attribute / Method       | Type           | Visibility | Rationale                                                      |
|------------------|--------------------------|----------------|------------|----------------------------------------------------------------|
| **Customer**      | customerId               | String         | private    | it gives Unique identifier for the customer.                           |
|                   | name                     | String         | private    | Recives Full name of the customer.                                    |
|                   | email                    | String         | private    | Used for login and communication.                             |
|                   | phoneNumber              | String         | private    | For contacting the customer.                                  |
|                   | addressList              | List<String>   | private    | To store multiple delivery addresses.                         |
|                   | placeOrder()             | void           | public     | Allows the customer to place a new order.                     |
|                   | viewOrderHistory()       | List<Order>    | public     | Lets customer view previous orders.                           |
|                   | giveFeedback()           | void           | public     | Enables the customer to give ratings/comments.                |
| **Restaurant**     | restaurantId            | String         | private    | Unique ID for the restaurant.                                 |
|                   | name                     | String         | private    | Restaurant name.                                              |
|                   | location                 | String         | private    | Address of the restaurant.                                    |
|                   | contactNumber            | String         | private    | For customer and delivery communication.                      |
|                   | openingHours             | String         | private    | Business operating hours.                                     |
|                   | menuItems                | List<MenuItem> | private    | Menu items offered.                                           |
|                   | updateMenu()             | void           | public     | To modify the restaurant menu.                                |
|                   | receiveOrder()           | void           | public     | Accept and process customer orders.                           |
| **MenuItem**       | itemId                  | String         | private    | Unique identifier for each item.                              |
|                   | name                     | String         | private    | Name of the menu item.                                        |
|                   | description              | String         | private    | Short details about the item.                                 |
|                   | price                    | double         | private    | Price of the item.                                            |
|                   | dietaryInfo              | String         | private    | Info like vegetarian, gluten-free.                            |
|                   | availability             | boolean        | private    | Shows if the item is in stock.                                |
|                   | updateAvailability()     | void           | public     | Used when item becomes out of stock or back in stock.         |
| **Order**          | orderId                 | String         | private    | Unique ID for the order.                                      |
|                   | customerId               | String         | private    | The ID of the customer placing the order.                     |
|                   | restaurantId             | String         | private    | The restaurant receiving the order.                           |
|                   | orderItems               | List<MenuItem> | private    | Items in the order.                                           |
|                   | status                   | String         | private    | Tracks order status (placed, preparing, etc.).                |
|                   | deliveryTime             | String         | private    | Expected delivery time.                                       |
|                   | processOrder()           | void           | public     | Handles order flow from placed to completed.                  |
|                   | cancelOrder()            | void           | public     | Allows user to cancel before dispatch.                        |
| **Payment**        | paymentId               | String         | private    | Unique ID for each payment.                                   |
|                   | orderId                  | String         | private    | Associated order’s ID.                                        |
|                   | amount                   | double         | private    | Total amount to be paid.                                      |
|                   | paymentMethod            | String         | private    | Credit card, mobile, cash, etc.                               |
|                   | paymentStatus            | String         | private    | Paid, pending, or failed.                                     |
|                   | processPayment()         | boolean        | public     | Initiates payment process.                                    |
| **DeliveryPersonnel** | deliveryId           | String         | private    | Unique ID for each delivery person.                           |
|                   | name                     | String         | private    | Name of the delivery person.                                  |
|                   | vehicleType              | String         | private    | Bike, car, etc.                                                |
|                   | currentLocation          | String         | private    | For route tracking.                                           |
|                   | assignedOrders           | List<Order>    | private    | Orders assigned to them.                                      |
|                   | updateStatus()           | void           | public     | Updates order delivery status.                                |
|                   | confirmDelivery()        | void           | public     | Confirms the delivery completion.                             |


## Detailed Relationships and Associations (Aggregation and composition)

| Relationship               | Aggregation/Composition | Cardinality       | Explanation                                                                 |
|----------------------------|--------------------------|-------------------|-----------------------------------------------------------------------------|
| Customer – Order           | Association              | One-to-Many       | A single customer can place multiple orders, but each order belongs to one customer. |
| Order – OrderItem          | Composition              | One-to-Many       | Order items are part of the order. If the order is deleted, its items go too. |
| Order – DeliveryDriver     | Association              | One-to-One        | An order can be delivered by one driver, and a driver can handle multiple orders. |
| Restaurant – MenuItem      | Aggregation              | One-to-Many       | A restaurant has several menu items, but items can exist independently of the restaurant. |
| Restaurant – Order         | Association              | One-to-Many       | Each restaurant can receive many orders from customers.                     |
| DeliveryDriver – Order     | Association              | One-to-Many       | A delivery driver can deliver multiple orders during their shift.           |


## Explanation of Applied OOP Principles
### 1. Encapsulation
we made all class attributes private and used getter and setter methods to access them.  
**Example:** In the `Customer` class, `name` and `address` are private, and we used `getName()` and `setName()` to access them safely.  
This way, the internal state is protected from unwanted changes.

### 2. Abstraction
we only included the most important information in each class.  
**Example:** In the `Order` class, we focused on `orderId`, `customer`, and `orderItems`, which are the main things needed to handle an order.  
This makes the design clean and easier to understand.

### 3. Inheritance
we planned to use inheritance to make user roles more flexible.  
**Example:** If we add `AdminUser` and `DeliveryUser`, they can inherit from a general `User` class that has common things like `login()` or `email`.  
That way, we avoid repeating code for each user type.

### 4. Polymorphism
Polymorphism lets me write one method that works in different ways.  
**Example:** we can create a method like `viewDashboard()` that behaves differently for a `Customer`, `Restaurant`, or `DeliveryUser`.  
That gives flexibility without changing existing code.
