package com.example.addishiwot;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class LabTest {
    private final SimpleStringProperty testName;
    private final SimpleBooleanProperty selected;

    public LabTest(String testName) {
        this.testName = new SimpleStringProperty(testName);
        this.selected = new SimpleBooleanProperty(false);
    }

    public String getTestName() {
        return testName.get();
    }

    public SimpleStringProperty testNameProperty() {
        return testName;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
