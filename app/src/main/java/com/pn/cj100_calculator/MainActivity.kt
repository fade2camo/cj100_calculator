package com.pn.cj100_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pn.cj100_calculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDeciman = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
    fun numberAction(view: View){
        if(view is Button){

            if(view.text == "."){

                if(canAddDeciman){
                    binding.workingsTV.append(view.text)
                    canAddDeciman = false
                }
            }
            else
                binding.workingsTV.append(view.text)
            canAddOperation = true

        }
    }
    fun operationAction(view: View){
        if(view is Button && canAddOperation){
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDeciman = true

        }
    }
    fun equalsAction(view: View){
        var result: String  = calculateResults()
        binding.resultsTV.text = result
        binding.workingsTV.text = ""
    }
    fun allClearAction(view: View){
        binding.resultsTV.text = ""
        binding.workingsTV.text = ""
    }
    fun onBackspaceAction(view: View){
        val length = binding.workingsTV.text.length
        if(length> 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
    }
    private  fun calculateResults() : String{
        val digitOperators = digitsOperators()
        if(digitOperators.isEmpty())
            return ""

        val timesDivision = timesDivisionCalculate(digitOperators)
        if(timesDivision.isEmpty())
            return ""

        val result = addSubtractCalculate(timesDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator.equals('+'))
                {
                    result += nextDigit
                }
                if (operator == '-')
                {
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {

        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for( i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
               if(i > restartIndex)
                   newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperators () : MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        var ch: String = ""
        for(charactor in binding.workingsTV.text) {
            ch = charactor.toString()
            if (charactor.isDigit() || ch == ".")
                currentDigit += charactor
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(charactor)
            }
    }
        if(currentDigit != "")
        {
            list.add(currentDigit.toFloat())
        }
        return list
    }
}