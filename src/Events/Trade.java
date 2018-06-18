package Events;

import static Main.Game.me;
import Things.Entities.Entity;
import Things.Item;
import Things.Thing;
import javafx.scene.control.Button;

public class Trade extends Event {
	
	public static final String[] BUTTON_SET = {"Go Back", "Buy", "Sell"};
	private static final String TRADE_SUCCESS_TEXT = "%s successfully %s." +
			" You now have %d copper, %d silver, and %d gold.\n";
	private static final String TRADE_FAIL_TEXT =
			"You could not %s %s because the buyer does not have enough money.";

	private boolean toBuy;

	private Entity other;

	public Trade(String title, String text, Entity npc, Event parentEvent) {
		super(title, text, parentEvent, npc, BUTTON_SET);
		other = npc;
	}

	@Override
	public Event chooseNewEvent(Button button) {
	    String command = button.getText();

		System.err.println("Trade command: " + command);
		switch(command) {
			case "Buy":
				toBuy = true;
				return (new Inventory(other, this));
			case "Sell":
				return (new Inventory(me, this));
			case "Go Back":
				return parentEvent;

			default:
				int id = (int) button.getUserData();
				if(toBuy) {
					toBuy = false;
					return buy(id, command);
				} else {
					return sell(id, command);
				}
		}
	}
	
	private Event buy(int itemID, String itemName) {
		if(me.trade(other, itemID)) {
			String temp = String.format(TRADE_SUCCESS_TEXT, itemName,
					"bought", me.getCopper(), me.getSilver(), me.getGold());
			return new Next("buySuccess", temp, this);
		} else {
			String temp = String.format(TRADE_FAIL_TEXT, "buy", itemName);
			return new Next("buyFail", temp, this);
		}
	}
	
	private Event sell(int itemID, String itemName) {
		if(other.trade(me, itemID)) {
            String temp = String.format(TRADE_SUCCESS_TEXT, itemName,
                "sold", me.getCopper(), me.getSilver(), me.getGold());
			return new Next("sellSuccess", temp, this);
		} else {
			String temp = String.format(TRADE_FAIL_TEXT, "sell", itemName);
			return new Next("sellFail", temp, this);
		}
	}

	@Override
	public void validate() {}
}
