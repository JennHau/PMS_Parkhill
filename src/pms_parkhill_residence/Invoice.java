/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author wongj
 */
public class Invoice {
    private String invoiceNo;
    private String unitNo;
    private String feeType;
    private String unitCategory;
    private String consumption;
    private String unit;
    private Float unitPrice;
    private Float totalPrice;
    private String period;
    private String issuedDate;
    private String deleteID;
    private List<String> availableFees;
    
    private FileHandling fh = new FileHandling();
    protected TextFile TF = new TextFile();
    
    public Invoice() {}
    
    public Invoice(String invoiceNo) {
        List<String> availableList = fh.fileRead("invoices.txt");
        availableFees = new ArrayList<>();
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] invoiceDetails = availableList.get(i).split(";");
            String eInvoiceNo = invoiceDetails[0];
            String unitNo = invoiceDetails[1];
            
            if(eInvoiceNo.equals(invoiceNo)) {
                this.invoiceNo = invoiceNo;
                this.unitNo = unitNo;
                availableFees.add(availableList.get(i));
            }
        }
    }
    
    public Invoice(String invoiceNo, String unitNo, String feeType,
            String unitCategory, String consumption, String unit, String unitPrice,
            String totalPrice, String period, String issuedDate, String deleteID) {
        this.invoiceNo = invoiceNo;
        this.unitNo = unitNo;
        this.feeType = feeType;
        this.unitCategory = unitCategory;
        this.consumption = consumption;
        this.unit = unit;
        this.unitPrice = Float.parseFloat(unitPrice);
        this.totalPrice = Float.parseFloat(totalPrice);
        this.period = period;
        this.issuedDate = issuedDate;
        this.deleteID = deleteID;
    }
    
    public Invoice(String[] invoiceData) {
        this.invoiceNo = invoiceData[0];
        this.unitNo = invoiceData[1];
        this.feeType = invoiceData[2];
        this.unitCategory = invoiceData[3];
        this.consumption = invoiceData[4];
        this.unit = invoiceData[5];
        this.unitPrice = Float.parseFloat(invoiceData[6]);
        this.totalPrice = Float.parseFloat(invoiceData[7]);
        this.period = invoiceData[8];
        this.issuedDate = invoiceData[9];
        this.deleteID = invoiceData[10];
    }
    
    // store new invoice into text file
    public void issueInvoice() {
        List<String> newData = new ArrayList<>();
        newData.add(invoiceNo +";"+ unitNo +";"+ feeType +";"+ unitCategory +";"+
            consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
            period +";"+ issuedDate+";"+ deleteID +";");
        fh.fileWrite("invoices.txt", true, newData);
    }
    
    // extract all specific fee type invoice details
    public List<String> displayInvoiceDetails(String feeTypeName, String target,
            String status, String monthYear) {

        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        String cUnit = "";
        String cUnitPrice = "";
        for (int i = 1; i < feeTypesList.size(); i++) {

            String[] feeTypesDetails = feeTypesArray[i].split(";");
            String feeType = feeTypesDetails[0];
            String eTarget = feeTypesDetails[1];
            String unit = feeTypesDetails[2];
            String unitPrice = feeTypesDetails[3];

            if (feeType.equals(feeTypeName) && eTarget.equals(target)) {
                cUnit = unit;
                cUnitPrice = unitPrice;
            }
        }

        // get data for invoice that havent issue
        List<String> availableInvoices = new ArrayList<String>();
        if (status.equals("PENDING")) {
            List<String> propertyDetailsList = fh.fileRead("propertyDetails.txt");
            String[] propertyDetailsArray = new String[propertyDetailsList.size()];
            propertyDetailsList.toArray(propertyDetailsArray);
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/"))
                    + monthYear.substring(monthYear.indexOf("/") + 1);

            for (int i = 1; i < propertyDetailsList.size(); i++) {
                boolean check = false;
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                String squareFeet = propertyDetails[2];

                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int j = 1; j < invoicesList.size(); j++) {
                    String[] invoiceDetails = invoicesArray[j].split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String eFeeType = invoiceDetails[2];
                    if (eInvoiceNo.equals(eUnitNo + cMonthYear)
                            && eFeeType.equals(feeTypeName)) {
                        check = true;
                    }
                }
                if (check == false && eTarget.equals(target)) {
                    if (cUnit.equals("Square Feet")) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        float totalPrice = Float.valueOf(squareFeet) * Float.valueOf(cUnitPrice);
                        String totalPrice2 = df.format(totalPrice);
                        String invoiceLine = eUnitNo + ";" + squareFeet + ";"
                                + cUnit + ";" + cUnitPrice + ";" + String.valueOf(totalPrice2);
                        availableInvoices.add(invoiceLine);
                    } else {
                        float consump = (new Random().nextFloat() * (250.00f - 0.00f)) + 0.00f;
                        DecimalFormat df = new DecimalFormat("0.00");
                        float totalPrice = consump * Float.valueOf(cUnitPrice);
                        String totalPrice2 = df.format(totalPrice);
                        String consump2 = df.format(consump);
                        String invoiceLine = eUnitNo + ";" + consump2 + ";"
                                + cUnit + ";" + cUnitPrice + ";" + String.valueOf(totalPrice2);
                        availableInvoices.add(invoiceLine);
                    }
                }
            }
        // get data for invoice that issued    
        } else if (status.equals("ISSUED")) {
            List<String> propertyDetailsList = fh.fileRead("propertyDetails.txt");
            String[] propertyDetailsArray = new String[propertyDetailsList.size()];
            propertyDetailsList.toArray(propertyDetailsArray);

            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/"))
                    + monthYear.substring(monthYear.indexOf("/") + 1);

            for (int i = 1; i < propertyDetailsList.size(); i++) {
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];

                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int j = 1; j < invoicesList.size(); j++) {
                    String[] invoiceDetails = invoicesArray[j].split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String unitNo = invoiceDetails[1];
                    String eFeeType = invoiceDetails[2];
                    String consump = invoiceDetails[4];
                    String unit = invoiceDetails[5];
                    String unitPrice = invoiceDetails[6];
                    String totalPrice = invoiceDetails[7];

                    if (eInvoiceNo.equals(eUnitNo + cMonthYear)
                            && eTarget.equals(target) && eFeeType.equals(feeTypeName)) {
                        String invoiceLine = unitNo + ";" + consump + ";"
                                + unit + ";" + unitPrice + ";" + totalPrice;
                        availableInvoices.add(invoiceLine);
                    }
                }
            }
        }
        return availableInvoices;
    }
    
    // method to extract specific invoice details
    public List<String> extractOneInvoiceDetails(Invoice INV) {
        List<String> RavailableFees = new ArrayList<>();
        
        for (int i=0; i<INV.availableFees.size(); i++) {
            String[] paymentDetails = INV.availableFees.get(i).split(";");
            String eInvoiceNo = paymentDetails[0];
            String feeType = paymentDetails[2];
            String issueDate = paymentDetails[9];
            String consump = paymentDetails[4];
            String unit = paymentDetails[5];
            String unitPrice = paymentDetails[6];
            String totalPrice = paymentDetails[7];
            
            RavailableFees.add(feeType +";"+ issueDate +";"+ consump +";"+
                    unit +";"+ unitPrice +";"+ totalPrice +";");
        } 
        return RavailableFees;
    }
    
    public ArrayList<Invoice> getCurrentUnitInvoice(String unitNo) {
        ArrayList<Invoice> incompleteInvoice = new ArrayList<>();     
        
        List<String> invoiceFile = fh.fileRead(TF.invoiceFile);
        
        boolean firstLine = true;
        for (String eachInv : invoiceFile) {
            if (!firstLine) {
                String[] invDet = eachInv.split(TF.sp);
            
                Invoice invoice = new Invoice(invDet);

                if (invoice.getUnitNo().equals(unitNo)) {
                    if (invoice.getDeleteID().equals(TF.empty)) {
                        incompleteInvoice.add(invoice);
                    }
                }
            }
            
            firstLine = false;
        }
        
        return incompleteInvoice;
    }
    
    public ArrayList getInvoiceCode(String unitNo) {
        ArrayList<String> invoiceCode = new ArrayList<>();

        ArrayList<Invoice> invoices = getCurrentUnitInvoice(unitNo);
        
        for (Invoice eachInv : invoices) {
            if (!invoiceCode.contains(eachInv.getInvoiceNo())) {
                invoiceCode.add(eachInv.getInvoiceNo());
            }
        }
        
        return invoiceCode;
    }
    
    // convert today date to dd/MM/yyyy format
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
    public double getTotalPricePerInvoice(String invoiceId, ArrayList<Invoice> dataList) {
        double totalAmount = 0;
        for (Invoice eachInv : dataList) {
            
            if (eachInv.getInvoiceNo().equals(invoiceId)) {
                double eachPrice = eachInv.getTotalPrice();
                totalAmount += eachPrice;
            }
        }
        
        return totalAmount;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    /**
     * @return the feeType
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * @param feeType the feeType to set
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    /**
     * @return the unitCategory
     */
    public String getUnitCategory() {
        return unitCategory;
    }

    /**
     * @param unitCategory the unitCategory to set
     */
    public void setUnitCategory(String unitCategory) {
        this.unitCategory = unitCategory;
    }

    /**
     * @return the consumption
     */
    public String getConsumption() {
        return consumption;
    }

    /**
     * @param consumption the consumption to set
     */
    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the unitPrice
     */
    public Float getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the totalPrice
     */
    public Float getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * @return the issuedDate
     */
    public String getIssuedDate() {
        return issuedDate;
    }

    /**
     * @param issuedDate the issuedDate to set
     */
    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * @return the deleteID
     */
    public String getDeleteID() {
        return deleteID;
    }

    /**
     * @param deleteID the deleteID to set
     */
    public void setDeleteID(String deleteID) {
        this.deleteID = deleteID;
    }
}
