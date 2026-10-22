package com.mycompany.type_project;

import java.util.concurrent.ThreadLocalRandom;

public class Bot extends Thread{
    
    public int difficultyNum;
    public static int botStringIndex = 0;
    public String stringToType;
    public double minSleepTime;
    public double maxSleepTime;
    public int maxRange;
    
    public Bot(String stringToType, int difficultyNum) {
        this.stringToType = stringToType;
        this.difficultyNum = difficultyNum;
        
        switch (difficultyNum) {
            case 0: 
                minSleepTime = 1;
                maxSleepTime = 2;
                maxRange = 15;
                break;
                
            case 1:
                minSleepTime = 0.5;
                maxSleepTime = 1.3;
                maxRange = 30;
                break;
                
            case 2:
                minSleepTime = 0.3;
                maxSleepTime = 0.8;
                maxRange = 40;
                break;
                
            case 3: 
                minSleepTime = 0.15;
                maxSleepTime = 0.6;
                maxRange = 60;
                break;
        }
    }
    
    @Override
    public void run() {
        try {
            do {
                // chance for the bot to make error
                int randomNum = ThreadLocalRandom.current().nextInt(0, maxRange + 1);
                if (randomNum == 1) {
                    System.out.println("Bot missed an input!");
                    botStringIndex = 0;
                    Type_Project.botMadeMistake = true;
                    continue;
                } 
                
                botStringIndex += 1;
                if (botStringIndex == stringToType.length()) {
                    System.out.println("Bot won!");
                    Type_Project.botWon = true;
                    Type_Project.endRound();
                    throw new InterruptedException();
                } else {
                    System.out.println("Bot at" + botStringIndex);
                }
                int finalSleepTime = (int)(ThreadLocalRandom.current().nextDouble(minSleepTime, maxSleepTime) * 1000);
                FightPanel.UpdateTextDisplay(Type_Project.playerStringIndex, botStringIndex, Type_Project.stringToType);
                Thread.sleep(finalSleepTime); // this way the typespeed of the bot depends on difficulty 
            } while(true);
        } catch (InterruptedException e) {
            
        } finally {
            System.out.println("Bot killed itself.");
        }
    }
}
