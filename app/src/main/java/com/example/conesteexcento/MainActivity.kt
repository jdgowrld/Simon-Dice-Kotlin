package com.example.conesteexcento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conesteexcento.ui.theme.ConEsteExcentoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConEsteExcentoTheme {
                VistaPrev()
            }
        }
    }
}

@Composable
fun SimonDice() {
    var secuenciaSimon by remember { mutableStateOf(listOf<Int>()) }
    var secuenciaJugador by remember { mutableStateOf(listOf<Int>()) }
    var jugando by remember { mutableStateOf(false) }
    var repetirDisponible by remember { mutableStateOf(true) }
    var botonIluminado by remember { mutableStateOf<Int?>(null) }
    var mensaje by remember { mutableStateOf("Presiona NEW ") }
    var recordHistorico by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    fun reproducirSecuencia() {
        scope.launch {
            jugando = false
            mensaje = "wacha la secuencia..."
            delay(500)

            secuenciaSimon.forEach { colorId ->
                botonIluminado = colorId
                delay(400)
                botonIluminado = null
                delay(200)
            }

            jugando = true
            mensaje = "Vas!"
        }
    }

    fun manejarClic(colorId: Int) {
        if (!jugando) return

        botonIluminado = colorId
        scope.launch {
            delay(200)
            botonIluminado = null
        }

        val nuevaSecuenciaJugador = secuenciaJugador + colorId
        secuenciaJugador = nuevaSecuenciaJugador

        val indiceActual = nuevaSecuenciaJugador.size - 1

        if (nuevaSecuenciaJugador[indiceActual] != secuenciaSimon[indiceActual]) {
            jugando = false
            mensaje = "¡U lose, Nivel: ${secuenciaSimon.size}"
            secuenciaSimon = emptyList()
            secuenciaJugador = emptyList()
            return
        }

        if (nuevaSecuenciaJugador.size == secuenciaSimon.size) {
            jugando = false
            mensaje = "¡buena! next nivel..."

            if (secuenciaSimon.size > recordHistorico) {
                recordHistorico = secuenciaSimon.size
            }

            scope.launch {
                delay(1000)
                secuenciaJugador = emptyList()
                secuenciaSimon = secuenciaSimon + Random.nextInt(4)
                repetirDisponible = true
                reproducirSecuencia()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Récord: $recordHistorico",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = mensaje,
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .alpha(if (botonIluminado == 0) 1f else 0.3f)
                    .background(Color.Red)
                    .clickable { manejarClic(0) }
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .alpha(if (botonIluminado == 1) 1f else 0.3f)
                    .background(Color.Green)
                    .clickable { manejarClic(1) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .alpha(if (botonIluminado == 2) 1f else 0.3f)
                    .background(Color.Blue)
                    .clickable { manejarClic(2) }
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .alpha(if (botonIluminado == 3) 1f else 0.3f)
                    .background(Color.Yellow)
                    .clickable { manejarClic(3) }
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    secuenciaJugador = emptyList()
                    secuenciaSimon = List(3) { Random.nextInt(4) }
                    repetirDisponible = true
                    reproducirSecuencia()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("NEW")
            }

            Button(
                onClick = {
                    repetirDisponible = false
                    secuenciaJugador = emptyList()
                    reproducirSecuencia()
                },
                enabled = repetirDisponible && secuenciaSimon.isNotEmpty() && jugando,
                modifier = Modifier.weight(1f)
            ) {
                Text("REPETIR")
            }

            Button(
                onClick = {
                    jugando = false
                    secuenciaSimon = emptyList()
                    secuenciaJugador = emptyList()
                    mensaje = "jaja perdedor"
                },
                enabled = secuenciaSimon.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("RENDIR")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPrev() {
    ConEsteExcentoTheme {
        SimonDice()
    }
}