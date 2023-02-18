/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author wongj
 */
public class Dashboard {

    FileHandling fh = new FileHandling();
    SecurityGuard sg = new SecurityGuard();

    
//    To count the number of checkpoint that hvt check
    public Integer count_uncheck_checkpoint() {
        
        String patrollingScheduleFileFormat = "patrollingScheduleFiles/patrollingFile_";
//        sg.currentdate()
        List<String> row = fh.fileRead(patrollingScheduleFileFormat + "2023-01-31.txt");
        String[] rowary = new String[row.size()];
        row.toArray(rowary);
        int number = 0;
        for (int i = 1; i < rowary.length; i++) {
            String line = rowary[i].toString().trim();
            line.toUpperCase().split(";");
            String[] line_split = line.toUpperCase().split(";");

            if (!line_split[9].equalsIgnoreCase("CHECKED")) {
                number++;
            }
        }
//         System.out.println(number);
        return number;
    }

    //    To count the number of visitor that will come on the current date
    public Integer count_today_visitor() {
        sg.currentdate().toString();
        List<String> row = fh.fileRead("visitorpass.txt");
        String[] rowary = new String[row.size()];
        row.toArray(rowary);
        int number = 0;
        for (int i = 1; i < rowary.length; i++) {
            String line = rowary[i].toString().trim();
            line.toUpperCase().split(";");
            String[] line_split = line.toUpperCase().split(";");

            if (line_split[5].equalsIgnoreCase(sg.currentdate().toString())) {
                number++;
            }
        }
         System.out.println(number);
        return number;
    }

    //    To count the number of checkpoint that check
    public Integer count_checked_checkpoint() {
        String patrollingScheduleFileFormat = "patrollingScheduleFiles/patrollingFile_";
//        sg.currentdate()
        List<String> row = fh.fileRead(patrollingScheduleFileFormat + "2023-01-31.txt");
        String[] rowary = new String[row.size()];
        row.toArray(rowary);
        int number = 0;
        for (int i = 1; i < rowary.length; i++) {
            String line = rowary[i].toString().trim();
            line.toUpperCase().split(";");
            String[] line_split = line.toUpperCase().split(";");

            if (line_split[9].equalsIgnoreCase("CHECKED")) {
                number++;
            }
        }
//         System.out.println(number);
        return number;
    }

//    interface 
    public class numberincident {

        int pending = 0;
        int progess = 0;
        int complete = 0;
    }

    
//    count number of incident
    public numberincident count_number_incident() {
        numberincident n = new numberincident();

        List<String> row = fh.fileRead("SG_Incident.txt");
        String[] rowary = new String[row.size()];
        row.toArray(rowary);
//        int pending = 0;
//        int progess = 0;
//        int complete = 0;

        for (int i = 1; i < rowary.length; i++) {
            String line = rowary[i].toString().trim();
            line.toUpperCase().split(";");
            String[] line_split = line.toUpperCase().split(";");

            if (line_split[4].equalsIgnoreCase("PENDING")) {
                n.pending++;
            } else if (line_split[4].equalsIgnoreCase("progressing")) {
                n.progess++;
            } else {
                n.complete++;
            }
        }
//         System.out.println(number);
        return n;
    }

    // calculate total amount of unit (Pass "Residential" or "Commercial")
    public Integer countTotalUnit(String type) {
        List<String> unitList = fh.fileRead("propertyDetails.txt");

        int total = 0;
        for (int i = 1; i < unitList.size(); i++) {
            String[] unitDetails = unitList.get(i).split(";");
            String eType = unitDetails[1];
            if (eType.equals(type)) {
                total++;
            }
        }
        return total;
    }

    // calculate total amount of Tenant/Resident (Pass "Tenant" or "Resident")
    public Integer countTotalTenantResident(String type) {
        List<String> userList = fh.fileRead("userProfile.txt");

        int total = 0;
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userList.get(i).split(";");
            String userID = userDetails[0];
            if (type.equals("Tenant") && (userID.startsWith("tnt") || userID.startsWith("vdr"))) {
                total++;
            } else if (type.equals("Resident") && userID.startsWith("rsd")) {
                total++;
            }
        }
        return total;
    }

    public JFreeChart createLineChart(List<String> x, List<String> y, String title, String xName, String yName) {
        // create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // declare arrayList to store intake code

        for (int i = 0; i < x.size(); i++) {
            dataset.setValue(Double.parseDouble(y.get(i)), yName, x.get(i));
        }

        //create chart
        JFreeChart linechart = ChartFactory.createLineChart(title, xName, yName,
                dataset, PlotOrientation.VERTICAL, false, true, false);

        // create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        // create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(13, 50, 79);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        return linechart;
    }

    public JFreeChart createBarChart(List<String> x, List<String> y, String title, String xName, String yName) {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int NA = 0;

        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < x.size(); i++) {
            dataset.setValue(Double.valueOf(y.get(i)), yName, x.get(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(title, xName, yName,
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setRangeGridlinePaint(Color.BLUE);
        categoryPlot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color clr3 = new Color(13, 50, 79);
        renderer.setSeriesPaint(0, clr3);

        return chart;
    }

    public JFreeChart createPieChart(List<String> variables, List<Integer> frequency, String title) {
        // calculate total frequency
        int total = 0;
        for (int num : frequency) {
            total += num;
        }
        //create dataset
        DefaultPieDataset barDataset = new DefaultPieDataset();
        for (int i = 0; i < variables.size(); i++) {
            double percentage = ((double) frequency.get(i) / (double) total) * 100;
            barDataset.setValue(variables.get(i) + "(" + frequency.get(i) + ")", percentage);
        }

        //create chart
        JFreeChart piechart = ChartFactory.createPieChart(title, barDataset, false, true, false);//explain
        PiePlot piePlot = (PiePlot) piechart.getPlot();

        //changing pie chart blocks colors
        Integer[] R = {13, 13, 0, 201, 254, 0};
        Integer[] G = {24, 50, 99, 231, 128};
        Integer[] B = {42, 79, 178, 254, 170};

        for (int i = 0; i < variables.size(); i++) {
            if (i <= R.length) {
                piePlot.setSectionPaint(variables.get(i) + "(" + frequency.get(i) + ")", new Color(R[i], G[i], B[i]));
            } else {
                piePlot.setSectionPaint(variables.get(i) + "(" + frequency.get(i) + ")", new Color(102, 102, 102));
            }
        }

        piePlot.setBackgroundPaint(Color.white);

        return piechart;
    }
}
