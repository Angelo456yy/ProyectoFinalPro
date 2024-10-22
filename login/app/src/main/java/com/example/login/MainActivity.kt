package com.example.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.login.ui.theme.LoginTheme

public class MainActivity extends AppCompatActivity {

    Button Btn_Login, Btn_Registro:

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn_Login = findViewById(R.id.Btn_Login);
        Btn_Registro = findViewById(R.id.Btn_Registro);

        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( packageContext:MainActivity.this, Login.class));
            }
        });

        Btn_Registro.setOnClickListener(new View OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent packageContext: MainActivity, this, Registro.class);
            }
    }
        });

