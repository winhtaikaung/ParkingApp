package models;

/**
 * Created by winhtaikaung on 2/28/16.
 */
public class Link
{
    private String title;

    private String rel;

    private String href;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getRel ()
    {
        return rel;
    }

    public void setRel (String rel)
    {
        this.rel = rel;
    }

    public String getHref ()
    {
        return href;
    }

    public void setHref (String href)
    {
        this.href = href;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", rel = "+rel+", href = "+href+"]";
    }
}
