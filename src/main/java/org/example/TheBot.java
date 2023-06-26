package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheBot extends TelegramLongPollingBot {

    private int startChatCounter = 0;
    private final Map<Long , Integer> phases = new HashMap<>(); // נגדיר מפה כדי לתת לכל משתמש מספר ייחודי משלו
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
        long chatId = update.getMessage().getChatId(); //נצהיר על משתנה מסוג long כמו המפה
        Integer phase = phases.get(chatId);  //נצהיר על משתנה מסוג Integer גם בשביל המפה

        // התנאי הבא בודק שכאשר מתחילים שיחה עם הבוט הוא סופר את כמות הבקשות וגם את כמות המשתמשים היחודיים השונים
        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String messageText = update.getMessage().getText();
            if (messageText.equals("/start")) {
                startChatCounter++;
                System.out.println("The bot got: " + "'" + startChatCounter + "'" + " requests start chat.");
                this.phases.put(chatId, phase); //המפה תחלק כל משתמש ייחודי לפי המזהה שלו ובעצם כל משתמש יופיע כייחודי
                theSize = this.phases.size();
                System.out.println("There are " + theSize + " different users who called the bot.");
                System.out.println();
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
        System.out.println("The most active user is: " + mostActiveUser + " with " + maxOfMessages + " messages.");//נדפיס את המשתשמש ששולח הכי הרבה הודעות
        System.out.println();



        try {
            com.mashape.unirest.http.HttpResponse<String> response2 = Unirest.get("https://official-joke-api.appspot.com/random_joke").asString();
            ObjectMapper objectMapper2 = new ObjectMapper();
            Jokes jokes = objectMapper2.readValue(response2.getBody() , Jokes.class);
            System.out.println(jokes.getSetup() + " " + jokes.getPunchline());


            com.mashape.unirest.http.HttpResponse<String> response5 =Unirest.get("https://catfact.ninja/fact").asString();
            ObjectMapper objectMapper5 = new ObjectMapper();
            Cats cats = objectMapper5.readValue(response5.getBody() , Cats.class);
            System.out.println(cats.getFact());


            com.mashape.unirest.http.HttpResponse<String> response4 =Unirest.get("https://api.quotable.io/random").asString();
            ObjectMapper objectMapper4 = new ObjectMapper();
            Quotes quotes = objectMapper4.readValue(response4.getBody() , Quotes.class);
            System.out.println(quotes.getAuthor() + ": '' " + quotes.getContent() + " ''");


            com.mashape.unirest.http.HttpResponse<String> response3 =Unirest.get("http://numbersapi.com/random?json").asString();
            ObjectMapper objectMapper3 = new ObjectMapper();
            Numbers numbers = objectMapper3.readValue(response3.getBody() , Numbers.class);
            System.out.println(numbers.getText());


            com.mashape.unirest.http.HttpResponse<String> response1 =Unirest.get("https://restcountries.com/v2/alpha/ISR").asString();
            ObjectMapper objectMapper1 = new ObjectMapper();
            Countries countries = objectMapper1.readValue(response1.getBody() , Countries.class);
            System.out.println("The population: " + countries.getPopulation() +". The capital is: " + countries.getCapital() + ". The borders: " + countries.getBorders() + " .");


        } catch (JsonProcessingException | UnirestException e) {
            throw new RuntimeException(e);
        }
        



        //////////יצירת כפתורי אפשרויות/////
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

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


        String callBackData = update.getCallbackQuery().getData();
        if (callBackData.contains(null))


        send(sendMessage); //ההודעה נשלחת עם ה-"מקלדת אפשרויות" שיצרנו



    }





    private void send (SendMessage sendMessage){
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }

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


