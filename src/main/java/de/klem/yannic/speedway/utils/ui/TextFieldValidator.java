package de.klem.yannic.speedway.utils.ui;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.function.Predicate;

public class TextFieldValidator {

    private static final String MUST_NOT_BE_EMPTY = "Feld darf nicht leer sein.";
    private final VBox vbox;
    private final Label errorMessage;
    private final TextField textFieldToValidate;
    private final ChangeListener<String> validationTextFieldListener;
    private final ChangeListener<Boolean> validationFocusListener;
    private final Predicate<String> validationPredicate;

    public TextFieldValidator(final TextField textFieldToValidate, final Predicate<String> validationPredicate,
                              final String messageToShow) {
        this.textFieldToValidate = textFieldToValidate;
        this.validationPredicate = validationPredicate;

        this.errorMessage = new Label(messageToShow);
        this.errorMessage.setTextFill(Color.web("red"));

        Parent parent = textFieldToValidate.getParent();
        if (parent instanceof GridPane) {
            int originalIndex = ((GridPane) parent).getChildren().indexOf(textFieldToValidate);
            this.vbox = new VBox(textFieldToValidate);
            GridPane.setColumnIndex(vbox, GridPane.getColumnIndex(textFieldToValidate));
            GridPane.setRowIndex(vbox, GridPane.getRowIndex(textFieldToValidate));
            ((GridPane) parent).getChildren().add(originalIndex, vbox);
        } else if (parent instanceof VBox) {
            this.vbox = (VBox) parent;
        } else {
            throw new IllegalStateException("Text field must either be contained of a GridPane or a VBox");
        }

        this.validationTextFieldListener = (observable, oldValue, newValue) -> onValidate(isValid());
        this.validationFocusListener = (observable, oldValue, newValue) -> {
            if (!newValue) {
                onValidate(isValid());
            }
        };
    }

    public void enable() {
        this.textFieldToValidate.textProperty().addListener(validationTextFieldListener);
        this.textFieldToValidate.focusedProperty().addListener(validationFocusListener);
    }

    public void disable() {
        this.textFieldToValidate.textProperty().removeListener(validationTextFieldListener);
        this.textFieldToValidate.focusedProperty().removeListener(validationFocusListener);
    }

    public boolean isValid() {
        return validationPredicate.test(textFieldToValidate.getText());
    }

    private void onValidate(final boolean isValid) {
        ObservableList<Node> children = vbox.getChildren();
        if (isValid && children.contains(errorMessage)) {
            children.remove(errorMessage);
        } else if (!isValid && !children.contains(errorMessage)) {
            children.add(errorMessage);
        }
    }

    public static TextFieldValidator notEmptyValidator(final TextField textFieldToValidate) {
        return new TextFieldValidator(textFieldToValidate, text -> !text.trim().isEmpty(), MUST_NOT_BE_EMPTY);
    }
}
