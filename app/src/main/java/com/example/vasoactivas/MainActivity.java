package com.example.vasoactivas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView txtml,txtml2,txtml3,textDilusion,texDroga,txtDosis,txtDosis2,txtDosis3,txtTitulo,txtampolla,txAtributos;
    NumberPicker numberPicker, pickerSolvente, pickerPeso;
    String[] array_drogas, array_dopa, array_noradrenalina,array_solventes,array_pesos;
    String medBuscado = "DOPAMINA",micadena;
    int dosisNumero,peso,cantidad,contador, contador2;
    DecimalFormat form = new DecimalFormat("0.0");
    DBHelper manager;
    String array[] = new String[5];
    String array_medi[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new DBHelper(this);

       DBHelper dbHelper = new DBHelper(MainActivity.this);

       SQLiteDatabase db = dbHelper.getWritableDatabase();
        array_medi = manager.todas_medicina();
       inicilizar();
       cantidad = 200;
       peso = 60;
        array = manager.medicina(medBuscado);
        txAtributos.setText(""+array[1]+" "+array[2]+" mg en "+ array[3]+" ml");
        cambiodosis();
        calcular(peso);


    }

    private void inicilizar() {
        textDilusion = findViewById(R.id.textDilusion);
        txtml = findViewById(R.id.textml);
        txtml2 = findViewById(R.id.textml2);
        txtml3 = findViewById(R.id.textml3);
        texDroga = findViewById(R.id.texDroga);
        txtDosis = findViewById(R.id.texDosis);
        txtDosis2 = findViewById(R.id.texDosis2);
        txtDosis3 = findViewById(R.id.texDosis3);
        txtTitulo = findViewById(R.id.textView2);
        txAtributos = findViewById(R.id.txAmpAtributos);
        txtampolla = findViewById(R.id.txAmpolla);
        array_drogas = array_medi;
        array_dopa = getResources().getStringArray(R.array.arraydopa);
        array_solventes =getResources().getStringArray(R.array.arraysolventes);
        array_noradrenalina = getResources().getStringArray(R.array.arraynoradrenalina);
        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(array_drogas.length-1);
        numberPicker.setDisplayedValues(array_drogas);
        pickerPeso = findViewById(R.id.pickerPeso);
        pickerPeso.setMinValue(10);
        pickerPeso.setMaxValue(200);
        pickerPeso.setDisplayedValues(array_pesos);
        pickerPeso.setValue(60);

        texDroga.setText(String.format("%s", array_drogas[numberPicker.getValue()]));
        txtDosis.setText(String.format("%s", array_dopa[0]));
        txtDosis2.setText(String.format("%s", array_dopa[1]));
        txtDosis3.setText(String.format("%s", array_dopa[2]));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int antVal, int nuevoVal) {
                texDroga.setText(String.format("%s", array_drogas[nuevoVal]));
                dosisNumero = nuevoVal;

                if (dosisNumero == 0){
                    txtDosis.setText(String.format("%s", array_dopa[0]));
                    txtDosis2.setText(String.format("%s", array_dopa[1]));
                    txtDosis3.setText(String.format("%s", array_dopa[2]));
                    medBuscado = array_drogas[0];
                }
                if (dosisNumero == 1){
                    txtDosis.setText(String.format("2 ug/kg/min"));
                    txtDosis2.setText(String.format("10 ug/kg/min"));
                    txtDosis3.setText(String.format(""));
                    medBuscado = array_drogas[1];
                }
                if (dosisNumero == 2){
                    txtDosis.setText(String.format("%s", array_noradrenalina[0]));
                    txtDosis2.setText(String.format("%s", array_noradrenalina[1]));
                    txtDosis3.setText(String.format(""));
                    medBuscado = array_drogas[2];
                }
                if (dosisNumero == 3){
                    txtDosis.setText(String.format(""));
                    txtDosis2.setText(String.format("30 ug/min"));
                    txtDosis3.setText(String.format(""));
                    medBuscado = array_drogas[3];
                }
                cambiodosis();
             calcular(peso);
            }
        });
        pickerSolvente = findViewById(R.id.pickerSolvente);
        pickerSolvente.setMinValue(0);
        pickerSolvente.setMaxValue(5);
        pickerSolvente.setDisplayedValues(array_solventes);
        pickerSolvente.setValue(2);
        pickerSolvente.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int antVal, int nuevoVal) {
                if (nuevoVal ==0){ cantidad = 50;}
                if (nuevoVal ==1){ cantidad = 100; }
                if (nuevoVal ==2){ cantidad = 200;}
                if (nuevoVal ==3){ cantidad = 250; }
                if (nuevoVal ==4){ cantidad = 500;}
                if (nuevoVal ==5){ cantidad = 1000; }
                cambiodosis();
                calcular(peso);
            }
        });
        pickerPeso.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                peso = newVal;
                cambiodosis();
                calcular(peso);
            }
        });
        }

    private void calcular(int peso) {
        double priDosis = 0;
        double segDosis = 0;
        double terDosis = 0;
        if (dosisNumero ==0){
            priDosis = ((2.5*peso)*60/(200000/cantidad));
            segDosis = ((5*peso)*60/(200000/cantidad));
            terDosis = ((7*peso)*60/(200000/cantidad));

        }
        if (dosisNumero ==1){
            priDosis = (((2*peso)*60)*cantidad)/(250000);
            segDosis = (((10*peso)*60)*cantidad)/(250000);
        }
        if (dosisNumero ==2){
            priDosis = ((0.05*peso)*60/(200000/cantidad));
            segDosis = ((0.5*peso)*60/(200000/cantidad));
        }
        if (dosisNumero ==3){
            segDosis = ((30*peso)*60/(200000/cantidad));
        }
        txtml.setText("");
        txtml2.setText("");
        txtml3.setText("");
        if(priDosis >0) {
            txtml.setText(form.format(priDosis) + " ml/hora");
        }
        if(segDosis >0) {
            txtml2.setText(form.format(segDosis) + " ml/hora");
        }
        if(terDosis >0) {
            txtml3.setText(form.format(terDosis) + " ml/hora");
        }

    }

    private void cambiodosis() {
        array = manager.medicina(medBuscado);
        txAtributos.setText(""+array[1]+" "+array[2]+" mg en "+ array[3]+" ml");
        txtampolla.setText("AMPOLLA "+array[2]+" mg");
    }
}