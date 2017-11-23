import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

public class Main {

    public static void main (String[] args){

        System.out.println("Application initialized.");

        String pathToFile = args[0];
        String pathToFileCreate = args[1];

        int indexForIssueKey = 0;
        int indexForSummaryKey = 0;
        int index = 0;
        CsvReader products;
        String[] headers;
        List indexesForLoggedWork = new ArrayList();
        List<TicketLoggedWork> records = new ArrayList<TicketLoggedWork>();

        try {
            products = new CsvReader(pathToFile);
            products.readHeaders();
            headers = products.getHeaders();
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not found input file: " + args[0]);
            e.printStackTrace();
            return;
        }
        catch (IOException e1) {
            System.out.println("Could not read headers from file: " + args[0]);
            e1.printStackTrace();
            return;
        }

        System.out.println("Transformation started ...");
        for(String val : headers){
            if(val.equals("Issue key")){
                indexForIssueKey = index;
            }
            if(val.equals("Log Work")){
                indexesForLoggedWork.add(index);
            }
            if(val.equals("Summary")){
                indexForSummaryKey = index;
            }
            index++;
        }

        try {
            while (products.readRecord()) {
                List<String> workLogged = new ArrayList<String>();
                for (int i = 0; i < indexesForLoggedWork.size(); i++) {
                    if (!products.get((Integer) indexesForLoggedWork.get(i)).equalsIgnoreCase(""))
                        workLogged.add(products.get((Integer) indexesForLoggedWork.get(i)));
                }
                records.add(new TicketLoggedWork(products.get(indexForIssueKey), workLogged, products.get(indexForSummaryKey)));
            }
            products.close();
        }
        catch (IOException e2) {
            System.out.println("Error occured while working with file: " + args[0]);
            e2.printStackTrace();
            return;
        }

        for(TicketLoggedWork item : records){
            item.calculate();
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(pathToFileCreate));
        } catch (FileNotFoundException e3) {
            System.out.println("Could not create file: " + args[1]);
            e3.printStackTrace();
            return;
        }
        StringBuilder builder = new StringBuilder();
        String ColumnNamesList = "Number,Summary,Work Log Author, Time";
        builder.append(ColumnNamesList +"\n");

        for(TicketLoggedWork item : records){
            item.calculate();
            for (Map.Entry<String, String> entry : item.timeByAuthors.entrySet()) {
                builder.append(item.number + ',');
                builder.append(item.summary + ',');
                builder.append(entry.getKey() + ',');
                builder.append("\"=" + entry.getValue() + "/172800\"");
                builder.append('\n');
            }
        }
        pw.write(builder.toString());
        pw.close();
        System.out.println("Transformation completed!");
    }

}
