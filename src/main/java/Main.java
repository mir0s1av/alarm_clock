
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AlarmClock alarmTerminal = new AlarmClock(scanner);
        AlarmClock clockDisplay = new AlarmClock(scanner){

            @Override
            public void run() {
                AlarmClock.displayTime();
            }
        };
        AlarmClock alarmChecker = new AlarmClock(scanner){
            @Override
            public void run(){
                AlarmClock.alarmChecker();
            }
        };

        Thread alarmCheckerThread = new Thread(alarmChecker);
        Thread clockDisplayThread = new Thread(clockDisplay);
        Thread alarmTerminalTread = new Thread(alarmTerminal);

        clockDisplayThread.start();
        alarmTerminalTread.start();
        alarmCheckerThread.start();

        try{
            alarmCheckerThread.join();
            clockDisplayThread.join();
            alarmTerminalTread.join();
        } catch (InterruptedException e) {
            System.out.println(" ");
        }

    }
}
