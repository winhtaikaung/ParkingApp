package models;

/**
 * Created by winhtaikaung on 2/28/16.
 */
public class Title
{
    private String content;

    private String type;

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

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
        return "ClassPojo [content = "+content+", type = "+type+"]";
    }
}

