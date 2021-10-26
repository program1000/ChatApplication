package chat.model;

public class Contact {

    private int id;

    private String name;
    private String alias;

    public Contact( String name, String alias ) {
        this.name = name;
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

}
