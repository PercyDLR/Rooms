package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.functions.FirebaseFunctions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class FormDisponibilidadActivity extends AppCompatActivity {

    Button inputFecha, inputHoraInicio, inputHoraFin ,btnGuardarDisponibilidad;

    HashMap<String,Object> datos = new HashMap<>();
    boolean hasFecha = false, hasHoraInicio = false, hasHoraFin = false;
    EspacioDTO espacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_disponibilidad);

        espacio = (EspacioDTO) getIntent().getSerializableExtra("espacio");
        datos.put("keyEspacio",espacio.getKey());

        inputFecha = findViewById(R.id.inputFecha);
        inputHoraInicio = findViewById(R.id.inputHoraInicio);
        inputHoraFin = findViewById(R.id.inputHoraFin);
        btnGuardarDisponibilidad = findViewById(R.id.btnAgregarDisponibilidad);

    }

    public void seleccionarFecha (View view){

        CalendarConstraints constraint = new CalendarConstraints.Builder()
                .setStart(MaterialDatePicker.todayInUtcMilliseconds())
                .setValidator(DateValidatorPointForward.now()).build();

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Elija una fecha de disponibilidad")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraint).build();

        datePicker.show(getSupportFragmentManager(),"tag");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDateTime dia = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) selection), ZoneId.of("UTC"));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("E d MMM YY",new Locale("es"));
            inputFecha.setText(dia.format(df));

            datos.put("fecha",selection.toString());
            hasFecha = true;

            Log.d("formDisponibilidad", "Fecha seleccionada: " + dia.format(df) +"\na partir de: "+selection);
        });
    }

    public void seleccionarHoraInicio (View view){
        MaterialTimePicker horaInicio = new MaterialTimePicker.Builder()
                .setTitleText("Elija una hora de Inicio")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(LocalTime.now().getHour())
                .setMinute(LocalTime.now().getMinute())
                .build();

        horaInicio.show(getSupportFragmentManager(),"tag");

        horaInicio.addOnPositiveButtonClickListener(v -> {
            Integer horaInicioInt = (Integer) horaInicio.getHour();
            inputHoraInicio.setText(horaInicioInt+":00");

            datos.put("horaInicio",horaInicioInt);
            hasHoraInicio = true;

            Log.d("formDisponibilidad", "Hora Fin Seleccionada: "+horaInicioInt);
        });
    }

    public void seleccionarHoraFin (View view){

        MaterialTimePicker horaFin = new MaterialTimePicker.Builder()
                .setTitleText("Elija una hora de Inicio")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(LocalTime.now().getHour())
                .setMinute(LocalTime.now().getMinute())
                .build();

        horaFin.show(getSupportFragmentManager(),"tag");

        horaFin.addOnPositiveButtonClickListener(v -> {
            Integer horaFinInt = (Integer) horaFin.getHour();
            inputHoraFin.setText(horaFinInt+":00");

            datos.put("horaFin",horaFinInt);
            hasHoraFin = true;

            Log.d("formDisponibilidad", "Hora Fin Seleccionada: "+horaFinInt);
        });
    }

    public void agregarDisponibilidad (View view){
        if (!hasFecha || !hasHoraInicio || !hasHoraFin){
            Toast.makeText(FormDisponibilidadActivity.this, "Debe llenar todos los campos para poder continuar", Toast.LENGTH_SHORT).show();
            Log.d("formDisponibilidad", "Debe llenar todos los campos para poder continuar");
            return;
        } else if ((Integer) datos.get("horaFin") <= (Integer) datos.get("horaInicio")){
            inputHoraInicio.setText("Inicio");
            inputHoraFin.setText("Fin");
            hasHoraInicio = false;
            hasHoraFin= false;

            Toast.makeText(FormDisponibilidadActivity.this, "La fecha de inicio debe ser mayor a la fecha de fin", Toast.LENGTH_SHORT).show();
            Log.d("formDisponibilidad", "La fecha de inicio debe ser mayor a la fecha de fin");
            return;
        }

        // Si se pasan las validaciones
        FirebaseFunctions.getInstance().getHttpsCallable("agregarDisponibilidad").call(datos)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(FormDisponibilidadActivity.this, "Disponibilidad Agregada con éxito", Toast.LENGTH_SHORT).show();
                        Log.d("formDisponibilidad", "Disponibilidad Agregada con éxito");

                        Intent intent = new Intent(FormDisponibilidadActivity.this,DetallesEspacioActivity.class);
                        intent.putExtra("espacio",espacio);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("funcion","admin");
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(FormDisponibilidadActivity.this, "Disponibilidad Agregada con éxito", Toast.LENGTH_SHORT).show();
                        Log.d("formDisponibilidad",task.getException().getMessage());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (espacio != null) {
                    Intent intent = new Intent(FormDisponibilidadActivity.this,DetallesEspacioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("espacio",espacio);
                    intent.putExtra("funcion","admin");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(FormDisponibilidadActivity.this,ListaEspaciosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}