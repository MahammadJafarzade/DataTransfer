package com.example.classtask.features.productList

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.classtask.R
import com.example.classtask.databinding.ActivityMainBinding
import com.example.classtask.features.adapters.ProductListAdapter
import com.example.classtask.features.newProduct.AddProductActivity
import com.example.classtask.model.Product

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    lateinit var adapter:ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding= ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner=this
        binding.viewModel=viewModel
        setContentView(binding.root)

         adapter=ProductListAdapter(this.baseContext, mutableListOf(), onItemClick = { product->
            println(product.name)

        })
        binding.productListView.adapter=adapter
    }

    fun openAddProductActivity(){
        val intent = Intent(this,AddProductActivity::class.java)
            //startActivity(intent)
        newProductLauncher.launch(intent)
    }

    private val newProductLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode== Activity.RESULT_OK){
            val item=result.data?.getParcelableExtra<Product>("product" )
            item?.let{
                adapter.addNewItem(it)
            }
        }
    }

    fun observeAll(){
        viewModel.addProductObserver.observe(this){
            if(it){
                openAddProductActivity()
            }
        }
    }

    fun removeObservers(){
        viewModel.addProductObserver.removeObservers(this)
       viewModel.addProductObserver.postValue(false)
    }


    override fun onResume(){
        super.onResume()
        observeAll()
    }
    override fun onPause(){
        super.onPause()
        removeObservers()
    }
}