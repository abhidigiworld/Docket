import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.io.*;
//Creating a Outputer class
class Outputer{
    Outputer(){
    }
    Outputer(String filename){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            String contents = stringBuilder.toString();
            reader.close();
            JFrame frame1=new JFrame("Schedule Table");
            frame1.setSize(800, 1000);
            frame1.setVisible(true);
            //frame1.setLayout(null);
            JTextArea Area=new JTextArea(contents);
            Area.setEditable(false);
            frame1.add(Area);
        } catch (IOException e) {
            System.out.println("There is error in file "+e);
        }
    }
}

//Class scheduler which implements the the algo of the docket
class Scheduler{
    static LocalTime startTime;
    static LocalTime endTimer;

    public static void timeAssign(String startTimeString, String endTimeString) {
          // Parse the user input into a LocalTime object
          startTime = LocalTime.parse(startTimeString);
          endTimer = LocalTime.parse(endTimeString);
    }

    public static void generateStudySchedule(int restTime, int topicTime, int maxFocusTime, String[] topics, String endDateStr) {
        // Parse the end date input
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE);

        // Calculate the total time required to cover all the topics
        int totalTime;
        if(topicTime>maxFocusTime){
            int session=topicTime/maxFocusTime;
            totalTime = ((topics.length * topicTime)+((session-1)*restTime*topics.length))+ ((topics.length-1)*restTime);
        }
        else{
            totalTime = (topics.length * topicTime)+ ((topics.length-1)*restTime);
        }
        // Get the current day
        LocalDate startDate = LocalDate.now();

        // Calculate the total study time available
        Period period = Period.between(startDate, endDate);
        int totalStudyTime = period.getDays()*24*60;

