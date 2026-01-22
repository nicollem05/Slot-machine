package com.example.slot_machine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class SlotMachineFX extends Application {

    // Add components
    private ImageView img1, img2, img3;
    private TextField betField;
    private Label spinWinLabel;      // Label for "Amount Won This Spin"
    private Label totalWonLabel;     // Label for "Total Amount Won"
    private Label messageLabel;      // Label for error messages or o money

    // add variables
    private int balance = 100;
    private int totalWon = 0;
    private final Random random = new Random(); //create a random for the images

    // Add images we are going to use
    private final Image cherry = new Image(getClass().getResourceAsStream("/com/example/slot_machine/Cherry.png"));
    private final Image lime = new Image(getClass().getResourceAsStream("/com/example/slot_machine/Lime.png"));
    private final Image orange = new Image(getClass().getResourceAsStream("/com/example/slot_machine/Orange.png"));

    // Make and array of the symbols and add names
    private final Image[] symbols = {cherry, lime, orange};
    private final String[] symbolNames = {"Cherry", "Lime", "Orange"};

    public void start(Stage stage) {

        // Add and start the images
        img1 = new ImageView(symbols[0]); // show symbols
        img2 = new ImageView(symbols[1]);
        img3 = new ImageView(symbols[2]);
        //add values of the images
        img1.setFitWidth(120); img1.setFitHeight(120);
        img2.setFitWidth(120); img2.setFitHeight(120);
        img3.setFitWidth(120); img3.setFitHeight(120);

        HBox imagesBox = new HBox(20, img1, img2, img3);
        imagesBox.setAlignment(Pos.CENTER);

        // Make th session for the bet
        betField = new TextField();
        betField.setPrefWidth(80);
        betField.setText("10"); // I add a value

        // The label to say the player to insert the amount for the bet
        HBox betInputBox = new HBox(10, new Label("Amount Inserted:"), betField);
        betInputBox.setAlignment(Pos.CENTER);

        // Spin Botton
        Button spinButton = new Button("Spin");
        spinButton.setOnAction(e -> spin());

        // The output of the game

        // Label for the total win money in that spin
        Label spinWinDesc = new Label("Amount Won This Spin:");
        spinWinLabel = new Label("$0.00");
        HBox spinWinBox = new HBox(10, spinWinDesc, spinWinLabel);
        spinWinBox.setAlignment(Pos.CENTER_LEFT);

        // Label for the total you have
        Label totalWonDesc = new Label("Total Amount Won:");
        totalWonLabel = new Label("$0.00");
        HBox totalWonBox = new HBox(10, totalWonDesc, totalWonLabel);
        totalWonBox.setAlignment(Pos.CENTER_LEFT);

        VBox metricsBox = new VBox(5, spinWinBox, totalWonBox);
        metricsBox.setPadding(new Insets(10, 0, 0, 0)); //Spaces to make more clean the window

        // Label for message and balance
        messageLabel = new Label("Balance: $" + balance);
        messageLabel.setStyle("-fx-font-weight: bold;");


        // principal box
        VBox centerBox = new VBox(20, imagesBox, betInputBox, spinButton, metricsBox, messageLabel);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        Scene scene = new Scene(centerBox, 450, 480);

        stage.setTitle("Slot Machine");
        stage.setScene(scene);
        stage.show();
    }

    private void spin() {
        int bet;

        try {
            //bet validation
            bet = Integer.parseInt(betField.getText());
            if (bet <= 0) {
                messageLabel.setText("Balance: $" + balance + " | Bet must be > $0.");
                return;
            }
            if (bet > balance) {
                messageLabel.setText("Balance: $" + balance + " | Not enough balance to bet $" + bet + ".");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Balance: $" + balance + " | Enter a valid number for the bet.");
            return;
        }

        // How much is the deduction
        balance -= bet;

        int index1 = random.nextInt(symbols.length);
        int index2 = random.nextInt(symbols.length);
        int index3 = random.nextInt(symbols.length);

        // Update the images
        img1.setImage(symbols[index1]);
        img2.setImage(symbols[index2]);
        img3.setImage(symbols[index3]);

        // We calculate total of payout
        int payout = calculatePayout(index1, index2, index3, bet);
        balance += payout;
        totalWon += payout;

        // 4. Actualización de la GUI (Métricas y Balance)
        spinWinLabel.setText("$" + payout + ".00");
        totalWonLabel.setText("$" + totalWon + ".00");
        messageLabel.setText("Balance: $" + balance + " | Last win: $" + payout + ".00");

        // Check if the balance is 0 and show Alarm
        if (balance == 0 && payout == 0) {
            messageLabel.setText("You can't play. Your balance is $0.");
        }
    }

    //Method to find the coincidences and how much we win
    private int calculatePayout(int i1, int i2, int i3, int bet) {
        // Verify three coincidences
        if (i1 == i2 && i2 == i3) {
            return bet * 3;
        }
        // Verify two coincidences
        if (i1 == i2 || i2 == i3 || i1 == i3) {
            return bet * 2;
        }
        // if its 0 or it is a match
        return 0;
    }
    // we call the program
    public static void main(String[] args) {
        launch();
    }
}