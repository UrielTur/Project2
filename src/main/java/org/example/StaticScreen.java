package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;


public class StaticScreen extends JFrame {
    private static final int WINDOW_WIDTH = 600; //רוחב
    private static final int WINDOW_HEIGHT = 600; //גובה


    private final JLabel botStatics;
    private final JLabel amountOfRequestsToBot;
    private final JLabel amountOfRequestsToBotNumber;
    private final JLabel uniqueUser;
    private final JLabel uniqueUserToBotNumber;
    private final JLabel popularUser;
    private static JLabel popularUserToString;
    private final JLabel popularActivity;
    private final TheBot theBot;

    private static List<JCheckBox> selectedCheckboxes;

    //    private static List<String> selectedCheckboxesToString;
    private final int maxSelectedCheckboxes = 3;


//    private JButton buttonOfStatics;
//    private JButton buttonOfGraph;


    public StaticScreen() {
        JPanel mainPanel = new JPanel();
        try {
            this.setResizable(false);
            this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setTitle("TheBotProject-UEM-Statics");
            this.setLocationRelativeTo(null);

            theBot = new TheBot();
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(theBot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        mainPanel.setBackground(new Color(34 , 158 , 250));

        this.botStatics = new JLabel(" TelegramBot Statics: ");
        this.botStatics.setFont(new Font("Arial", Font.BOLD, 27));
        this.botStatics.setBounds(0, 10, 370, 30);
        this.add(botStatics);


        this.amountOfRequestsToBot = new JLabel(" The sum of requests to bot: ");
        this.amountOfRequestsToBot.setFont(new Font("Arial", Font.BOLD, 17));
        this.amountOfRequestsToBot.setBounds(0, 60, 350, 20);
        this.add(amountOfRequestsToBot);

        this.amountOfRequestsToBotNumber = new JLabel(" ");
        this.amountOfRequestsToBotNumber.setFont(new Font("Arial", Font.BOLD, 17));
        this.amountOfRequestsToBotNumber.setBounds(240, 60,200 , 25);
        this.add(amountOfRequestsToBotNumber);


        this.uniqueUser = new JLabel(" The unique users: ");
        this.uniqueUser.setFont(new Font("Arial", Font.BOLD, 17));
        this.uniqueUser.setBounds(0, 90, 250, 25);
        this.add(uniqueUser);


        this.uniqueUserToBotNumber = new JLabel(" ");
        this.uniqueUserToBotNumber.setFont(new Font("Arial", Font.BOLD, 17));
        this.uniqueUserToBotNumber.setBounds(160, 90, 400, 25);
        this.add(uniqueUserToBotNumber);


        this.popularUser = new JLabel(" The popular user: ");
        this.popularUser.setFont(new Font("Arial", Font.BOLD, 17));
        this.popularUser.setBounds(0, 120, 250, 25);
        this.add(popularUser);


        this.popularUserToString = new JLabel(" ");
        this.popularUserToString.setFont(new Font("Arial", Font.BOLD, 17));
        this.popularUserToString.setBounds(155, 120, 250, 25);
        this.add(popularUserToString);


        this.popularActivity = new JLabel(" The popular activity: ");
        this.popularActivity.setFont(new Font("Arial", Font.BOLD, 17));
        this.popularActivity.setBounds(0, 150, 250, 25);
        this.add(popularActivity);




        JLabel optionsLabel = new JLabel("Choose a 3 options for the TelegramBot");
        optionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        optionsLabel.setBounds(10, 260, 350, 30);
        this.add(optionsLabel);


        JLabel errorMessage = new JLabel("You can choose only 3 options!");
        errorMessage.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessage.setBounds(10, 280, 300, 25);
        this.add(errorMessage);


        JCheckBox[] jCheckBoxes = new JCheckBox[5];
        jCheckBoxes[0] = new JCheckBox("Tell a joke");
        jCheckBoxes[0].setBounds(10, 320, 100, 25);

        jCheckBoxes[1] = new JCheckBox("Fact about cats");
        jCheckBoxes[1].setBounds(10, 360, 120, 25);

        jCheckBoxes[2] = new JCheckBox("Tell a quote");
        jCheckBoxes[2].setBounds(10, 400, 100, 25);

        jCheckBoxes[3] = new JCheckBox("Country information");
        jCheckBoxes[3].setBounds(10, 440, 150, 25);

        jCheckBoxes[4] = new JCheckBox("Number fact");
        jCheckBoxes[4].setBounds(10, 480, 100, 25);

        this.add(jCheckBoxes[0]);
        this.add(jCheckBoxes[1]);
        this.add(jCheckBoxes[2]); // בשביל האופציות האפשריות של הבוט
        this.add(jCheckBoxes[3]);
        this.add(jCheckBoxes[4]);


        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(10, 510 , 150, 25);
        refreshButton.addActionListener(e -> {
            System.out.println(TheBot.getSelectedCheckboxesToString());


            revalidate();
            repaint();
        });
        this.add(refreshButton);








        new Thread(() -> {
            selectedCheckboxes = new ArrayList<>();

            for (int i = 0; i < jCheckBoxes.length; i++) {
                JCheckBox checkBox = jCheckBoxes[i];
                checkBox.addItemListener(e -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (selectedCheckboxes.size() >= this.maxSelectedCheckboxes) {
                            checkBox.setSelected(false); // שינוי מצב התיבה ללא מסומנת
                        } else {
                            selectedCheckboxes.add(checkBox);
                            TheBot.getSelectedCheckboxesToString().add(checkBox.getText());
                        }
                    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        selectedCheckboxes.remove(checkBox);
                        TheBot.getSelectedCheckboxesToString().remove(checkBox.getText());
                    }

                });
            }
        }).start();





//        this.buttonOfStatics = new JButton("Bot Activities");
//         this.buttonOfStatics.setBounds(20 , 500 , 120 , 35);
//         this.add(buttonOfStatics);
//
//         this.buttonOfGraph = new JButton("Graph Static");
//         this.buttonOfGraph.setBounds(170 , 500 , 130 , 35);
//         this.add(buttonOfGraph);




        this.loop();
        this.add(mainPanel);
    }




    public void loop(){

        new Thread(()->{
            while (true){
                amountOfRequestsToBotNumber.setText(theBot.getStartChatCounter()+" ");
            }
        }).start();

        new Thread(()->{
            while (true){

                uniqueUserToBotNumber.setText(theBot.getTheSize()+" ");
            }
        }).start();

        new Thread(()->{
            while (true){
                if (theBot.getMostActiveUser().equals(" ") && theBot.getMaxOfMessages() == 0) {
                    popularUserToString.setText("No one");
                }else {
                    popularUserToString.setText("'" + theBot.getMostActiveUser()+ "'" + " with " + theBot.getMaxOfMessages() + " messages");
                }
            }
        }).start();



    }




    public void showWindow () {
        this.setVisible(true);
    }



}
