package org.example;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jdk.jshell.execution.Util;
import org.glassfish.jersey.message.internal.Utils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.swing.text.Utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TheBot extends TelegramLongPollingBot {
    private int startChatCounter = 0;
    private final Map<Long , Integer> phases = new HashMap<>(); // נגדיר מפה כדי לתת לכל משתמש מספר ייחודי משלו
    private final Map<Long , Integer> phasesForLevels = new HashMap<>(); // נגדיר מפה לשלבים

    private final Map<String , Integer> countMessage = new HashMap<>();
    private int theSize = 0;
    private String mostActiveUser = " ";
    private int maxOfMessages = 0;
    private final static List<String> selectedCheckboxesToString = new ArrayList<>();

    public static List<String> getSelectedCheckboxesToString() {
        return selectedCheckboxesToString;
    }



    @Override
    public String getBotUsername() {
        return "UEM123Bot";
    }

    @Override
    public String getBotToken() {
        return "6252492308:AAFvhULvbXdjynwKqNVhYrP7gLDpSzq46M4";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }





    @Override
    public void onUpdateReceived(Update update) {

        /////////  ///סעיף 1//////////////////////////////////

        long chatId;

        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            chatId = update.getMessage().getChatId();
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        Integer specialPhase = this.phasesForLevels.get(chatId);


        if (specialPhase == null) {

            sendMessage.setText("Choose an option: "); //ההודעה למשתמש שיבחר אופציה


            InlineKeyboardButton option1 = new InlineKeyboardButton();
            option1.setText(getSelectedCheckboxesToString().get(0));
            option1.setCallbackData(getSelectedCheckboxesToString().get(0));

            InlineKeyboardButton option2 = new InlineKeyboardButton();
            option2.setText(getSelectedCheckboxesToString().get(1));
            option2.setCallbackData(getSelectedCheckboxesToString().get(1));

            InlineKeyboardButton option3 = new InlineKeyboardButton();
            option3.setText(getSelectedCheckboxesToString().get(2));
            option3.setCallbackData(getSelectedCheckboxesToString().get(2));


            List<InlineKeyboardButton> topRow = List.of(option1, option2, option3); //פה אני מגדיר שהכפתורי האופציות יוצגו רשמית
            List<List<InlineKeyboardButton>> keyBord = List.of(topRow); // צורה לקבל את המתודה של המקלדת

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //רשת שיש בה כפתורים אבל לא מייצג את האובייקט
            inlineKeyboardMarkup.setKeyboard(keyBord); // ולכן זאת השורה שמצהירה על המקלדת שהיא מוכנה

            sendMessage.setReplyMarkup(inlineKeyboardMarkup); // להעביר את המקלדת


            this.phasesForLevels.put(chatId, 1);

        } else if (specialPhase == 1) {

            if (update.getCallbackQuery().getData().equals("Tell a joke")) {
                sendAJoke(chatId);
                this.phasesForLevels.put(chatId, 4);

            } else if (update.getCallbackQuery().getData().equals("Tell a quote")) {
                sendQuote(chatId);
                this.phasesForLevels.put(chatId, 4);


            } else if (update.getCallbackQuery().getData().equals("Fact about cats")) {
                sendFact(chatId);
                this.phasesForLevels.put(chatId, 4);


            } else if (update.getCallbackQuery().getData().equals("Country information")) {
                sendMessage.setText("Write the code of the country that you want to know about her. For example: ISR - Israel.");
                this.phasesForLevels.put(chatId, 2);

            } else if (update.getCallbackQuery().getData().equals("Number fact")) {
                sendMessage.setText("Write the number that you wants to know about him!");
                this.phasesForLevels.put(chatId, 3);
            }

        } else if (specialPhase == 2) {

            String text = update.getMessage().getText();

            try {
                com.mashape.unirest.http.HttpResponse<String> response1 = Unirest.get("https://restcountries.com/v2/alpha/" + text).asString();
                ObjectMapper objectMapper1 = new ObjectMapper();
                Countries country = objectMapper1.readValue(response1.getBody(), Countries.class);
                if (country.getName() == null) {
                    sendMessage.setText("Error, Country have not found! Please try again!");
                } else {
                    String countryInfo = "The country you choose is: " + country.getName() + ". The population: " + country.getPopulation() + ". The capital is: " + country.getCapital() + ". The region is: " + country.getRegion() + " .";
                    sendMessage.setText(countryInfo);

                    try {
                        Thread.sleep(2000);
                        continueMessage(chatId);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    this.phasesForLevels.put(chatId, 4);

                }
            } catch (JacksonException | UnirestException exception) {
                throw new RuntimeException(exception);
            }

        } else if (specialPhase == 3) {

            String text = update.getMessage().getText();

            try {
                com.mashape.unirest.http.HttpResponse<String> response3 = Unirest.get("http://numbersapi.com/" + text + "?json").asString();
                ObjectMapper objectMapper3 = new ObjectMapper();
                Numbers number = objectMapper3.readValue(response3.getBody(), Numbers.class);
                if (number.getNumber() >= Double.NEGATIVE_INFINITY && number.getNumber() <= Double.POSITIVE_INFINITY) {
                    String numberInfo = number.getText();
                    sendMessage.setText(numberInfo);
                    try {
                        Thread.sleep(2000);
                        continueMessage(chatId);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    this.phasesForLevels.put(chatId, 4);
                } else {
                    sendMessage.setText("Error, choose only number!");

                }
            } catch (JacksonException | UnirestException exception) {
                throw new RuntimeException(exception);
            }


        }else if (specialPhase == 4) {
            String callbackData = update.getCallbackQuery().getData().toLowerCase();
            if (callbackData.equals("yes")) {
                this.phasesForLevels.put(chatId, null);
            } else if (callbackData.equals("no")) {
                sendMessage.setText("Alright then. See you soon!. \nType /start to restart the bot.");
                this.phasesForLevels.put(chatId, 5);
            }

        }else if (specialPhase == 5){
            String callbackData = update.getMessage().getText();
            if(callbackData.equals("/start"))
                this.phasesForLevels.put(chatId, null);
        }


        send(sendMessage);




//סעיף 2 //////////////////////////////////////////////  /



        Integer phase = phases.get(chatId);  //נצהיר על משתנה מסוג Integer גם בשביל המפה

        // התנאי הבא בודק שכאשר מתחילים שיחה עם הבוט הוא סופר את כמות הבקשות וגם את כמות המשתמשים היחודיים השונים
        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String messageText = update.getMessage().getText();
            if (messageText.equals("/start")) {
                startChatCounter++;
                this.phases.put(chatId, phase); //המפה תחלק כל משתמש ייחודי לפי המזהה שלו ובעצם כל משתמש יופיע כייחודי
                theSize = this.phases.size();
            }
        }

        String userId = update.getMessage().getFrom().getFirstName(); // מציאת המשתמש הפעיל ביותר
        if (countMessage.containsKey(userId)) {
            int messageCount = countMessage.get(userId);
            countMessage.put(userId, messageCount + 1);//
        } else {
            countMessage.put(userId, 1);
        }

        for (Map.Entry<String, Integer> entry : countMessage.entrySet()) {//פה אנחנו עושים לולאה שמחשבת את הפעמים שהמשתמש שולח הודעה
            userId = entry.getKey();
            int messageCount = entry.getValue();
            if (messageCount > maxOfMessages) {
                maxOfMessages = messageCount;
                mostActiveUser = userId;
            }
        }



    }





    private void send (SendMessage sendMessage){
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }


    public void sendAJoke(long chatId){
        try {
            com.mashape.unirest.http.HttpResponse<String> response2 = Unirest.get("https://official-joke-api.appspot.com/random_joke").asString();
            ObjectMapper objectMapper2 = new ObjectMapper();
            Jokes jokes = objectMapper2.readValue(response2.getBody() , Jokes.class);
            String joke = jokes.getSetup() + " " + jokes.getPunchline();
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(chatId);
            sendMessage.setText(joke);
            send(sendMessage);

            try {
                Thread.sleep(2000);
                continueMessage(chatId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }catch (JacksonException | UnirestException exception){
            throw new RuntimeException(exception);
        }
    }


    public void sendQuote(long chatId){
        try {
            com.mashape.unirest.http.HttpResponse<String> response4 =Unirest.get("https://api.quotable.io/random").asString();
            ObjectMapper objectMapper4 = new ObjectMapper();
            Quotes quotes = objectMapper4.readValue(response4.getBody() , Quotes.class);
            String quote = quotes.getAuthor() + " says: " + quotes.getContent();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(quote);
            send(sendMessage);

            try {
                Thread.sleep(2000);
                continueMessage(chatId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (JsonProcessingException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFact(long chatId){
        try {
            com.mashape.unirest.http.HttpResponse<String> response5 =Unirest.get("https://catfact.ninja/fact").asString();
            ObjectMapper objectMapper5 = new ObjectMapper();
            Cats cats = objectMapper5.readValue(response5.getBody() , Cats.class);
            String catsFact = cats.getFact();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(catsFact);
            send(sendMessage);

            try {
                Thread.sleep(2000);
                continueMessage(chatId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (JsonProcessingException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public void continueMessage(long chatId){
        new Thread(() -> {

                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setChatId(chatId);
                sendMessage1.setText("Would you like to continue?");

                InlineKeyboardButton yes = new InlineKeyboardButton();
                yes.setText("yes");
                yes.setCallbackData("yes");
                InlineKeyboardButton no = new InlineKeyboardButton();
                no.setText("no");
                no.setCallbackData("no");

                List<InlineKeyboardButton> topRow = List.of(yes, no);

                List<List<InlineKeyboardButton>> keyboard = List.of(topRow);


                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(keyboard);

                sendMessage1.setReplyMarkup(inlineKeyboardMarkup);

                send(sendMessage1);




        }).start();


    }





    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    public int getStartChatCounter() {
        return startChatCounter;
    }

    public int getTheSize() {
        return theSize;
    }

    public String getMostActiveUser() {
        return mostActiveUser;
    }

    public int getMaxOfMessages() {
        return maxOfMessages;
    }



}


