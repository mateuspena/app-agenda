package com.example.minhaagenda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{
    public ArrayList<Data> datas;

    public DataAdapter(ArrayList<Data> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.bind(this.datas.get(position));
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay;
        TextView txtDate;
        TextView imgCarga;
        TextView imgFeriado;

        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtDay = itemView.findViewById(R.id.txt_day);
            this.txtDate = itemView.findViewById(R.id.txt_date);
            this.imgCarga = itemView.findViewById(R.id.img_carga);
            this.imgFeriado = itemView.findViewById(R.id.img_feriado);
        }

        @SuppressLint("ResourceAsColor")
        void bind(Data data) {
            switch (data.diaSemana()) {
                case 1: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_segunda)); break;
                case 2: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_terca)); break;
                case 3: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_quarta)); break;
                case 4: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_quinta)); break;
                case 5: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_sexta)); break;
                case 6: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_sabado)); break;
                default: this.txtDay.setText(itemView.getContext().getString(R.string.txt_dia_domingo));
            }
            this.txtDate.setText( data.formatarData() );

            if (LocalDate.now().equals(data.getDate())) {
                this.itemView.setBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.light_primary));
            } else {
                this.itemView.setBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), R.color.white));
            }

            if (data.getCompromissos() == 0) {
                this.imgCarga.setBackgroundResource( R.drawable.ic_baseline_notifications_none_24 );
            } else if (data.getCompromissos() <= 5) {
                this.imgCarga.setBackgroundResource( R.drawable.ic_baseline_notifications_24 );
            } else {
                this.imgCarga.setBackgroundResource( R.drawable.ic_baseline_notifications_active_24 );
            }

            this.imgCarga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                    dialogBuilder.setTitle("Compromissos");
                    if (data.getCompromissos() < 1) {
                        dialogBuilder.setMessage("Você não tem compromissos neste dia.");
                    } else {
                        dialogBuilder.setMessage("Você tem " + data.getCompromissos() + " compromissos neste dia.");
                    }
                    dialogBuilder.setPositiveButton("OK", null);
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            });

            if (data.getFeriado() != null) {
                Feriado feriado = data.getFeriado();
                this.imgFeriado.setVisibility(View.VISIBLE);

                if (feriado.typeCode != 1 && feriado.typeCode != 2 && feriado.typeCode != 3) {
                    this.imgFeriado.setBackgroundResource( R.drawable.ic_baseline_insert_invitation_24 );
                }

                this.imgFeriado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                        dialogBuilder.setTitle(feriado.type);
                        dialogBuilder.setMessage(feriado.description);

                        dialogBuilder.setPositiveButton("OK", null);
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                    }
                });
            } else {
                this.imgFeriado.setVisibility(View.GONE);
            }
        }
    }
}
