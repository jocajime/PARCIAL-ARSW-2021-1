package eci.arsw.covidanalyzer.hilos;

import eci.arsw.covidanalyzer.Result;
import eci.arsw.covidanalyzer.ResultAnalyzer;
import eci.arsw.covidanalyzer.TestReader;

import java.io.File;
import java.util.List;

public class threadCovidAnalyzer extends Thread{
    private List<File> files;
    private int fileanalize = 0;
    private TestReader testReader;
    private ResultAnalyzer resultAnalyzer;
    private boolean estado = true;

    public threadCovidAnalyzer(List<File> files){
        this.files = files;
        this.testReader = new TestReader ();
        resultAnalyzer = new ResultAnalyzer ();
        
    }

    public void corra() throws InterruptedException {
        for (File resultFile : files) {
            verificar ();
            List<Result> results = testReader.readResultsFromFile(resultFile);
            for (Result result : results) {
                verificar ();
                resultAnalyzer.addResult(result);
            }
            this.fileanalize+= 1;
        }
    }
    public void verificar() throws InterruptedException {
        if (!estado){
            while (!estado){
                pausar();
            }
        }
    }

    public synchronized void pausar() throws InterruptedException {
        wait();
    }

    public void parar(){
        this.estado=false;
    }
    public void reiniciar(){
        this.estado=true;
    }

    public int getPositive(){
        return resultAnalyzer.listOfPositivePeople ().size ();
    }

    public int getFileanalize() {
        return fileanalize;
    }

    @Override
    public void run() {
        try {
            corra();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
