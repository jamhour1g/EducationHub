package com.jamhour.educationhub.controllers;

import atlantafx.base.controls.Tile;
import com.jamhour.educationhub.App;
import com.jamhour.educationhub.Themes;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class SettingsDialog {

    public static void showSettingsDialog(Window owner) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Settings");
        dialog.initOwner(owner);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        Tile themeSctionTile = new Tile("Theme", "Select a theme from the app");
        ComboBox<Themes> themeComboBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        Themes.values()
                )
        );

        themeSctionTile.setAction(themeComboBox);
        themeComboBox.setOnAction(_ -> {
            if (themeComboBox.getSelectionModel().getSelectedItem() != null) {
                App.setTheme(themeComboBox.getSelectionModel().getSelectedItem().getTheme());
            }
        });


        dialog.getDialogPane().setContent(
                new VBox(
                        new Separator(Orientation.HORIZONTAL),
                        themeSctionTile,
                        new Separator(Orientation.HORIZONTAL)
                )
        );
        dialog.showAndWait().ifPresent(_ -> dialog.close());
    }
}
