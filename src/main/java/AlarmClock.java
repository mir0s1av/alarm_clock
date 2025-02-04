import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AlarmClock implements Runnable {
    final static Map<LocalDate, ArrayList<LocalTime>> alarms = new HashMap<>();
    private final Scanner scanner;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private void addAlarm() {
        try {

            System.out.print("Enter the date (dd/mm/yyyy): ");
            String date = scanner.nextLine();
            LocalDate alarmDate = LocalDate.parse(date, dateFormatter);
            alarms.computeIfAbsent(alarmDate, _ -> new ArrayList<>());
            while (true) {
                System.out.print("Enter the alarm time (HH:mm): ");
                String time = scanner.nextLine();
                LocalTime alarmTime = LocalTime.parse(time, timeFormatter);
                if (alarms.get(alarmDate).contains(alarmTime)) {
                    System.out.println("Alarm already set for this time.");
                    continue;
                }
                alarms.get(alarmDate).add(alarmTime);
                System.out.println("Alarm set for: " + alarmTime);
                System.out.print("Add another alarm? (y/n): ");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("y")) {
                    break;
                }
            }


        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please try again.");
        }
    }

    private void showAlarms() {
        if (alarms.isEmpty()) {
            System.out.println("No alarms set.");
        } else {
            while (true) {
                System.out.println("Choose a date to show alarms for: ");
                Map<Integer, LocalDate> options = new HashMap<>();
                int i = 0;
                for (Map.Entry<LocalDate, ArrayList<LocalTime>> entry : alarms.entrySet()) {

                    i++;
                    options.put(i, entry.getKey());
                    System.out.printf("%s) %s\n", i, entry.getKey());

                }
                int selectedDate = scanner.nextInt();
                if (selectedDate < 1 || selectedDate > options.size()) {
                    System.out.println("Invalid option. Please try again.");
                    continue;
                }
                if (!alarms.containsKey(options.get(selectedDate))) {
                    System.out.println("No alarms set for this date.");

                }
                scanner.nextLine();
                ArrayList<LocalTime> alarmTimes = alarms.get(options.get(selectedDate));
                System.out.printf("%s alarm(s) set for %s:\n", alarmTimes.size(), options.get(selectedDate));
                for (LocalTime alarmTime : alarmTimes) {
                    System.out.println(alarmTime);
                }
                break;

            }
        }
    }

    private void listAlarms() {
        if (alarms.isEmpty()) {
            System.out.println("No alarms set.");
        } else {
            int total = 0;
            for (Map.Entry<LocalDate, ArrayList<LocalTime>> entry : alarms.entrySet()) {
                total += entry.getValue().size();
            }

            System.out.printf("You have %s alarms set.\n", total);

        }
    }

    AlarmClock(Scanner scanner) {
        this.scanner = scanner;
    }

    static void displayTime() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.print("\rCurrent time: " + LocalDateTime.now().format(FORMATTER));
                //System.out.printf("\rCurrent time: %s", LocalDateTime.now().format(FORMATTER));
            } catch (InterruptedException e) {
                System.out.println("Clock display interrupted.");
                break;
            }
        }
    }

    static void alarmChecker() {
        LocalDate today = LocalDateTime.now().toLocalDate();
        Optional<ArrayList<LocalTime>> existingAlarms = AlarmClock.alarms.entrySet().stream()
                .filter(entry -> entry.getKey().isEqual(today))
                .map(Map.Entry::getValue)
                .findFirst();


        while (existingAlarms.isPresent()) {
            try {

                Thread.sleep(1000);
                LocalDateTime now = LocalDateTime.now();
                System.out.println(existingAlarms);
                if(existingAlarms.stream().anyMatch(r -> r.getFirst().isBefore(now.toLocalTime()) )){
                    Toolkit.getDefaultToolkit().beep();
                    break;
                }
                //System.out.printf("\rCurrent time: %s", now.format(FORMATTER));
            } catch (InterruptedException e) {
                System.out.println("Clock display interrupted.");
                break;
            }
        }

    }

    @Override
    public void run() {

        int option = 0;
        while (option != 3) {

            listAlarms();

            System.out.println("1) Add alarm 2) Show alarms 3) Exit");
            System.out.print("Choose an option: ");
            scanner.nextLine();
            option = scanner.nextInt();

            switch (option) {
                case 1 -> addAlarm();
                case 2 -> showAlarms();
                case 3 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option. Please try again.");
            }

        }
    }
}
