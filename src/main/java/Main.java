import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.csvreader.CsvReader;
import com.dudar.ConnectionData;
import com.dudar.JIRA_Accessor;

import javax.security.sasl.AuthenticationException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application{

    public static void main (String[] args) {

        launch(args);

//        JIRA_Accessor accessor = new JIRA_Accessor();
//
//        try {
//            accessor.testResponse(PropertiesReader.readPropertiesToCredentiasData());
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//
//        doTheJob(args);


    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JIRA Worklog Time Parser");

        String url = "";
        String user = "";
        String pass = "";

        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(Main.class.getClassLoader().getResourceAsStream("credentials.properties"));

            //get the property value and print it out
            url = prop.getProperty("url");
            user = prop.getProperty("user_name");
            pass = prop.getProperty("user_pass");

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }



        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label jiraUrl = new Label("JIRA URL:");
        grid.add(jiraUrl, 0, 1);

        TextField urlTextField = new TextField();
        urlTextField.setText(url);
        grid.add(urlTextField, 1, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 2);

        TextField userTextField = new TextField();
        userTextField.setText(user);
        grid.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 3);

        PasswordField pwBox = new PasswordField();
        pwBox.setText(pass);
        grid.add(pwBox, 1, 3);

        Label usersLog = new Label("Name of users (separateed by comma):");
        grid.add(usersLog, 0, 4);

        TextField usersTextField = new TextField();
        usersTextField.setText("Name of users (separateed by comma)");
        grid.add(usersTextField, 1, 4);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start");

                JIRA_Accessor accessor = new JIRA_Accessor();

                try {
                    accessor.testResponse(new ConnectionData(urlTextField.getText(), userTextField.getText(), pwBox.getText()), usersTextField.getText());

                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }

                System.out.println("Finish");
//                calculation();
            }
        });

//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(grid, 300, 250));
        primaryStage.show();
    }

    private static void doTheJob(String[] args) {
        String pathToFile;
        String pathToFileCreate;
        String reportMonth;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Application started.");
        while (true) {
            String input;

            System.out.print("Enter input file name : ");
            pathToFile = scanner.nextLine();

            System.out.print("Enter out file name : ");
            pathToFileCreate = scanner.nextLine();

            System.out.print("Enter month to calculate : ");
            reportMonth = scanner.nextLine();

            System.out.print("Enter [c] for calculate, or [q] for EXIT.");
            input = scanner.nextLine();

            if ("q".equals(input)) {
                System.out.println("Exit!");
                System.exit(0);
            }

            if ("c".equals(input)) {
                System.out.println("Going to Calculation.");
                break;
            }

        }

        scanner.close();

        if(pathToFileCreate.isEmpty()){
            pathToFileCreate = "OUTPUT_RESULTS.csv";
        }

        if (!pathToFile.isEmpty()) {

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
            } catch (FileNotFoundException e) {
                System.out.println("Could not found input file: " + args[0]);
                e.printStackTrace();
                return;
            } catch (IOException e1) {
                System.out.println("Could not read headers from file: " + args[0]);
                e1.printStackTrace();
                return;
            }

            System.out.println("Transformation started ...");
            for (String val : headers) {
                if (val.equals("Issue key")) {
                    indexForIssueKey = index;
                }
                if (val.equals("Log Work")) {
                    indexesForLoggedWork.add(index);
                }
                if (val.equals("Summary")) {
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
            } catch (IOException e2) {
                System.out.println("Error occured while working with file: " + args[0]);
                e2.printStackTrace();
                return;
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
            builder.append(ColumnNamesList + "\n");

            for (TicketLoggedWork item : records) {

                if(reportMonth.isEmpty())
                    item.calculate();
                else
                    item.calculate(reportMonth);

                for (Map.Entry<String, String> entry : item.timeByAuthors.entrySet()) {
                    builder.append(item.number + ',');
                    builder.append(item.summary + ',');
                    builder.append(entry.getKey() + ',');
                    builder.append("\"=" + entry.getValue() + "/86400\"");
                    builder.append('\n');
                }
            }
            pw.write(builder.toString());
            pw.close();
            System.out.println("Transformation completed!");
        }
    }

}
