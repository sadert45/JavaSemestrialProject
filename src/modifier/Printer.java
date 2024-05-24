package modifier;

import opener.People;

import java.util.List;

public class Printer {

    PeopleListModifier peopleListModifier = new PeopleListModifier();
    List<People> peopleCopy = peopleListModifier.getPeople();

    public void Printer(){
        peopleListModifier.listCorrection();
        for(int i = 0; i < peopleListModifier.getPeople().size(); i++){
            System.out.println(peopleListModifier.getPeople().get(i).toString());
        }
    }
}
