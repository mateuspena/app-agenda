package com.example.minhaagenda;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Data {
    private LocalDate date;
    private Feriado feriado;
    private int compromissos;

    public Data(LocalDate date, int compromissos, Feriado feriado) {
        this.date = date;
        this.compromissos = compromissos;
        this.feriado = feriado;
    }

    public void setFeriado(Feriado feriado) {
        this.feriado = feriado;
    }

    public Feriado getFeriado() {
        return feriado;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCompromissos() {
        return compromissos;
    }

    public void setCompromissos(int compromissos) {
        this.compromissos = compromissos;
    }



    public String formatarData() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(this.date);
    }

    public int diaSemana() {
        return this.date.getDayOfWeek().getValue();
    }
}
