package models;
  
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Event {

    public String name;
    public String description;
    public Boolean isActive;
    public String period;
    public String hourMin;
    public String hourMax;
    public ArrayList<Item> items;
    public String displayName;
    
    public Event(String name, Boolean isActive, String period, ArrayList<Item> items) {
        super();
        this.name = name;
        this.isActive = isActive;
        this.period = period;
        this.items = items;
    }
    
    public boolean isAvailableForCurrentDate()
    { 
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        
        HashMap<String, Integer> days = new HashMap<String, Integer>();
        days.put("monday", Calendar.MONDAY);
        days.put("tuesday", Calendar.TUESDAY);
        days.put("wednesday", Calendar.WEDNESDAY);
        days.put("friday", Calendar.FRIDAY);
        days.put("saturday", Calendar.SATURDAY);
        days.put("sunday", Calendar.SUNDAY);
        
        String dayEvent = "";
        String[] res = this.period.split(" ");
        
        for(int i=0; i < res.length; i++)
        {
            dayEvent = res[i];
        
            if(!days.containsKey(dayEvent))
            {
                continue;
            }
            else
            {
                Integer day = days.get(dayEvent);
                
                if(day.intValue() == currentDay)
                {
                    int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    
                    if (currentHour >= Integer.parseInt(this.hourMin) && currentHour < Integer.parseInt(this.hourMax)) {
                        return true;
                    } 
                }            
            }
        }
        
        return false;
        
    }
}
