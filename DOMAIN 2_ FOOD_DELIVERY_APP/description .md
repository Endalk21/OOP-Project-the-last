## Domain Description

This project is an **Online Food Delivery System** that lets people order food from local restaurants using a web or mobile platform. The system is designed for three main users: **customers**, **restaurants**, and **delivery personnel**.

Customers can make an account, look through restaurants, check their menus, and place orders. They can also track their order status in real-time, like when it’s being prepared or out for delivery. The system allows them to save delivery addresses, leave reviews, and get notifications about discounts or order updates.

Restaurants can sign up, add their profile details like working hours and location, and manage their menu items. They receive customer orders, update the status (e.g., preparing, ready), and communicate with delivery personnel.

Delivery personnel get notified about new orders and are responsible for picking up the food and delivering it to the customer. They can update the delivery status and view their delivery history.

The system also includes features for secure payment through different methods (like cards or cash on delivery), discount codes, loyalty points for repeat customers, and personalized suggestions based on past orders. It’s made to be scalable so it works well even during busy times and keeps user data safe and private.

Overall, this system aims to make the food ordering process smooth, easy, and secure for everyone involved.
## Initial Object Identification Table

| Object Name         | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| Customer            | Represents users who browse restaurants, place orders, and make payments.   |
| Restaurant          | Entity that offers food, manages menus, and prepares orders.                |
| MenuItem            | Individual food items listed by restaurants.                                |
| Order               | Represents a customer's request for specific menu items.                    |
| DeliveryPersonnel   | Person responsible for delivering orders to customers.                      |
| Payment             | Represents the payment made for an order.                                   |
| Cart                | Temporary storage for items selected by a customer before checkout.         |
| Rating              | Feedback provided by customers on restaurants, menu items, or delivery.     |
| Notification        | Alerts sent to customers about order updates or promotions.                 |
| Address             | Delivery location provided by the customer.                                 |
| LoyaltyProgram      | Tracks points and rewards for returning customers.                          |
| DiscountCode        | Code applied at checkout to reduce the total price.                         |
| OrderStatus         | Represents the current state of an order (e.g., Placed, Preparing, Out).    |
