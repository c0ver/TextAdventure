package Events;

import java.util.ArrayList;
import java.util.List;

import Things.Entities.Entity;
import javafx.scene.control.Button;

import static Main.Game.displayEvent;
import static Main.Game.windowHeight;
import static Main.Game.windowWidth;

public abstract class Event {

    private static final String NULL_EVENT_ERROR = "ERROR: null event was " +
            "given to displayNewEvent";

	private static final double buttonHeight = 0.1;
	private static final double buttonWidth = 0.333;

	private static final String TEXT_ERROR =
			"ERROR: Necessary texts: titleText and text are missing";

	protected static final String BUTTON_ERROR = "ERROR: The button that was " +
            "clicked is not considered an option";

	// texts of the setting
	private String title, text;

	// used by Special events to create their buttons
	protected String response;

	private transient Button[] buttonSet;

	protected Entity other;

	protected Event parentEvent;

	// placeholder
	public Event(String title, String text) {
		this.title = title;
		this.text = text;
	}

	/* used in creating quest and Default events */
	public Event(String title, String text, String[] buttonSet) {
		this.title = title;
		this.text = text;
		createButtons(buttonSet);
	}

	public Event(String title, String text, Event parentEvent) {
		// needs title and text
		if(title == null || text == null) {
			System.err.println(TEXT_ERROR);
		}

		this.title = title;
		this.text = text;
		this.parentEvent = parentEvent;
	}

	public Event(String title, String text, Event parentEvent, String[]
            buttonSet) {
		this(title, text, parentEvent);
		createButtons(buttonSet);
	}

	protected void resetEvent(String title, String text) {
		this.title = title;
		this.text = text;
	}

	/* used by inventory event */
	protected void createButtons(List<String> buttonSet) {
		this.buttonSet = new Button[buttonSet.size()];
		for(int x=0; x<buttonSet.size(); x++) {
			this.buttonSet[x] = new Button(buttonSet.get(x));
			this.buttonSet[x].setPrefSize(windowWidth * buttonWidth, windowHeight * buttonHeight);
			this.buttonSet[x].setOnAction(e -> buttonPress(((Button) e.getSource()).getText()));
			//this.buttonSet[x].setUserData(parentEvent);
		}
	}

	private void createButtons(String[] buttonSet) {
		this.buttonSet = new Button[buttonSet.length];
		for(int x=0; x<buttonSet.length; x++) {
			this.buttonSet[x] = new Button(buttonSet[x]);
			this.buttonSet[x].setPrefSize(windowWidth * buttonWidth, windowHeight * buttonHeight);
			this.buttonSet[x].setOnAction(e -> buttonPress(((Button) e.getSource()).getText()));
			//this.buttonSet[x].setUserData(parentEvent);
		}
	}

	public abstract Event chooseNewEvent(String command);

	public abstract void validate();

    private void buttonPress(String command) {
		Event newEvent = chooseNewEvent(command);
		//newEvent = this;
		if(newEvent != null) {
			displayEvent(newEvent);
		} else {
			System.err.println(NULL_EVENT_ERROR);
		}
	}

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Event)) {
            return false;
        }

        Event obj = (Event) o;

        return title.equals(obj.title);
    }

    public void setParentEvent(Event parentEvent) { this.parentEvent = parentEvent; }

    public String getTitle() { return title; }

    public String getText() { return text; }

    public int getButtonCount() { return buttonSet.length; }

    public Button[] getButtonSet() { return buttonSet; }

    public Entity getOther() { return other; }

    public String getResponse() { return response; }
}
