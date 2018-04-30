package com.ibm.mobile.jsonstoreofflinesync.manager;

import android.content.Context;
import android.util.Log;

import com.ibm.mobile.jsonstoreofflinesync.activities.MakeSaleItems;
import com.ibm.mobile.jsonstoreofflinesync.activities.FoodMenuItems;

import com.worklight.jsonstore.api.*;
import com.worklight.jsonstore.exceptions.JSONStoreCloseAllException;
import com.worklight.jsonstore.exceptions.JSONStoreException;
import com.worklight.jsonstore.exceptions.JSONStoreFileAccessException;
import com.worklight.jsonstore.exceptions.JSONStoreInvalidPasswordException;
import com.worklight.jsonstore.exceptions.JSONStoreInvalidSchemaException;
import com.worklight.jsonstore.exceptions.JSONStoreMigrationException;
import com.worklight.jsonstore.exceptions.JSONStoreSchemaMismatchException;
import com.worklight.jsonstore.exceptions.JSONStoreTransactionDuringInitException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


/**
 * Created by abhilash_m on 16/02/18.
 */


// Singleton Class
// For accessing List of Food Menu Items
public class FlightAttendantManager{

    //Creating a list for containing the food items
    private ArrayList<FoodMenuItems> foodmenuList = new ArrayList<>();
    private ArrayList<MakeSaleItems> showsalesList = new ArrayList<>();

    //It is a sync listener for downstream Sync
    FoodMenuSyncListener foodMenuSyncListener;

    static private FlightAttendantManager thisInstance = null;

    //Collection for Downstream Sync
    /*
    "foodmenu" JSONStoreCollection will be used to get data from remote DB.
    This will be initialized and will contain data pertaining to all the food items, their quantity and price that will be
    set in the backend Cloudant DB
    */
     JSONStoreCollection foodmenu;
     String FOODMENU_COLLECTION_NAME = "foodmenu";


    //Collection for Upstream Sync
    /*
    "makesale" JSONStoreCollection will be used to send data to remote DB.
    This will be initialized with an upstream sync policy and any addition/deletion or replacement of data in this collection
    will be reflected in the remote Cloudant DB.
    */
    public JSONStoreCollection makesale;
    String MAKESALE_COLLECTION_NAME = "makesales";


