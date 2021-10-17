package com.carrtre.tipcalc

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INIT_TIP_PERCENT = 15
private const val INIT_NUM_GUESTS = 2
private const val MIN_GUESTS = 1

class MainActivity : AppCompatActivity() {
    private lateinit var  etBaseAmount: EditText
    private lateinit var  sbTip: SeekBar
    private lateinit var  tvTipPercentLabel: TextView
    private lateinit var  tvTipAmount: TextView
    private lateinit var  tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var sbGuests: SeekBar
    private lateinit var tvGuestNumber: TextView
    private lateinit var tvEachAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        sbTip = findViewById(R.id.sbTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        sbGuests = findViewById(R.id.sbGuests)
        tvGuestNumber = findViewById(R.id.tvGuestNumber)
        tvEachAmount = findViewById(R.id.tvEachAmount)


        sbTip.progress = INIT_TIP_PERCENT
        sbGuests.progress = INIT_NUM_GUESTS
        tvTipPercentLabel.text = "$INIT_TIP_PERCENT%"
        tvGuestNumber.text = "$INIT_NUM_GUESTS"
        updateTipDescription(INIT_TIP_PERCENT)
        updateNumGuests(INIT_NUM_GUESTS)

        sbTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        sbGuests.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvGuestNumber.text = "$progress%"
                computeTipAndTotal()
                updateNumGuests(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

        })

    }

    private fun updateNumGuests(numGuests: Int) {
        val modifiedNum = numGuests + MIN_GUESTS
        tvGuestNumber.text = "$modifiedNum"
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        // Update color based on tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / sbTip.max,
            ContextCompat.getColor(this, R.color.light_grey),
            ContextCompat.getColor(this, R.color.bird_blue)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvEachAmount.text = ""
            return
        }

        // 1. Get the value of base, tip percent, and number of guests
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = sbTip.progress
        val numGuests = sbGuests.progress + MIN_GUESTS
        // 2. computer the tip an total
        val tipAmount = baseAmount  * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        val eachAmount = totalAmount / numGuests
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvEachAmount.text = "%.2f".format(eachAmount)
    }
}