package com.amitKundu.tourmate.Classes;

public class IndividualTrip {

    private String trip_id;
    private String trip_Name;
    private String trip_Description;
    private String trip_fromDate;
    private String trip_toDate;
    private String trip_StartPlace;
    private String trip_Budget;

    public IndividualTrip() {
    }

    public IndividualTrip(String trip_id, String trip_Name, String trip_Description, String trip_fromDate, String trip_toDate, String trip_StartPlace, String trip_Budget) {
        this.trip_id = trip_id;
        this.trip_Name = trip_Name;
        this.trip_Description = trip_Description;
        this.trip_fromDate = trip_fromDate;
        this.trip_toDate = trip_toDate;
        this.trip_StartPlace = trip_StartPlace;
        this.trip_Budget = trip_Budget;
    }

    public IndividualTrip(String trip_Name, String trip_Description, String trip_fromDate, String trip_toDate, String trip_StartPlace, String trip_Budget) {
        this.trip_Name = trip_Name;
        this.trip_Description = trip_Description;
        this.trip_fromDate = trip_fromDate;
        this.trip_toDate = trip_toDate;
        this.trip_StartPlace = trip_StartPlace;
        this.trip_Budget = trip_Budget;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_Name() {
        return trip_Name;
    }

    public void setTrip_Name(String trip_Name) {
        this.trip_Name = trip_Name;
    }

    public String getTrip_Description() {
        return trip_Description;
    }

    public void setTrip_Description(String trip_Description) {
        this.trip_Description = trip_Description;
    }

    public String getTrip_fromDate() {
        return trip_fromDate;
    }

    public void setTrip_fromDate(String trip_fromDate) {
        this.trip_fromDate = trip_fromDate;
    }

    public String getTrip_toDate() {
        return trip_toDate;
    }

    public void setTrip_toDate(String trip_toDate) {
        this.trip_toDate = trip_toDate;
    }

    public String getTrip_StartPlace() {
        return trip_StartPlace;
    }

    public void setTrip_StartPlace(String trip_StartPlace) {
        this.trip_StartPlace = trip_StartPlace;
    }

    public String getTrip_Budget() {
        return trip_Budget;
    }

    public void setTrip_Budget(String trip_Budget) {
        this.trip_Budget = trip_Budget;
    }
}