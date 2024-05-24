package saver;

import opener.People;
import modifier.PeopleListModifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Saver {

    PeopleListModifier peopleListModifier = new PeopleListModifier();
    List<People> peopleCopy = peopleListModifier.getPeople();

    public void saver(String path){
        peopleListModifier.listCorrection();
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("<root xmlns:h=\"http://www.w3.org/TR/html4/\">\n");
            fileWriter.write("\t<h:people>\n");
            for(int i = 0; i < peopleCopy.size();i++){
                fileWriter.write("\t\t<h:person id=\""+peopleCopy.get(i).getId()+"\">\n");
                if(peopleCopy.get(i).getName() != null) {
                    fileWriter.write("\t\t\t<h:name> " + peopleCopy.get(i).getName() + " </h:name>\n");
                }
                if(peopleCopy.get(i).getAddress() != null) {
                    fileWriter.write("\t\t\t<h:address> " + peopleCopy.get(i).getAddress() + " </h:address>\n");
                }
                fileWriter.write("\t\t</h:person>\n");
            }
            fileWriter.write("\t</h:people>\n");
            fileWriter.write("</h:root>\n");
            fileWriter.close();
        }catch (IOException e){
            System.out.println("Cannon create/modify file");
        }

    }
}
