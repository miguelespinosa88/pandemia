package edu.itesm.pandemia

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Collections.addAll

data class Pais(var nombre: String,
                var latitude: Double,
                var longitude: Double,
                var casos: Double,
                var recuperados: Double,
                var defunciones: Double,
                var tests: Double)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val url = "https://disease.sh/v3/covid-19/countries"//"https://gist.githubusercontent.com/miguelespinosa88/9b701e10926c106a7b4701f126a4d473/raw/b4d53a929a4629b6287fd3c18bd365a6189197d0/db.json"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        cargaDatos()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /*val requestQueue = Volley.newRequestQueue(this)
        val peticion = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener {
            val jsonArray = it
            for (i in 0 until jsonArray.length()){
                val pais = jsonArray.getJSONObject(i)
                val nombre = pais.getString("name")
                val lat = pais.getDouble("lat")
                val lng = pais.getDouble("lng")
                val latLng = LatLng(lat,lng)
                mMap.addMarker(MarkerOptions().position(latLng).title(nombre))

            }

        },Response.ErrorListener {

        })

        requestQueue.add(peticion)*/
        // Add a marker in Sydney and move the camera
        /*val mexico = LatLng(19.432608, -99.133209)
        mMap.addMarker(MarkerOptions().position(mexico).title("Marker in Mexico"))
        mMap.addMarker(MarkerOptions().position(LatLng(18.432608, -99.133209 )).title("Desconocido"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))*/
        //mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE

    }

    fun viewData(view: View){
        mMap.clear()
        for (pais in data){
            mMap.addMarker(MarkerOptions().position(
                    LatLng(pais.latitude, pais.longitude)).title( pais.nombre))
        }
    }


    private val data = mutableListOf<Pais>()
    fun cargaDatos(){
        val requestQueue = Volley.newRequestQueue(this)
        val peticion = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener {
            val jsonArray = it
            for (i in 0 until jsonArray.length()){
                val pais = jsonArray.getJSONObject(i)
                val nombre = pais.getString("country")
                val countryInfoData = pais.getJSONObject("countryInfo")

                val latitude = countryInfoData.getDouble("lat")
                val longitude = countryInfoData.getDouble("long")
                val casos = pais.getDouble("cases")
                val recuperados = pais.getDouble("recovered")
                val defunciones = pais.getDouble("deaths")
                val tests = pais.getDouble("tests")

                val paisObject = Pais(nombre,latitude, longitude, casos, recuperados,defunciones, tests)

                data.add(paisObject)

            }

        },Response.ErrorListener {

        })

        requestQueue.add(peticion)
    }

    private var muerte = mutableListOf<Pais>()
    fun viewMuertes(view: View){
        data.sortByDescending { it.defunciones }
        muerte=data.subList(0,10)
        mMap.clear()
         for (pais in muerte){
             mMap.addMarker(MarkerOptions().position(
                     LatLng(pais.latitude, pais.longitude)).title( pais.nombre).icon(BitmapDescriptorFactory.fromResource(R.drawable.calavera)))
         }
    }

    private var caso = mutableListOf<Pais>()
    fun viewCasos(view: View){
        data.sortBy { it.casos }
        caso=data.subList(0,10)
        mMap.clear()
        for (pais in caso){
            mMap.addMarker(MarkerOptions().position(
                    LatLng(pais.latitude, pais.longitude)).title( pais.nombre).icon(BitmapDescriptorFactory.fromResource(R.drawable.cubrebocas)))
        }
    }

    private var test = mutableListOf<Pais>()
    fun viewTests(view: View){
        data.sortByDescending { it.tests }
        test=data.subList(0,10)
        mMap.clear()
        for (pais in test){
            mMap.addMarker(MarkerOptions().position(
                    LatLng(pais.latitude, pais.longitude)).title( pais.nombre).icon(BitmapDescriptorFactory.fromResource(R.drawable.prueba)))
        }
    }

}
