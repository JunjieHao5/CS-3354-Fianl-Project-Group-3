package testing;

import java.math.*;

public class Item {
	private String name;
	private boolean infinite; //does this item have infinite quantity?
	private int quantity; //if the item is infinite, this equals Integer.MAX_VALUE. Otherwise, it's the number of items available.
	private BigDecimal cost;
	
	public Item(String name, BigDecimal cost, int quantity, boolean infinite) {
		this.name = name;
		this.cost = cost;
		this.infinite = infinite;
		if (infinite)
		{
			this.quantity = Integer.MAX_VALUE;
		}
		else
		{
			this.quantity = quantity;
		}
	}
	
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
	public boolean isInfinite()
	{
		return infinite;
	}
	//does nothing if infinite is true.
	public void setQuantity(int quantity) {
		if (infinite) {
			return;
		}
		this.quantity = quantity;
	}
	public BigDecimal getCost() {
		return cost;
	}
}