        // Check if there is enough time to cover all the topics
        if (totalTime > totalStudyTime) {
            System.out.println("There is not enough time to cover all the topics.");
            System.out.println("Consider reducing topic time or increasing focus time.");
        } 
        else {
            // Calculate the study schedule
            int minutesRemaining = totalTime;
            LocalDate currentDate = startDate;
            LocalTime currentTime = startTime;
            System.out.println("========================================================================================================");
            System.out.println("\tDate\t|  Start Time\t|  End Time\t|  Topic");
            System.out.println("========================================================================================================");
            for (String topic : topics) {
                if(topicTime > maxFocusTime){
                    int sessions=topicTime/maxFocusTime;
                    for(int i=sessions;i!=0;i--){
                        
                // Calculate the time required to cover the current topic
                int topicMinutes = Math.min(maxFocusTime, Math.min(topicTime, minutesRemaining ));
                // Calculate the end time for the current topic
                LocalTime endTime = currentTime.plusMinutes(topicMinutes);

                // If the end time is after the end of the study day, skip to the next day
                if (endTime.isAfter(endTimer)) {
                    currentDate = currentDate.plusDays(1);
                    currentTime = startTime;
                    endTime = currentTime.plusMinutes(topicMinutes);
                }

                // Print the study schedule for the current topic
                System.out.println("\t"+currentDate + "\t|  " + currentTime + "\t|  " + endTime + "\t|  " + topic.trim());
                // Update the remaining minutes and current time
                minutesRemaining -= topicMinutes;
                currentTime = endTime.plusMinutes(restTime);
                System.out.println("\t"+currentDate + "\t|  " + endTime + "\t|  " + currentTime + "\t|  " + "Break Time");
                    }
                }
                else{
                // Calculate the time required to cover the current topic
                int topicMinutes = Math.min(maxFocusTime, Math.min(topicTime, minutesRemaining ));

                // Calculate the end time for the current topic
                LocalTime endTime = currentTime.plusMinutes(topicMinutes);

                // If the end time is after the end of the study day, skip to the next day
                if (endTime.isAfter(LocalTime.of(18, 0))) {
                    currentDate = currentDate.plusDays(1);
                    currentTime = LocalTime.of(8, 0);
                    endTime = currentTime.plusMinutes(topicMinutes);
                }

                // Print the study schedule for the current topic
                System.out.println("\t"+currentDate + "\t|  " + currentTime + "\t|  " + endTime + "\t|  " + topic.trim());
                // Update the remaining minutes and current time
                minutesRemaining -= topicMinutes;
                currentTime = endTime.plusMinutes(restTime);
                System.out.println("\t"+currentDate + "\t|  " + endTime + "\t|  " + currentTime + "\t|  " + "Break Time");
                }

                // If there are no more minutes remaining, break out of the loop
                if (minutesRemaining == 0) {
                    break;
                }
            }
            System.out.println("Success is the sum of small efforts, repeated day in and day out.");
        }
    }
}
class gui {
    public static void main(String args[]){
                    //Creating a frame
                    JFrame jf=new JFrame("Docket");
                    //Geting the image for the icon
                    Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\abhid\\OneDrive\\Desktop\\java project\\Docket_gui\\logo-2.png");
                    //Inserting image to the frame icon
                    jf.setIconImage(icon);
                    //Set the width and height of the frame
                    jf.setSize(800, 500);
                    //Setting the layout of the frame
                    jf.setLayout(new GridLayout(8, 2,20,20));
                    //Prevent it from resize;
                    jf.setResizable(false);
                    //Creating a label
                    JLabel label0=new JLabel("Enter the topic serprated by (,) comma : ");
                    jf.add(label0);
                    JTextArea area=new JTextArea(4,5);
                    jf.add(area);
                    JLabel label1=new JLabel("Enter the start time of the event in 24-hour format (e.g. 08:00)");
                    jf.add(label1);
                    TextField field1=new TextField(6);
                    jf.add(field1);
                    JLabel label2=new JLabel("Enter the end time of the event in 24-hour format (e.g. 18:00)");
                    jf.add(label2);
                    TextField field2=new TextField(6);
                    jf.add(field2);
                    JLabel label3=new JLabel("Enter maximum focus time (in minutes)  ");
                    jf.add(label3);
                    TextField field3=new TextField(6);
                    jf.add(field3);
                    JLabel label4=new JLabel("Enter time required to cover one topic (in minutes)  ");
                    jf.add(label4);
                    TextField field4=new TextField(6);
                    jf.add(field4);
                    JLabel label5=new JLabel("Enter time needed for rest (in minutes)        ");
                    jf.add(label5);
                    TextField field5=new TextField(6);
                    jf.add(field5);
                    JLabel label6=new JLabel("Enter end date (in yyyy-mm-dd format)    ");
                    jf.add(label6);
                    TextField field6=new TextField(10);
                    jf.add(field6);
                    //craeting a button b1 with submit
                    JButton b1= new JButton("SUBMIT");  
                    //adding button in the frame 
                    jf.add(b1);
                    //frame visibilty is true 
                    jf.setVisible(true);
                    //addlister for the button
                    b1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Get the input values
                            String userInput = area.getText().toUpperCase();
                            String topics[] = userInput.split(",");
                            String startTimeString = field1.getText();
                            String endTimeString = field2.getText();
                            int maxFocusTime = Integer.parseInt(field3.getText());
                            int topicTime = Integer.parseInt(field4.getText());
                            int restTime = Integer.parseInt(field5.getText());
                            String endDateStr = field6.getText();

                            try {
                                // Create a new file output stream
                                FileOutputStream fileOut = new FileOutputStream("output.txt");
                                // Create a new print stream that writes to the file output stream
                                PrintStream newOut = new PrintStream(fileOut);
                                System.setOut(newOut);
                                // Generate the schedule
                                Scheduler.timeAssign(startTimeString, endTimeString);
                                // Call generateStudySchedule to store the output of the java in the 
                                Scheduler.generateStudySchedule(restTime, topicTime, maxFocusTime, topics, endDateStr);
                                // Close the file
                                fileOut.close();
                                

                            } catch (IOException er) {
                                System.out.println("There is error in file"+er);
                            }
                            jf.dispose();
                            Outputer outs=new Outputer("output.txt");
                        }
                    });    
    }
}
