package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;


public  class StaticScreen extends JFrame {


    private static final int WINDOW_WIDTH = 1000; //רוחב
    private static final int WINDOW_HEIGHT = 700; //גובה
    Graph graph;


    private final JLabel botStatics;
    private final JLabel amountOfRequestsToBot;
    private final JLabel amountOfRequestsToBotNumber;
    private final JLabel uniqueUser;
    private final JLabel uniqueUserToBotNumber;
    private final JLabel popularUser;
    private final JLabel popularUserToString;
    private final JLabel popularActivity;
    private final JLabel popularActivityNumber;
    private final JLabel graphStatics;

    private JLabel historyOfTenLast;
    private JLabel historyOfTenLastList;
    // JLabel  historyOfTenLastList = new JLabel("<html>Last ten messages: </html>");// html helps with text
    private final TheBot theBot;


    private static List<JCheckBox> selectedCheckboxes;

    private final int maxSelectedCheckboxes = 3;

//
//    public void html1(StaticScreen historyOfTenLastList) {
//        String str = "<html>";
//        for (int i = 0; i < TheBot.mostFrequentString.size(); i++) {
//            str += TheBot.mostFrequentString.get(i) + "<br>";
//        }
//        str += "/html";



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
        this.graph = new Graph();
        this.add(graph);

        mainPanel.setBackground(new Color(34, 158, 250));

        this.botStatics = new JLabel(" TelegramBot Statics: ");
        this.botStatics.setFont(new Font("Arial", Font.BOLD, 27));
        this.botStatics.setBounds(0, 10, 370, 30);
        this.add(botStatics);

        this.graphStatics = new JLabel("Graph Statics: ");
        this.graphStatics.setFont(new Font("Arial", Font.BOLD, 27));
        this.graphStatics.setBounds(700, 10, 370, 30);
        this.add(graphStatics);


        this.amountOfRequestsToBot = new JLabel(" The sum of requests to bot: ");
        this.amountOfRequestsToBot.setFont(new Font("Arial", Font.BOLD, 17));
        this.amountOfRequestsToBot.setBounds(0, 60, 350, 20);
        this.add(amountOfRequestsToBot);

        this.amountOfRequestsToBotNumber = new JLabel(" ");
        this.amountOfRequestsToBotNumber.setFont(new Font("Arial", Font.BOLD, 17));
        this.amountOfRequestsToBotNumber.setBounds(240, 60, 200, 25);
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


        this.popularActivityNumber = new JLabel(this.theBot.getPopularActivity());
        this.popularActivityNumber.setFont(new Font("Arial", Font.BOLD, 17));
        this.popularActivityNumber.setBounds(180, 150, 200, 25);
        this.add(popularActivityNumber);


        this.historyOfTenLast = new JLabel(" The history of 10 activities: ");
        this.historyOfTenLast.setFont(new Font("Arial", Font.BOLD, 17));
        this.historyOfTenLast.setBounds(0, 550, 250, 25);
        this.add(historyOfTenLast);


        this.historyOfTenLastList = new JLabel(" ");
        this.historyOfTenLastList.setFont(new Font("Arial", Font.BOLD, 8));
        this.historyOfTenLastList.setBounds(0, 570, 5000, 20);


        this.add(historyOfTenLastList);


        String str = "<html>";
        for (int i = 0; i < TheBot.mostFrequentString.size(); i++) {
            str += TheBot.mostFrequentString.get(i) + "<br>";
        }
        str += "/html";


        //  private static final int WINDOW_WIDTH = 1000; //רוחב
        //    private static final int WINDOW_HEIGHT = 700; //גובה


        JLabel optionsLabel = new JLabel("Choose 3 options for the TelegramBot");
        optionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        optionsLabel.setBounds(10, 260, 350, 30);
        this.add(optionsLabel);


        JLabel errorMessage = new JLabel("You can't choose much more than 3 options!");
        errorMessage.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessage.setBounds(10, 285, 400, 25);
        this.add(errorMessage);


        JCheckBox[] jCheckBoxes = new JCheckBox[5];
        jCheckBoxes[0] = new JCheckBox("Tell a joke");
        jCheckBoxes[0].setSelected(true);

        jCheckBoxes[0].setBounds(10, 320, 100, 25);

