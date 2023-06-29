package com.driver.controllers;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
public class AirportService{

    AirPortRepository airPortRepository = new AirPortRepository();

    public void addAirport(Airport airport) {
        airPortRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        Set<Airport> airportSet = airPortRepository.getAirports();

        int maxTerminal = 0;
        for(Airport airport : airportSet){
            maxTerminal = Math.max(maxTerminal, airport.getNoOfTerminals());
        }

        ArrayList<String> list = new ArrayList<>();
        for(Airport airport : airportSet){
            if(airport.getNoOfTerminals() == maxTerminal)
                list.add(airport.getAirportName());
        }

        Collections.sort(list);
        return list.get(0);
    }

    public double getDurationBetweenCities(City fromCity, City toCity) {
        Set<Flight> flights = airPortRepository.getFlights();

        boolean hasDirectFlight = false;
        double duration = Double.MAX_VALUE;

        for(Flight flight : flights){
            if(flight.getFromCity() == fromCity && flight.getToCity() == toCity){
                hasDirectFlight = true;
                duration = Double.min(duration, flight.getDuration());
            }
        }
        if(!hasDirectFlight) return -1;
        return duration;
    }

    public int getNumberOfPeopleOn(Date date, String airportName){
        Airport airport = airPortRepository.getAirportByName(airportName);
        List<Flight> flightsOnThatDate = airPortRepository.getFlightsOnDate(date);
        if(flightsOnThatDate.isEmpty()) return 0;

        int count = 0;

        for(Flight flight : flightsOnThatDate){
            City city = airport.getCity();
            if(flight.getFromCity().equals(city) || flight.getToCity().equals(city)){
                List<Integer> passengerList = airPortRepository.getTickets(flight.getFlightId());
                count += passengerList.size();
            }
        }

        return count;
    }

    public int calculateFlightFare(Integer flightId){
        List<Integer> alreadyBooked = airPortRepository.getTickets(flightId);
        int total = alreadyBooked.size();

        return (3000 + total*50);
    }

    public String bookATicket(Integer flightId, Integer passengerId){
        return airPortRepository.bookATicket(flightId, passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        return airPortRepository.cancelATicket(flightId, passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        return airPortRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public void addFlight(Flight flight) {
        airPortRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId){
        return airPortRepository.getAirportNameFromFlightId(flightId);
    }

    public void addPassenger(Passenger passenger) {
        airPortRepository.addPassenger(passenger);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        int passengerCount = airPortRepository.getPassengerCount(flightId);
        int total = passengerCount * 3000;
        int additional = 0;
        for(int i = 0; i < passengerCount-1; i++){
            additional += 50 * additional;
        }

        return total+additional;
    }
}
