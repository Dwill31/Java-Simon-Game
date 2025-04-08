package com.example.simongame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

class SimonGame extends Application {
    private int[] colorSequence = new int[8];
    private int currentIndex = 0;

    private Button[] colorButtons = new Button[4];
    private Button newGameButton;
    private Button rememberGameButton;
    private Button endGameButton;
    private Label instructionsLabel;

    private MediaPlayer[] audioPlayers = new MediaPlayer[4];

    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
        setupEventHandlers();

        primaryStage.setTitle("Simon Game");
        primaryStage.show();
    }

    private void initializeUI(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Initialize color buttons and set their styles
        for (int i = 0; i < 4; i++) {
            colorButtons[i] = new Button();
            colorButtons[i].setMinSize(80, 80);

            // Set the background color based on the color code
            String colorStyle = switch (i) {
                case 0 -> "-fx-background-color: red;";
                case 1 -> "-fx-background-color: blue;";
                case 2 -> "-fx-background-color: green;";
                case 3 -> "-fx-background-color: yellow;";
                default -> "-fx-background-color: white;";
            };
            colorButtons[i].setStyle(colorStyle);

            // Create a MediaPlayer for each color (replace with your audio file paths)
            String audioPath = switch (i) {
                case 0 -> "/Sounds/Red.wav";
                case 1 -> "/Sounds/Blue.wav";
                case 2 -> "/Sounds/Green.wav";
                case 3 -> "/Sounds/Yellow.wav";
                default -> "";
            };
            audioPlayers[i] = new MediaPlayer(new Media(getClass().getResource(audioPath).toExternalForm()));
        }

        newGameButton = new Button("New Game");
        rememberGameButton = new Button("Remember Game");
        endGameButton = new Button("End Game");
        instructionsLabel = new Label("Click 8 color buttons, one at a time");

        // Disable color buttons and rememberGameButton initially
        disableColorButtons(true);
        rememberGameButton.setDisable(true);

        // Add color buttons to the grid
        for (int i = 0; i < 4; i++) {
            gridPane.add(colorButtons[i], i, 0);
        }

        // Create an HBox for control buttons
        HBox controlBox = new HBox(10);
        controlBox.getChildren().addAll(newGameButton, rememberGameButton, endGameButton);

        // Add controls and instructions label to the grid
        gridPane.add(controlBox, 0, 1, 4, 1);
        gridPane.add(instructionsLabel, 0, 2, 4, 1);

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
    }

    private void setupEventHandlers() {
        // Event handlers for color buttons
        for (int i = 0; i < 4; i++) {
            final int colorIndex = i;
            colorButtons[i].setOnAction(event -> {
                if (currentIndex < 8) {
                    // Record the selected color
                    colorSequence[currentIndex] = colorIndex;

                    // Play audio for the selected color and stop the previous audio
                    audioPlayers[colorIndex].stop();
                    audioPlayers[colorIndex].play();

                    // Update instructions
                    currentIndex++;
                    instructionsLabel.setText("Selected: " + currentIndex + " / 8 colors");
                }

                // If all 8 colors are selected, enable rememberGameButton
                if (currentIndex == 8) {
                    rememberGameButton.setDisable(false);
                }
            });
        }

        // Event handler for New Game button
        newGameButton.setOnAction(event -> {
            // Reset game state
            currentIndex = 0;
            instructionsLabel.setText("Click 8 color buttons, one at a time");
            rememberGameButton.setDisable(true);

            // Enable color buttons
            disableColorButtons(false);
        });

        // Event handler for Remember Game button
        rememberGameButton.setOnAction(event -> {
            // Display the recorded colors in the label
            StringBuilder recordedColors = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                recordedColors.append(getColorName(colorSequence[i]));
                if (i < 7) {
                    recordedColors.append(" -- ");
                }
            }
            instructionsLabel.setText(recordedColors.toString());
        });

        // Event handler for End Game button (closes the application)
        endGameButton.setOnAction(event -> {
            System.exit(0);
        });
    }

    private void disableColorButtons(boolean disable) {
        for (Button button : colorButtons) {
            button.setDisable(disable);
        }
    }

    private String getColorName(int colorIndex) {
        switch (colorIndex) {
            case 0:
                return "Red";
            case 1:
                return "Blue";
            case 2:
                return "Green";
            case 3:
                return "Yellow";
            default:
                return "Unknown";
        }
    }
}
