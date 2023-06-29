package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AirportService {
    private AirportRepository airportRepository = new AirportRepository();

    public void addAirPort(Airport airport) {
        airportRepository.addAirPort(airport);
    }

    public String getLargestAirportName() {
        String str = airportRepository.getLargestAirportName();
        return str;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double ans = airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);
        if(ans == (int)Double.MAX_VALUE) return -1;
        return ans;
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        return airportRepository.bookATicket(flightId,passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        return airportRepository.canacelATicket(flightId, passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        int curr_flight_size = airportRepository.getCurrFlightSize(flightId);
        int total3000 = curr_flight_size * 3000;
        int addtional50 = 0;
        for(int i=0; i<curr_flight_size-1; i++){
            addtional50 += 50 + addtional50;
        }
        int totalrevenue = total3000 + addtional50;
        return  totalrevenue;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Airport airport = airportRepository.getAirport(airportName);
        List<Flight> flightList = airportRepository.getAllFlights();
        int count = 0;
        if(airport != null){
            City city = airport.getCity();
            for(Flight flight : flightList){
                if(date.equals(flight.getFlightDate())){
                    if(city.equals(flight.getToCity()) || city.equals(flight.getFromCity())){
                        Integer flightId = flight.getFlightId();
                        List<Integer> list = airportRepository.getPassengers(flightId);
                        if(list != null){
                            count += list.size();
                        }
                    }
                }
            }
        }
        return count;
    }

    public int calculateFlightFare(Integer flightId) {
        int curr_flight_size = airportRepository.getCurrFlightSize(flightId);
        int next_person_price = (curr_flight_size * 50) + 3000;
        return next_person_price;
    }
}