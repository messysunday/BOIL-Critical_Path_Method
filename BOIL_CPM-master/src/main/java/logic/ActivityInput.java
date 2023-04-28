package logic;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityInput {
    public String name; //A,B
    public ArrayList<String> directlyPrecedingActivities;
    public Double time;

    public ActivityInput(String name, ArrayList<String> directlyPrecedingActivities, Double time) {
        this.name = name;
        this.directlyPrecedingActivities = directlyPrecedingActivities;
        this.time = time;
    }
    public ActivityInput(String name, String directlyPrecedingActivities, Integer time) {
        this(name,directlyPrecedingActivities,Double.valueOf(time));
    }
    public ActivityInput(String name, String directlyPrecedingActivities, Double time) {
        //name = name.replaceAll(" ", ""); //remove all spaces
        //directlyPrecedingActivities = directlyPrecedingActivities.replaceAll(" ", ""); //remove all spaces
        this.name = name;
        if (directlyPrecedingActivities.equals("-"))
            //if list is empty (== "-")
            this.directlyPrecedingActivities = new ArrayList<String>();
        else
            //convert comma separated string to arrayList of single items
            this.directlyPrecedingActivities = new ArrayList<String>(Arrays.asList(directlyPrecedingActivities.split("\\s*,\\s*")));
        this.time = time;
    }
}
