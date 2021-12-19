package com.rad.ms.corona_view.auth.aws.sns.model;

import java.math.BigDecimal;

public class Message
{
	private String		mailTo;
	private String		phoneTo;
	private String		category;
	private String		productName;
	private EventType	eventType;
	private String		seller;
	private BigDecimal	newPrice;

	public Message()
	{
	}

	/**
	 * @param mailTo
	 * @param phoneTo
	 * @param category
	 * @param productName
	 * @param eventType
	 * @param seller
	 * @param newPrice
	 */
	public Message(String mailTo, String phoneTo, String category, String productName, EventType eventType, String seller, BigDecimal newPrice)
	{
		super();
		this.mailTo = mailTo;
		this.phoneTo = phoneTo;
		this.category = category;
		this.productName = productName;
		this.eventType = eventType;
		this.seller = seller;
		this.newPrice = newPrice;
	}

	public String getMailTo()
	{
		return this.mailTo;
	}

	public void setMailTo(String mailTo)
	{
		this.mailTo = mailTo;
	}

	public String getPhoneTo()
	{
		return this.phoneTo;
	}

	public void setPhoneTo(String phoneTo)
	{
		this.phoneTo = phoneTo;
	}

	public String getCategory()
	{
		return this.category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getProductName()
	{
		return this.productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public EventType getEventType()
	{
		return this.eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public String getSeller()
	{
		return this.seller;
	}

	public void setSeller(String seller)
	{
		this.seller = seller;
	}

	public BigDecimal getNewPrice()
	{
		return this.newPrice;
	}

	public void setNewPrice(BigDecimal newPrice)
	{
		this.newPrice = newPrice;
	}

}
