package eci.arsw.covidanalyzer;

import eci.arsw.covidanalyzer.hilos.threadCovidAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Camel Application
 */
public class CovidAnalyzerTool {

    private ResultAnalyzer resultAnalyzer;
    private TestReader testReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private int cantHilos = 5;
    private Boolean bandera = false;

    public CovidAnalyzerTool() {
        resultAnalyzer = new ResultAnalyzer();
        testReader = new TestReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processResultData(List<File> resultFiles) {
        amountOfFilesProcessed.set(0);
        amountOfFilesTotal = resultFiles.size();
        for (File resultFile : resultFiles) {
            List<Result> results = testReader.readResultsFromFile(resultFile);
            for (Result result : results) {
                resultAnalyzer.addResult(result);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    private List<File> getResultFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }


    public Set<Result> getPositivePeople() {
        return resultAnalyzer.listOfPositivePeople();
    }

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        //hilos
        CovidAnalyzerTool covidAnalyzerTool = new CovidAnalyzerTool();
        List<File> resultFiles = covidAnalyzerTool.getResultFileList ();
        int tamañoFiles = resultFiles.size ();
        ArrayList<threadCovidAnalyzer> hilos = new ArrayList<threadCovidAnalyzer> ();
        for (int i = 0; i< covidAnalyzerTool.cantHilos; i++){
            List<File> sublist = resultFiles.subList ( ((int) tamañoFiles/covidAnalyzerTool.cantHilos)*i,
                    (((int) tamañoFiles/covidAnalyzerTool.cantHilos)*i)+(int) tamañoFiles/covidAnalyzerTool.cantHilos);
            hilos.add(new threadCovidAnalyzer (sublist));
        }
        for (int i = 0; i< covidAnalyzerTool.cantHilos; i++){
            hilos.get (i).start ();
        }

        //proceso
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            if (!line.contains("exit")){
                for(threadCovidAnalyzer t: hilos){
                    synchronized (line) {
                        t.parar ();
                    }
                }
                //INFO
                int fileProcessed = 0;
                int positive = 0;
                for(threadCovidAnalyzer t: hilos){
                    fileProcessed+=t.getFileanalize ();
                    positive += t.getPositive ();
                }

                String message = "Processed %d out of %d files.\nFound %d positive people:\n%s";
                Set<Result> positivePeople = covidAnalyzerTool.getPositivePeople();
                String affectedPeople = positivePeople.stream().map(Result::toString).reduce("", (s1, s2) -> s1 + "\n" + s2);
                message = String.format(message, fileProcessed, resultFiles.size (), positive, affectedPeople);
                System.out.println(message);
                String line_2 = scanner.nextLine();
                for(threadCovidAnalyzer t: hilos){
                    synchronized (line_2){
                        t.reiniciar ();
                        synchronized (t){
                            t.notifyAll();
                        }
                    }
                }
            }else {
                break;
            }

        }
    }

    public void parar() throws InterruptedException {
        bandera = true;
        while(bandera){
            this.esperar();
        }
    }

    public synchronized void esperar() throws InterruptedException {
        wait();
    }
    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }
}

