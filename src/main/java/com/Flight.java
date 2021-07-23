package com.example;

// Used for saved flights table
public class Flight
{
    private String id;
    
    private String username;

    private String price;

    private String origin;
    private String destination;

    private String outboundDate;

    private String airline;

    public String getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPrice()
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

    public String getAirline()
    {
        return airline();
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setUsername(String s)
    {
        username = s;
    }

    public void setPrice(String s)
    {
        price = s;
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

    public void setAirline(String s)
    {
        airline = s;
    }
}
