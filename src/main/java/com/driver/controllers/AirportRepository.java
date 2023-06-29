package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {
    private Map<String,Airport> airportMap = new HashMap<>();
    private Map<Integer,Flight> flightMap = new HashMap<>();
    private Map<Integer,Passenger> passengerMap = new HashMap<>();
    private Map<Integer, List<Integer>> flight_passengers = new HashMap<>();
    public void addAirPort(Airport airport) {

        airportMap.put(airport.getAirportName(),airport);
    }

    private String compare(String a, String b){
        if(a.compareTo(b)==0) return a;
        else if (a.compareTo(b)==-1) {
            return a;
        }else return  b;
    }
    public String getLargestAirportName() {
        int term = 0;
        String portName = "";
        for(String port : airportMap.keySet()){
            int currPortTerminals = airportMap.get(port).getNoOfTerminals();
            if(currPortTerminals > term){
                term = currPortTerminals;
                portName = port;
            } else if (currPortTerminals == term) {
                portName = compare(portName,port);
            }
        }
        return portName;
    }

    public void addFlight(Flight flight) {
        flightMap.put(flight.getFlightId(), flight);
    }

    public void addPassenger(Passenger passenger) {
        passengerMap.put(passenger.getPassengerId(), passenger);
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        for(Integer id : flightMap.keySet()){
            if(id == flightId){
                City city = flightMap.get(flightId).getFromCity();
                for(String portname : airportMap.keySet()){
                    City city1 = airportMap.get(portname).getCity();
                    if(city1.equals(city)){
                        return portname;
                    }
                }
            }
        }
        return null;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double maxDist = (int)Double.MAX_VALUE;
        for(Integer id : flightMap.keySet()){
            City city = flightMap.get(id).getFromCity();
            City city1 = flightMap.get(id).getToCity();
            if(city.equals(fromCity) && city1.equals(toCity)){
                double duration = flightMap.get(id).getDuration();
                if(duration < maxDist){
                    maxDist = duration;
                }
            }
        }
        return maxDist;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        if(flight_passengers.containsKey(flightId)){
            List<Integer> passlist = flight_passengers.get(flightId);
            int maxSize = flightMap.get(flightId).getMaxCapacity();
            if(passlist.size() >= maxSize) return "FAILURE";
            else if (passlist.contains(passengerId)) {
                return "FAILURE";
            }
            passlist.add(passengerId);
            flight_passengers.put(flightId,passlist);
        }else{
            List<Integer> passlist = new ArrayList<>();
            passlist.add(passengerId);
            flight_passengers.put(flightId, passlist);
        }
        return "SUCCESS";
    }

    public String canacelATicket(Integer flightId, Integer passengerId) {
        if(flight_passengers.containsKey(flightId)){
            List<Integer> passlist = flight_passengers.get(flightId);
            if(passlist.contains(passengerId)) {
                passlist.remove(passengerId);
                flight_passengers.put(flightId, passlist);
                return "SUCCESS";
            }

        }
        return "FAILURE";
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flightMap.values());
    }
    public List<Integer> getPassengers(Integer flightId) {
        return flight_passengers.get(flightId);
    }
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int count = 0;
        for(Integer id : flight_passengers.keySet()){
            List<Integer> passlist = flight_passengers.get(id);
            for(Integer passid : passlist){
                if(passid == passengerId) count++;
            }
        }
        return count;
    }

    public int getCurrFlightSize(Integer flightId) {
        if(flight_passengers.containsKey(flightId)){
            return flight_passengers.get(flightId).size();
        }
        return 0;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int count = 0;
        for(Integer id : flightMap.keySet()){
            Date reqdate = flightMap.get(id).getFlightDate();
            if(reqdate.equals(date)){
                String fcity = String.valueOf(flightMap.get(id).getFromCity());
                String tcity = String.valueOf(flightMap.get(id).getToCity());
                if(airportMap.containsKey(airportName)) {
                    if (fcity.equals(airportName) || tcity.equals(airportName)) {
                        count += flight_passengers.get(id).size();
                    }
                }
            }
        }
        return count;
    }

    public Airport getAirport(String airportName) {
        return airportMap.get(airportName);
    }
}