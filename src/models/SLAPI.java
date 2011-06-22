package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Location;
import org.w3c.dom.*;

import javax.xml.transform.Transformer;

public class SLAPI {

    public static void save(Object obj, String path) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    public static File[] listFiles(String parent, String directoryPath) {
        File[] files = null;
        File directoryToScan = new File(parent, directoryPath);
        files = directoryToScan.listFiles();
        return files;
    }

    public static Object load(String path) throws Exception {
        File test = new File(path);
        if (!test.exists()) {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(new ArrayList<MyLocation>());
            oos.flush();
            oos.close();

            return new  ArrayList<Location>();
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Object result = ois.readObject();
        ois.close();
        return result;
    }

    public static void remove(String string) {
        // TODO Auto-generated method stub
        File test = new File(string);
        test.delete();
    }

}