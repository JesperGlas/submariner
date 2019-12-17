package main;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class UILabel extends Label {

    public UILabel(String message, double parentWidth, double percentWidth, double padding) {
        super(message);
        setPadding(new Insets(padding));
        setWidth(parentWidth * percentWidth);
        setTextFill(Color.GREEN);
        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }
}
