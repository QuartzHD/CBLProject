package com.mycompany.type_project;

import java.util.concurrent.ThreadLocalRandom;

public class Bot extends Thread {

    public int difficultyNum;
    public int botStringIndex = 0;
    public String stringToType;
    public double minSleepTime;
    public double maxSleepTime;
    public int maxRange;

    private volatile boolean paused = false;
    private volatile boolean stopRequested = false;
    private final Object pauseLock = new Object();

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

    public void pauseBot() {
        paused = true;
    }

    public void resumeBot() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void stopBot() {
        stopRequested = true;
        resumeBot();
        interrupt();
    }

    private void waitIfPaused() throws InterruptedException {
        synchronized (pauseLock) {
            while (paused && !stopRequested) {
                pauseLock.wait();
            }
        }
        if (stopRequested) throw new InterruptedException();
    }

    @Override
    public void run() {
        try {
            while (!stopRequested) {
                waitIfPaused();

                int randomNum = ThreadLocalRandom.current().nextInt(0, maxRange + 1);
                if (randomNum == 1) {
                    System.out.println("Bot missed an input!");
                    botStringIndex = 0;
                    Type_Project.botMadeMistake = true;
                } else {
                    botStringIndex++;
                    if (botStringIndex >= stringToType.length()) {
                        if (!stopRequested) {
                            System.out.println("Bot won!");
                            Type_Project.botWon = true;
                            Type_Project.endRound();
                        }
                        break;
                    } else {
                        System.out.println("Bot at " + botStringIndex);
                    }
                }

                int finalSleepTime = (int) (ThreadLocalRandom.current()
                        .nextDouble(minSleepTime, maxSleepTime) * 1000);
                Type_Project.updateTextDisplay();
                Thread.sleep(finalSleepTime);
            }
        } catch (InterruptedException e) {
            // The bot was paused/stopped or the fight ended.
        } finally {
            System.out.println("Bot stopped.");
        }
    }
}
