package com.facci.conversorbc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityBC extends AppCompatActivity {

    final String [] datos = new String []{"Dólar","Euro","Peso Mexicano"};

    private Spinner MonedaActualBC;
    private Spinner MonedaCambiarBC;
    private EditText CanjearMonedaBC;
    private TextView ResultadoBC;

    final private double factorDolarEuro = 0.87;
    final private double factorPesoDolar = 0.54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_bc);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);

        MonedaActualBC = (Spinner) findViewById(R.id.MonedaActualBC);
        MonedaActualBC.setAdapter(adaptador);
        MonedaCambiarBC = (Spinner) findViewById(R.id.MonedaCambiarBC);

        SharedPreferences preferencias = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        String tmpMonedaActual = preferencias.getString("MonedaActual","");
        String tmpMonedaCambiar = preferencias.getString("MonedaCmabiar","");

        if (tmpMonedaActual.equals("")){
            int indice = adaptador.getPosition(tmpMonedaActual);
            MonedaActualBC.setSelection(indice);
        }

        if(tmpMonedaCambiar.equals("")){
            int indice = adaptador.getPosition(tmpMonedaCambiar);
            MonedaCambiarBC.setSelection(indice);
        }

    }

    public  void clickConvertir(View v){
        MonedaActualBC = (Spinner) findViewById(R.id.MonedaActualBC);
        MonedaCambiarBC = (Spinner) findViewById(R.id.MonedaCambiarBC);
        CanjearMonedaBC = (EditText) findViewById(R.id.CanjearMonedaBC);
        ResultadoBC = (TextView) findViewById(R.id.ResultadoBC);

        String MonedaActual = MonedaActualBC.getSelectedItem().toString();
        String MonedaCambiar = MonedaCambiarBC.getSelectedItem().toString();

        double CanjearMoneda = Double.parseDouble(CanjearMonedaBC.getText().toString());
        double Resultado = procesarConversion(MonedaActual,MonedaCambiar,CanjearMoneda);

        if (Resultado>0){
            ResultadoBC.setText(String.format("Por %5.2f %s, usted recibira %5.2f %s",CanjearMoneda,MonedaActual,Resultado,MonedaCambiar));
            CanjearMonedaBC.setText("");

            SharedPreferences preferencias = getSharedPreferences("Mis Preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();

            editor.putString("MonedaActual",MonedaActual);
            editor.putString("MonedaCambiar",MonedaCambiar);

            editor.commit();

        }else {
            ResultadoBC.setText(String.format("Usted Recibira"));
            Toast.makeText(MainActivityBC.this,"Las opciones elegidas no tienen un factor de conversion", Toast.LENGTH_SHORT).show();
        }

    }

    private double procesarConversion (String MonedaActual,String MonedaCambio, double CanjearMoneda){
        double resultadosConversion= 0;

        switch (MonedaActual){
            case "Dólar":
                if (MonedaCambio.equals("Euro"))
                    resultadosConversion = CanjearMoneda * factorDolarEuro;

                if(MonedaCambio.equals("Peso Mexicano"))
                    resultadosConversion = CanjearMoneda / factorPesoDolar;

                break;
            case "Euro":
                if(MonedaCambio.equals("Dólar"))
                    resultadosConversion = CanjearMoneda / factorDolarEuro;

                break;
            case "Peso Mexicano":
                if (MonedaCambio.equals("Dólar")){
                    resultadosConversion = CanjearMoneda * factorPesoDolar;
                }
                break;
        }

        return resultadosConversion;


    }
}
