/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;



/**
 *
 * @author wongj
 */
public class FacilityBookingPaymentByBooking extends Facility{

    String totalPrice;
    
    @Override
    public void calculateBookingFee() {
        totalPrice = this.getPrice();
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }
}
