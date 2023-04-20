import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.Period;
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
        int session=topicTime/maxFocusTime;
        int totalTime = ((topics.length * topicTime)+((session-1)*restTime*topics.length))+ ((topics.length-1)*restTime);
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
            System.out.println("Study Schedule:");
            System.out.println("Date\t\tStart Time\tEnd Time\tTopic");
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
                System.out.println(currentDate + "\t" + currentTime + "\t\t" + endTime + "\t\t" + topic.trim());
                // Update the remaining minutes and current time
                minutesRemaining -= topicMinutes;
                currentTime = endTime.plusMinutes(restTime);
                System.out.println(currentDate + "\t" + endTime + "\t\t" + currentTime + "\t\t" + "Break Time");
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
                System.out.println(currentDate + "\t" + currentTime + "\t\t" + endTime + "\t\t" + topic.trim());
                // Update the remaining minutes and current time
                minutesRemaining -= topicMinutes;
                currentTime = endTime.plusMinutes(restTime);
                System.out.println(currentDate + "\t" + endTime + "\t\t" + currentTime + "\t\t" + "Break Time");
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
public class Docket{
    public static void main(String[] args) {
    // Get input from user
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        String exitKeyword = "$done";
        System.out.println("Enter the topic serprated by (,) comma (or type '$done' to exit): ");
        while (true) {
            String inputLine = scanner.nextLine();
            if (inputLine.equals(exitKeyword)) {
        break;
        }
        userInput += inputLine.toUpperCase();
        }  

        String topics[]=userInput.split(",");
        System.out.print("Enter the start time of the event in 24-hour format (e.g. 08:00): ");
            String startTimeString = scanner.nextLine();
        System.out.print("Enter the end time of the event in 24-hour format (e.g. 18:00): ");
            String endTimeString = scanner.nextLine();
        System.out.print("Enter maximum focus time (in minutes): ");
            int maxFocusTime = scanner.nextInt();
            
        System.out.print("Enter time required to cover one topic (in minutes): ");
            int topicTime = scanner.nextInt();
        
        System.out.print("Enter time needed for rest (in minutes): ");
            int restTime = scanner.nextInt();
            
        System.out.print("Enter end date (in yyyy-mm-dd format): ");
            String endDateStr = scanner.next();
        scanner.close();
        Scheduler.timeAssign(startTimeString, endTimeString);
        Scheduler.generateStudySchedule(restTime, topicTime, maxFocusTime, topics, endDateStr);
    }
}
