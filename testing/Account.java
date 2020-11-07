package testing;

import java.math.*;
import java.util.*;

public class Account {
	private BigDecimal balance;
	private HashMap<String,Item> inventory;
	
	public Account() {
		balance = BigDecimal.ZERO;
		inventory = new HashMap<String,Item>();
	}
	
	public void setBalance(BigDecimal amount) {
		balance = amount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	//if an item with the same name exists in the inventory, it adds the quantity
	public void AddItem(Item item) throws Exception
	{
		if (item.getQuantity() <= 0)
		{
			return; //shouldn't do anything if the item doesn't have a quantity.
		}
		Item i = inventory.get(item.getName());
		if (i != null && !item.getCost().equals(i.getCost()))
		{
			throw new Exception("item cost and existing costs do not match");
		}
		if (i != null && !i.isInfinite())
		{
			//item is in the hashmap, so we update its quantity
			if (item.isInfinite())
			{
				inventory.replace(item.getName(), item);
			}
			else
			{
				i.setQuantity(i.getQuantity() + item.getQuantity());
			}
		}
		else
		{
			//item is not in the hashmap, so we insert it
			inventory.put(item.getName(), item);
		}
	}
	//returns null if no item is found
	public Item getItem(String name)
	{
		return inventory.get(name);
	}
}
