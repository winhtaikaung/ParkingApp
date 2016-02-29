package models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by winhtaikaung on 2/28/16.
 */
public class Feed
{
    private String id;

    @SerializedName("@type")
    private String title;

    private String updated;

    private Link link;

    @SerializedName("entry")
    private List<Entry> entry;

    private String xmlns;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getUpdated ()
    {
        return updated;
    }

    public void setUpdated (String updated)
    {
        this.updated = updated;
    }

    public Link getLink ()
    {
        return link;
    }

    public void setLink (Link link)
    {
        this.link = link;
    }

    public List<Entry> getEntry ()
    {
        return entry;
    }

    public void setEntry (List<Entry> entry)
    {
        this.entry = entry;
    }

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+", updated = "+updated+", link = "+link+", entry = "+entry+", xmlns = "+xmlns+"]";
    }
}
