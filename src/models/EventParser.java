package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EventParser {

    ArrayList<Event> _events;

    public ArrayList<Event> getEvents()
    {
        return _events;
    }
    
    public EventParser(String path) {
        _events = new ArrayList<Event>();

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(path)); // normalize
                                                                                             // text
                                                                                             // representation
            doc.getDocumentElement().normalize();
            System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            NodeList listOfEvent = doc.getElementsByTagName("event");

            for (int s = 0; s < listOfEvent.getLength(); s++) {
                Node eventNode = listOfEvent.item(s);

                String name = "";
                String displayName = "";
                String description = "";
                boolean isActive = true;
                String period = "";
                String hourMin = "";
                String hourMax = "";
                ArrayList<Item> items = new ArrayList<Item>();

                NodeList childsOfEvents = eventNode.getChildNodes();
                for (int c = 0; c < childsOfEvents.getLength(); c++) {
                    Node childOfEvent = childsOfEvents.item(c);
                    
                    String valueChildOfEvent = "";
                    
                    if(childOfEvent.hasChildNodes())
                    {
                        valueChildOfEvent = childOfEvent.getChildNodes().item(0).getNodeValue();
                    } 
                    
                    if (childOfEvent.getNodeName() == "name") { 
                        name = valueChildOfEvent;
                    }

                    if (childOfEvent.getNodeName() == "is_active") {
                        System.out.println("valueChildOfEvent is_active" + valueChildOfEvent);
                        if(valueChildOfEvent.equals("true"))
                        isActive =  true;
                        else isActive = false;
                    }

                    if (childOfEvent.getNodeName() == "period") { 
                        period = valueChildOfEvent;
                    }

                    if (childOfEvent.getNodeName() == "displayName") { 
                        displayName = valueChildOfEvent;
                    }

                    if (childOfEvent.getNodeName() == "hourMin") { 
                        hourMin = valueChildOfEvent;
                    }

                    if (childOfEvent.getNodeName() == "description") { 
                        description = valueChildOfEvent;
                    }
                    
                    if (childOfEvent.getNodeName() == "hourMax") { 
                        hourMax = valueChildOfEvent;
                    }

                    if (childOfEvent.getNodeName() == "items") { 
                        NodeList itemsNode;
                        itemsNode = childOfEvent.getChildNodes();
                        

                        for (int i = 0; i < itemsNode.getLength(); i++) {

                            Item itemObject = new Item();
                            
                            Node itemNode = itemsNode.item(i);

                            NodeList propertyItemsNode = itemNode.getChildNodes();
                            for (int pi = 0; pi < propertyItemsNode.getLength(); pi++) {
                                Node pitemNode = propertyItemsNode.item(pi);
 

                                if(pitemNode.hasChildNodes())
                                {

                                    String valuePitemNode = pitemNode.getChildNodes().item(0).getNodeValue();
                                     
                                    if (pitemNode.getNodeName() == "object_id") {
                                        int object_id = Integer.parseInt(valuePitemNode);
                                        itemObject.objectId = object_id;
                                    }

                                    if (pitemNode.getNodeName() == "number") {
                                       int number = Integer.parseInt(valuePitemNode);
                                       itemObject.number = number;
                                    } 

                                    if (pitemNode.getNodeName() == "errorMsg") { 
                                       itemObject.errorMsg = valuePitemNode;
                                    }
                                    
                                }
                            }
                            if(itemObject.objectId != 0)
                            items.add(itemObject);

                        }

                    }
                }

                Event currentEvent = new Event(name, isActive, period, items);
                currentEvent.hourMin = hourMin;
                currentEvent.hourMax = hourMax;
                currentEvent.displayName = displayName;
                currentEvent.description = description;
                _events.add(currentEvent);

            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
