package logic;

import java.util.ArrayList;

public class Activity {
    public String name; //A,B
    public ArrayList<Activity> directlyPrecedingActivities;
    public Double time;

    public ArrayList<Event> eventFrom = new ArrayList<Event>(), eventTo = new ArrayList<Event>();

    public Double ES=0., EF=0., LS=0., LF=0., reserve=0.;
    public Boolean isCritical = false;

    private static int apparentCounter = 0;

    public Activity(String name, ArrayList<Activity> directlyPrecedingActivities, Double time) {
        this.name = name;
        this.directlyPrecedingActivities = directlyPrecedingActivities;
        this.time = time;
    }
    private Activity findActivityWithGivenName(String name, ArrayList<Activity> allActivities){
        try {
            for (Activity a : allActivities)
                if (a.name.equals(name))
                    return a;
        }
        catch (Exception e) {
            System.err.println("bad data received! cannot find Activity with given name!");
        }
        return null;
    }
    public Activity(ActivityInput activityInput, ArrayList<Activity> allActivities) {
        this.name = activityInput.name;

        ArrayList<Activity> activities = new ArrayList<Activity>();
        for (String n : activityInput.directlyPrecedingActivities)
            activities.add(findActivityWithGivenName(n, allActivities));

        this.directlyPrecedingActivities = activities;
        this.time = activityInput.time;
    }
    public void addEventFrom(Event eventFrom){
        this.eventFrom.add(eventFrom);
    }
    public void addEventTo(Event eventTo){
        this.eventTo.add(eventTo);
    }
    public String getApparentName(){
        return ("p"+Integer.toString(apparentCounter++));
    }

    public Double getES() {return ES;}
    public Double getEF() {return EF;}
    public Double getLS() {return LS;}
    public Double getLF() {return LF;}
    public void resetCounters(){
        apparentCounter=0;
    }
}
