// package com.ishapirov.hotelreservation.hotel_objects;

// import java.util.HashMap;
// import java.util.LinkedList;



// public class Hotel {
// 	// create linklist for different types of rooms
// 	private Integer customers = 1;
// 	private Integer rooms = 4;
// 	private LinkedList<SingleRoom> singleList;
// 	private LinkedList<DoubleRoom> doubleList;
// 	private LinkedList<SuiteRoom> suiteList;
// 	private LinkedList<PentHouseRoom> pentHouseList;
// 	private HashMap<Integer,Customer> customersMap;
	
// 	public Hotel() 
// 	{
// 		singleList = new LinkedList<SingleRoom>(); 
// 		doubleList = new LinkedList<DoubleRoom>(); 
// 		suiteList = new LinkedList<SuiteRoom>(); 
// 		pentHouseList = new LinkedList<PentHouseRoom>();
// 		customersMap= new HashMap<>();
// 		for(int i = 1;i <= rooms;i= i+4) {
// 			singleList.add(new SingleRoom(i));
// 			doubleList.add(new DoubleRoom(i+1));
// 			suiteList.add(new SuiteRoom(i+2));
// 			pentHouseList.add(new PentHouseRoom(i+3));
// 		}
// 	}
	
// 	public void incCustomer() {
// 		customers = customers + 1;
// 	}
	
// 	public boolean reserveRoom(Customer currentCustomer) {
// 		TypeOfRoom typeOfRoom = currentCustomer.getType();

// 		Room roomForCustomer;
// 		switch(typeOfRoom) {
// 		case SINGLE: {
// 			for(int i = 0; i < rooms/4;i++)
// 			{
// 				if(singleList.get(i).checkIfAvailable(currentCustomer.getStartDay(),currentCustomer.getDuration())) {
// 					roomForCustomer = singleList.get(i);
// 					currentCustomer.setRoom(roomForCustomer);
// 					roomForCustomer.updateDates(currentCustomer.getStartDay(),currentCustomer.getDuration());
// 					currentCustomer.setCustomerID(customers);
// 					customers++;
// 					customersMap.put(currentCustomer.getCustomerID(),currentCustomer);
// 					return true;
// 				}
// 			}
// 			return false;
// 		}
// 		case DOUBLE:
// 			for(int i = 0; i < rooms/4;i++)
// 			{
// 				if(doubleList.get(i).checkIfAvailable(currentCustomer.getStartDay(),currentCustomer.getDuration())) {
// 					roomForCustomer = doubleList.get(i);
// 					currentCustomer.setRoom(roomForCustomer);
// 					roomForCustomer.updateDates(currentCustomer.getStartDay(),currentCustomer.getDuration());
// 					currentCustomer.setCustomerID(customers);
// 					customers++;
// 					customersMap.put(currentCustomer.getCustomerID(),currentCustomer);
// 					return true;
// 				}
// 			}
// 			return false;
			
// 		case SUITE:
// 			for(int i = 0; i < rooms/4;i++)
// 			{
// 				if(suiteList.get(i).checkIfAvailable(currentCustomer.getStartDay(),currentCustomer.getDuration())) {
// 					roomForCustomer = suiteList.get(i);
// 					currentCustomer.setRoom(roomForCustomer);
// 					roomForCustomer.updateDates(currentCustomer.getStartDay(),currentCustomer.getDuration());
// 					currentCustomer.setCustomerID(customers);
// 					customers++;
// 					customersMap.put(currentCustomer.getCustomerID(),currentCustomer);
// 					return true;
// 				}
// 			}
// 			return false;
	
// 		case PENTHOUSE:
// 			for(int i = 0; i < rooms/4;i++)
// 			{
// 				if(pentHouseList.get(i).checkIfAvailable(currentCustomer.getStartDay(),currentCustomer.getDuration())) {
// 					roomForCustomer = pentHouseList.get(i);
// 					currentCustomer.setRoom(roomForCustomer);
// 					roomForCustomer.updateDates(currentCustomer.getStartDay(),currentCustomer.getDuration());
// 					currentCustomer.setCustomerID(customers);
// 					customers++;
// 					customersMap.put(currentCustomer.getCustomerID(),currentCustomer);
// 					return true;
// 				}
// 			}
// 			return false;
			
// 		default:
// 			return false;
// 		}
// 	}
// 	public Integer getCustomers() {
// 		return customers;
// 	}
// 	public boolean cancelReservation(Integer customerID) {
// 		if(customersMap.containsKey(customerID)) {
// 		Customer cancelCustomer = customersMap.get(customerID);
// 		cancelCustomer.getRoom().clearDates(cancelCustomer.getStartDay(),cancelCustomer.getDuration());
// 		customersMap.remove(customerID);
// 		return true;
// 		}
// 		else
// 			return false;
// 	}
	
// 	public void updateCustomerDates(Customer currentCustomer,int startday, int duration) {
// 		currentCustomer.setStartDay(startday);
// 		currentCustomer.setDuration(duration);
// 	}

// 	public void printReceipt(Customer customer) {
// 		System.out.println(customer);
// 	}
	
// 	public void checkStatus(Integer customerID) {
// 		if(idExists(customerID))
// 		printReceipt(customersMap.get(customerID));
// 		else
// 			System.out.println("Sorry that ID does not exist.");
// 	}
	
// 	public boolean idExists(Integer customerID) {
// 		if(customersMap.containsKey(customerID))
// 			return true;
// 		else
// 			return false;
// 	}
// }