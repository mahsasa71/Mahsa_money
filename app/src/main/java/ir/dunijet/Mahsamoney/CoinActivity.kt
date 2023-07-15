package ir.dunijet.Mahsamoney

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import ir.dunijet.Mahsamoney.ApiManager.*
import ir.dunijet.Mahsamoney.ApiManager.Models.ChartAdapter
import ir.dunijet.Mahsamoney.ApiManager.Models.ChartData
import ir.dunijet.Mahsamoney.ApiManager.Models.CoinAboutItem
import ir.dunijet.Mahsamoney.ApiManager.Models.CoinsData
import ir.dunijet.mahsamoney.R
import ir.dunijet.mahsamoney.databinding.ActivityCoinBinding

import ir.dunijet.mahsamoney.databinding.ActivityMarketBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    lateinit var datathiscoin:CoinsData.Data
    lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fromIntent = intent.getBundleExtra("bundle")!!
        datathiscoin = fromIntent.getParcelable<CoinsData.Data>("bundle1")!!

        if (fromIntent.getParcelable<CoinAboutItem>("bundle2") != null) {
            dataThisCoinAbout = fromIntent.getParcelable<CoinAboutItem>("bundle2")!!
        } else {
            dataThisCoinAbout = CoinAboutItem()
        }

        binding.layouttoolbar.layoutToolbar.title=datathiscoin.coinInfo.fullName

        initUi()
    }

    private fun initUi() {
        initchart()
        initStatistic()
        initAbout()
    }

    private fun initAbout() {

        binding.layoutAbout.textWeb.text = dataThisCoinAbout.coinWebsite
        binding.layoutAbout.textGit.text = dataThisCoinAbout.coinGithub
        binding.layoutAbout.txtReddit.text = dataThisCoinAbout.coinReddit
        binding.layoutAbout.txtAboutCoin.text = "@" + dataThisCoinAbout.coinTwitter
        binding.layoutAbout.txtAboutCoin.text = dataThisCoinAbout.coinDesc

        binding.layoutAbout.textWeb.setOnClickListener {
            openWebsiteDataCoin(dataThisCoinAbout.coinWebsite!!)
        }
        binding.layoutAbout.txtAboutCoin.setOnClickListener {
            openWebsiteDataCoin(dataThisCoinAbout.coinGithub!!)
        }
        binding.layoutAbout.txtReddit.setOnClickListener {
            openWebsiteDataCoin(dataThisCoinAbout.coinReddit!!)
        }
        binding.layoutAbout.textTwiter.setOnClickListener {
            openWebsiteDataCoin(BASE_URL_TWITTER + dataThisCoinAbout.coinWebsite!!)
        }

    }
    private fun openWebsiteDataCoin(url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }

    private fun initStatistic() {

        binding.layoutStatistic.textOpen.text = datathiscoin.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStatistic.texthigh.text = datathiscoin.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStatistic.texttodaylow.text = datathiscoin.dISPLAY.uSD.lOW24HOUR
        binding.layoutStatistic.texttodayschange.text = datathiscoin.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStatistic.textalgoritm.text = datathiscoin.coinInfo.algorithm
        binding.layoutStatistic.texttotalvalum.text = datathiscoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistic.textmarketcap.text = datathiscoin.dISPLAY.uSD.mKTCAP
        binding.layoutStatistic.textsupply.text = datathiscoin.dISPLAY.uSD.sUPPLY


    }

    private fun initchart() {
        var period: String = HOUR
        requestAndShowChart(period)
        binding.layoutChart.Radio000.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_12h -> {
                    period = HOUR
                }
                R.id.radio1d -> {
                    period = HOURS24
                }
                R.id.radio1w -> {
                    period = WEEK
                }
                R.id.radio1m -> {
                    period = MONTH
                }
                R.id.radio3m -> {
                    period = MONTH3
                }
                R.id.radio1y -> {
                    period = YEAR
                }
                R.id.radioall -> {
                    period = ALL
                }
            }
            requestAndShowChart(period)
        }

        binding.layoutChart.textChartPrice.text = datathiscoin.dISPLAY.uSD.pRICE
        binding.layoutChart.txtChartChange2.text = " " + datathiscoin.dISPLAY.uSD.cHANGE24HOUR

        if (datathiscoin.coinInfo.fullName == "BUSD") {
            binding.layoutChart.txtChartChange2.text = "0%"
        } else {
            binding.layoutChart.txtChartChange2.text = datathiscoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
        }

        val taghir = datathiscoin.rAW.uSD.cHANGEPCT24HOUR
        if (taghir > 0) {

            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.textChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.textChartUpdown.text = "▲"

            binding.layoutChart.sparkView.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorGain
            )

        } else if (taghir < 0) {

            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.textChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.textChartUpdown.text = "▼"

            binding.layoutChart.sparkView.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorLoss
            )


        }

        binding.layoutChart.sparkView.setScrubListener {

            // show price kamel
            if ( it == null ) {
                binding.layoutChart.textChartPrice.text = datathiscoin.dISPLAY.uSD.pRICE
            } else {
                // show price this dot
                binding.layoutChart.textChartPrice.text = "$ " + (it as ChartData.Data).close.toString()
            }

        }

    }

    fun requestAndShowChart(period: String) {

        apiManager.getChartData(
            datathiscoin.coinInfo.name,
            period,
            object : ApiManager.ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {
                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())
                    binding.layoutChart.sparkView.adapter = chartAdapter
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(
                        this@CoinActivity,
                        "error => " + errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    }


