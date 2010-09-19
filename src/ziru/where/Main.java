package ziru.where;


import java.io.IOException;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements LocationListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        _TextView = (TextView)findViewById(R.id.TextView); 
        _tvGetInfo =  (TextView)findViewById(R.id.tvGetInfo);
        _Location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _btopenMap = (Button)findViewById(R.id.btOpenMap);	
        _btopenMap.setOnClickListener(on_openMap);
        
        Criteria _Criteria = new Criteria(); 
     
    	_Criteria.setPowerRequirement(Criteria.POWER_HIGH);
    	
    	NUM_SATELLITES = getMaxSatellites();
        _Criteria.setAccuracy(Criteria.ACCURACY_FINE);

        	        
        //_Criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //_Criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        
        
        String _BestProvider = _Location.getBestProvider(_Criteria, true);
        
        _Location.requestLocationUpdates(_BestProvider, 100, 1, this);
        
        
    }
    
    private TextView _TextView = null;
    private TextView _tvGetInfo = null;
    private LocationManager _Location = null;
    private Button _btopenMap = null;
    private double _Latitude = 0;
    private double _Longitude = 0;
    public int NUM_SATELLITES = 0;
    
    public int getMaxSatellites() {
        return NUM_SATELLITES;
    }
    
    private View.OnClickListener on_openMap = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			do_OpenMap(_Latitude,_Longitude);			
		}
	};
	
    private void do_OpenMap(double ALatitude, double ALongitude ){
    	Uri _GeoURI = Uri.parse(String.format("geo:%f,%f", ALatitude,ALongitude));
    	Intent _Geomap = new Intent(Intent.ACTION_VIEW, _GeoURI);
    	startActivity(_Geomap);
    }
	
    public void onProviderEnabled (String provider){
    	_TextView.setText("onProviderEnabled");

    }
    
    public void onStatusChanged (String provider, int status, Bundle extras){
    	
    }

    private String get_Geocode(double ALatitude, double ALongitud){
    	String _Result = "";
    	
    	Geocoder _Geocoder = new Geocoder(this);
    	try{
    		Iterator<Address> _Address = _Geocoder.getFromLocation(ALatitude, ALongitud, 1).iterator();
    		
    		//_Result = String.format("\nWhere am I?\n");
    		
    		if(_Address != null){
    			while(_Address.hasNext()){
    				Address namedLoc = _Address.next();
    				String placeName = namedLoc.getLocality();
    				String featureName = namedLoc.getFeatureName();
    				String country = namedLoc.getCountryName();
    				String road = namedLoc.getThoroughfare();
    				//_Result += String.format("\n [%s] [%s] [%s] [%s]", placeName, featureName,road,country);
    				
    				int addIdx = namedLoc.getMaxAddressLineIndex();
    				
    				for(int idx = 0; idx <= addIdx; idx++){
    					String addLine = namedLoc.getAddressLine(idx);
    					//_Result += String.format("\nLine %d: %s", idx, addLine);
    					_Result += String.format("%s\n", addLine);
    					
    				}
    			}
    		}
    		
    		return _Result;
    		
    	} catch(IOException e){
    		return "Opps~ Can't find GPS signal.";
    	}
 
    }
    
    public void onLocationChanged (Location location){
    	_TextView.setText(
    			String.format("Latitude: %f, Longitude: %f",
    					location.getLatitude(),
    					location.getLongitude()
    					)
    					
    			);
    	
    	_tvGetInfo.setText(get_Geocode(location.getLatitude(), location.getLongitude()));
    }
    
    public void onProviderDisabled(String provider){
    	_TextView.setText("Can't find location.");
    }
    
}