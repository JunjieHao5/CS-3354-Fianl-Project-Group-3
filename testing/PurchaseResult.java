package testing;

public class PurchaseResult {
	private boolean successful;
	private boolean itemFound;
	private boolean sufficientStock;
	private boolean sufficientFunds;
	
	//only possible results. class is immutable so this is okay.
	public static PurchaseResult RESULT_SUCCESSFUL = new PurchaseResult(true,true,true,true);
	public static PurchaseResult RESULT_ITEM_NOT_FOUND = new PurchaseResult(false,false,false,false);
	public static PurchaseResult RESULT_OUT_OF_STOCK = new PurchaseResult(false,true,false,false);
	public static PurchaseResult RESULT_INSUFFICIENT_FUNDS = new PurchaseResult(false,true,true,false);
	
	//if successful, sets the other fields to true regardless of their values here.
	private PurchaseResult(boolean successful, boolean itemFound, boolean sufficientStock, boolean sufficientFunds)
	{
		this.successful = successful;
		if (successful)
		{
			itemFound = true;
			sufficientFunds = true;
			sufficientStock = true;
		}
		else
		{
			this.itemFound = itemFound;
			this.sufficientStock = sufficientStock;
			this.sufficientFunds = sufficientFunds;
		}
	}
	
	public boolean isSuccessful() {return successful;}
	public boolean isItemFound() {return itemFound;}
	public boolean hasSufficientStock() {return sufficientStock;}
	public boolean hasSufficientFunds() {return sufficientFunds;}
}
