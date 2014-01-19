package com.example.contactmanager;

import android.util.Log;

public class Contact {

	private static final String TAG = "log";
	private int id = 0;
	private String firstName = "";
	private String lastName = "";
	private long phoneNumber = 0L;
	private int zipCode = 0;
	private String gender = "U";
	private int age = 0;
	private String address = "";
	private String city = "";
	private String state = "";
	private int tempature = -999;
	private String weatherText = "";
	
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	public void setPhoneNumber(long phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public void setZipCode(int zipCode)
	{
		this.zipCode = zipCode;
	}
	
	public void setTempature(int tempature)
	{
		this.tempature = tempature;
	}
	
	public void setWeatherText(String weatherText)
	{
		this.weatherText = weatherText;
	}
	
	
	
	public int getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public long getPhoneNumber()
	{
		return phoneNumber;
	}
	
	public String getState()
	{
		return state;
	}
	
	public int getZipCode()
	{
		return zipCode;
	}
	

	public int getTempature()
	{
		return tempature;
	}
	
	public String getWeatherText()
	{
		return weatherText;
	}
	

	
	

	public boolean isValidId()
	{
		if (this.id >= 0)
			return true;
		else
			return false;
		
	}

	public boolean isValidFirstName()
	{
		if (this.firstName.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidLastName()
	{
		if (this.lastName.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidAddress()
	{
		if (this.address.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidAge()
	{
		if (this.age >= 0 && this.age <= 100 )
			return true;
		else 
			return false;
	}
	
	public boolean isValidCity()
	{
		if (this.city.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidGender()
	{
		if (this.gender.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidPhoneNumber()
	{
		if (Long.toString(this.phoneNumber).length() == 10)
			return true;
		else
			return false;
	}
	
	public boolean isValidState()
	{
		if (this.state.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidZipCode()
	{
		if (Integer.toString(this.zipCode).length() == 5)
			return true;
		else
			return false;
	}
	

	public boolean isValidTempature()
	{
		if (this.tempature != - 999)
			return true;
		else
			return false;
	}
	
	public boolean isValidWeatherText()
	{
		if (this.weatherText.length() > 0)
			return true;
		else 
			return false;
	}
	
	public boolean isValidContact()
	{
		if (isValidZipCode() && isValidState() && isValidPhoneNumber() && isValidGender() && isValidCity() && isValidAge() && isValidAddress() && isValidLastName() && isValidFirstName())
			return true;
		else
			return false;
	
	}


	

}
