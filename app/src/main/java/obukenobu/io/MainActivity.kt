package obukenobu.io

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }
    private var operand1: Double? = null
    private var pendingOperation = "="
    private val STATE_DEPENDING_OPERATION = "PendingOperation"
    private val STATE_OPERAND1 = "Operand1"
    private val STATE_OPERAND_STORED = "OperandStored"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        val button0: Button = findViewById(R.id.no0)
        val button1: Button = findViewById(R.id.no1)
        val button2: Button = findViewById(R.id.no2)
        val button3: Button = findViewById(R.id.no3)
        val button4: Button = findViewById(R.id.no4)
        val button5: Button = findViewById(R.id.no5)
        val button6: Button = findViewById(R.id.no6)
        val button7: Button = findViewById(R.id.no7)
        val button8: Button = findViewById(R.id.no8)
        val button9: Button = findViewById(R.id.no9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        val btnEquals: Button = findViewById(R.id.equals)
        val btnAdd: Button = findViewById(R.id.add)
        val btnDivide: Button = findViewById(R.id.divide)
        val btnMultiply: Button = findViewById(R.id.multiply)
        val btnMinus: Button = findViewById(R.id.minus)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
            Log.d("onClick", "Clicked button ${b.text}")
        }


        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                Log.d("onPerformException", "exception: $e")
                newNumber.text.clear()
            }
            pendingOperation = op
            displayOperation.text = pendingOperation

        }

        btnAdd.setOnClickListener(opListener)
        btnDivide.setOnClickListener(opListener)
        btnEquals.setOnClickListener(opListener)
        btnMinus.setOnClickListener(opListener)
        btnMultiply.setOnClickListener(opListener)
    }

    private fun performOperation(value: Double, op: String) {
        if (operand1 == null) {
            operand1 = value
        } else {


            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.text.clear()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND_STORED, true)
            Log.d("onSaveInstance", "saved oprand")
        }
        outState.putString(STATE_DEPENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(STATE_OPERAND_STORED)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        } else{
            operand1 = null
        }
        pendingOperation = savedInstanceState.getString(STATE_DEPENDING_OPERATION).toString()
        displayOperation.text = pendingOperation


    }
}