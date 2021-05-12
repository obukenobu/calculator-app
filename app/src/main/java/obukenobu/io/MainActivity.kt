package obukenobu.io

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="
    private val STATE_DEPENDING_OPERATION = "PendingOperation"
    private val STATE_OPERAND1 = "Operand1"
    private val STATE_OPERAND_STORED = "OperandStored"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
            Log.d("onClick", "Clicked button ${b.text}")
        }

        no0.setOnClickListener(listener)
        no1.setOnClickListener(listener)
        no2.setOnClickListener(listener)
        no3.setOnClickListener(listener)
        no4.setOnClickListener(listener)
        no5.setOnClickListener(listener)
        no6.setOnClickListener(listener)
        no7.setOnClickListener(listener)
        no8.setOnClickListener(listener)
        no9.setOnClickListener(listener)
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
            operation.text = pendingOperation

        }

        add.setOnClickListener(opListener)
        divide.setOnClickListener(opListener)
        equals.setOnClickListener(opListener)
        minus.setOnClickListener(opListener)
        multiply.setOnClickListener(opListener)

        clear.setOnClickListener{view ->
            result.text.clear()
            newNumber.text.clear()
            operation.text=" "
        }

        negationButton.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    Log.d("onNegation", "exception: $e")
                    newNumber.text.clear()
                }
            }
        }
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
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND_STORED)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_DEPENDING_OPERATION).toString()
        operation.text = pendingOperation


    }
}