        jCheckBoxes[1] = new JCheckBox("Fact about cats");
        jCheckBoxes[1].setSelected(true);
        jCheckBoxes[1].setBounds(10, 360, 120, 25);

        jCheckBoxes[2] = new JCheckBox("Tell a quote");
        jCheckBoxes[2].setSelected(true);
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


        JButton refreshButton = new JButton("Refresh Options");
        refreshButton.setBounds(20, 520, 150, 30);
        refreshButton.addActionListener(e -> {
            System.out.println(TheBot.getSelectedCheckboxesToString());


            revalidate();
            repaint();
        });
        this.add(refreshButton);


        new Thread(() -> {
            selectedCheckboxes = new ArrayList<>();

            TheBot.getSelectedCheckboxesToString().add(jCheckBoxes[0].getText());
            TheBot.getSelectedCheckboxesToString().add(jCheckBoxes[1].getText());
            TheBot.getSelectedCheckboxesToString().add(jCheckBoxes[2].getText());

            selectedCheckboxes.add(jCheckBoxes[0]);
            selectedCheckboxes.add(jCheckBoxes[1]);
            selectedCheckboxes.add(jCheckBoxes[2]);


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


        this.loop();
        this.add(graph);
        this.add(mainPanel);
    }


    public void loop() {

        new Thread(() -> {
            while (true) {
                amountOfRequestsToBotNumber.setText(theBot.getStartChatCounter() + " ");
                uniqueUserToBotNumber.setText(theBot.getTheSize() + " ");
                this.popularActivityNumber.setText(this.theBot.getPopularActivity());

                if (theBot.getMostActiveUser().equals(" ") && theBot.getMaxOfMessages() == 0) {
                    popularUserToString.setText("No one");
                } else {
                    popularUserToString.setText("'" + theBot.getMostActiveUser() + "'" + " with " + theBot.getMaxOfMessages() + " messages");
                }

                if (theBot.getMostFrequentString().size() <= 10) {
                    historyOfTenLastList.setText(theBot.getMostFrequentString() + " ");
                    //text
                } else {
                    for (int i = theBot.getMostFrequentString().size(); i <= theBot.getMostFrequentString().size() - 10; i--) {//תראה פה ניסתי ממה שיחיאל אמר לי
                        historyOfTenLastList.setText(historyOfTenLastList.getText()+
                               "<html>"
                                +"historyOfTenLastList.get(i)"
                                +"<br>"
                                +"historyOfTenLastList.get(i-1)"
                                +"<html/>");

                    };

                }


//
//                        historyOfTenLastList.setText("<html><body>theBot.getMostFrequentString().get(i)" +
//                                "<br>theBot.getMostFrequentString().get(i)<br>" +
//                                "שורה שלשית</body></html>");


                 //       historyOfTenLastList.setText(theBot.getMostFrequentString().get(i));



            }


        }).start();

    }




//
//    public void newJLabel() {
//        //בדיקה ומעבר לשורה הבאה כאשר התווית מגיעה לקצה הימני של החלון
//        Dimension labelSize = historyOfTenLastList.getSize();
//        int historyOfTenLastList_labelHeight= (int) labelSize.getHeight();
//        int historyOfTenLastList_labelWidth= (int) labelSize.getWidth();
//        int x = getX();
//       int y = getY();
//        for (int i = 0; i < theBot.getMostFrequentString().size(); i++) {
//              int limit_x = Integer.parseInt(theBot.getMostFrequentString().get(i));
//            String currentString = theBot.getMostFrequentString().get(i);
//            int count = theBot.getMostFrequentString().lastIndexOf(currentString) - theBot.getMostFrequentString().indexOf(currentString) + 1;
//            if (limit_x == count) {
//                if (getX() + labelSize.width <= WINDOW_WIDTH) {
//                  x += labelSize.width;
//                } else {
//                    x = 0;
//                    y += historyOfTenLastList_labelHeight;
//                }
//
//                // הזזת התווית לשורה הבאה
//                y += 20;
//                if (y + historyOfTenLastList_labelHeight <= WINDOW_HEIGHT) {
//                    JLabel label = new JLabel(currentString);
//                    label.setLocation(x, y);
//                    historyOfTenLastList.add(label);
//                }
//            }
//        }
//

    // }


    public void showWindow() {
        this.setVisible(true);
    }
}

