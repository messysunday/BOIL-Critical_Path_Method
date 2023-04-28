package logic;

import java.util.ArrayList;

public class Event {
    public String name; //1,2,3
    public ArrayList<Activity> inActivites;
    public ArrayList<Activity> outActivites;
    private static int eventCounter = 0;
    private static int apparentCounter = 0;
    public Event(String name, ArrayList<Activity> inActivites, ArrayList<Activity> outActivites) {
        this.name = name;
        this.name = Integer.toString(eventCounter++);
        this.inActivites = inActivites;
        this.outActivites = outActivites;
        //add EventFrom to all outActivities
        for (Activity a: outActivites)
            a.addEventFrom(this);
    }
    public String getApparentName(){
        return ("e"+Integer.toString(apparentCounter++));
    }
    public Double getES(){
        Double maxEF = 0.;
        for (Activity a: inActivites){
            maxEF = Math.max(maxEF,a.EF);
        }
        return maxEF;
    }
    public Double getEF(){
        Double minLS;
        if (outActivites.size()==0) //last one
            minLS = this.getES();
        else
            minLS = outActivites.get(0).LS;

        for (Activity a: outActivites){
            minLS = Math.min(minLS,a.LS);
        }
        return minLS;
    }
    public Double getReserve(){
        return this.getEF() - this.getES();
    }
    public void resetCounters(){
        eventCounter=0;
        apparentCounter=0;
    }
}
