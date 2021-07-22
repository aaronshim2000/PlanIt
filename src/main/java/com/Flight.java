package com.example;

// Used for saved flights table
public class Flight
{
    private String id;
    
    private int price;

    private String origin;
    private String destination;

    private String outboundDate;
    private String inboundDate;

    public String getId()
    {
        return id;
    }

    public int getPrice()
    {
        return price;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getOutboundDate()
    {
        return outboundDate;
    }

    public String getInboundDate()
    {
        return inboundDate;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setPrice(int i)
    {
        price = i;
    }

    public void setOrigin(String s)
    {
        origin = s;
    }

    public void setDestination(String s)
    {
        destination = s;
    }

    public void setOutboundDate(String s)
    {
        outboundDate = s;
    }

    public void setInboundDate(String s)
    {
        inboundDate = s;
    }
}
