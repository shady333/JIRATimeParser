import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello");

//        String pathToFile = args[0];

//        String pathToFileCreate = args[1];

//        String pathToFile = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\Logged work Automation current Month (JIRA).csv";
        String pathToFile = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\LoggedworkAutomationcurrentMonth(JIRA).csv";

        String pathToFileCreate = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\ReportMonth_17112017.csv";

        CsvReader products;
        String[] headers;

        try {

            products = new CsvReader(pathToFile);

        } catch (FileNotFoundException e) {
            System.out.println("Could not found input file: " + args[0]);
            e.printStackTrace();
            return;
        }
        try {
            products.readHeaders();
            headers = products.getHeaders();
        } catch (IOException e1) {
            System.out.println("Could not read headers from file: " + args[0]);
            e1.printStackTrace();
            return;
        }

        int indexForIssueKey = 0;
            List indexesForLoggedWork = new ArrayList();



        int index = 0;
        for(String val : headers){
            if(val.equals("Issue key")){
                indexForIssueKey = index;
            }
            if(val.equals("Log Work")){
                indexesForLoggedWork.add(index);
            }
            index++;
        }

        List<TicketLoggedWork> records = new ArrayList<TicketLoggedWork>();

        try {
            while (products.readRecord()) {
                List<String> workLogged = new ArrayList<String>();
                String number = products.get(indexForIssueKey);
                for (int i = 0; i < indexesForLoggedWork.size(); i++) {
                    if (!products.get((Integer) indexesForLoggedWork.get(i)).equalsIgnoreCase(""))
                        workLogged.add(products.get((Integer) indexesForLoggedWork.get(i)));
                }
                records.add(new TicketLoggedWork(number, workLogged));
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
                System.out.println("Number: " + item.number);
                for (Map.Entry<String, Integer> entry : item.timeByAuthors.entrySet()) {
                    System.out.println(entry.getKey()+" : "+entry.getValue());
                }
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
            String ColumnNamesList = "Number,Work Log Author, Time";
            builder.append(ColumnNamesList +"\n");

            for(TicketLoggedWork item : records){
                item.calculate();
                System.out.println("Number: " + item.number);
                for (Map.Entry<String, Integer> entry : item.timeByAuthors.entrySet()) {
                    System.out.println(entry.getKey()+" : "+entry.getValue());

                    builder.append(item.number + ',');
                    builder.append(entry.getKey() + ',');
                    builder.append(entry.getValue());
                    builder.append('\n');
                }
            }
            pw.write(builder.toString());
            pw.close();
            System.out.println("Transformation completed!");
    }

}
