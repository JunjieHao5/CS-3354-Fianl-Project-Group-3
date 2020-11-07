package testing;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class StoreTest {
	private Store store;
	private Account account;
	@Before
	public void before()
	{
		System.out.println("BeforeStoreTest");
		//setting up store with items
		store = new Store();
		account = new Account();
	}
	@Test
	public void testInvalidItemPurchase()
	{
		//---- invalid item exclusion ----
		
		try
		{
			store.addItem(new Item("pawn", BigDecimal.valueOf(-1), 0, false));
			store.addItem(new Item("king", BigDecimal.valueOf(99999999999L), -1, false));
		}
		catch (Exception e)
		{
			fail("store additem fail");
		}
		
		//first check that the pawn and king are not in the store inventory
		//they were added with quantities 0 and -1
		
		try {
		PurchaseResult res = store.purchaseItem("pawn", 1, account);
		assertEquals(res, PurchaseResult.RESULT_ITEM_NOT_FOUND);
		res = store.purchaseItem("king", 1, account);
		assertEquals(res, PurchaseResult.RESULT_ITEM_NOT_FOUND);
		}
		catch (Exception e)
		{
			fail("purchase exception: " + e.toString());
		}
	}
	@Test
	public void testFiniteItemPurchase()
	{
		//---- finite item purchasing ----
		
		try
		{
			store.addItem(new Item("bishop", BigDecimal.valueOf(0.1), 2, false));
			store.addItem(new Item("queen", BigDecimal.valueOf(1), Integer.MAX_VALUE, false));
		}
		catch (Exception e)
		{
			fail("store additem fail");
		}
		
		//now check that purchasing a bishop fails with insufficient funds.
		//make sure that it doesn't modify the quantity in store, the account inventory, or the account balance.
		try {
		account.setBalance(BigDecimal.ZERO);
		PurchaseResult res = store.purchaseItem("bishop", 1, account);
		assertEquals(PurchaseResult.RESULT_INSUFFICIENT_FUNDS, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
		assertEquals(2, store.getItem("bishop").getQuantity());
		assertEquals(null, account.getItem("bishop"));
		
		//now check that purchasing a bishop works with sufficient funds.
		//make sure that it subtracts 1 from the quantity in store, adds it to the account inventory, subtracts from the account balance.
		account.setBalance(BigDecimal.valueOf(0.1));
		res = store.purchaseItem("bishop", 1, account);
		assertEquals(PurchaseResult.RESULT_SUCCESSFUL, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
		assertEquals(1, store.getItem("bishop").getQuantity());
		assertEquals(1, account.getItem("bishop").getQuantity());
		
		//try purchasing it again. this time it should fail
		res = store.purchaseItem("bishop", 1, account);
		assertEquals(PurchaseResult.RESULT_INSUFFICIENT_FUNDS, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
		assertEquals(1, store.getItem("bishop").getQuantity());
		assertEquals(1, account.getItem("bishop").getQuantity());
		
		//purchase it successfully once more and make sure it disappears from the stop
		account.setBalance(BigDecimal.valueOf(0.2));
		res = store.purchaseItem("bishop", 1, account);
		assertEquals(PurchaseResult.RESULT_SUCCESSFUL, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.valueOf(0.1)));
		assertEquals(null, store.getItem("bishop"));
		assertEquals(2, account.getItem("bishop").getQuantity());
		
		//move onto the queen: purchase Integer.MAX_VALUE queens successfully.
		account.setBalance(BigDecimal.valueOf(Integer.MAX_VALUE));
		res = store.purchaseItem("queen", Integer.MAX_VALUE, account);
		assertEquals(PurchaseResult.RESULT_SUCCESSFUL, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
		assertEquals(null, store.getItem("queen"));
		assertEquals(Integer.MAX_VALUE, account.getItem("queen").getQuantity());
		}
		catch (Exception e) {
			fail("purchase exception: " + e.toString());
		}
	
	}
	@Test
	public void testInfiniteItemPurchase()
	{
		//---- infinite item purchasing ----
		
		try
		{
			store.addItem(new Item("knight", BigDecimal.valueOf(5), 1, true));
		}
		catch (Exception e)
		{
			fail("store additem fail");
		}
		
		//try to fail the purchase with insufficient funds
		//make sure that it doesn't modify the quantity in store, the account inventory, or the account balance.
		
		try {
		account.setBalance(BigDecimal.ZERO);
		PurchaseResult res = store.purchaseItem("knight", 1, account);
		assertEquals(PurchaseResult.RESULT_INSUFFICIENT_FUNDS, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
		assertEquals(Integer.MAX_VALUE, store.getItem("knight").getQuantity());
		assertEquals(null, account.getItem("knight"));
		
		//successfully purchase 2 knights
		//make sure that it doesn't subtract from the quantity in store, adds it to the account inventory, subtracts from the account balance.
		account.setBalance(BigDecimal.valueOf(11));
		res = store.purchaseItem("knight", 2, account);
		assertEquals(PurchaseResult.RESULT_SUCCESSFUL, res);
		assertEquals(0, account.getBalance().compareTo(BigDecimal.ONE));
		assertEquals(Integer.MAX_VALUE, store.getItem("knight").getQuantity());
		assertEquals(2, account.getItem("knight").getQuantity());
		}
		catch (Exception e)
		{
			fail("purchase exception: " + e.toString());
		}
	}
}
