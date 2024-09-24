public class Word {

    String name = "";
    String note = "";

    public Word(String s, String n){
        name = s;
        note = n;
    }

    public String getName(){
        return name;
    }

    public String getNote(){
        return note;
    }
}
