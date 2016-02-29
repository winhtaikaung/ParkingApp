package models;

/**
 * Created by winhtaikaung on 2/28/16.
 */
public class Content
{
    private String type;

    private Properties properties;

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [type = "+type+"]";
    }

    public Properties getMproperties() {
        return properties;
    }

    public void setMproperties(Properties mproperties) {
        this.properties = mproperties;
    }
}
