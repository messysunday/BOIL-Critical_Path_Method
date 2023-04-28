package logic;

import java.util.ArrayList;
import java.util.Comparator;

public class CriticalPath {
    ArrayList<Activity> allActivities = new ArrayList<Activity>();
    ArrayList<Event> allEvents = new ArrayList<Event>();
    public CriticalPath(ArrayList<Activity> allActivities, ArrayList<Event> allEvents) {
        this.allActivities = allActivities;
        this.allEvents = allEvents;
    }
    public void calc(){
        criticalPathStepForward();
        criticalPathStepBackward();
        calculateReserve();
    }
    private Event findFistEvent(){
        ArrayList<Event> firstEvent = new ArrayList<>();
        try {
            for (Event e: allEvents)
                if (e.inActivites.size()==0)
                    firstEvent.add(e);
            if (firstEvent.size()>1)
                throw(new Exception());
        }
        catch (Exception e){
            System.err.println("error in findFistEvent(): there is more than one first event");
        }
        return firstEvent.get(0);
    }
    private Event findLastEvent(){
        ArrayList<Event> lastEvent = new ArrayList<>();
        try {
            for (Event e: allEvents)
                if (e.outActivites.size()==0)
                    lastEvent.add(e);
            if (lastEvent.size()>1)
                throw(new Exception());
        }
        catch (Exception e){
            System.err.println("error in findFistEvent(): there is more than one first event");
        }
        return lastEvent.get(0);
    }
    private void criticalPathStepForward(){
        Event startFrom = findFistEvent();
        recursiveStepForward(startFrom);
    }
    private void recursiveStepForward(Event e){
        ArrayList<Event> nextEvents = new ArrayList<>();
        if (e.inActivites.size() == 0) { //no activity enters event
            for (Activity a: e.outActivites){
                a.ES = 0.;
                a.EF = a.ES + a.time;
                nextEvents.addAll(a.eventTo);
            }
        }
        else if (e.inActivites.size() == 1) { //only one activity enters event
            for (Activity a: e.outActivites){
                a.ES = e.inActivites.get(0).EF;
                a.EF = a.ES + a.time;
                nextEvents.addAll(a.eventTo);
            }
        }
        else { //more than one activity enters event
            for (Activity a: e.outActivites){
                a.ES = e.inActivites.stream().max(Comparator.comparing(Activity::getEF)).get().EF;
                a.EF = a.ES + a.time;
                nextEvents.addAll(a.eventTo);
            }
        }
        //can be added: calc T (max of EF of last Event)
        for (Event nextE: nextEvents)
            recursiveStepForward(nextE);
    }
    private void criticalPathStepBackward(){
        Event startFrom = findLastEvent();
        recursiveStepBackward(startFrom);
    }
    private void recursiveStepBackward(Event e) {
        ArrayList<Event> nextEvents = new ArrayList<>();
        if (e.outActivites.size() == 0) { //no activity exits event
            for (Activity a: e.inActivites) {
                a.LF = e.inActivites.stream().max(Comparator.comparing(Activity::getEF)).get().EF;
                a.LS = a.LF - a.time;
                nextEvents.addAll(a.eventFrom);
            }
        }
        else { //some activity exits event
            for (Activity a: e.inActivites) {
                a.LF = e.outActivites.stream().min(Comparator.comparing(Activity::getLS)).get().LS;
                a.LS = a.LF - a.time;
                nextEvents.addAll(a.eventFrom);
            }
        }
        for (Event nextE: nextEvents)
            recursiveStepBackward(nextE);
    }
    private void calculateReserve(){
        for (Activity a: allActivities){
            a.reserve = a.LS - a.ES;
            if (a.reserve == 0)
                a.isCritical = true;
        }
    }
}
