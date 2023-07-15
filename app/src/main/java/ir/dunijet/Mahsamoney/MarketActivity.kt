package ir.dunijet.Mahsamoney

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import ir.dunijet.Mahsamoney.ApiManager.ApiManager
import ir.dunijet.Mahsamoney.ApiManager.MarketAdapter
import ir.dunijet.Mahsamoney.ApiManager.Models.CoinAboutData
import ir.dunijet.Mahsamoney.ApiManager.Models.CoinAboutItem
import ir.dunijet.Mahsamoney.ApiManager.Models.CoinsData
import ir.dunijet.mahsamoney.databinding.ActivityMarketBinding

class MarketActivity : AppCompatActivity(),MarketAdapter.RecyclerCallback {
    lateinit var binding: ActivityMarketBinding
    lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>

    lateinit var datanews: ArrayList<Pair<String, String>>
    private val apiManager = ApiManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layoutToolbar.layoutToolbar.title = "Mahsapool Market"

        binding.layoutWatchlist.btnMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }
        binding.Swiprefresh.setOnRefreshListener {

            initui()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.Swiprefresh.isRefreshing = false
            }, 1500)

        }
        getAboutdataFrpmAsset()


    }

    override fun onResume() {
        super.onResume()
        initui()
    }

    private fun initui() {

        getNewsFromApi()
        getTopCoinsFromApi()

    }


    private fun getNewsFromApi() {
        apiManager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {

            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                datanews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {
                Log.v("test", errorMessage)
            }

        })
    }

    fun refreshNews() {
        val random = (0..49).random()
        binding.layoutNews.txtNews.text = datanews[random].first
        binding.layoutNews.imageNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(datanews[random].second))
            startActivity(intent)
        }

        binding.layoutNews.txtNews.setOnClickListener {
            refreshNews()
        }

    }
    fun getAboutdataFrpmAsset(){

        val fileInString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        aboutDataMap = mutableMapOf<String, CoinAboutItem>()

        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString, CoinAboutData::class.java)

        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )
        }




    }

    private fun getTopCoinsFromApi() {

        apiManager.getCoinsList(object : ApiManager.ApiCallback<List<CoinsData.Data>> {
            override fun onSuccess(data: List<CoinsData.Data>) {

                showDataInRecycler(data)

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MarketActivity, "error => " + errorMessage, Toast.LENGTH_SHORT)
                    .show()
                Log.v("testLog", errorMessage)
            }
        })

    }
    private fun showDataInRecycler(data: List<CoinsData.Data>) {

       val adapter = MarketAdapter(ArrayList(data), this)
        binding.layoutWatchlist.recyclerView.adapter = adapter
        binding.layoutWatchlist.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putParcelable("bundle2", aboutDataMap[dataCoin.coinInfo.name])

        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }
}
