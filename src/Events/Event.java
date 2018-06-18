package Events;

import java.util.List;

import Main.DisplayEvent;
import Plot.Plot;
import Things.Entities.Entity;
import Things.Plottable;
import javafx.scene.control.Button;

import static Main.DisplayEvent.windowHeight;
import static Main.DisplayEvent.windowWidth;

public abstract class Event {

    private static final String NULL_EVENT_ERROR = "ERROR: null event was " +
            "given to displayNewEvent";

	private static final String TEXT_ERROR =
			"ERROR: Necessary texts: titleText and text are missing";

	protected static final String BUTTON_ERROR = "ERROR: The button that was " +
            "clicked is not considered an option";

	private static final String ADD_DATA_ERROR =
			"ERROR: The data given does not match the buttons";

	public static final String RETURN = "Go Back";

	private static final double buttonHeight = 0.1;
	private static final double buttonWidth = 0.333;

	// used by displayEvents
	private String title, text;
	private transient Button[] buttonSet;
	protected Plottable other;

	// used by Special events to create their buttons
	protected String response;

	protected Event parentEvent;

	public Event(String title, String text, String[] buttonNames) {
		if(title == null || text == null) System.err.println(TEXT_ERROR);

		this.title = title;
		this.text = text;
		if(buttonNames != null) createButtons(buttonNames);
	}

	public Event(String title, String text, Event parentEvent, Entity other,
				 String[] buttonNames) {
		this(title, text, buttonNames);
		this.parentEvent = parentEvent;
		this.other = other;
	}

	protected void resetEvent(String title, String text) {
		this.title = title;
		this.text = text;
	}

	private void createButtons(String[] buttonNames) {
		buttonSet = new Button[buttonNames.length];
		for(int x=0; x<buttonNames.length; x++) {
			buttonSet[x] = new Button(buttonNames[x]);
			buttonSet[x].setPrefSize(windowWidth * buttonWidth, windowHeight * buttonHeight);
			buttonSet[x].setOnAction(e -> buttonPress((Button) e.getSource()));
		}
	}

	public void addUserDataToButtons(Object[] o) {

		if(o.length > buttonSet.length) System.err.println(ADD_DATA_ERROR);

	    for(int x = 0; x < o.length; x++) {
            buttonSet[x].setUserData(o[x]);
		}
	}

	public abstract Event chooseNewEvent(Button button);

	public abstract void validate();

    private void buttonPress(Button button) {
		Event newEvent = chooseNewEvent(button);
		//newEvent = this;
		if(newEvent != null) {
			DisplayEvent.show(newEvent);
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

    public Plottable getOther() { return other; }

    public String getResponse() { return response; }
}
