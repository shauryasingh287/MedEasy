import java.util.ArrayList;

public class Artist {
    String destination,source,id;
    ArrayList<String> med;
    public Artist()
    {

    }

    public Artist(String id, String source,String destination) {
        this.destination = destination;
        this.source = source;
        this.id=id;
    }

    public Artist(ArrayList<String> med1) {
     med=med1;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }
}
