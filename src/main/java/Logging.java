import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
    String username;
    File f;
    public Logger logger;
    FileHandler fh;

    public Logging(String user) throws IOException {
        this.username = user;
        f = new File(user+".txt");
        if (!f.exists()){
            f.createNewFile();
            System.out.println("Log File created: " + f.getName());
        }
        fh = new FileHandler(user+".txt",true);
        logger = Logger.getLogger("Log");
        logger.addHandler(fh);
        SimpleFormatter form = new SimpleFormatter();
        fh.setFormatter(form);
    }
    public void Action(String s) {
        this.logger.info(s);
    }
    public void logConflict(String s) {
        this.logger.warning(s);
    }

    public static void recreateScheduleFromLog(String user, ArrayList<Course> schedule){
        try {
            File file = new File(user + ".txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String temp = sc.nextLine();
                if(temp.contains("Successfuly removed the course:")){
                    System.out.println("R");
                }else if(temp.contains("has added the course:")){
                    System.out.println("A");
                }else{
                    System.out.println("None");
                }
            }
        }catch (Exception e){
            System.out.println("Sorry");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Logging Testing!");
        //Logging lg1 = new Logging("tom");
        ArrayList<Course> c = new ArrayList<>();
        recreateScheduleFromLog("guest", c);
        //lg1.logger.setLevel(Level.ALL);
        //lg1.Action("Hi");
        //lg1.logger.info("Hello testing logger.");
        //lg1.logger.warning("conflict with scheduling accepted");//
        System.out.println("Thank you.");
    }
}
