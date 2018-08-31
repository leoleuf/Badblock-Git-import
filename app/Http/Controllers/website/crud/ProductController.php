<?php

namespace App\Http\Controllers\website\crud;

use App\Models\Category;
use App\Models\Server;
use App\Models\Product;
use Illuminate\Http\Request;
use Illuminate\Http\Redirect;


class ProductController extends \App\Http\Controllers\Controller {
    //Test for git



    /**
     * Display a listing of users
     *
     * @return Response
     */
    public function index()
    {
        $Product = Product::all();

        foreach ($Product as $row){
            if (!empty($row->cat_id)){
                $cat = Category::find($row->cat_id);
                if (isset($cat->name)){
                    $row->cat = $cat->name;
                }
            }else{
                $row->cat = 'Catégorie Inconnue';
            }
        }


        return view('website.product.product', compact('Product'));
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

        $Product->save();

        return redirect('/website/crud/product/');
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

        return view('website.Product.Product-edit', compact('Product'),compact('cat'));
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

        $Product->save();

        return redirect('/website/crud/product/');

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

        return redirect('/website/crud/product/');


    }
}