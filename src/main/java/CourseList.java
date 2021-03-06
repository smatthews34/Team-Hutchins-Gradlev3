import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class CourseList {
    Stack<Course> courseHist = new Stack<>();
    Stack<String> commandHist = new Stack<>();

    Stack<Course> undoCourseHist = new Stack<>();
    Stack<String> undoCommandHist = new Stack<>();

    public static ArrayList<Course> courseList = importCourseList(); //Remove if broken

    private void updateHistory(String command, Course course){
        commandHist.push(command);
        courseHist.push(course);
    }

    /**
     * Reverts to the state before the user's previous action (add or remove)
     * @param schedule - user's working schedule
     */
    public void undo(ArrayList<Course> schedule){
        if (!courseHist.isEmpty() && !commandHist.isEmpty()) {
            String lastCommand = commandHist.pop();
            Course lastCourse = courseHist.pop();

            //do opposite of last command to applicable course
            if (lastCommand.equals("add")) {
                schedule.remove(lastCourse);
            }

            else if (lastCommand.equals("remove")) {
                schedule.add(lastCourse);
            }

            undoCommandHist.push(lastCommand);
            undoCourseHist.push(lastCourse);
        }
        else {
            System.out.println("No actions to undo.");
        }
    }

    /**
     * Re-applies last user action (add or remove)
     * @param schedule - user's working schedule
     */
    public void redo(ArrayList<Course> schedule) {

        if (!undoCommandHist.isEmpty() && !undoCourseHist.isEmpty()){
            String lastCommand = undoCommandHist.pop();
            Course lastCourse = undoCourseHist.pop();

            if (lastCommand.equals("add")) {
                schedule.add(lastCourse);
            }
            else if (lastCommand.equals("remove")) {
                schedule.remove(lastCourse);
            }
        }

        else {
            System.out.println("No actions to redo.");
        }
    }

    /**
     *Removes the given course from user schedule
     * @param course - course to be removed from user schedule
     * @param Schedule - user's working schedule
     */
    public void removeClass(Course course, ArrayList<Course> Schedule){
        if (checkDouble(course, Schedule)) {
            Schedule.remove(course);
        }

        updateHistory("remove", course);
    }

    /**
     * conflictResolution
     * @param s the ArrayList<Course> that represents the user's current schedule
     * @return the list of courses that conflict with another course
     */
    public ArrayList<Course> conflictResolution(ArrayList<Course> s){
        ArrayList<Course> conflictList = new ArrayList<Course>(); //course list that the conflicting courses
        //Cycles through the user's schedule to check if there are any conflicts within the course
        for (int p = 0; p < s.size(); p++){
            ArrayList<Course> temp = new ArrayList<Course>();
            for (int i = 0; i <s.size(); i++){
                temp.add(s.get(i));
            }
            //removes the current course that is being checked for confliction to avoid duplication of courses on the list
            temp.remove(p);
            //goes through and adds any courses that conflict with the current course being checked
            if(checkConfliction(s.get(p), temp)){
                conflictList.add(s.get(p));
            }
        }
        return conflictList;
    }

    /**
     *
     * @param course the course that is being attempted to be added to user schedule
     * @param Schedule
     */
    public void addClass(Course course, ArrayList<Course> Schedule){ //static
        //checks to see if the course being added is a duplicate.
        if(checkDouble(course, Schedule)){
            //System.out.println("That course already is on your schedule, cannot be added.");
        }/*else if(checkConfliction(course, Schedule)){ //checks to see if the course conflicts
            System.out.println("There is a time conflict with your schedule."); //alerts the user there is a conflict
            Scanner scn = new Scanner(System.in);
            String answer = "";
            while (!answer.equals("No")&&!answer.equals("no")&&!answer.equals("yes")&&!answer.equals("Yes")&&!answer.equals("N")&&!answer.equals("n")&&!answer.equals("Y")&&!answer.equals("y")) { //gives the user the ability to add if conflicting.
                System.out.println("Would you like to add anyway? (Y/N");
                answer = scn.next();
                if (answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes")) {
                    Schedule.add(course);
                    updateHistory("add", course);
                    System.out.println("Conflicting course added.");
                    break;
                } else if (answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("No")) {
                    System.out.println("Conflicting course was not added.");
                    break;
                } else {
                    System.out.println("Invalid response please select Y or N.");
                }*/
           // }
        //}
        else{ //if the course is not a duplicate or a not conflicting course it wil be added to the user's schedule.
            updateHistory("add", course);
            Schedule.add(course);
            System.out.println("The course has successfully been added to your schedule.");
        }
    }

    /**
     * @param C course being checked for duplication
     * @param S current schedule being checked
     * @return True if there is a conflict
     * @return False if there is no conflict
     */
    public static boolean checkDouble(Course C, ArrayList<Course> S){
        boolean check = false;
        //goes through the user's current schedule to see if they already have the course they are trying to add.
        for(int i = 0; i < S.size(); i++){
            if(S.get(i).courseCode.equals(C.courseCode)){
                check = true;
            }
        }
        return check;
    }

    /**
     * @param C course being checked for duplication
     * @param S current schedule being checked
     * @return True if there is a conflict
     * @return False if there is no conflict
     */
    /*public static boolean checkConfliction(Course C, ArrayList<Course> S){
        boolean check = false;
        for(int i = 0; i < S.size(); i++){
            //checks the user list of courses and check if the course the user wants to add has a time and day confliction with any courses they currently have.
            if(S.get(i).startTime != null && S.get(i).startTime.equals(C.startTime) && S.get(i).meets.equals(C.meets)){
                check = true;
            }
        }
        return check;
    }*/
    //REMOVE IF BROKEN

    /**
     * checkConfliction
     * @param C Course to be check with the user's current schedule
     * @param S ArrayList<Course> of the user's current schedule to be checked for confliction with new course.
     * @return
     */
    public static boolean checkConfliction(Course C, ArrayList<Course> S){
        //Null check to avoid errors with courses with no time or days value
        if (C.meets != null && C.startTime != null && C.endTime != null) {
            boolean check = false;
            //Splits up the string time into seperate int values to be accurately check for time conflict
            //startTime
            String StartNums[] = C.startTime.split(":");
            int S_hour = Integer.parseInt(StartNums[0]);
            int S_minute = Integer.parseInt(StartNums[1]);
            //endTime
            String EndNums[] = C.endTime.split(":");
            int E_hour = Integer.parseInt(EndNums[0]);
            int E_minute = Integer.parseInt(EndNums[1]);

            for (int i = 0; i < S.size(); i++) {
                //startTime
                String snums[] = S.get(i).startTime.split(":");
                int h1 = Integer.parseInt(snums[0]);
                int m1 = Integer.parseInt(snums[1]);
                //endTime
                String endnums[] = S.get(i).endTime.split(":");
                int h2 = Integer.parseInt(endnums[0]);
                int m2 = Integer.parseInt(endnums[1]);

                if (S.get(i).startTime != null && C.startTime != null && S.get(i).startTime.equals(C.startTime) && (S.get(i).meets.equals(C.meets) || (S.get(i).meets.contains(C.meets) || C.meets.contains(S.get(i).meets)))) {
                    check = true;
                } else if (S.get(i).startTime != null && C.startTime != null && (h2 == S_hour && m2 >= S_minute) && (S.get(i).meets.equals(C.meets) || (S.get(i).meets.contains(C.meets) || C.meets.contains(S.get(i).meets)))) {
                    check = true;
                } else if (S.get(i).startTime != null && C.startTime != null && (h1 == E_hour && E_minute >= m1) && (S.get(i).meets.equals(C.meets) || (S.get(i).meets.contains(C.meets) || C.meets.contains(S.get(i).meets)))) {
                    check = true;
                }
            }
            return check;
        }
        return false;
    }
    //REMOVE IF BROKEN
    /**
     *
     * @param code of a course to be added
     * @return the course the user was trying to access
     * @return null if class does not exist.
     */
    public static Course getCourse(String code){
        ArrayList<Course> courseList = importCourseList(); //Gathers all of the possible courses from the given data.
        Course c; //a temp Course that will hold the value of the course that the user is looking for.
        for(int j = 0; j < courseList.size(); j++){
            //if the course is found it will set the empty course and have it set to the course the user wants
            if(courseList.get(j).courseCode.equals(code)){
                c = courseList.get(j);
                return c;
            }
        }
        return null; //if there is no such course will return nothing.
    }
    /**
     *  Similar to the getResults() but edited to just gather all of the courses in the data file
     * @return the grand course list for finding and adding a course that the user requested for.
     */
    public static ArrayList<Course> importCourseList(){
        ApachePOI poi = new ApachePOI();
        return poi.courseList();
//        try {
//            File classFile = new File("classFile.txt");
//            Scanner classScan = new Scanner(classFile);
//            String course;
//            int index = 0;
//            ArrayList<Course> courseList = new ArrayList<>();
//            ArrayList<String> theStrings = new ArrayList<>();
//            int potentialIndex = 0;
//            Course potentialCourse;
//            classScan.nextLine();
//            while (classScan.hasNextLine()) {
//                course = classScan.nextLine(); //grabs the line of code (the course info)
//                Scanner courseScan = new Scanner(course); //Creates a new scanner to read the line
//                courseScan.useDelimiter(",");
//                Scanner potentialScan = new Scanner(course);
//                potentialScan.useDelimiter(",");
//                while (potentialScan.hasNext()){
//                    String potentialData = potentialScan.next();
//                    theStrings.add(potentialData);
//                    potentialIndex++;
//                }
//                potentialCourse = new Course(theStrings.get(0), theStrings.get(1), theStrings.get(2), theStrings.get(3), theStrings.get(4), theStrings.get(5), theStrings.get(6), theStrings.get(7));
//                courseList.add(potentialCourse);
//                index = 0;
//                courseScan.close();
//                potentialScan.close();
//                theStrings.clear();
//            }
//            classScan.close();
//            return courseList;
//        } catch (FileNotFoundException e) {
//            System.out.println("Could Not Import Courses.");
//            e.printStackTrace();
//            return null;
//        }
    }

    /**
     * autoFill
     * @description a method that will automaticallly sign the user up for the required core courses for that year and semester,
     * had to find a few replacement courses since the list of courses only contains spring courses.
     * @param classPosition Grades of Fresh for Freshman, Soph for Sophomore, Junior for Junior, and Senior for Senior
     * @param semester Semester's of F for Fall Semester or S for Spring Semester.
     * @param schedule the current user's schedule
     */
    public static void autoFill(String classPosition, String semester, ArrayList<Course> schedule){
        /*SOME COURSES WERE NOT IN THE EXCEL DOCUMENT SO WE USED SUBSTITUTES FOR CERTAIN COURSES*/
        //default courses for freshman in the fall semester
        if(classPosition.equals("Fresh") && semester.equals("F")){
            schedule.add(getCourse("HUMA 102  A"));
            schedule.add(getCourse("CHEM 102  O    L"));
            schedule.add(getCourse("CHEM 102  B"));
        }else if(classPosition.equals("Soph") && semester.equals("F")){ //default courses for sophomores in the fall semester
            schedule.add(getCourse("HIST 120  A"));
            schedule.add(getCourse("HUMA 202  A"));
        }else if(classPosition.equals("Junior") && semester.equals("F")){ //default courses for junior in the fall semester
            schedule.add(getCourse("HUMA 301  A"));
        }else if(classPosition.equals("Senior") && semester.equals("F")){ //default courses for senior in the fall semester
            schedule.add(getCourse("HUMA 302  B")); //would be HUMA 303 but the course list given is missing HUMA 303
        }else if(classPosition.equals("Fresh") && semester.equals("S")){ //default courses for freshman in the spring semester
            schedule.add(getCourse("WRIT 101  A"));
            schedule.add(getCourse("PHYE 102  C")); //Should be 101 but not on list
            schedule.add(getCourse("BIOL 102  O    L"));
            schedule.add(getCourse("BIOL 102  B"));
        }else if(classPosition.equals("Soph") && semester.equals("S")){ //default courses for sophomores in the spring semester
            schedule.add(getCourse("HUMA 202  A"));
        }else if(classPosition.equals("Junior") && semester.equals("S")){ //default courses for juniors in the spring semester
            schedule.add(getCourse("HUMA 301  A"));
        }else if(classPosition.equals("Senior") && semester.equals("S")){ //default courses for seniors in the spring semester
            schedule.add(getCourse("HUMA 302  B")); //would be HUMA 303 but the course list given is missing HUMA 303
        }else {
            //Should never be reached but here just in case
            System.out.println("Super Senior Has No Designated HUMA or Base Courses, Seek an Advisors Assistance to Figure What HUMAs and Courses you Might be Missing.");
        }
    }

    /**
     * clearList()
     * clears all of the courses on the user's current schedule
     */
    public static void clearList(){
        courseList.clear();
    }
    //remove if broken.

    /**
     * FeelingLucky()
     * @param schedule the user's current schedule
     * Will put a course at random onto the user's current schedule, the course will not conflict with any courses on schedule.
     */
    public void FeelingLucky(ArrayList<Course> schedule){
        Boolean conflict = true;
        ArrayList<Course> options = importCourseList();
        Random rand = new Random();
        Course c;
        int choice = 0;
        //The while loop will loop until a non-conflicting course is selected.
        while(conflict) {
            choice = rand.nextInt(options.size());
            c = options.get(choice);
            conflict = checkConfliction(c,schedule);
            //if the course that was randomly selected does not conflict it will be put into the user's current schedule.
            if(conflict == false && (!c.roomNum.equals(null)&&!c.endTime.equals(null)&&!c.startTime.equals(null)&&!c.courseCode.equals(null)&&!c.meets.equals(null)&&!c.building.equals(null)&&!c.longTitle.equals(null)&&!c.shortTitle.equals(null))) {
                schedule.add(c);
                updateHistory("add", c);
                break;
            }
        }

    }
    public static void main(String[] args) {
        Course zack = getCourse("ACCT 202  A");
        System.out.println(zack);
        CourseList cList = new CourseList();
        //
        ArrayList<Course> courseList = importCourseList();
        for (int i = 0; i < courseList.size();i++){
            System.out.println(courseList.get(i));
        }
        //System.out.println(courseList);
        //Test 1
        System.out.println("Test 1:");
        ArrayList<Course> test = new ArrayList<>();
        Course test_c = new Course("MATH 101", "Intro Math", "Introduction to Mathematics", "9", "10", "MWF", "SHAL", "101");
        System.out.println(test);
        //cList.addClass(test_c,test);
        System.out.println(test);

        //Test 2
        System.out.println("Test 2:");
        System.out.println(test);
        //cList.addClass(test_c, test);
        System.out.println(test);

        //Test 3
        System.out.println("Test 3:");
        Course test_c2 = new Course("PHIL 101", "Intro Phil", "Introduction to Philosophy", "9", "10", "MWF", "SHAL", "102");
        System.out.println(test);
        //cList.addClass(test_c2, test);
        System.out.println(test);

        //Test 4, test for confirm schedule
        System.out.println("Test 4:");
        System.out.println(test);
        //confirmS(test);

        //Test 5
        System.out.println("Test 5:");
        cList.removeClass(test_c, test);
        System.out.println(test);

    }
}

