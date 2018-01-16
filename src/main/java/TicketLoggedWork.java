import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketLoggedWork {
    String number;
    String summary;
    List<String> loggedWork;
    List<String> loggedWorkFinal;

    class RecordInfo{
        String time;
        String day;
        String month;
        String year;
    }

    Map<String, String> timeByAuthors = new HashMap<String, String>();
    Map<String, RecordInfo> timesRecord = new HashMap<String, RecordInfo>();


    void calculate(){
        for(String comment : loggedWork){
            String name;
            String time;

            String[] parts = comment.split(";");

            name = parts[parts.length - 2];
            time = parts[parts.length - 1];



            if(timeByAuthors.containsKey(name)){
                String a = timeByAuthors.get(name);
                timeByAuthors.put(name, String.valueOf(Integer.parseInt(a) + Integer.parseInt(time)));
            }
            else{
                timeByAuthors.put(name, String.valueOf(Integer.parseInt(time)));
            }
        }
    }

    void calculate(String month){

        for(String comment : loggedWork){
            String name;
            String time;

            String[] parts = comment.split(";");

            name = parts[parts.length - 2];
            time = parts[parts.length - 1];

            if(month.equalsIgnoreCase(parts[parts.length - 3].split("/")[1])) {
                if(timeByAuthors.containsKey(name)){
                    String a = timeByAuthors.get(name);
                    timeByAuthors.put(name, String.valueOf(Integer.parseInt(a) + Integer.parseInt(time)));
                }
                else{
                    timeByAuthors.put(name, String.valueOf(Integer.parseInt(time)));
                }
            }
        }
    }

    TicketLoggedWork(){
        summary = "";
        number = "";
        loggedWork = new ArrayList<String>();
    }

    TicketLoggedWork(String numb, List<String> work){
        number = numb;
        loggedWork = work;
    }

    TicketLoggedWork(String numb, List<String> work, String sum){
        number = numb;
        loggedWork = work;
        summary = "\"" + sum + "\"";
    }

    void addRecordNumber(String record){
        this.number = record;
    }

    void addRecordSummary(String summary){
        this.summary = summary;
    }

    void addRecordLoggedWork(String work){
        this.loggedWork.add(work);
    }
}
