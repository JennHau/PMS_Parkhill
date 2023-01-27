/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.List;

/**
 *
 * @author wongj
 */
public abstract class Facility {
    private String facilityID;
    private String facilityName;
    private boolean booking;
    private boolean payment;
    private String price;
    private String priceUnit;
    private String startTime;
    private String endTime;
    private Integer quantity;
    private String active;
    FileHandling fh = new FileHandling();
    
    public Facility() {}
    
    public void setFacilityDetails(String facilityID) {
        
        List<String> availableList = fh.fileRead("facility.txt");
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] employeeDetails = availableList.get(i).split(";");
            String eFacilityID = employeeDetails[0];
            String facilityName = employeeDetails[1];
            String booking = employeeDetails[2];
            String payment  = employeeDetails[3];
            String price = employeeDetails[4];
            String priceUnit = employeeDetails[5];
            String startTime = employeeDetails[6];
            String endTime = employeeDetails[7];
            Integer quantity = Integer.valueOf(employeeDetails[8]);
            String active = employeeDetails[9];
            
            if(eFacilityID.equals(facilityID.toLowerCase())) {
                this.facilityID = eFacilityID;
                this.facilityName = facilityName;
                this.booking = Boolean.valueOf(booking);
                this.payment = Boolean.valueOf(payment);
                this.price = price;
                this.priceUnit = priceUnit;
                this.startTime = startTime;
                this.endTime = endTime;
                this.quantity = quantity;
                this.active = active;
            }
        }
        
    }

    public abstract void calculateBookingFee();
    
    /**
     * @return the facilityID
     */
    public String getFacilityID() {
        return facilityID;
    }

    /**
     * @param facilityID the facilityID to set
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * @return the facilityName
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     * @param facilityName the facilityName to set
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     * @return the booking
     */
    public boolean isBooking() {
        return booking;
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(boolean booking) {
        this.booking = booking;
    }

    /**
     * @return the payment
     */
    public boolean isPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the priceUnit
     */
    public String getPriceUnit() {
        return priceUnit;
    }

    /**
     * @param priceUnit the priceUnit to set
     */
    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }
}
