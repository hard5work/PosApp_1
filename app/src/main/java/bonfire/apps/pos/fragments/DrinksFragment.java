package bonfire.apps.pos.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.MainActivity;
import bonfire.apps.pos.adapter.MenuItemAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link DrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean _areLecturesLoaded = false;
    boolean is_Visible = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String drinks = "/GetCategory";
    ServerManager sm;
    public static RecyclerView drinksView;
    public List<DataModule> dataModules = new ArrayList<>();



    public DrinksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrinksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinksFragment newInstance(String param1, String param2) {
        DrinksFragment fragment = new DrinksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drinks, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drinksView = view.findViewById(R.id.drinksListRecycler);
        drinksView.hasFixedSize();

        int ori = getResources().getConfiguration().orientation;
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        if (width > 750) {
            if (ori == Configuration.ORIENTATION_PORTRAIT) {
                drinksView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                dataModules.clear();
            }

            if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                drinksView.setLayoutManager(new GridLayoutManager(getContext(), 6));
                dataModules.clear();
            }
        } else {
            if (ori == Configuration.ORIENTATION_PORTRAIT) {
                drinksView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                dataModules.clear();
            }

            if (ori == Configuration.ORIENTATION_LANDSCAPE) {
                drinksView.setLayoutManager(new GridLayoutManager(getContext(), 5));
                dataModules.clear();
            }
        }


        //food.clear();
        //  if (isInternetOn())

        //    if (is_Visible && _areLecturesLoaded) {
        drinkList();


        MainActivity.searchView.setQueryHint("Search ");
        /*MainActivity.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataModules.clear();
                drinkList();

                return true;
            }
        });*/

        MainActivity.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    dataModules.clear();
                    drinkList();
                   // food.clear();
                    foodList();

                } else {
                    //       Toast.makeText(getContext(), "drinks" + s, Toast.LENGTH_SHORT).show();
//                        dataModules.clear();
//                        drinkList(s);
                    List<DataModule> searchList = new ArrayList<>();
                    for (int j = 0; j < dataModules.size(); j++) {
                        if (dataModules.get(j).getItemName().toUpperCase().contains(s.toUpperCase())) {
                            searchList.add(dataModules.get(j));
                        }
                        MenuItemAdapter adapter = new MenuItemAdapter(getContext(), searchList);
                        drinksView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    dataModules.clear();
                    drinkList(s);
                }

                return false;
            }
        });

        //  }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList() {
        dataModules.clear();
        //pb.show();
        final List<DataModule> food = new ArrayList<>();
        food.clear();
        RequestQueue rq;
        StringRequest sr;
        try {
            rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + drinks,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            //  pb.dismiss();
                            //   Log.e("Toast",response);

                            try {
                                JSONObject object = new JSONObject(response);

                                JSONArray jAry = object.getJSONArray("CategoryDetails");

                                for (int i = 0; i < jAry.length(); i++) {
                                    JSONObject jo = jAry.getJSONObject(i);

                                    DataModule m = new DataModule();
                                    m.setItemID(jo.getInt("category_id"));
                                    m.setItemImage(jo.getString("product_image"));
                                    m.setItemName(jo.getString("categoryname"));
                                    food.add(m);

                                }

                                MenuItemAdapter adapter = new MenuItemAdapter(getContext(), food);
                                FoodFragment.recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                    //  pb.dismiss();
                    //   Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
//

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("CategoryType", "1");
                    return map;
                }
            };
            rq.add(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            is_Visible = true;
            _areLecturesLoaded = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void drinkList() {
        dataModules.clear();
        RequestQueue rq;
        StringRequest sr;
        try {
            rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + drinks,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);

                                JSONArray jAry = object.getJSONArray("CategoryDetails");

                                for (int i = 0; i < jAry.length(); i++) {
                                    JSONObject jo = jAry.getJSONObject(i);

                                    DataModule m = new DataModule();
                                    m.setItemID(jo.getInt("category_id"));
                                    m.setItemImage(jo.getString("product_image"));
                                    m.setItemName(jo.getString("categoryname"));
                                    dataModules.add(m);

                                }

                                MenuItemAdapter adapter = new MenuItemAdapter(getContext(), dataModules);
                                drinksView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
//                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
//

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("CategoryType", "2");
                    return map;
                }
            };
            rq.add(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void drinkList(final String s) {
        dataModules.clear();
        RequestQueue rq;
        StringRequest sr;
        final List<DataModule> food = new ArrayList<>();
        food.clear();
        try {
            rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + drinks,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);

                                JSONArray jAry = object.getJSONArray("CategoryDetails");

                                for (int i = 0; i < jAry.length(); i++) {
                                    JSONObject jo = jAry.getJSONObject(i);

                                    DataModule m = new DataModule();
                                    m.setItemID(jo.getInt("category_id"));
                                    m.setItemImage(jo.getString("product_image"));
                                    m.setItemName(jo.getString("categoryname"));
                                    String s1 = jo.getString("categoryname");
                                    if (s1.toUpperCase().contains(s.toUpperCase()))
                                        food.add(m);

                                }

                                MenuItemAdapter adapter = new MenuItemAdapter(getContext(), food);
                                FoodFragment.recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
//

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("CategoryType", "1");
                    return map;
                }
            };
            rq.add(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isInternetOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
