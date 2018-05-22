package Events;

import static main.Game.me;

import Things.Entity;

public class Fight extends Event  {

    private static final String NULL_ENEMY_ERROR = "ERROR: Enemy is null";

    private static final String[] BUTTON_SET = {"Attack", "Defend",
            "Inventory", "Run"};

    private static final String WIN_TEXT = "You won against a %s.";
    private static final String ATTACK_TEXT = "%s attacked %s for %d " +
            "damage.\n";

    /* halves the incoming damage */
    private static final double defenseModifier = 0.5;

    public Fight(String title, String text, Entity npc, Event
                 nextEvent) {
        super(title, text, nextEvent, BUTTON_SET);
        other = npc;
    }

    private boolean checkCommand(Entity person, String command) {
        return person.getEnergy() >= person.energyCost(command);
    }

    private void actionEffects(Entity person, String command) {
        switch (command) {
            case "Attack":
                person.loseEnergy(command);
                break;
            case "Defend":
                person.loseEnergy(command);
                person.changeTempModifier(defenseModifier);
                break;
            case "Inventory":
                break;
        }
    }

    @Override
    public Event  chooseNewEvent(String command) {
        /* for now, the monster will do the same as the player */
        Entity[] participants = {me, other};

        /* check if both participants are able to do their actions before
        doing anything
         */
        for(Entity participant : participants) {
            if(!checkCommand(participant, command)) {
                System.err.println(participant.getName() + " cannot do this " +
                        "action.");
                return this;
            }
        }

        /* add the effects of each participant */
        for (Entity participant : participants) {
            actionEffects(participant, command);
        }

        String mainText = "";

        for(Entity self : participants) {
            Entity enemy = null;
            for(Entity temp : participants) {
                if (temp != self) {
                    enemy = temp;
                    break;
                }
            }

            if(enemy == null) {
                System.err.println(NULL_ENEMY_ERROR);
                return this;
            }

            switch (command) {
                case "Attack":
                    int damage = (int) (self.getBaseAttack() * self
                            .getTempModifier() * enemy.getTempModifier());
                    if(damage == 0) {
                        damage = 1;
                    }

                    /* Someone won the battle */
                    if (!enemy.loseHP(damage)) {

                        /* Player lost the battle */
                        if(enemy == me) {
                            System.err.println("YOU LOST");
                            System.exit(1);
                        }

                        /* Player won the battle */
                        mainText += String.format(WIN_TEXT, enemy.getName());
                        self.changeTempModifier(1);
                        me.loot(enemy);
                        return new Next("winBattle", mainText, self
                                .getLocation().getEvent());
                    }

                    /* normal attack scene */
                    mainText += String.format(ATTACK_TEXT, self.getName(),
                            enemy.getName(), damage);
                    break;

                case "Run":
                    /* right now, only player can run away */
                    if(self != me) {
                        continue;
                    }

                    int random = (int) (Math.random() * 4 + 1);
                    switch (random) {
                        case 1:
                            me.goUp();
                            break;
                        case 2:
                            me.goDown();
                            break;
                        case 3:
                            me.goLeft();
                            break;
                        case 4:
                            me.goRight();
                            break;
                    }
                    me.changeTempModifier(1);
                    return me.getLocation().getEvent();
            }
        }

        /* reset the modifiers after each round */
        for(Entity participant : participants) {
            participant.changeTempModifier(1);
        }

        return (new Next("battleScene", mainText, this, other));
    }
}
