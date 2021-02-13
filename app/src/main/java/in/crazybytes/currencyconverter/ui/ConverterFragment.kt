package `in`.crazybytes.currencyconverter.ui

import `in`.crazybytes.currencyconverter.R
import `in`.crazybytes.currencyconverter.databinding.FragmentConverterSecondBinding
import `in`.crazybytes.currencyconverter.main.MainViewModel
import `in`.crazybytes.currencyconverter.other.Constants.SOURCE_FROM
import `in`.crazybytes.currencyconverter.other.Constants.SOURCE_TO
import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConverterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentConverterSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        viewModel.convert()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterSecondBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fromCurrencyTitleTv.setOnClickListener(this)
        binding.toCurrencyTitleTv.setOnClickListener(this)
        binding.fromCurrencyAmountTv.setOnClickListener(this)
        binding.fabBtnSwap.setOnClickListener(this)

        //info: Setting TextSwitcher
        setFactoryAndAnimToAllTextSwitchers(activity as AppCompatActivity)

        //info: Setting Chart Details
        setupChart(activity as AppCompatActivity)


        viewModel.fromCurrency.observe(viewLifecycleOwner) { currencyEvent ->

            currencyEvent.getContentIfNotHandled().let { currency ->

                if (currency != null) {
                    binding.fromCurrencySymbolTv.setText(currency.symbol)
                    binding.fromCurrencyTitleTv.setText(currency.title)
                    binding.fromCurrencyCodeTv.setText(currency.code)
                } else {
                    binding.fromCurrencySymbolTv.setCurrentText(currencyEvent.peekContent().symbol)
                    binding.fromCurrencyTitleTv.setCurrentText(currencyEvent.peekContent().title)
                    binding.fromCurrencyCodeTv.setCurrentText(currencyEvent.peekContent().code)
                }

            }


        }

        viewModel.toCurrency.observe(viewLifecycleOwner) { currencyEvent ->

            currencyEvent.getContentIfNotHandled().let { currency ->

                if (currency != null) {
                    binding.toCurrencySymbolTv.setText(currency.symbol)
                    binding.toCurrencyTitleTv.setText(currency.title)
                    binding.toCurrencyCodeTv.setText(currency.code)
                } else {
                    binding.toCurrencySymbolTv.setCurrentText(currencyEvent.peekContent().symbol)
                    binding.toCurrencyTitleTv.setCurrentText(currencyEvent.peekContent().title)
                    binding.toCurrencyCodeTv.setCurrentText(currencyEvent.peekContent().code)
                }

            }

        }

        viewModel.amount.observe(viewLifecycleOwner) { amountEvent ->

            amountEvent.getContentIfNotHandled().let {
                if (it != null) {
                    binding.fromCurrencyAmountTv.text = it
                } else {
                    binding.fromCurrencyAmountTv.text = amountEvent.peekContent()
                }
            }

        }

        viewModel.conversion.observe(viewLifecycleOwner) { currencyRateEvent ->

            when (currencyRateEvent) {
                is MainViewModel.CurrencyRateEvent.Success -> {
                    Log.d(TAG, "onViewCreated: ConvertedValue:- {${currencyRateEvent.result}}")
                    binding.toCurrencyAmountTv.text = currencyRateEvent.result
                    binding.progressBar.isVisible = false
                }

                is MainViewModel.CurrencyRateEvent.Failure -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, currencyRateEvent.errorText, Toast.LENGTH_SHORT)
                        .show()
                }
                is MainViewModel.CurrencyRateEvent.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.toCurrencyAmountTv.text = ""
                }

                is MainViewModel.CurrencyRateEvent.Empty -> {
                    binding.toCurrencyAmountTv.text = ""
                }
            }


        }

    }

    private fun setupChart(appCompatActivity: AppCompatActivity) {
        val entries = ArrayList<Entry>()

//Part2
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

//Part3
        val vl = LineDataSet(entries, "My Type")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.gray
        vl.fillAlpha = R.color.red

        //Part5
        binding.lineChartView.xAxis.labelRotationAngle = 0f

//Part6
        binding.lineChartView.data = LineData(vl)

//Part7
        binding.lineChartView.axisRight.isEnabled = false
//        binding.lineChartView.xAxis.axisMaximum = j + 0.1f

////Part8
//        binding.lineChartView.setTouchEnabled(true)
//        binding.lineChartView.setPinchZoom(true)

//Part9
        binding.lineChartView.description.text = "Days"
        binding.lineChartView.setNoDataText("No forex yet!")

//Part10
        binding.lineChartView.animateX(1800, Easing.EaseInExpo)

//Part11
//        val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
//        binding.lineChartView.marker = markerView
    }

    private fun setFactoryAndAnimToAllTextSwitchers(activity: AppCompatActivity) {

        val textEnterUp = AnimationUtils.loadAnimation(activity, R.anim.text_enter_up)
        val textEnterDown = AnimationUtils.loadAnimation(activity, R.anim.text_enter_down)
        val textExitUp = AnimationUtils.loadAnimation(activity, R.anim.text_exit_up)
        val textExitDown = AnimationUtils.loadAnimation(activity, R.anim.text_exit_down)

        binding.fromCurrencyTitleTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.END
            textView.textSize = 16f
            textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            textView.setTextColor(Color.WHITE)
            textView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down_white,
                0
            )
            textView.compoundDrawablePadding = 8
            textView
        }

        binding.fromCurrencyTitleTv.inAnimation = textEnterDown
        binding.fromCurrencyTitleTv.outAnimation = textExitUp

        binding.fromCurrencyCodeTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 20f
            textView.setTextColor(getColor(activity, R.color.transparentWhite))
            textView
        }

        binding.fromCurrencyCodeTv.inAnimation = textEnterDown
        binding.fromCurrencyCodeTv.outAnimation = textExitUp

        binding.fromCurrencySymbolTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 20f
            textView.setTextColor(getColor(activity, R.color.transparentWhite))
            textView
        }

        binding.fromCurrencySymbolTv.inAnimation = textEnterDown
        binding.fromCurrencySymbolTv.outAnimation = textExitUp

        binding.toCurrencyTitleTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.END
            textView.textSize = 16f
            textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            textView.setTextColor(getColor(activity, R.color.purple_500))
            textView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_drop_down_purple,
                0
            )
            textView.compoundDrawablePadding = 8
            textView
        }

        binding.toCurrencyTitleTv.inAnimation = textEnterUp
        binding.toCurrencyTitleTv.outAnimation = textExitDown

        binding.toCurrencyCodeTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 20f
            textView.setTextColor(getColor(activity, R.color.transparentPurple))
            textView
        }

        binding.toCurrencyCodeTv.inAnimation = textEnterUp
        binding.toCurrencyCodeTv.outAnimation = textExitDown

        binding.toCurrencySymbolTv.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 20f
            textView.setTextColor(getColor(activity, R.color.transparentPurple))
            textView
        }

        binding.toCurrencySymbolTv.inAnimation = textEnterUp
        binding.toCurrencySymbolTv.outAnimation = textExitDown

    }


    companion object {

        private const val TAG = "ConverterFragment"

        @JvmStatic
        fun newInstance() =
            ConverterFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.fromCurrencyTitleTv -> {

                    findNavController().navigate(
                        ConverterFragmentDirections.actionConverterFragmentToSelectCurrencyFragment(
                            SOURCE_FROM
                        )
                    )
                }

                R.id.toCurrencyTitleTv -> {
                    findNavController().navigate(
                        ConverterFragmentDirections.actionConverterFragmentToSelectCurrencyFragment(
                            SOURCE_TO
                        )
                    )
                }

                R.id.fromCurrencyAmountTv -> {
                    findNavController().navigate(
                        ConverterFragmentDirections.actionConverterFragmentToAmountFragment(
                            binding.fromCurrencyAmountTv.text.toString()
                        )
                    )
                }

                R.id.fabBtnSwap -> {
                    viewModel.swapCurrencies()
                }
            }
        }
    }
}