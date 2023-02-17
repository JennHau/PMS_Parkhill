/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;



/**
 *
 * @author wongj
 */
public class FacilityBookingPaymentByBooking extends Facility{

    String totalPrice;
    
    // calculation for booking fee charge by per booking
    @Override
    public void calculateBookingFee() {
        totalPrice = this.getPrice();
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }
}
