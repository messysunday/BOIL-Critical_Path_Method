package logic;

import java.util.ArrayList;

public class MainLogic {
    ArrayList<Activity> allActivities = new ArrayList<Activity>();
    ArrayList<Event> allEvents = new ArrayList<Event>();
    private void testPrintEvents(){
        for (Event e: allEvents){
            System.out.print(e.name+":");
            for (Activity a: e.inActivites)
                System.out.print(a.name);
            System.out.print(":");
            for (Activity a: e.outActivites)
                System.out.print(a.name);
            System.out.println();
        }
        System.out.println();
    }
    private void testPrintActivities(){
        System.out.println("        \t"+"time\t  ES\t  EF\t  LS\t  LF\t reserve");
        for (Activity a : allActivities){
            System.out.print(a.name+": ");
            for (Event e: a.eventFrom)
                System.out.print(e.name);
            System.out.print("->");
            for (Event e: a.eventTo)
                System.out.print(e.name);
            System.out.println("  \t"+String.format("%4s",a.time)+String.format("\t%4s",a.ES)+String.format("\t%4s",a.EF)+String.format("\t%4s",a.LS)+String.format("\t%4s",a.LF)+String.format("\t%4s",a.reserve)+(a.isCritical?"\tcrit":""));
        }

        System.out.println();
    }
    private void validateGraphEquivalence() throws Exception {
        try {
            System.out.println(" @ Graph validation");
            System.out.print(" @ Activities: ");
            boolean activitiesOk = true;
            for (Activity a : allActivities)
                if (!a.eventTo.get(0).inActivites.contains(a) || !a.eventFrom.get(0).outActivites.contains(a)) {
                    activitiesOk = false;
                    System.out.print("\n @ failed at activity: " + a.name);
                }
            System.out.println(activitiesOk ? " OK" : "");
            System.out.print(" @ Events: ");
            boolean eventsOk = true;
            for (Event e : allEvents) {
                for (Activity outs : e.outActivites)
                    if (!outs.eventFrom.contains(e)) {
                        eventsOk = false;
                        System.out.print("\n @ failed at event: " + e.name);
                    }
                for (Activity ins : e.inActivites)
                    if (!ins.eventTo.contains(e)) {
                        eventsOk = false;
                        System.out.print("\n @ failed at event: " + e.name);
                    }
            }
            System.out.println(eventsOk ? " OK" : "");
        }
        catch (Exception e){
            throw new Exception("the given graph is invalid");//return 0x0;
        }
    }
    public boolean resetEverything(){
        try {
            allActivities.clear();
            allEvents.clear();
        }
        catch (Exception e){
            System.err.println("error in resetEveryThing()");
            return false;
        }
        return true;
    }
    public boolean addActivityInput(ActivityInput activityInputToAdd){
        ArrayList<ActivityInput> t = new ArrayList<ActivityInput>();
        t.add(activityInputToAdd);
        return addActivityInput(t);
    }
    public boolean addActivityInput(ArrayList<ActivityInput> activityInputToAdd){
        try {
            for (ActivityInput a : activityInputToAdd)
                allActivities.add(new Activity(a, allActivities));
        }
        catch (Exception e){
            System.err.println("error while adding activityInputToAdd");
            return false;
        }
        return true;
    }
    public void test(){
        ArrayList<ActivityInput> testingData = new ArrayList<ActivityInput>();

        /*testingData.add(new ActivityInput("A","-",6.));
        testingData.add(new ActivityInput("B","-",8.));
        testingData.add(new ActivityInput("C","A,B",12.));
        testingData.add(new ActivityInput("D","C",4.));
        testingData.add(new ActivityInput("E","C",6.));
        testingData.add(new ActivityInput("F","D,   E",15.));
        testingData.add(new ActivityInput("G","E",12.));
        testingData.add(new ActivityInput("H","F, G",8.));*/

        /*testingData.add(new ActivityInput("A","-",5));
        testingData.add(new ActivityInput("B","-",7));
        testingData.add(new ActivityInput("C","A",6));
        testingData.add(new ActivityInput("D","A",8));
        testingData.add(new ActivityInput("E","B",3));
        testingData.add(new ActivityInput("F","C",4));
        testingData.add(new ActivityInput("G","C",2));
        testingData.add(new ActivityInput("H","E,D,F",5));*/

        testingData.add(new ActivityInput("A","-",6));
        testingData.add(new ActivityInput("B","-",10));
        testingData.add(new ActivityInput("C","A",6));
        testingData.add(new ActivityInput("D","A",12));
        testingData.add(new ActivityInput("E","B,C",5));
        testingData.add(new ActivityInput("F","B,C",8));
        testingData.add(new ActivityInput("G","E",8));
        testingData.add(new ActivityInput("H","D,F",7));
        testingData.add(new ActivityInput("I","D,F",8));
        testingData.add(new ActivityInput("J","H,G",6));
        testingData.add(new ActivityInput("K","I,J",7));

        for (ActivityInput a : testingData)
            addActivityInput(a);
        //if (calc() != 0)
        //    System.err.println("there was some error in calc()!");
    }
    public Integer calc() throws Exception{
        resetCounters();

        basicDumbAlgorithm();
        simplifySameOuts();
        apparentActivityCase1();
        apparentActivityCase2();
        CriticalPath criticalPath = new CriticalPath(allActivities,allEvents);
        criticalPath.calc();

        fixEventsNames();

        testPrintEvents();
        testPrintActivities();
        validateGraphEquivalence();
        return 0x0;
    }
    private void basicDumbAlgorithm() {
        //most suboptimal algorithm ever
        //step1: add every Activity with empty directlyPrecedingActivities to out of first Event
        ArrayList<Activity> outActivitiesToAdd = new ArrayList<Activity>();
        for (Activity a : allActivities)
            if (a.directlyPrecedingActivities.size() <= 0)
                outActivitiesToAdd.add(a); //add to Event
        allEvents.add(new Event("", new ArrayList<Activity>(), outActivitiesToAdd));
        //step2: some fancy loop
        while (true) {
            boolean waitIHaveWorkToDo = false;
            //step2a: for each "out" without "in" create new event
            for(Activity a: allActivities){
                //if there is from but no to
                if (a.eventFrom.size() > 0 && a.eventTo.size() <= 0){
                    ArrayList<Activity> inActivitiesToAdd = new ArrayList<Activity>();
                    inActivitiesToAdd.add(a);
                    Event e = new Event("",inActivitiesToAdd, new ArrayList<Activity>());
                    allEvents.add(e);
                    a.eventTo.add(e);
                    waitIHaveWorkToDo = true;
                }
            }
            //step2b: if pre is in "in" put in "out"
            for(Activity a: allActivities){
                for (Activity pre: a.directlyPrecedingActivities){
                    for (Event e: allEvents){
                        for (Activity in: e.inActivites){
                            if (pre == in && !e.outActivites.contains(a)) {
                                e.outActivites.add(a);
                                a.eventFrom.add(e);
                                waitIHaveWorkToDo = true;
                            }
                        }
                    }
                }
            }
            if (!waitIHaveWorkToDo)
                break;
        }
    }
    private void simplifySameOuts(){
        ArrayList<Event> toRemove = new ArrayList<Event>();
        for (int i = 0; i < allEvents.size(); i++) {
            Event e1 = allEvents.get(i);
            for (int j = i+1; j < allEvents.size(); j++) {
                Event e2 = allEvents.get(j);
                if (e1 == e2)
                    continue;
                if (e1.outActivites.size() != e2.outActivites.size())
                    continue;
                boolean eventIsRemovable = true;
                for (Activity a : e1.outActivites)
                    if (!e2.outActivites.contains(a)) {
                        eventIsRemovable = false;
                        break;
                    }
                if (eventIsRemovable) {
                    e1.inActivites.addAll(e2.inActivites); //move all inActivities from 2nd to 1st
                    for (Activity aa : e2.inActivites){
                        aa.eventTo.remove(e2);
                        aa.eventTo.add(e1);
                    }
                    toRemove.add(e2); //and remove the duplicate (actually add to remove queue)
                }
            }
        }
        allEvents.removeAll(toRemove); //remove all from remove queue
        //clear Activities from removed Events
        for (Activity a: allActivities){
            a.eventFrom.removeAll(toRemove);
            a.eventTo.removeAll(toRemove);
        }
    }
    private boolean checkIfAccessible(Event e1, Event e2){
        for(Activity a: e1.outActivites){
            if (a.eventTo.get(0) == e2)
                return true;
            return checkIfAccessible(a.eventTo.get(0), e2);
        }
        return false;
    }
    private void apparentActivityCase1(){
        ArrayList<Activity> toAllActivities = new ArrayList<>();
        for (Activity a: allActivities) {
            int k = a.eventFrom.size();
            if (k >=2){
                for (int i=0;i<k;i++){
                    for (int j=0;j<k;j++) {
                        if (i==j) continue; //don't replace yourself lol
                        if (checkIfAccessible(a.eventFrom.get(i),a.eventFrom.get(j)))
                            continue;
                        //add new apparent activity
                        ArrayList<Activity> directlyPrecedingActivities = new ArrayList<>();
                        directlyPrecedingActivities.addAll(a.directlyPrecedingActivities);
                        Activity toAdd = new Activity(a.getApparentName(), directlyPrecedingActivities, 0.);
                        toAllActivities.add(toAdd);
                        //add EventFrom and EventTo to newly created apparent activity
                        toAdd.eventFrom.add(a.eventFrom.get(j));
                        toAdd.eventTo.add(a.eventFrom.get(i));
                        //add newly created activity to events
                        a.eventFrom.get(j).outActivites.add(toAdd);
                        a.eventFrom.get(i).inActivites.add(toAdd);
                        //delete 2nd mount, fix old activity
                        a.eventFrom.get(j).outActivites.remove(a);
                        a.eventFrom.remove(j);
                        i=j=k; //same behaviour as 2 breaks
                    }
                }
            }
        }
        allActivities.addAll(toAllActivities);
    }
    private void apparentActivityCase2(){
        ArrayList<Event> toAllEvents = new ArrayList<>();
        ArrayList<Activity> toAllActivities = new ArrayList<>();
        for (Event e: allEvents){
            if (e.outActivites.size() < 2)
                continue;
            ArrayList<Event> toEvents = new ArrayList<Event>();
            for (Activity a : e.outActivites) {
                if (toEvents.contains(a.eventTo.get(0))){
                    //adding apparent activity
                    ArrayList<Activity> directlyPrecedingActivities = new ArrayList<>();
                    directlyPrecedingActivities.add(a);
                    Activity toAdd = new Activity(a.getApparentName(),directlyPrecedingActivities,0.);
                    toAllActivities.add(toAdd);
                    //adding new apparent event
                    ArrayList<Activity> inA = new ArrayList<>();
                    ArrayList<Activity> outA = new ArrayList<>();
                    inA.add(a);
                    outA.add(toAdd);
                    toAllEvents.add(new Event(e.getApparentName(),inA, outA));
                    //note: within Event constructor eventFrom is added automatic
                    //add eventTo to apparent activity
                    toAdd.eventTo.add(a.eventTo.get(0));
                    //add inActivities to newly created event
                    a.eventTo.get(0).inActivites.remove(a);
                    a.eventTo.get(0).inActivites.add(toAdd);
                    //remount eventTo old Activity to newly created event
                    a.eventTo.remove(0);
                    a.eventTo.add(toAdd.eventFrom.get(0));
                }
                toEvents.add(a.eventTo.get(0));
            }
        }
        allEvents.addAll(toAllEvents);
        allActivities.addAll(toAllActivities);
    }

    public ArrayList<Activity> getAllActivities(){
        return allActivities;
    }
    public ArrayList<Event> getAllEvents(){
        return allEvents;
    }
    private void resetCounters(){
        new Event("e",new ArrayList<Activity>(),new ArrayList<Activity>()).resetCounters();
        new Activity("a",new ArrayList<Activity>(),0.).resetCounters();
    }

    private void fixEventsNames(){
        int nameCounter=0;
        for (Event e: allEvents){
            e.name = Integer.toString(nameCounter++);
        }
    }
}