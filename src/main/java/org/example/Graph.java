package org.example;


import javax.swing.*;
import java.util.List;


public class Graph extends JPanel {
    private int numberCount = 0;

    public int getNumberCount(){
        return numberCount;
    }
    public void setNumberCount(int numberCount){
        this.numberCount = numberCount;

    }
    private int quoteCount = 0;

    public int getQuoteCount() {
        return quoteCount;
    }

    public void setQuoteCount(int quoteCount) {
        this.quoteCount = quoteCount;
    }

    private int countriesCount = 0;

    public int getCountriesCount() {
        return countriesCount;
    }

    public void setCountriesCount(int countriesCount) {
        this.countriesCount = countriesCount;
    }

    private int jokeCount = 0;

    public int getJokeCount() {
        return jokeCount;
    }

    public void setJokeCount(int jokeCount) {
        this.jokeCount = jokeCount;
    }

    private int catCount = 0;

    public int getCatCount() {
        return catCount;
    }

    public void setCatCount(int catCount) {
        this.catCount = catCount;
    }

    JLabel pic = new JLabel();


    public void setChartConfig() {//יוצר את הגרף בחלון
        new Thread(() -> {
            QuickChart chart = new QuickChart();//63
            chart.setConfig("{"
                    + "    type: 'bar',\n"
                    + "    data: {\n"
                    + "        labels: ['numbers', 'quote', 'countries', 'joke','cat'],\n"
                    + "        datasets: [{\n"
                    + "            label: 'Users',"
                    + "            data: ["+this.usersSize + "," +this.useOfOptions[0] + ","+ useOfOptions[1] + ","+useOfOptions[2]+"]"
                    + "        }]\n"
                    + "    }\n"
                    + "}"
            );
            chart.setWidth(330);
            chart.setHeight(300);
            byte[] image= chart.toByteArray();
            ImageIcon img= new ImageIcon(image);
            this.pic.setIcon(img);
        }).start();
    }

    private List<String> mostFrequentString;
    private List<String> options;
    private int usersSize;
    private int[] useOfOptions;

    public Graph(){//הגרף עצמו
        setChartConfig();
        this.setLayout(null);
        this.mostFrequentString = TheBot.mostFrequentString;
        this.options = TheBot.getSelectedCheckboxesToString();
        this.usersSize = TheBot.phases.size();
        this.useOfOptions = new int[3];
        updateData();

        this.add(pic);
        pic.setBounds(10,20,600,325);
        this.setLayout(null);
        this.setBounds(600,50,370,330);
        this.setVisible(true);
    }



    public void updateData(){
        new Thread(() ->{
            int listSize = this.mostFrequentString.size();
            while (true){
                this.mostFrequentString = TheBot.mostFrequentString;
                this.usersSize = TheBot.phases.size();
                this.options = TheBot.getSelectedCheckboxesToString();
                if (listSize != this.mostFrequentString.size() && this.mostFrequentString.size() != 0){
                    updateOptionsSize();
                    listSize = this.mostFrequentString.size();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    public void updateOptionsSize(){
        if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(0))){
            this.useOfOptions[0]++;
        } else if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(1))) {
            this.useOfOptions[1]++;
        } else if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(2))) {
            this.useOfOptions[2]++;
        }
    }
}