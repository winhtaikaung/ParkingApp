package models;

/**
 * Created by winhtaikaung on 2/29/16.
 */
public class Latitude {
    private String content;

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [content = "+content+"]";
    }
}
