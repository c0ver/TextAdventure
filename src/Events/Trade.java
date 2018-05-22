package Events;

import static main.Game.me;
import Things.Entity;

public class Trade extends Event {
	
	private static final String[] BUTTON_SET = {"Go Back", "Buy", "Sell"};
	private static final String BUY_SUCCESS_TEXT = "%s successfully bought. You now have %d money.\n";
	private static final String SELL_SUCCESS_TEXT = "%s successfully sold. You now have %d money.\n";
	private static final String BUY_FAIL_TEXT =
			"You could not buy %s. You have %d money and %s requires %d.\n";
	private static final String SELL_FAIL_TEXT =
			"You could not sell %s. Buyer has %d money and %s requires %d.\n";
	
	private boolean toBuy;

	public Trade(String title, String text, Entity npc, Event
			nextEvent) {
		super(title, text, nextEvent, BUTTON_SET);
		other = npc;
		System.err.println("Inventory size: " + other.getInventory().size());
	}

	@Override
	public Event chooseNewEvent(String command) {
		System.err.println("Trade command: " + command);
		switch(command) {
			case "Buy":
				toBuy = true;
				return (new Inventory(other, this));
			case "Sell":
				return (new Inventory(me, this));
			case "Go Back":
				return nextEvent;

			default:
				if(toBuy) {
					toBuy = false;
					return buy(command);
				} else {
					return sell(command);
				}
		}
	}
	
	private Event buy(String itemName) {
		int itemValue = other.getItemValue(itemName);
		if(me.loseMoney(itemValue)) {
			other.gainMoney(itemValue);
			other.removeItem(itemName);
			me.addItem(itemName);
			String temp = String.format(BUY_SUCCESS_TEXT, itemName, me.getMoney());
			return (new Next("buySuccess", temp, this));
		} else {
			String temp = String.format(BUY_FAIL_TEXT, itemName,
										me.getMoney(), itemName, itemValue);
			return (new Next("buyFail", temp, this));
		}
	}
	
	private Event sell(String itemName) {
		int itemValue = me.getItemValue(itemName);
		if(other.loseMoney(itemValue)) {
			me.gainMoney(itemValue);
			me.removeItem(itemName);
			other.addItem(itemName);
			String temp = String.format(SELL_SUCCESS_TEXT, itemName, me.getMoney());
			return (new Next("sellSuccess", temp, this));
		} else {
			String temp = String.format(SELL_FAIL_TEXT, itemName,
					other.getMoney(), itemName, itemValue);
			return (new Next("sellFail", temp, this));
		}
	}
}
