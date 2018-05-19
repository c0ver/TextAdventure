package main;

import static main.Game.windowHeight;
import static main.Game.windowWidth;
import static main.Game.displayEvent;

import java.util.ArrayList;

import Things.Entity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public abstract class Event implements EventHandler<ActionEvent> {

    private static final String NULL_EVENT_ERROR = "ERROR: null event was " +
            "given to displayNewEvent";
	
	private static final double buttonHeight = 0.1;
	private static final double buttonWidth = 0.333;
	
	private static final String TEXT_ERROR =
			"ERROR: Necessary texts: titleText and mainText are missing";

	protected static final String BUTTON_ERROR = "ERROR: The button that was " +
            "clicked is not considered an option";

	// texts of the setting
	private String title, mainText;
	
	private Button[] buttonSet;

	protected Entity other;
	
	protected Event parentEvent;
	
	public Event(String title, String mainText, Event parentEvent) {
		// needs title and mainText
		if(title == null || mainText == null) {
			System.err.println(TEXT_ERROR);
		}
		
		this.title = title;
		this.mainText = mainText;
		this.parentEvent = parentEvent;
	}
	
	/* not used ? */
	public Event(String title, String mainText, Event parentEvent, ArrayList<String> buttonSet) {
		this(title, mainText, parentEvent);
		createButtons(buttonSet);
	}
	
	public Event(String title, String mainText, Event parentEvent, String[] buttonSet) {
		this(title, mainText, parentEvent);
		createButtons(buttonSet);
	}
	
	protected void createButtons(ArrayList<String> buttonSet) {
		this.buttonSet = new Button[buttonSet.size()];
		for(int x=0; x<buttonSet.size(); x++) {
			this.buttonSet[x] = new Button(buttonSet.get(x));
			this.buttonSet[x].setPrefSize(windowWidth * buttonWidth, windowHeight * buttonHeight);
			this.buttonSet[x].setOnAction(this);
			//this.buttonSet[x].setUserData(parentEvent);
		}
	}
	
	private void createButtons(String[] buttonSet) {
		this.buttonSet = new Button[buttonSet.length];
		for(int x=0; x<buttonSet.length; x++) {
			this.buttonSet[x] = new Button(buttonSet[x]);
			this.buttonSet[x].setPrefSize(windowWidth * buttonWidth, windowHeight * buttonHeight);
			this.buttonSet[x].setOnAction(this);
			//this.buttonSet[x].setUserData(parentEvent);
		}
	}

	private void displayNewEvent(Event newEvent) {
		if(newEvent == null) {
			System.err.println(NULL_EVENT_ERROR);
			return;
		}
		displayEvent(newEvent);
	}
	
	public abstract Event chooseNewEvent(String command);

    @Override
    public void handle(ActionEvent event) {
        // Get the name of the button
        String command = ((Button) event.getSource()).getText();

        displayNewEvent(chooseNewEvent(command));
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Event)) {
            return false;
        }

        Event obj = (Event) o;

        return title.equals(obj.title);
    }

    public String getTitle() {
        return title;
    }

    public String getMainText() {
        return mainText;
    }

    public int getButtonCount() {
        return buttonSet.length;
    }

    public Button[] getButtonSet() {
        return buttonSet;
    }

    public Entity getOther() {
        return other;
    }
}
