package com.example.nexusbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.random.Random

private val Bg = Color(0xFF080E19)
private val Panel = Color(0xFF121E30)
private val Muted = Color(0xFF91A2BA)
private val Green = Color(0xFF36D6A3)
private val Red = Color(0xFFFF647C)

data class Trade(val side: String, val amount: Int, val result: Int, val signal: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NexusApp() }
    }
}

@Composable
fun NexusApp() {
    var balance by remember { mutableIntStateOf(140000) }
    var profit by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }
    var ticks by remember { mutableIntStateOf(0) }
    var price by remember { mutableStateOf(1.08420) }
    var status by remember { mutableStateOf("SIAP") }
    var trades by remember { mutableStateOf(listOf<Trade>()) }
    var wins by remember { mutableIntStateOf(0) }
    var losses by remember { mutableIntStateOf(0) }

    // Synthetic prices only: this app does not connect to Stockity or a broker.
    LaunchedEffect(running) {
        while (running) {
            delay(5000)
            val change = Random.nextDouble(-0.00035, 0.00035)
            price += change
            ticks++
            val side = if (change >= 0) "BUY" else "SELL"
            val amount = 14000
            if (balance >= amount) {
                val win = Random.nextBoolean()
                val result = if (win) 1100 else -amount
                balance = max(0, balance + result)
                profit += result
                if (win) wins++ else losses++
                trades = listOf(Trade(side, amount, result, "EMA + RSI + MACD (simulasi)")) + trades
                status = if (win) "HASIL SIMULASI: PROFIT" else "HASIL SIMULASI: LOSS"
            } else {
                running = false
                status = "SALDO SIMULASI TIDAK CUKUP"
            }
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Green,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("NEXUS BOT", fontSize = 25.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text("TRADING SIMULATOR  •  v1.0", color = Muted, fontSize = 11.sp)
                        }
                        Text("DEMO ONLY", color = Green, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                        Column(Modifier.padding(18.dp)) {
                            Text("TOTAL PROFIT / LOSS SIMULASI", color = Muted, fontSize = 11.sp)
                            Text("Rp ${"% ,d".format(profit).replace(',', '.')}", color = if (profit >= 0) Green else Red, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            Text("Saldo simulasi: Rp ${"% ,d".format(balance).replace(',', '.')}", color = Color.White)
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard("STATUS BOT", if (running) "RUNNING" else "STOPPED", Modifier.weight(1f), if (running) Green else Muted)
                        StatCard("KONEKSI", "OFFLINE", Modifier.weight(1f), Muted)
                    }
                }
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("MARKET PREVIEW", fontWeight = FontWeight.Bold)
                            Text("DATA HARGA SINTETIS • BUKAN HARGA STOCKITY", color = Muted, fontSize = 10.sp)
                            Text("EUR/USD   ${"%.5f".format(price)}", color = Green, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                            Text("Interval simulasi: 5 detik", color = Muted, fontSize = 12.sp)
                        }
                    }
                }
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("STRATEGI SIMULASI", fontWeight = FontWeight.Bold)
                            Text("EMA + RSI + MACD", color = Green, fontWeight = FontWeight.Bold)
                            Text("Indikator ditampilkan sebagai rancangan strategi; hasil transaksi di versi ini diacak untuk demonstrasi UI, bukan prediksi pasar.", color = Muted, fontSize = 12.sp)
                            Text("Modal awal: Rp140.000  •  Nilai per simulasi: Rp14.000", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { running = !running; status = if (running) "SIMULASI BERJALAN" else "SIMULASI DIHENTIKAN" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (running) Red else Green)
                        ) { Text(if (running) "STOP BOT" else "START BOT", color = Bg, fontWeight = FontWeight.Bold) }
                        OutlinedButton(onClick = {
                            running = false; balance = 140000; profit = 0; ticks = 0
                            trades = emptyList(); wins = 0; losses = 0; status = "DIRESET"
                        }, modifier = Modifier.weight(1f)) { Text("RESET") }
                    }
                    Text(status, color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 7.dp))
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard("WIN", wins.toString(), Modifier.weight(1f), Green)
                        StatCard("LOSS", losses.toString(), Modifier.weight(1f), Red)
                        StatCard("TOTAL", ticks.toString(), Modifier.weight(1f), Color.White)
                    }
                }
                item { Text("RIWAYAT SIMULASI", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                if (trades.isEmpty()) {
                    item { Text("Belum ada transaksi simulasi. Tekan START BOT.", color = Muted, fontSize = 12.sp) }
                } else {
                    items(trades.take(30)) { trade ->
                        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(12.dp)) {
                            Row(Modifier.fillMaxWidth().padding(13.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text("${trade.side}  •  Rp ${trade.amount}", color = if (trade.side == "BUY") Green else Red, fontWeight = FontWeight.Bold)
                                    Text(trade.signal, color = Muted, fontSize = 10.sp)
                                }
                                Text("${if (trade.result >= 0) "+" else ""}Rp ${trade.result}", color = if (trade.result >= 0) Green else Red, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                item {
                    Text("PENTING: Simulator ini tidak terhubung ke Stockity, tidak membaca harga real-time, dan tidak melakukan transaksi uang sungguhan. Angka hasil hanyalah demonstrasi.", color = Muted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier, color: Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = Muted, fontSize = 10.sp)
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
