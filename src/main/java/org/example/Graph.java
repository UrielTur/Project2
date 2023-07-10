package org.example;


import java.util.List;


public class Graph {

    private List<String> mostFrequentString;
    private List<String> options;
    private int usersSize;
    private int[] useOfOptions;
    // private ImageIcon chart.getUrl()

    public Graph(){
        this.mostFrequentString = TheBot.mostFrequentString;
        this.options = TheBot.getSelectedCheckboxesToString();
        this.usersSize = TheBot.phases.size();
        this.useOfOptions = new int[3];
        updateData();
    }


    public void getGraph() {
        QuickChart chart = new QuickChart();
        chart.setWidth(500);
        chart.setHeight(300);
        if (options.size() > 2 && useOfOptions != null)
            chart.setConfig("{" +
                    "  type: 'bar'," +
                    "  data: {" +
                    "    labels: ['users', '"+this.options.get(0)+"', '"+this.options.get(1)+"', '"+this.options.get(2)+"']," +
                    "    datasets: [{" +
                    "      label: 'amount'," +
                    "      data: [" + usersSize + "," +this.useOfOptions[0] + ","+ useOfOptions[1] + ","+useOfOptions[2]+ "]" +
                    "    }]" +
                    "  }" +
                    "}");


        System.out.println(chart.getUrl());


    }

    public void updateData(){
        new Thread(() ->{
            int listSize = this.mostFrequentString.size();
            while (true){
                getGraph();
                this.mostFrequentString = TheBot.mostFrequentString;
                this.usersSize = TheBot.phases.size();
                this.options = TheBot.getSelectedCheckboxesToString();
                System.out.println(options);
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
        System.out.println("c");
        if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(0))){
            this.useOfOptions[0]++;
            System.out.println("0");
        } else if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(1))) {
            this.useOfOptions[1]++;
            System.out.println("1");
        } else if (this.mostFrequentString.get(this.mostFrequentString.size() - 1).equalsIgnoreCase(this.options.get(2))) {
            this.useOfOptions[2]++;
            System.out.println("2");
        }
    }
}