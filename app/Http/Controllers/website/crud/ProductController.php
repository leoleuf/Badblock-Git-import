<?php

namespace App\Http\Controllers\website\crud;

use App\Http\Controllers\Controller;
use App\Models\Category;
use App\Models\Product;
use App\Models\Server;
use Illuminate\Http\Request;
use Illuminate\Http\Redirect;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;


class ProductController extends Controller {
    //Test for git


    private $unknowCategorieName = "Catégorie_inconnue";

    /**
     * Display a listing of users
     *
     * @return Response
     */

    public function gestion()
    {
        return view('website.shop' , ['cat' => Category::all(), 'servers' => Server::take(100)->get(), 'products' => Product::all()]);
    }

    public function index()
    {
        return redirect("/website/shop");
    }

    /**
     * Show the form for creating a new user
     *
     * @return Response
     */
    public function create()
    {
        $cat = Category::all();

        return view('website.product.product-create', compact('cat'));
    }
    /**
     * Store a newly created user in storage.
     *
     * @return Response
     */
    public function store(Request $request)
    {

        $Product = new Product;
        $Product->name = $request->input('name');
        $Product->price = $request->input('price');
        $Product->description = $request->input('desc');
        $Product->cat_id = new \MongoDB\BSON\ObjectId($request->input('category'));
        $Product->promo_coef = $request->input('promo_coef');
        $Product->promotion_new_price = $request->input('promotion_new_price');
        $Product->mode = $request->input('mode');
        $Product->depend_to = new \MongoDB\BSON\ObjectId($request->input('depend_to'));
        $Product->depend_name = $request->input('depend_name');
        $Product->queue = $request->input('queue');
        $Product->command = $request->input('command');
        $Product->image = $request->input('image');

        $Product->ig_material = $request->input('ig_material');
        $Product->ig_data = $request->input('ig_data');



        if ($request->input('promotion') == "on"){
            $Product->promotion = true;
        }else{
            $Product->promotion = false;
        }

        if ($request->input('promotion_view') == "on"){
            $Product->promotion_view = true;
        }else{
            $Product->promotion_view = false;
        }

        if ($request->input('depend') == "on"){
            $Product->depend = true;
        }else{
            $Product->depend = false;
        }

        if ($request->input('visibility') == "on"){
            $Product->visibility = true;
        }else{
            $Product->visibility = false;
        }

        //Log to mongoDB
        DB::connection('mongodb')->collection('shop_update')->insert([
            'date' => date("Y-m-d h:i:s"),
            'user' => Auth::user()->name,
            'data' => ['new' => $Product]
        ]);

        $Product->save();

        //Discord WebHook notif

        $data = array("username" => "Boutique","embeds" => array(0 => array(
            "url" => "http://badblock.fr/shop",
            "title" => "Ajout Boutique",
            "description" => "Article : " . $Product->name . "
            User : " . Auth::user()->name,
            "color" => 5788507
        )));

        $curl = curl_init(env('DISCORD_MONI'));
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);

        return redirect('/website/shop/');
    }
    /**
     * Display the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function show($id)
    {
        $user = Product::findOrFail($id);
        return view('product.show', compact('user'));
    }
    /**
     * Show the form for editing the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function edit($id)
    {
        $Product = Product::find($id);
        $cat = Category::all();

        return view('website.product.product-edit', compact('Product'),compact('cat'));
    }
    /**
     * Update the specified resource in storage.
     *
     * @param  int  $id
     * @return Response
     */
    public function update($id,Request $request)
    {
        $Product = Product::findOrFail($id);

        $Product->name = $request->input('name');
        $Product->price = $request->input('price');
        $Product->description = $request->input('desc');
        $Product->cat_id = new \MongoDB\BSON\ObjectId($request->input('category'));
        $Product->promo_coef = $request->input('promo_coef');
        $Product->promotion_new_price = $request->input('promotion_new_price');
        $Product->mode = $request->input('mode');
        $Product->depend_to = new \MongoDB\BSON\ObjectId($request->input('depend_to'));
        $Product->depend_name = $request->input('depend_name');
        $Product->queue = $request->input('queue');
        $Product->command = $request->input('command');
        $Product->image = $request->input('image');

        $Product->ig_material = $request->input('ig_material');
        $Product->ig_data = $request->input('ig_data');



        if ($request->input('promotion') == "on"){
            $Product->promotion = true;
        }else{
            $Product->promotion = false;
        }

        if ($request->input('promotion_view') == "on"){
            $Product->promotion_view = true;
        }else{
            $Product->promotion_view = false;
        }

        if ($request->input('depend') == "on"){
            $Product->depend = true;
        }else{
            $Product->depend = false;
        }

        if ($request->input('visibility') == "on"){
            $Product->visibility = true;
        }else{
            $Product->visibility = false;
        }

        //Log to mongoDB
        $Product_origi = Product::find($id)->toArray();
        $Product->save();
        $Product_nw = Product::find($id)->toArray();

        DB::connection('mongodb')->collection('shop_update')->insert([
            'date' => date("Y-m-d h:i:s"),
            'user' => Auth::user()->name,
            'data' => ['old' => $Product_origi, 'new' => $Product_nw]
        ]);



        //Discord WebHook notif

        $data = array("username" => "Boutique","embeds" => array(0 => array(
            "url" => "http://badblock.fr/shop",
            "title" => "Update Boutique",
            "description" => "Article : " . $Product->name . "
            User : " . Auth::user()->name,
            "color" => 5788507
        )));

        $curl = curl_init(env('DISCORD_MONI'));
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);


        return redirect('/website/shop/');

    }
    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return Response
     */
    public function destroy($id)
    {
        Product::destroy($id);

        return redirect('/website/shop/');


    }
}