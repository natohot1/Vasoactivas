package com.example.vasoactivas;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView txtml,txtml2,txtml3,textDilusion,texDroga,txtDosis,txtDosis2,txtDosis3,txtTitulo,txtampolla,txAtributos;
    NumberPicker medicamentoPicker, pickerSolvente, pickerPeso;
    String[] array_drogas, array_dopaInicio, array_noradrenalina,array_solventes,array_pesos;
    String medBuscado = "DOPAMINA",micadena;
    int dosisNumero,peso,cantidad,cantidadMg,contador, contador2;
    DecimalFormat form = new DecimalFormat("0.0");
    DBHelper manager;
    String arrayMedicamento[] = new String[5];
    String array_medi[], array_Dosis[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new DBHelper(this);

       DBHelper dbHelper = new DBHelper(MainActivity.this);

       SQLiteDatabase db = dbHelper.getWritableDatabase();
        arrayMedicamento = manager.medicina(medBuscado);
        //listado de solo nombres de medicanentos
        array_medi = manager.todas_medicina();
        array_Dosis  =manager.dosis(medBuscado);
       inicilizar();
       cantidad = 200;
       cantidadMg = 200;

       peso = 60;

        txAtributos.setText(""+ arrayMedicamento[1]+" "+ arrayMedicamento[2]+" mg en "+ arrayMedicamento[3]+" ml");
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
        array_dopaInicio = getResources().getStringArray(R.array.arraydopa);
        array_solventes =getResources().getStringArray(R.array.arraysolventes);
        array_noradrenalina = getResources().getStringArray(R.array.arraynoradrenalina);
        medicamentoPicker = findViewById(R.id.numberPicker);
        medicamentoPicker.setMinValue(0);
        medicamentoPicker.setMaxValue(array_drogas.length-1);
        medicamentoPicker.setDisplayedValues(array_drogas);
        pickerPeso = findViewById(R.id.pickerPeso);
        pickerPeso.setMinValue(10);
        pickerPeso.setMaxValue(200);
        pickerPeso.setDisplayedValues(array_pesos);
        pickerPeso.setValue(60);

        texDroga.setText(String.format("%s", array_drogas[medicamentoPicker.getValue()]));
        txtDosis.setText(String.format("%s", array_dopaInicio[0]));
        txtDosis2.setText(String.format("%s", array_dopaInicio[1]));
        txtDosis3.setText(String.format("%s", array_dopaInicio[2]));
        medicamentoPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int antVal, int nuevoVal) {
                medBuscado = array_drogas[nuevoVal];
                array_Dosis = manager.dosis(medBuscado);
                texDroga.setText(medBuscado);
                dosisNumero = nuevoVal;

              if (dosisNumero == 2){
                  txtDosis.setText(String.format("%s", array_dopaInicio[0]));
                  txtDosis2.setText(String.format("%s", array_dopaInicio[1]));
                  txtDosis3.setText(String.format("%s", array_dopaInicio[2]));
                  medBuscado = array_drogas[0];
              }
              if (dosisNumero == 1){
                  txtDosis.setText(String.format("2 ug/kg/min"));
                  txtDosis2.setText(String.format("20 ug/kg/min"));
                  txtDosis3.setText(String.format(""));
                  medBuscado = array_drogas[1];
              }
              if (dosisNumero == 4){
                  txtDosis.setText(String.format("%s", array_noradrenalina[0]));
                  txtDosis2.setText(String.format("%s", array_noradrenalina[1]));
                  txtDosis3.setText(String.format(""));
                  medBuscado = array_drogas[2];
              }
              if (dosisNumero == 3){
                  txtDosis.setText(String.format("30 ug/min"));
                  txtDosis2.setText(String.format(""));
                  txtDosis3.setText(String.format(""));
                  medBuscado = array_drogas[3];
              }
                cambiodosis();
                calcularSegun(array_Dosis);
           //  calcular(peso);
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

        //CALCULAR SEGUN MEDICAMENTO
    private void calcularSegun(String []arryMedicamento){
        txtml.setText("");
        txtml2.setText("");
        txtml3.setText("");
        double ampollaMicroGramos = (Double.valueOf(arryMedicamento[2]))+1000; //CANRIDAD AMPOLLA EN MICROGRAMOS
        double priDosis = 0;
        double segDosis = 0;
        double terDosis = 0;
            if((array_Dosis[3]).equals("ug/min")){
                //DOSIS NITROGLICERINA EMPEZAR A ESTA DOSIS Y SE PUEDE AUMENTYAR HASTA 10 VECES
                double dosisPorKilo = Double.valueOf(array_Dosis[2]);
                priDosis = (dosisPorKilo*cantidad)/ampollaMicroGramos;
            }
            if((array_Dosis[3]).equals("ug/kg/min")){
                double dosis = Double.valueOf(arryMedicamento[2]);
                priDosis = ((dosis*peso)*60/(ampollaMicroGramos/cantidad));
                if(array_Dosis[5]!=null) {
                    if ((array_Dosis[5]).equals("ug/kg/min")) {
                        double dosis1 = Double.valueOf(arryMedicamento[4]);
                        segDosis = ((dosis1 * peso) * 60 / (ampollaMicroGramos / cantidad));
                        if(array_Dosis[7]!=null) {
                            if ((array_Dosis[7]).equals("ug/kg/min")) {
                                double dosis2 = Double.valueOf(arryMedicamento[6]);
                                terDosis = ((dosis2 * peso) * 60 / (ampollaMicroGramos / cantidad));
                            }
                        }
                    }
                }
            }
            if((array_Dosis[3]).equals("mg/kg")){
                double ampollaMg =Double.valueOf(array_medi[2]); //100 oh 250 mg de fentoina
                double ampollaCC =Double.valueOf(array_medi[3]); //2 oh 5 cc de fentoina
                double dosis8 = Double.valueOf(array_Dosis[2]);
                double dosis9 = Double.valueOf(array_Dosis[4]); //dosis de 24 horas
                if(medBuscado.equals("FENITOINA")){
                    //MG PO KILO DOSIS DE ATAQUE EN 20 MINUTOS
                    double mgKg = (peso*dosis8);
                    //cantidad de ampollas para el peso 2 o 5 cc
                    priDosis = (mgKg/ampollaMg)*ampollaCC; //dosisAmpollas
                    //MG PO KILO DOSIS DE MANTENIMIENTO EN 24 HORAS
                    double mgKg24 = (peso*dosis9);
                    //cantidad de ampollas para el peso 2 o 5 cc
                    segDosis = (mgKg24/ampollaMg)*ampollaCC; //dosisAmpollas
                    txtml.setText(priDosis+" cc, en "+cantidad+" cc de Salino, en 20 minutos");
                    txtml2.setText(segDosis+" cc, en "+cantidad+" cc de Salino en 24 horas");
                }
            }

        if(medBuscado.equals("FENITOINA")){}else {
            txtml.setText(""+priDosis);
            txtml2.setText(""+segDosis);
            txtml3.setText(""+terDosis);
        }
    }

    public int tamanoarray(String[] mio){
        int devol=0,contador1=0;
        int contador = mio.length;
        while (contador1<=contador){
            if(mio[contador1]!=null){
                devol++;
            }
            contador1++;
        }
        return devol;
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
            segDosis = (((20*peso)*60)*cantidad)/(250000);
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
        arrayMedicamento = manager.medicina(medBuscado);
        txAtributos.setText(""+ arrayMedicamento[1]+" "+ arrayMedicamento[2]+" mg en "+ arrayMedicamento[3]+" ml");
        txtampolla.setText("AMPOLLA "+ arrayMedicamento[2]+" mg");
    }
}















