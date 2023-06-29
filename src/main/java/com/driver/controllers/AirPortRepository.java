package com.driver.controllers;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirPortRepository{
    Set<Airport> airports;
    Set<Flight> flights;
    Set<Passenger> passengers;
    Map<String, Airport> airportMap;
    Map<Integer, Flight> flightMap;
    Map<Integer, Passenger> passengerMap;
    Map<Integer, List<Integer>> ticketsMap;// flightId - List<PassengerId>
    Map<Integer, Integer> cancelledMap;// flightId - NumberOfPeopleCancelled
    public AirPortRepository(){
        this.airports = new HashSet<>();
        this.flights = new HashSet<>();
        this.passengers = new HashSet<>();

        this.airportMap = new HashMap<>();
        this.flightMap = new HashMap<>();
        this.passengerMap = new HashMap<>();
        this.ticketsMap = new HashMap<>();
        this.cancelledMap = new HashMap<>();
    }
    public void addAirport(Airport airport){
        airports.add(airport);
        airportMap.put(airport.getAirportName(), airport);
    }

    public Set<Airport> getAirports() {
        return airports;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public Airport getAirportByName(String airportName){
        for(Airport airport : airports){
            if(airport.getAirportName().equals(airportName))
                return airport;
        }
        return null;
    }

    public List<Flight> getFlightsOnDate(Date date){
        List<Flight> flightList = new ArrayList<>();
        for(Flight flight : flights){
            if(flight.getFlightDate().equals(date))
                flightList.add(flight);
        }

        return flightList;
    }

    public int getPassengerCount(int flightId) {
        List<Integer> passengerList = ticketsMap.get(flightId);
        return passengerList.size();
    }

    public List<Integer> getTickets(Integer flightId) {
        return ticketsMap.getOrDefault(flightId, new ArrayList<>());
    }

    public String bookATicket(Integer flightId, Integer passengerId){
        List<Integer> passengers = ticketsMap.getOrDefault(flightId, new ArrayList<>());

        Flight flight = flightMap.get(flightId);
        if(passengers.size() >= flight.getMaxCapacity() || passengers.contains(passengerId))
            return "FAILURE";

        passengers.add(passengerId);
        ticketsMap.put(flightId, passengers);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId){
        if(!ticketsMap.containsKey(flightId)) return "FAILURE";

        List<Integer> passengerList = ticketsMap.get(flightId);
        if(passengerList.contains(passengerId)){

            List<Integer> newList = new ArrayList<>();
            for(int id : passengerList){
                if(id != passengerId)
                    newList.add(id);
            }
            int removed = passengerList.size() - newList.size();
            cancelledMap.put(flightId, cancelledMap.getOrDefault(flightId, 0)+removed);

            ticketsMap.put(flightId, newList);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        int count = 0;
        for(Integer flightId : ticketsMap.keySet()){
            List<Integer> passengerList = ticketsMap.get(flightId);
            for(Integer id : passengerList){
                if(id == passengerId) count++;
            }
        }

        return count;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
        flightMap.put(flight.getFlightId(), flight);
    }

    public String getAirportNameFromFlightId(Integer flightId){
        Flight flight = null;
        for(Flight f : flights){
            if(f.getFlightId() == flightId){
                flight = f;
                break;
            }
        }

        if (flight == null) return null;

        City city = flight.getFromCity();
        for(Airport airport : airports){
            if(airport.getCity().equals(city))
                return airport.getAirportName();
        }
        return null;
    }

    public void addPassenger(Passenger passenger){
        passengers.add(passenger);
        passengerMap.put(passenger.getPassengerId(), passenger);
    }
}
