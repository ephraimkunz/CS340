package com.example.cs340.statepattern;

public class GumballMachine {
    private State state;
    private int money; // Num quarters
    private int gumballs;
    private boolean quarterInSlot;

    public GumballMachine() {
        money = 0;
        gumballs = 0;
        quarterInSlot = false;
        state = new NoGumballsNoQuarter();
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getGumballs() {
        return gumballs;
    }

    public void setGumballs(int gumballs) {
        this.gumballs = gumballs;
    }

    public boolean isQuarterInSlot() {
        return quarterInSlot;
    }

    public void setQuarterInSlot(boolean quarterInSlot) {
        this.quarterInSlot = quarterInSlot;
    }

    public void addGumballs(int count) {
        state.addGumballs(this, count);
    }

    public void insertQuarter() {
        state.insertQuarter(this);
    }

    public void removeQuarter() {
        state.removeQuarter(this);
    }

    public void turnHandle() {
        state.turnHandle(this);
    }

    public void setState(State next) {
        if (next != null) {
            state = next;
        }
    }
}

class State {
    public void addGumballs(GumballMachine machine, int count) {}
    public void insertQuarter(GumballMachine machine) {}
    public void removeQuarter(GumballMachine machine) {}
    public void turnHandle(GumballMachine machine) {}
}

class NoGumballsNoQuarter extends State {
    @Override
    public void addGumballs(GumballMachine machine, int count) {
        if (count > 0) {
            machine.setGumballs(machine.getGumballs() + count);
        }

        if (machine.getGumballs() > 0) {
            machine.setState(new GumballsNoQuarter());
        }
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        machine.setQuarterInSlot(true);
        machine.setState(new NoGumballsQuarter());
    }
}

class NoGumballsQuarter extends State {
    @Override
    public void addGumballs(GumballMachine machine, int count) {
        if (count > 0) {
            machine.setGumballs(machine.getGumballs() + count);
        }

        if (machine.getGumballs() > 0) {
            machine.setState(new GumballsQuarter());
        }
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        machine.setQuarterInSlot(false);
        machine.setState(new NoGumballsNoQuarter());
    }

    @Override
    public void turnHandle(GumballMachine machine) { // We just got paid for a gumball, but have none to dispense
        machine.setMoney(machine.getMoney() + 1);
        machine.setState(new NoGumballsNoQuarter());
    }
}

class GumballsQuarter extends State {
    @Override
    public void addGumballs(GumballMachine machine, int count) {
        if (count > 0) {
            machine.setGumballs(machine.getGumballs() + count);
        }
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        machine.setQuarterInSlot(false);
        machine.setState(new GumballsNoQuarter());
    }

    @Override
    public void turnHandle(GumballMachine machine) { // Purchased a gumball
        machine.setMoney(machine.getMoney() + 1);
        machine.setGumballs(machine.getGumballs() - 1);
        machine.setQuarterInSlot(false);
        if (machine.getGumballs() > 0) {
            machine.setState(new GumballsNoQuarter());
        } else {
            machine.setState(new NoGumballsNoQuarter());
        }
    }
}

class GumballsNoQuarter extends State {
    @Override
    public void addGumballs(GumballMachine machine, int count) {
        if (count > 0) {
            machine.setGumballs(machine.getGumballs() + count);
        }
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        machine.setQuarterInSlot(true);
        machine.setState(new GumballsQuarter());
    }
}
