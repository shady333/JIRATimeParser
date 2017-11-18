import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

/**
 * Created by Oleg_Dudar on 17-Nov-17.
 */
public class Main {

    public static void main(String[] args){
        System.out.println("Hello");

//        String pathToFile = args[0];

//        String pathToFileCreate = args[1];

//        String pathToFile = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\Logged work Automation current Month (JIRA).csv";
        String pathToFile = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\LoggedworkAutomationcurrentMonth(JIRA).csv";

        String pathToFileCreate = "C:\\Users\\Oleg_Dudar\\Documents\\Projects\\CTCO-MDAT\\ReportMonth_17112017.csv";

        try {

            CsvReader products = new CsvReader(pathToFile);

            products.readHeaders();

            int indexForIssueKey = 0;
            List indexesForLoggedWork = new ArrayList();

            String[] headers = products.getHeaders();

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



            List<String> loggedWork;

                    class TicketLoggedWork {
                        String number;
                        List<String> loggedWork;

                        Map<String, Integer> timeByAuthors = new HashMap<String, Integer>();

                        void calculate(){
                            for(String comment : loggedWork){
                                String name;
                                String time;

                                String[] parts = comment.split(";");

                                name = parts[parts.length - 2];
                                time = parts[parts.length - 1];

                                if(timeByAuthors.containsKey(name)){
                                    Integer a = timeByAuthors.get(name);
                                    timeByAuthors.put(name, a + Integer.parseInt(time));
                                }
                                else{
                                    timeByAuthors.put(name, Integer.parseInt(time));
                                }
                            }
                        }

                        TicketLoggedWork(){
                            number = "";
                            loggedWork = new ArrayList<String>();
                        }

                        TicketLoggedWork(String numb, List<String> work){
                            number = numb;
                            loggedWork = work;
                        }

                        void addRecordNumber(String record){
                            number = record;
                        }

                        void addRecordLoggedWork(String work){
                            loggedWork.add(work);
                        }
                    }

                    class Author{
                        String name;

                    }

            List<TicketLoggedWork> records = new ArrayList<TicketLoggedWork>();

            //reading records
            while (products.readRecord())
            {
                List<String> workLogged = new ArrayList<String>();
                String number = products.get(indexForIssueKey);
                for(int i=0; i < indexesForLoggedWork.size(); i++){
                    if(!products.get((Integer) indexesForLoggedWork.get(i)).equalsIgnoreCase(""))
                        workLogged.add(products.get((Integer) indexesForLoggedWork.get(i)));
                }



                records.add(new TicketLoggedWork(number, workLogged));






                // perform program logic here
//                System.out.println(issueKey + ":" + logWork);
            }

            products.close();


            for(TicketLoggedWork item : records){
                item.calculate();
                System.out.println("Number: " + item.number);
                for (Map.Entry<String, Integer> entry : item.timeByAuthors.entrySet()) {
                    System.out.println(entry.getKey()+" : "+entry.getValue());
                }
            }



            //Write files
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new File(pathToFileCreate));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StringBuilder builder = new StringBuilder();
            String ColumnNamesList = "Number,Work Log Author, Time";
// No need give the headers Like: id, Name on builder.append
            builder.append(ColumnNamesList +"\n");
//            builder.append("1"+",");
//            builder.append("Chola");
//            builder.append('\n');


            for(TicketLoggedWork item : records){
                item.calculate();
                System.out.println("Number: " + item.number);
                for (Map.Entry<String, Integer> entry : item.timeByAuthors.entrySet()) {
                    System.out.println(entry.getKey()+" : "+entry.getValue());

                    builder.append(item.number + ',');
                    builder.append(entry.getKey() + ',');
//                    builder.append("=" + entry.getValue() + "/86400");
                    builder.append(entry.getValue());
                    builder.append('\n');
                }
            }

            pw.write(builder.toString());

            pw.close();
            System.out.println("done!");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
