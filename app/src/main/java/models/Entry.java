package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by winhtaikaung on 2/28/16.
 */
public class Entry
{

    private String id;

    private Summary summary;

    @SerializedName("content")
    private Content content;

    private Author author;

    private Title title;

    private Category category;

    private String updated;

    private Link link;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Summary getSummary ()
    {
        return summary;
    }

    public void setSummary (Summary summary)
    {
        this.summary = summary;
    }


    public Content getContent ()
    {
        return content;
    }

    public void setContent (Content content)
    {
        this.content = content;
    }

    public Author getAuthor ()
    {
        return author;
    }

    public void setAuthor (Author author)
    {
        this.author = author;
    }

    public Title getTitle ()
    {
        return title;
    }

    public void setTitle (Title title)
    {
        this.title = title;
    }

    public Category getCategory ()
    {
        return category;
    }

    public void setCategory (Category category)
    {
        this.category = category;
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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", summary = "+summary+", content = "+content+", author = "+author+", title = "+title+", category = "+category+", updated = "+updated+", link = "+link+"]";
    }
}
