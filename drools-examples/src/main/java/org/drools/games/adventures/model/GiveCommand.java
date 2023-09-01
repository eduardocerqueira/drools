package org.drools.games.adventures.model;

import org.kie.api.definition.type.Position;

public class GiveCommand extends Command {
    @Position(1)
    private Character giver;

    @Position(2)
    private Thing     thing;

    @Position(3)
    private Character receiver;

    public GiveCommand(Character giver, Thing thing, Character receiver) {
        this.giver = giver;
        this.thing = thing;
        this.receiver = receiver;
    }

    public Character getGiver() {
        return giver;
    }

    public void setGiver(Character giver) {
        this.giver = giver;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public Character getReceiver() {
        return receiver;
    }

    public void setReceiver(Character receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        GiveCommand that = (GiveCommand) o;

        if (!giver.equals(that.giver)) { return false; }
        if (!receiver.equals(that.receiver)) { return false; }
        if (!thing.equals(that.thing)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        int result = giver.hashCode();
        result = 31 * result + thing.hashCode();
        result = 31 * result + receiver.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GiveCommand{" +
               "receiver=" + receiver +
               ", thing=" + thing +
               ", giver=" + giver +
               '}';
    }
}
