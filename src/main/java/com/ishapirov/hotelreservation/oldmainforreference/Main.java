// package com.ishapirov.hotelreservation.oldmainforreference;

// import java.util.Scanner;

// import com.ishapirov.hotelreservation.hotel_objects.Customer;
// import com.ishapirov.hotelreservation.hotel_objects.Hotel;
// import com.ishapirov.hotelreservation.hotel_objects.TypeOfRoom;

// public class Main {
// 	public static void main(String[] args) {
// 		Hotel hotel = new Hotel();
// 		Scanner scan = new Scanner(System.in);

// 		boolean runAgain = true;

// 		while (runAgain) {

// 			System.out.println("What would you like to do?");
// 			System.out.println("1, Reserve a Room.");
// 			System.out.println("2, Cancel Reservation");
// 			System.out.println("3, Confirm Reservation");

// 			int choice = scan.nextInt();

// 			if (choice == 1) {
// 				boolean reserve = true;
// 				while (reserve) {

// 					System.out.println("Hotel Reservation System");
// 					System.out.println("Please Enter The Following Information: ");
// 					System.out.print("First Name: ");
// 					String firstName = scan.next();
// 					System.out.print("Last Name: ");
// 					String lastName = scan.next();
// 					String name = firstName + " " + lastName;
					
// 					boolean validRoom = false;
// 					TypeOfRoom roomType = TypeOfRoom.valueOf("SINGLE");
// 					System.out.println("Room Type(Single ($180 per night), Double ($220 per night), Suite ($380 per night), PentHouse ($2500 per night)): ");
// 					while(!validRoom) {
// 						String type = scan.next();
// 						try {
// 							roomType = TypeOfRoom.valueOf(type.toUpperCase());
// 							validRoom = true;
// 						    } catch (IllegalArgumentException ex) {  
// 						    	System.out.println("That is not a valid room type. Please select Single, Double, Suite, or Penthouse");
// 						  }
// 					}
					
// 					System.out.print("Start Day(Days From Today): ");
// 					int startDay = scan.nextInt();
// 					System.out.print("Duration (Days): ");
// 					int duration = scan.nextInt();
					
// 					Customer currentCustomer = new Customer(name, roomType, startDay, duration);

// 					if (hotel.reserveRoom(currentCustomer)) {
// 						System.out.println("The room is available, please choose one of the following options: ");
// 						System.out.println("1, No, I want to change my information");
// 						System.out.println("2, Yes, Done Reserving");

// 						int option = scan.nextInt();

// 						if (option == 1) {
// 							hotel.cancelReservation(currentCustomer.getCustomerID());
// 							System.out.println("You will be redirected to reenter your information");
// 						} else {
// 							System.out.println("Your reservation has been booked, and your receipt is below");
// 							hotel.printReceipt(currentCustomer);
// 							reserve = false;
// 						}
// 					} else {
// 						System.out.println(
// 								"The room you chose was not available during your intended stay, please reenter your information if you wish to make a different reservation");
// 						System.out.println();
// 						try {
// 							Thread.sleep(10);
// 						} catch (InterruptedException ex) {
// 							Thread.currentThread().interrupt();
// 						}
// 					}
// 				}

// 			}

// 			if (choice == 2) {
// 				System.out.print("Please enter your Customer ID: ");
// 				Integer customerID = scan.nextInt();
// 				if (hotel.cancelReservation(customerID)) {
// 					System.out.println("Reservation cancelled.");
// 				} else {
// 					System.out.println("Sorry, that Customer ID was not found.");
// 				}
// 			}

// 			if (choice == 3) {
// 				System.out.print("Please enter your Customer ID: ");
// 				Integer customerID = scan.nextInt();
// 				hotel.checkStatus(customerID);
// 			}

// 			System.out.println("Would you like to go back to the main menu or end the program?");
// 			System.out.println("1, Main Menu");
// 			System.out.println("2, End Program");
// 			int checkIfEnd = scan.nextInt();
// 			if (checkIfEnd != 1) {
// 				runAgain = false;
// 			}

// 		}
// 		System.out.println("Program Ended");
// 		scan.close();
// 	}

// }