/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.text.DecimalFormat;


/**
 *
 * @author wongj
 */
public class FacilityBookingPaymentByHour extends Facility{

    int hour; String totalPrice;
    
    // calculation for booking fee charge by per hour
    @Override
    public void calculateBookingFee() {
        float bTotalPrice = hour * Float.valueOf(this.getPrice());
        DecimalFormat df = new DecimalFormat("0.00");
        totalPrice = df.format(bTotalPrice);
    }
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }
}
