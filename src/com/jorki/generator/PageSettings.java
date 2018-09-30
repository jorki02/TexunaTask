package com.jorki.generator;

import java.util.List;

public class PageSettings {

    private int pageWidth;
    private int pageHeight;
    private List<String> columns;
    private List<Integer> columnsSize;

    public PageSettings(int pageWidth, int pageHeight, List<String> columns, List<Integer> columnsSize) {
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.columns = columns;
        this.columnsSize = columnsSize;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public List<String> getColumns() {
        return columns;
    }

    public Integer getColumnsNumber(){
        return columnsSize.size();
    }

    public List<Integer> getColumnsSize() {
        return columnsSize;
    }

    public int getNthColumnWidth(int i){
        return this.columnsSize.get(i);
    }

    @Override
    public String toString() {
        return "PageSettings{" +
                "pageWidth=" + pageWidth +
                ", pageHeight=" + pageHeight +
                ", columns=" + columns +
                ", columnsSize=" + columnsSize +
                '}';
    }
}
