package manipulator;

import opener.People;
import modifier.PeopleListModifier;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectManipulator {
    PeopleListModifier peopleListModifier = new PeopleListModifier();
    List<People> peopleCopy = peopleListModifier.getPeople();


    private Scanner scanner;
    Pattern pattern  = Pattern.compile("<(\\w+):(\\w+)>([^<]*)</\\1:\\2>");
    Pattern attributePattern = Pattern.compile("[<]([a-zA-Z]+):([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*=\\s*\"([0-9_]+)\"[>]");
    private String filePath = null;

    public ObjectManipulator(String filePath){
        this.filePath = filePath;
        try {
            scanner = new Scanner(new File(filePath));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private Matcher fileScanner(){
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            Matcher matcher = pattern.matcher(text);
            Matcher attributeMatcher = attributePattern.matcher(text);
            boolean attributeMatchFound = attributeMatcher.find();
            boolean matchFound = matcher.find();
            if (attributeMatchFound)
                return attributeMatcher;
            if (matchFound)
                return matcher;
        }
        return null;
    }

    public void textOfElement(String id){
        while(true){
            String text = null;
            Matcher matcher = fileScanner();
            if(matcher.groupCount() > 2 && matcher.group(3).equals("id") && matcher.group(4).equals(id)){
                do{
                    text = scanner.nextLine().trim();
                    System.out.println(text);
                }while (!text.equals("</h:person>"));
                break;
            }
        }
    }

    public String selectElement(String id, String key){
        while (true){
            Matcher matcher = fileScanner();
            if(matcher.groupCount() > 2 && matcher.group(3).equals("id") && matcher.group(4).equals(id)) {
                while (true) {
                    if (matcher.group(2).equals(key)) {
                        return matcher.group(3);
                    } else {
                        matcher = fileScanner();
                    }
                }
            }
        }
    }

    public void setElement(String id, String key, String value){
        int counter = 0;
        for(int i = 0; i < peopleCopy.size(); i++){
            if(peopleCopy.get(i).getId().equals(id)){
                if(key.equals("name")){
                    peopleCopy.get(i).setName(value);
                    System.out.println("Name successfully changed");
                }else if(key.equals("address")) {
                    peopleCopy.get(i).setAddress(value);
                    System.out.println("Address successfully changed");
                }
            }
        }
    }

    public void deleteElement(String id, String key){
        for(int i = 0; i < peopleCopy.size(); i++){
            if(peopleCopy.get(i).getId().equals(id)) {
                if (key.equals("name")) {
                    peopleCopy.get(i).setName(null);
                    System.out.println("Name successfully deleted");
                } else if (key.equals("address")) {
                    peopleCopy.get(i).setAddress(null);
                    System.out.println("Address successfully deleted");
                }
            }
        }
    }

    public void childrenElements(String id){
        for(int i = 0; i < peopleCopy.size(); i++){
            if(peopleCopy.get(i).getId().equals(id)) {
                System.out.println("Id: " + peopleCopy.get(i).getId() +"\n" +
                        "Name: " + peopleCopy.get(i).getName() + "\n" +
                        "Address: " + peopleCopy.get(i).getAddress()+"\n");
            }
        }
    }

    public String childElement(String id, String n){
        while (true){
            Matcher childMather = fileScanner();
            if(childMather.groupCount() > 2 && childMather.group(3).equals("id") && childMather.group(4).equals(id)){
                for(int i = 0; i < Integer.parseInt(n);i++){
                    childMather = fileScanner();
                }
                return childMather.group(3);
            }
        }
    }
    public void newChild(String id){
        peopleCopy.add(new People(id, null,null));
        System.out.println("You successfully add new child");
    }
}
