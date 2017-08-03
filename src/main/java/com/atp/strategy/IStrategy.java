package com.atp.strategy;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import com.atp.data.PriceBar;

/**
 * @author snclucas
 *
 */
public abstract class IStrategy  extends Observable {
	

	
	private Vector<Observer> observersList;
	
	public IStrategy(String n) {
		observersList = new Vector<Observer>();
	}
	
	

	
	
	public abstract double tick(PriceBar priceBar);

	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		observersList.addElement(o);
		System.err.println("added");
	}

	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
		observersList.remove(o);
	}

	@Override
	public void notifyObservers(Object arg) {
		super.notifyObservers(arg);
		// Send notify to all Observers
	    for (int i = 0; i < observersList.size(); i++) {
	      Observer observer = observersList.elementAt(i);
	      observer.update(this, arg);
	      //observer.refreshData(this);
	    }
	}
	
	

}
