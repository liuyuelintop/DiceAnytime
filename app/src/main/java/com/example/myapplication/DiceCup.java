package com.example.myapplication;

// Package添加:
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//this is the Dice Cup class it can contain less than 6 dice in the cup
public class DiceCup {

    // Attributes
    private ArrayList<Dice> cup;
    private int numberOfDice;

    // constructor


    public DiceCup(){
        this.cup = new ArrayList<Dice>();
        this.numberOfDice = 0;
    }

    // fill the dice cup with specified numbers of dice

    public void setDice(int numberOfDice){
        this.numberOfDice = numberOfDice;
        for(int i =0; i< numberOfDice; i++){
            Dice d = new Dice();
            this.cup.add(d);
        }

    }

    // Add Dice
    public void addDice(Dice d){
        this.cup.add(d);
        this.numberOfDice++;
    }

    // Shake Dice Cup
    public void shakeDiceCup(){
        for (Dice d: cup) {
            d.randomDice();
        }
    }

    // Return the top results of all dice
    public ArrayList<Integer> getOnTops(){
        int addEmptySpace = 12-this.numberOfDice;
        int countEmpty = 0;
        int countVaild = 0;
        ArrayList<Integer> topList = new ArrayList<Integer>();
        for(int i=0; i<12; i++){
            if ((countEmpty < addEmptySpace) && (countVaild < this.numberOfDice)){
                Random rand = new Random();
                float f = rand.nextFloat();
                if (f <= ((float)addEmptySpace/12)){
                    Dice temp = cup.get(i-countEmpty);
                    topList.add(temp.getOnTop());
                    countVaild ++;
                }
                else{
                    topList.add(0);
                    countEmpty ++;
                }
            }
            else if (countEmpty >= addEmptySpace){
                Dice temp = cup.get(i-countEmpty);
                topList.add(temp.getOnTop());
                countVaild ++;
            }
            else {
                topList.add(0);
                countEmpty ++;
            }
        }
        return topList;
    }


//    get the dices results

    public ArrayList<Integer> getResultList() {
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for(int i=0; i<this.numberOfDice; i++){
            Dice temDice = cup.get(i);
            resultList.add(temDice.getOnTop());
        }
        return resultList;
    }

//    get the dices results map
    public Map<String, Integer> getResultMap() {
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        resultMap.put("Empty",0);
        resultMap.put("One",0);
        resultMap.put("Two",0);
        resultMap.put("Three",0);
        resultMap.put("Four",0);
        resultMap.put("Five",0);
        resultMap.put("Six",0);

        ArrayList<Integer> resultList = this.getOnTops();
        //System.out.print(resultList);

        for(int i=0; i<12; i++){
            int top = resultList.get(i);
            switch(top){
                case 0:
                    resultMap.put("Empty", resultMap.containsKey("Empty") ? resultMap.get("Empty") + 1 : 1);
                    break;
                case 1:
                    resultMap.put("One", resultMap.containsKey("One") ? resultMap.get("One") + 1 : 1);
                    break;
                case 2:
                    resultMap.put("Two", resultMap.containsKey("Two") ? resultMap.get("Two") + 1 : 1);
                    break;
                case 3:
                    resultMap.put("Three", resultMap.containsKey("Three") ? resultMap.get("Three") + 1 : 1);
                    break;
                case 4:
                    resultMap.put("Four", resultMap.containsKey("Four") ? resultMap.get("Four") + 1 : 1);
                    break;
                case 5:
                    resultMap.put("Five", resultMap.containsKey("Five") ? resultMap.get("Five") + 1 : 1);
                    break;
                case 6:
                    resultMap.put("Six", resultMap.containsKey("Six") ? resultMap.get("Six") + 1 : 1);
                    break;
            }
        }
        return resultMap;
    }
}

