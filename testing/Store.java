package testing;

import java.util.*;
import java.math.*;

public class Store {
	private HashMap<String,Item> stock;
	
	public Store()
	{
		stock = new HashMap<String,Item>();
	}
	//updates the store stock and the buyer's account if successful purchase.
	//if unsuccessful, doesn't modify anything
	public PurchaseResult purchaseItem(String itemName, int quantity, Account buyer) throws Exception
	{
		//first we try to find the item.
		Item toBuy = stock.get(itemName);
		//if the item is not found, we return an unsuccessful PurchaseResult
		if (toBuy == null)
		{
			return PurchaseResult.RESULT_ITEM_NOT_FOUND;
		}
		//if the item does not have enough stock, we return an unsuccessful PurchaseResult
		if (toBuy.getQuantity() < quantity)
		{
			return PurchaseResult.RESULT_OUT_OF_STOCK;
		}
		//calculate the total cost: quantity*toBuy.getCost()
		BigDecimal cost = toBuy.getCost().multiply(BigDecimal.valueOf(quantity));
		//if the account doesn't have sufficient funds, we return an unsuccessful PurchaseResult
		if (cost.compareTo(buyer.getBalance()) > 0)
		{
			return PurchaseResult.RESULT_INSUFFICIENT_FUNDS;
		}
		//now we know all criteria are met. we carry out the purchase and add it to the account's inventory.
		//we also subtract the cost form the account's balance and subtract the amount from the stock.
		//if this purchase sets the item's stock to 0, we remove the item from the stock map.
		toBuy.setQuantity(toBuy.getQuantity()-quantity);
		if (toBuy.getQuantity() <= 0)
		{
			stock.remove(itemName);
		}
		buyer.setBalance(buyer.getBalance().subtract(cost));
		buyer.AddItem(new Item(toBuy.getName(), toBuy.getCost(), quantity, false));		
		return PurchaseResult.RESULT_SUCCESSFUL;
	}
	//adds an item to the stock or increases the quantity if an item with the same name already exists.
	public void addItem(Item item) throws Exception
	{
		if (item.getQuantity() <= 0)
		{
			return; //shouldn't do anything if the item doesn't have a quantity.
		}
		Item i = stock.get(item.getName());
		if (i != null && !item.getCost().equals(i.getCost()))
		{
			throw new Exception("item cost and existing costs do not match");
		}
		if (i != null && !i.isInfinite())
		{
			//item is in the hashmap, so we update its quantity
			if (item.isInfinite())
			{
				stock.replace(item.getName(), item);
			}
			else
			{
				i.setQuantity(i.getQuantity() + item.getQuantity());
			}
		}
		else
		{
			//item is not in the hashmap, so we insert it
			stock.put(item.getName(), item);
		}
	}
	//returns the item or null if not found.
	public Item getItem(String name)
	{
		return stock.get(name);
	}
}
