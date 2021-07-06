package com.example.minhaagenda;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Calendario {
    private Context context;

    public Calendario(Context context) {
        this.context = context;
    }

    public ArrayList<Data> montaCalendario(Map<LocalDate, JSONObject> feriados) {
        LocalDate date = LocalDate.parse("2021-01-01");
        LocalDate dateEnd = LocalDate.parse("2021-12-31");

        ArrayList<Data> datas = new ArrayList<>();
        while (!date.isAfter(dateEnd)) {
            Feriado feriado = null;

            JSONObject jsonFeriado = (JSONObject) feriados.get(date);
            if (jsonFeriado != null) {
                try {
                    feriado = new Feriado(
                        jsonFeriado.getString("name"),
                        jsonFeriado.getString("type"),
                        jsonFeriado.getInt("type_code")
                    );
                } catch (JSONException e) {
                }
            }

            datas.add(
                new Data(date, this.numeroCompromissosData(date), feriado)
            );

            date = date.plusDays(1);
        }

        return datas;
    }

    public int numeroCompromissosData(LocalDate date)
    {
        Compromisso c = new Compromisso(this.context);
        return c.getDataNumeroCompromissos(date);
    }

    public static int retornaPosicaoInicial()
    {
        return LocalDate.now().getDayOfYear() - 1;
    }


    public static int retornaPosicaoData(LocalDate data)
    {
        return data.getDayOfYear() - 1;
    }
}
