import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleg_Dudar on 21-Nov-17.
 */
public class TicketLoggedWork {
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
