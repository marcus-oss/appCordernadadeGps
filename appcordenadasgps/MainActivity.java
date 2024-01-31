package projeto.aula15.appcordenadasgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    String[] permissoesRequiradas = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final int APP_PERMISSOES_ID = 2023;

    TextView txtValorLatitude, txtValorLongitude;

    double latitude, longitude;
    boolean gpsAtivo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtValorLatitude = findViewById(R.id.txtValorLatitude);
        txtValorLongitude = findViewById(R.id.txtValorLongitude);
        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        gpsAtivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsAtivo) {
            obterCordenadas();
        } else {
            latitude = 0.00;
            longitude = 0.00;

            txtValorLongitude.setText(String.valueOf(longitude));
            txtValorLatitude.setText(String.valueOf(latitude));
            Toast.makeText(this,
                    "Coordenadas não Disponiveis!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void obterCordenadas() {
        boolean permissaoAtiva = permissaoParaObterLocalizacao();
        if (!permissaoAtiva) {
            capturarUltimaLocalizaoValida();

        }
    }

    private boolean permissaoParaObterLocalizacao() {
        Toast.makeText(this,
                "Aplicativo não tem permissao!",
                Toast.LENGTH_SHORT).show();

        List<String> permissoesNegada = new ArrayList<>();
        int verificacoesPermissoes;
        for (String permissao : this.permissoesRequiradas) {

            verificacoesPermissoes = ContextCompat.checkSelfPermission(MainActivity.this, permissao);

            if (verificacoesPermissoes != PackageManager.PERMISSION_GRANTED) {
                permissoesNegada.add(permissao);
            }
        }

        if (!permissoesNegada.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissoesNegada.toArray(new String[permissoesNegada.size()]), APP_PERMISSOES_ID);

            return true;
        } else {

            return false;
        }

    }

    private void capturarUltimaLocalizaoValida() {
        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();


        } else {
            latitude = 00.00;
            longitude = 00.00;

        }

        txtValorLongitude.setText(String.valueOf(longitude));
        txtValorLatitude.setText(String.valueOf(latitude));
        Toast.makeText(this,
                "Coordenadas obtidas com Sucesso!",
                Toast.LENGTH_SHORT).show();


    }

    private String formatarGeopoint(double valor) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        return decimalFormat.format(valor);
    }
}