package com.jorki.generator;

import com.google.common.base.Splitter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportGenerator {

    private PageSettings pageSettings;

    public ReportGenerator(PageSettings pageSettings){
        this.pageSettings = pageSettings;
    }

    public void generateReport(File fileCsv){
        try {
            CSVParser csvParser = CSVParser.parse(fileCsv, Charset.forName("UTF-16"), CSVFormat.TDF);
            List<CSVRecord> csvRecordList = csvParser.getRecords();
            List<List<String>> lines = new ArrayList<>();
            for(CSVRecord csvRecord : csvRecordList){
                lines.add(parseRecord(parseCSVRecords(csvRecord)));
            }
            List<String> titles = parseRecord(pageSettings.getColumns());
            if(checkPageSettings(titles)){
                writeReport(createPages(titles, lines));
            } else {
                writeError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPageSettings(List<String> titles){
        if(titles.size() + 1 >=  pageSettings.getPageHeight()){
            return false;
        }

        int fullWidthOfPage = 1;
        for(Integer size : pageSettings.getColumnsSize()){
            if(size <= 0){
                return false;
            }
            fullWidthOfPage += size;
            fullWidthOfPage += 3;
        }

        if(fullWidthOfPage > pageSettings.getPageWidth()){
            return false;
        }

        return true;
    }

    private void writeError(){
        BufferedWriter writer = null;
        File outputFile = new File("src/properties/output.txt");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_16));
            writer.write("Параметры страниц несовместимы с данными");
            writer.flush();
        } catch (IOException ignored){}
    }

    private void writeReport(List<String> pages){
        BufferedWriter writer = null;
        File outputFile = new File("src/properties/output.txt");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_16));
            for(String line : pages){
                writer.write(line);
                writer.write("\n");
                writer.flush();
            }
        } catch (IOException ignored){}
    }

    private List<String> createPages(List<String> titleList, List<List<String>> lines){
        List<String> report = new ArrayList<>(titleList);
        report.add(StringUtils.repeat("-", pageSettings.getPageWidth()));
        int remainingLines = pageSettings.getPageHeight() - titleList.size() - 1;
        for(List<String> line : lines){
            for(String str : line){
                if(remainingLines == 0){
                    report.add("~");
                    report.addAll(titleList);
                    report.add(StringUtils.repeat("-", pageSettings.getPageWidth()));
                    remainingLines = pageSettings.getPageHeight() - titleList.size() - 1;
                }
                report.add(str);
                remainingLines--;
            }

            report.add(StringUtils.repeat("-", pageSettings.getPageWidth()));
            remainingLines--;
        }

        return report;
    }

    private List<String> parseRecord(List<String> record){
        int maxLineSize = 0;
        List<List<StringBuilder>> listColumns = splitRecordOnTokens(record);
        for(List<StringBuilder> column : listColumns){
            maxLineSize = Math.max(maxLineSize, column.size());
        }

        return constructLinesOfPage(listColumns, maxLineSize);
    }

    private List<List<StringBuilder>> splitRecordOnTokens(List<String> record){
        List<List<StringBuilder>> listColumns = new ArrayList<>();

        for(int i = 0; i < pageSettings.getColumnsNumber(); i++){
            List<StringBuilder> list = new ArrayList<>();
            List<String> splittedBySpace =
                    new ArrayList<>(Arrays.asList(record.get(i).split("(?=[^а-яА-Я0-9])")));
            int currentLine = -1;
            int remainedSpace = pageSettings.getNthColumnWidth(i);
            currentLine++;
            for(String word : splittedBySpace){
                if(word.length() <= remainedSpace){
                    if(list.size() <= currentLine){
                        list.add(new StringBuilder());
                    }
                    list.get(currentLine).append(word);
                    remainedSpace = remainedSpace - word.length();
                } else {
                    List<String> splittedWord = new ArrayList<>();
                    Splitter.fixedLength(pageSettings.getNthColumnWidth(i))
                            .split(word.trim()).forEach(splittedWord::add);
                    currentLine = currentLine + splittedWord.size();
                    splittedWord.forEach(x->{list.add(new StringBuilder(x));});
                    remainedSpace = pageSettings.getNthColumnWidth(i) - list.get(list.size() - 1).length();
                }
            }
            listColumns.add(list);
        }
        return listColumns;
    }

    private List<String> constructLinesOfPage(List<List<StringBuilder>> listColumns, int maxLineSize){
        List<String> listLines = new ArrayList<>();

        for(int i = 0; i < maxLineSize; i++){
            StringBuilder line = new StringBuilder();

            for(int j = 0; j < pageSettings.getColumnsNumber(); j++){
                if(i < listColumns.get(j).size()){
                    line.append(" | ").append(StringUtils.rightPad(listColumns.get(j).get(i).toString(),
                            pageSettings.getNthColumnWidth(j), " "));
                } else {
                    line.append(" | ").append(StringUtils.repeat(" ", pageSettings.getNthColumnWidth(j)));
                }
            }
            line.append(" |");
            listLines.add(line.substring(1));
        }

        return listLines;
    }

    private List<String> parseCSVRecords(CSVRecord csvRecord){
        List<String> listColumnRecords = new ArrayList<>();
        for(int i = 0; i < pageSettings.getColumnsNumber(); i++){
            listColumnRecords.add(csvRecord.get(i));
        }
        return listColumnRecords;
    }

}