    public void initJsonStoreCollections(Context context) throws JSONStoreInvalidSchemaException, JSONStoreMigrationException, JSONStoreTransactionDuringInitException, JSONStoreSchemaMismatchException, JSONStoreCloseAllException, JSONStoreInvalidPasswordException, JSONStoreFileAccessException {

        try {
            //Creating initOptions for a jsonstore collection that will be using the sync_policy of SYNC_UPSTREAM
            JSONStoreInitOptions initOptionsUpstream = new JSONStoreInitOptions();

            /* Set sync policy to SYNC_UPSTREAM. This will ensure that any data added/deleted/modified in this
            jsonstore collection is synced with CloudantDB(when there is an internet connection to communicate with the backend) */
            initOptionsUpstream.setSyncPolicy(JSONStoreSyncPolicy.SYNC_UPSTREAM);

            //Adapter path contains the name of the adapter
            initOptionsUpstream.setSyncAdapterPath("JSONStoreCloudantSync");


            //Creating a list of JSONStroreCollection as opencollections method take list of collections as input.

            List<JSONStoreCollection> collectionsMakeSales = new LinkedList<>();
            //Initialising the collection with the name with which a DB is created in Cloudant (Here "makesales" is the Coudant DB)
            makesale = new JSONStoreCollection(MAKESALE_COLLECTION_NAME);

            //adding collection to the list
            collectionsMakeSales.add(makesale);

            //This opencollection method of WLJSONStore will create a jsonstore with an upstream sync syncpolicy
            WLJSONStore.getInstance(context).openCollections(collectionsMakeSales, initOptionsUpstream);
        }
        catch (Exception makesSalesCreateException) {
            logError("Error in Upstream Sync- " +makesSalesCreateException.getStackTrace());
        }

        //Creating a list of JSONStroreCollection as opencollections method take list of collections as input.
        List<JSONStoreCollection> collectionsMenu = new LinkedList<>();
        //Initialising the collection with the name with which a DB is created in Cloudant (Here "foodmenu" is the Coudant DB)
        foodmenu =new  JSONStoreCollection(FOODMENU_COLLECTION_NAME);
        //adding collection to the list
        collectionsMenu.add(foodmenu);

        //This syncListener will be called after response is received from the adapter
        final JSONStoreSyncListener foodmenuSyncListener = new JSONStoreSyncListener() {
            //When sync succeeds, onSuccess will be called
            @Override
            public void onSuccess(JSONObject jsonObject){
                Log.d("ListenerSuccess", "Successfully initiliazed from cloudant");
                try {
                    //Clear the list every time before rendering to the Application , if not cleared the list will be flooded with duplicates
                    FlightAttendantManager.getInstance().clearFoodMenuItems();

                    //Adding find options
                    JSONStoreFindOptions findOptions = new JSONStoreFindOptions();
                    findOptions.addSearchFilter("_id");
                    findOptions.addSearchFilter("json");

                    //For getting all the documents from Cloudant
                    List<JSONObject> results = foodmenu.findAllDocuments(findOptions);

                    StringBuilder displayText = new StringBuilder();

                    /*This will gather all the data from JSONStore of food items and add that to the list
                      in the Singleton Class(FlightAttendantManager)*/
                    for (JSONObject result: results) {
                        JSONObject foodmenuObject = result.getJSONObject("json");
                        displayText.append("Name :" + foodmenuObject.getString("item_name")).append("\n")
                                .append("Quantity :" + foodmenuObject.getString("quantity") + "\n")
                                .append("Price :" + foodmenuObject.getString("base_price") + "\n")
                                .append("Image :" + foodmenuObject.getString("image_url") + "\n\n\n");
                        //Creating foodmenu_item object and Adding data to it
                        FoodMenuItems foodmenu_item = new FoodMenuItems(foodmenuObject.getString("item_name"),foodmenuObject.getString("quantity"),foodmenuObject.getString("base_price"), foodmenuObject.getString("image_url"));
                        //Adding data  object to the singleton class
                        FlightAttendantManager.getInstance().addFoodMenuItems(foodmenu_item);
                    }
                    Log.d("Item:", displayText.toString());
                    //This foodSyncComplete function is written for making progress bar invisible
                    foodMenuSyncListener.foodSyncComplete("Sync Done Successfully!!");
                }
                catch (Exception e) {
                    logError("Error in Downstream Sync- "+e.getMessage());
                }
            }

            //If Listener fails after calling sync on any collection , it will call this function.
            @Override
            public void onFailure(JSONStoreException ex) {

                if(foodMenuSyncListener!=null)
                foodMenuSyncListener.foodSyncComplete("Sync Call Failed");

                Log.d("ListenerFailure", "Cloudant sync failed with exception :  " + ex.getMessage());
            }
        };

        //Setting the sync policy to SYNC_DOWNSTREAM for this JSONStore collection to be able to fetch all the data from the CloudantDB into this JSONStore collection
        JSONStoreInitOptions initOptionsDownstream = new JSONStoreInitOptions();

        /*A JSONStore collection is created with sync policy as SYNC_DOWNSTREAM and all the data from
         CloudantDB is downloaded into the JSONStore collection(when there is a network connectivity) */
        initOptionsDownstream.setSyncPolicy(JSONStoreSyncPolicy.SYNC_DOWNSTREAM);

        //Setting the adapter path to the name of the adapter
        initOptionsDownstream.setSyncAdapterPath("JSONStoreCloudantSync");

        initOptionsDownstream.setSyncListener(foodmenuSyncListener);

        WLJSONStore.getInstance(context).openCollections(collectionsMenu, initOptionsDownstream);

    }




    //This function will add item i.e. object having the type FoodMenuItems
    public void addFoodMenuItems(FoodMenuItems menuItems) {
        foodmenuList.add(menuItems);
    }
    //This function will add the sale objects to the showsaleList
    public void addMakeSaleItems(MakeSaleItems saleitems){
        showsalesList.add(saleitems);
    }
    //This function will clear the list of food items
    public void clearFoodMenuItems() {
        foodmenuList.clear();
    }
    //We can get the current list of food items by invoking this function
    public ArrayList<FoodMenuItems> getFoodMenuList(){
        return foodmenuList;
    }
    //We can get the current list of sale items by invoking this function
    public ArrayList<MakeSaleItems> getShowSalesList() {
        return showsalesList;
    }
    public void removeQuantity(String itemName,int quantityRecieved){
        int findIndex = -1;
        for(FoodMenuItems check : foodmenuList)
            if (check.getItem_name().equals(itemName))
                findIndex=foodmenuList.indexOf(check);

        int updatedQuantity=Integer.parseInt(foodmenuList.get(findIndex).getQuantity())-quantityRecieved;
        foodmenuList.get(findIndex).setQuantity(Integer.toString(updatedQuantity));

    }
    // It is analogous to static block in java
   public static FlightAttendantManager getInstance(){

        // This function will return an instance of this class

         if (thisInstance == null)
                thisInstance = new FlightAttendantManager();

            return thisInstance;
    }

       //This function is called when connection is detected
        public void upstreamsync()
        {
            makesale.sync();
        }

        //Perform this when Sync button is clicked
        public void syncFoodMenu(FoodMenuSyncListener foodMenuSyncListener)
        {
            this.foodMenuSyncListener = foodMenuSyncListener;
            foodmenu.sync();
        }


        //While displaying Error we always call this function.
        private void logError(String message) {
            Log.d("FlightAttendantManager",message);
        }


}
