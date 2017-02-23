import java.util.ArrayList;

public class MetricUtils {

    public static double calculateCTR(double noOfClicks, double noOfImpressions){
        return (noOfClicks/noOfImpressions);
    }

    public static double calculateTotalCost(ArrayList<Double> clickCost){
        return clickCost.stream().mapToDouble(Double::doubleValue).sum(); //Returns the sum of all numbers in the list
    }
}
